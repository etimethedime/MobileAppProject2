package com.example.chapterproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.activity.EdgeToEdge;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Locale;

public class Chapter4_MyContactsActivity extends AppCompatActivity implements DatePickerDialogue.SaveDateListener {
    private Contact currentContact;
    private Bitmap picture;
    final int PERMISSION_REQUEST_PHONE = 102;
    final int PERMISSION_REQUEST_CAMERA = 103;
    final int CAMERA_REQUEST = 1888;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter4_my_contacts);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initContactListButton();
        initMapButton();
        initSettingsButton();
        initToggleButton();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            initContact(extras.getLong("contactId"));
        } else {
            currentContact = new Contact();
        }
        setForEditing(false);
        initChangeDateButton();
        initSaveButton();
        initTextChangedEvents();
        initCallFunction();
        initContactImageButton();

    }

    private void initContactListButton() {
        ImageButton contactListButton = findViewById(R.id.contactListButton);
        contactListButton.setOnClickListener(v -> {
            Intent listIntent = new Intent(Chapter4_MyContactsActivity.this, Chapter4_ContactListActivity.class);
            listIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(listIntent);
        });
    }

    private void initMapButton() {
        ImageButton mapButton = findViewById(R.id.contactMapButton);
        mapButton.setOnClickListener(v -> {
            Intent listIntent = new Intent(Chapter4_MyContactsActivity.this, Chapter4_MapActivity.class);
            if (currentContact.getId() == -1) {
                Toast.makeText(this, "Contact must be saved before displaying location", Toast.LENGTH_LONG).show();
            } else {
                listIntent.putExtra("contactId", currentContact.getId());
            }
            listIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(listIntent);
        });
    }

    private void initSettingsButton() {
        ImageButton settingsButton = findViewById(R.id.contactSettingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent listIntent = new Intent(Chapter4_MyContactsActivity.this, Chapter4_SettingsActivity.class);
            listIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(listIntent);
        });
    }
    private void initContactImageButton(){
        ImageButton contactImageButton = findViewById(R.id.imageButtonContactPhoto);
        contactImageButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(Chapter4_MyContactsActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale
                                    (Chapter4_MyContactsActivity.this, Manifest.permission.CAMERA)) {
                                Log.d("Camera Acess", "Permission Asked");
                                Snackbar.make(findViewById(R.id.main), "Permission Required", Snackbar.LENGTH_INDEFINITE).
                                        setAction("Ok", v1 -> {
                                            ActivityCompat.requestPermissions(Chapter4_MyContactsActivity.this,
                                                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                                        }).show();
                            }
                            else {
                                ActivityCompat.requestPermissions(Chapter4_MyContactsActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                            }
                }
                else{
                    takePhoto();
                }
            }
            else{
                takePhoto();
            }
        });
    }

    private void initSaveButton() {
        Button saveButton = findViewById(R.id.buttonAddContact);
        saveButton.setOnClickListener(v -> {

            Log.d("SaveContact", "Name: " + currentContact.getContactName() +
                    ", Phone: " + currentContact.getCellNumber() +
                    ", Email: " + currentContact.getEmail());

            boolean wasSuccessful;
            ContactDataSource ds = new ContactDataSource(Chapter4_MyContactsActivity.this);
            ds.open();

            if (currentContact.getId() == -1) {
                wasSuccessful = ds.insertContact(currentContact);
                if (wasSuccessful) {
                    int newId = ds.getLastContactId();
                    currentContact.setId(newId);
                    Log.d("SaveContact", "Contact inserted successfully with ID: " + newId);
                } else {
                    Log.d("SaveContact", "Contact insertion failed.");
                }
            } else {
                wasSuccessful = ds.updateContact(currentContact);
                if (wasSuccessful) {
                    Log.d("SaveContact", "Contact updated successfully.");
                } else {
                    Log.d("SaveContact", "Contact update failed.");
                }
            }

            ds.close();

            if (wasSuccessful) {
                ToggleButton toggleButton = findViewById(R.id.onOffButton);
                toggleButton.toggle();
                setForEditing(false);
            }
        });
    }


    private void initToggleButton() {
        ToggleButton toggleButton = findViewById(R.id.onOffButton);
        toggleButton.setOnClickListener(v -> {
            setForEditing(toggleButton.isChecked());
        });
    }

    private void initChangeDateButton() {
        Button changeDateButton = findViewById(R.id.changeBirthdayButton);
        changeDateButton.setOnClickListener(v -> {
            DatePickerDialogue datePickerDialogue = new DatePickerDialogue();
            datePickerDialogue.show(getSupportFragmentManager(), "date picker");
        });
    }

    private void initContact(long id) {
        ContactDataSource ds = new ContactDataSource(Chapter4_MyContactsActivity.this);
        try {
            ds.open();
            currentContact = ds.getSpecificContact(id);
            ds.close();
        } catch (Exception e) {
            Log.w(this.getLocalClassName(), "Error getting contact");
        }
        EditText editName = findViewById(R.id.nameText);
        EditText editStreet = findViewById(R.id.streetText);
        EditText editCity = findViewById(R.id.cityText);
        EditText editState = findViewById(R.id.stateText);
        EditText editZipcode = findViewById(R.id.zipcodeText);
        EditText editCell = findViewById(R.id.cellNumberText);
        EditText editHome = findViewById(R.id.homePhoneText);
        EditText editEmail = findViewById(R.id.emailText);
        EditText editBirthday = findViewById(R.id.birthdayText);
        ImageButton picture = (ImageButton) findViewById(R.id.imageButtonContactPhoto);

        editName.setText(currentContact.getContactName());
        editStreet.setText(currentContact.getStreetAddress());
        editCity.setText(currentContact.getCity());
        editState.setText(currentContact.getState());
        editZipcode.setText(currentContact.getZipCode());
        editCell.setText(currentContact.getCellNumber());
        editHome.setText(currentContact.getHomePhoneNumber());
        editEmail.setText(currentContact.getEmail());
        editBirthday.setText(currentContact.getBirthday());
        if (currentContact.getPicture() != null) {
            picture.setImageBitmap(currentContact.getPicture());
        }
        else{
            picture.setImageResource(R.drawable.contact_photo);
        }

    }


    private void setForEditing(boolean enabled) {
        EditText editName = findViewById(R.id.nameText);
        EditText editStreet = findViewById(R.id.streetText);
        EditText editCity = findViewById(R.id.cityText);
        EditText editState = findViewById(R.id.stateText);
        EditText editZipcode = findViewById(R.id.zipcodeText);
        EditText editCell = findViewById(R.id.cellNumberText);
        EditText editHome = findViewById(R.id.homePhoneText);
        EditText editEmail = findViewById(R.id.emailText);
        EditText editBirthday = findViewById(R.id.birthdayText);
        Button changeDateButton = findViewById(R.id.changeBirthdayButton);
        Button saveButton = findViewById(R.id.buttonAddContact);
        ImageButton contactImageButton = findViewById(R.id.imageButtonContactPhoto);

        editName.setEnabled(enabled);
        editStreet.setEnabled(enabled);
        editCity.setEnabled(enabled);
        editState.setEnabled(enabled);
        editZipcode.setEnabled(enabled);
        editEmail.setEnabled(enabled);
        editBirthday.setEnabled(enabled);
        changeDateButton.setEnabled(enabled);
        saveButton.setEnabled(enabled);
        contactImageButton.setEnabled(enabled);


        if (enabled) {
            editName.requestFocus();
            editCell.setInputType(InputType.TYPE_NULL);
            editHome.setInputType(InputType.TYPE_NULL);
        } else {
            editCell.setInputType(InputType.TYPE_CLASS_PHONE);
            editHome.setInputType(InputType.TYPE_CLASS_PHONE);
        }

    }


    @Override
    public void didFinishDatePickerDialog(Calendar selectedDate) {
        TextView birthDay = findViewById(R.id.birthdayText);
        birthDay.setText(DateFormat.format("MM/dd/yyyy", selectedDate));
        currentContact.setBirthday(selectedDate.toString());
    }

    private void initTextChangedEvents() {
        final EditText etName = findViewById(R.id.nameText);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setContactName(s.toString());
            }
        });

        final EditText etStreet = findViewById(R.id.streetText);
        etStreet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setStreetAddress(s.toString());
            }
        });
        final EditText etCity = findViewById(R.id.cityText);
        etCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setCity(s.toString());
            }
        });
        final EditText etState = findViewById(R.id.stateText);
        etState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setState(s.toString());
            }
        });
        final EditText etZipcode = findViewById(R.id.zipcodeText);
        etZipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setZipCode(s.toString());
            }
        });
        final EditText etCell = findViewById(R.id.cellNumberText);
        etCell.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String formattedNumber = PhoneNumberUtils.formatNumber(s.toString(), Locale.getDefault().getCountry());
                currentContact.setCellNumber(formattedNumber);
            }
        });

        final EditText etHome = findViewById(R.id.homePhoneText);
        etHome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String formattedNumber = PhoneNumberUtils.formatNumber(s.toString(), Locale.getDefault().getCountry());
                currentContact.setHomePhoneNumber(formattedNumber);
            }
        });

        final EditText etEmail = findViewById(R.id.emailText);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setEmail(s.toString());
            }
        });
    }

    private void initCallFunction() {
        EditText editHome = findViewById(R.id.homePhoneText);
        editHome.setOnLongClickListener(v -> {
                checkPhonePermission(currentContact.getHomePhoneNumber());
                return false;
            });

        EditText editCell = findViewById(R.id.cellNumberText);
        editCell.setOnLongClickListener(v -> {
                checkPhonePermission(currentContact.getCellNumber());
                return false;
            });
    }

    private void checkPhonePermission(String phoneNumber) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(Chapter4_MyContactsActivity.this, Manifest.permission.CALL_PHONE)
                    != getPackageManager().PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale
                        (Chapter4_MyContactsActivity.this,
                                Manifest.permission.CALL_PHONE)) {
                    Snackbar.make(findViewById(R.id.main), "Permission Required", Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK", v -> {
                                ActivityCompat.requestPermissions(Chapter4_MyContactsActivity.this,
                                        new String[]{Manifest.permission.CALL_PHONE}, 1);
                            }).show();
                } else {
                    ActivityCompat.requestPermissions
                            (Chapter4_MyContactsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_PHONE);
                }
            } else {
                callContact(phoneNumber);
            }
        }
        else{
            callContact(phoneNumber);
        }

    }
    private void callContact(String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (Build.VERSION.SDK_INT>=23 && ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            return;
        }
        else{
            startActivity(intent);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("Permission", "Request triggered");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission to call granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permission to call not granted", Toast.LENGTH_LONG).show();
                }
            }
            case PERMISSION_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    Toast.makeText(this, "Permission to camera not granted", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST){
            if(resultCode == RESULT_OK){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Bitmap scaledPhoto = Bitmap.createScaledBitmap(photo, 100, 100, true);
                ImageButton imageButton = findViewById(R.id.imageButtonContactPhoto);
                imageButton.setImageBitmap(scaledPhoto);
                currentContact.setPicture(scaledPhoto);
            }
        }
    }
    public void takePhoto(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, PERMISSION_REQUEST_CAMERA);
    }
}
