package com.example.chapterproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Chapter4_SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter4_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initSettings();
        initSortByClick();
        initSortOrderClick();
        initContactListButton();
        initMapButton();
        initSettingsButton();



    }
    private void initContactListButton() {
        ImageButton contactListButton = findViewById(R.id.contactListButton);
        contactListButton.setOnClickListener(v -> {
            Intent listIntent = new Intent(Chapter4_SettingsActivity.this, Chapter4_ContactListActivity.class);
            listIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(listIntent);
        });
    }
    private void initMapButton() {
        ImageButton mapButton = findViewById(R.id.contactMapButton);
        mapButton.setOnClickListener(v -> {
            Intent listIntent = new Intent(Chapter4_SettingsActivity.this, Chapter4_MapActivity.class);
            listIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(listIntent);
        });
    }
    private void initSettingsButton() {
        ImageButton settingsButton = findViewById(R.id.contactSettingsButton);
        settingsButton.setEnabled(false);
        }

    private void initSettings(){
        String sortBy = getSharedPreferences
                ("MyContactListPreferences", MODE_PRIVATE).getString("sortfield", "contactname");
        String sortOrder = getSharedPreferences
                ("MyContactListPreferences", MODE_PRIVATE).getString("sortorder", "ASC");

        RadioButton rbName = findViewById(R.id.nameSelect);
        RadioButton rbCity = findViewById(R.id.citySelect);
        RadioButton rbBirthday = findViewById(R.id.birthdaySelect);
        if(sortBy.equalsIgnoreCase("contactname")){
            rbName.setChecked(true);
        } else if(sortBy.equalsIgnoreCase("city")){
            rbCity.setChecked(true);
        } else {
            rbBirthday.setChecked(true);
        }

        RadioButton rbAscending = findViewById(R.id.ascendingSelect);
        RadioButton rbDescending = findViewById(R.id.descendingSelect);
        if (sortOrder.equalsIgnoreCase("ASC")){
            rbAscending.setChecked(true);
        } else {
            rbDescending.setChecked(true);
        }
    }

    private void initSortByClick(){
        RadioGroup rgCategory = findViewById(R.id.categoryGroup);
        rgCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbName = findViewById(R.id.nameSelect);
                RadioButton rbCity = findViewById(R.id.citySelect);
                if(rbName.isChecked()){
                    getSharedPreferences("MyContactListPreferences", MODE_PRIVATE).edit().putString("sortfield", "contactname").apply();
                } else if(rbCity.isChecked()){
                    getSharedPreferences("MyContactListPreferences", MODE_PRIVATE).edit().putString("sortfield", "city").apply();
                } else {
                    getSharedPreferences("MyContactListPreferences", MODE_PRIVATE).edit().putString("sortfield", "birthday").apply();
                }
            }
        });
    }

    private void initSortOrderClick(){
        RadioGroup rgOrder = findViewById(R.id.orderGroup);
        rgOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbAscending = findViewById(R.id.ascendingSelect);
                if(rbAscending.isChecked()){
                    getSharedPreferences("MyContactListPreferences", MODE_PRIVATE).edit().putString("sortorder", "ASC").apply();
                } else {
                    getSharedPreferences("MyContactListPreferences", MODE_PRIVATE).edit().putString("sortorder", "DESC").apply();
                }
            }
        });
    }

}