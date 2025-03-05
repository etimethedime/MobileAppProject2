package com.example.chapterproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
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
    RecyclerView contactList;
    private BroadcastReceiver batteryReceiver;


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
        String sortField = getSharedPreferences("MyContactListPreferences", MODE_PRIVATE).getString("sortfield", "contactname");
        String sortOrder = getSharedPreferences("MyContactListPreferences", MODE_PRIVATE).getString("sortorder", "ASC");

        try {
            ds.open();
            contacts = ds.getContacts(sortField, sortOrder);
            ds.close();
            contactList = findViewById(R.id.contactRV);
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
    @Override
    public void onResume() {
        super.onResume();

        batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                double levelScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
                int batteryPercent = (int) Math.floor(batteryLevel / levelScale * 100);

                TextView tvBatteryLevel = findViewById(R.id.directionTextView);
                tvBatteryLevel.setText(batteryPercent + "%");
            }
        };

        IntentFilter batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, batteryFilter);

        String sortBy = getSharedPreferences("MyContactListPreferences", MODE_PRIVATE)
                .getString("sortfield", "contactname");
        String sortOrder = getSharedPreferences("MyContactListPreferences", MODE_PRIVATE)
                .getString("sortorder", "ASC");

        ContactDataSource ds = new ContactDataSource(Chapter4_ContactListActivity.this);
        try {
            ds.open();
            contacts = ds.getContacts(sortBy, sortOrder);
            ds.close();
            contactList.setAdapter(contactAdapter);
            contactAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
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
