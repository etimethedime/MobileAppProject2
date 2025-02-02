package com.example.chapterproject;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.activity.EdgeToEdge;
import android.text.format.DateFormat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;

public class Chapter4_MyContactsActivity extends AppCompatActivity implements DatePickerDialogue.SaveDateListener {
    private Contact currentContact;

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
        currentContact = new Contact();

        initContactListButton();
        initMapButton();
        initSettingsButton();
        initToggleButton();
        setForEditing(false);
        initChangeDateButton();

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

    private void initToggleButton() {
        ToggleButton toggleButton = findViewById(R.id.onOffButton);
        toggleButton.setOnClickListener(v -> {
            setForEditing(toggleButton.isChecked());
        });
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

       editName.setEnabled(enabled);
       editStreet.setEnabled(enabled);
       editCity.setEnabled(enabled);
       editState.setEnabled(enabled);
       editZipcode.setEnabled(enabled);
       editCell.setEnabled(enabled);
       editHome.setEnabled(enabled);
       editEmail.setEnabled(enabled);
       editBirthday.setEnabled(enabled);

       if (enabled) {
           editName.requestFocus();
       }

    }

    private void initChangeDateButton() {
        Button changeDateButton = findViewById(R.id.changeBirthdayButton);
        changeDateButton.setOnClickListener(v -> {
            DatePickerDialogue datePickerDialogue = new DatePickerDialogue();
            datePickerDialogue.show(getSupportFragmentManager(), "date picker");
        });
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
                if (!s.toString().equals(formattedNumber)) {
                    etCell.setText(formattedNumber);
                    etCell.setSelection(formattedNumber.length());
                }
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
                if (!s.toString().equals(formattedNumber)) {
                    etHome.setText(formattedNumber);
                    etHome.setSelection(formattedNumber.length());
                }
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

            }
        });
        etHome.addTextChangedListener(new PhoneNumberFormattingTextWatcher(){

        });
    }

}
