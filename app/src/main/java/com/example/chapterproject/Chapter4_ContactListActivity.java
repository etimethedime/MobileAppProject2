package com.example.chapterproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
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

    ArrayList<Contact> contacts;
    ContactAdapter contactAdapter;

    private View.OnClickListener onItemClickListener = view -> {
        RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
        int position = viewHolder.getAdapterPosition();
        long contactID = contacts.get(position).getId();
        Intent intent = new Intent(Chapter4_ContactListActivity.this, Chapter4_MyContactsActivity.class);
        intent.putExtra("contactId", contactID);
        startActivity(intent);
    };

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
        initAddContactButton();
        initDeleteSwitch();

        ContactDataSource ds = new ContactDataSource(Chapter4_ContactListActivity.this);

        try {
            ds.open();
            contacts = ds.getContacts();
            ds.close();
            RecyclerView contactList = findViewById(R.id.contactRV);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            contactList.setLayoutManager(layoutManager);

            contactAdapter = new ContactAdapter(contacts, this);
            contactList.setAdapter(contactAdapter);

            contactAdapter.setOnItemClickListener(onItemClickListener);
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_SHORT).show();
        }

    }

    private void initContactListButton() {
        ImageButton contactListButton = findViewById(R.id.contactListButton);
        contactListButton.setEnabled(false);
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

    private void initAddContactButton() {
        Button addContact = findViewById(R.id.buttonAddContact);
        addContact.setOnClickListener(v -> {
            Intent intent = new Intent(Chapter4_ContactListActivity.this, Chapter4_MyContactsActivity.class);
            startActivity(intent);
        });
    }

    private void initDeleteSwitch() {
        Switch deleteSwitch = findViewById(R.id.deleteSwtich);
        deleteSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (contactAdapter != null) {
                contactAdapter.setDelete(isChecked);
                contactAdapter.notifyDataSetChanged();
            }
        });
    }
}
