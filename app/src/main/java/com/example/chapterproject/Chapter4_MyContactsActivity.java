package com.example.chapterproject;

import android.content.Intent;
import android.os.Bundle;
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

public class Chapter4_MyContactsActivity extends AppCompatActivity implements DatePickerDialogue.SaveDateListener {

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
    }
}
