package com.example.chapterproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Chapter4_ContactListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter4_contact_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initContactListButton();
        initMapButton();
        initSettingsButton();

        ContactDataSource ds = new ContactDataSource(Chapter4_ContactListActivity.this);
        ArrayList<String> names;

        try {
            ds.open();
            names = ds.getContactNames();
            ds.close();
            RecyclerView contactList = findViewById(R.id.contactRV);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            contactList.setLayoutManager(layoutManager);
            ContactAdapter adapter = new ContactAdapter(names);
            contactList.setAdapter(adapter);
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_SHORT).show();
        }

    }
    private void initContactListButton() {
        ImageButton settingsButton = findViewById(R.id.contactSettingsButton);
        settingsButton.setEnabled(false);
    }
    private void initMapButton() {
        ImageButton mapButton = findViewById(R.id.contactMapButton);
        mapButton.setOnClickListener(v -> {
            Intent listIntent = new Intent(Chapter4_ContactListActivity.this, Chapter4_MapActivity.class);
            listIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(listIntent);
        });
    }
    private void initSettingsButton() {
        ImageButton settingsButton = findViewById(R.id.contactSettingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent listIntent = new Intent(Chapter4_ContactListActivity.this, Chapter4_SettingsActivity.class);
            listIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(listIntent);
        });
    }

}