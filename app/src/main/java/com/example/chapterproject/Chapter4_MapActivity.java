package com.example.chapterproject;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.List;

public class Chapter4_MapActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener gpsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter4_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initContactListButton();
        initMapButton();
        initSettingsButton();
    }

    @Override
    public void onPause() {
        super.onPause();
        try{
            locationManager.removeUpdates(gpsListener);
        } catch (SecurityException e) {
            Toast.makeText(getBaseContext(), "Error. Location not available", Toast.LENGTH_LONG).show();
            Log.d("MapActivity", "Location Still Running", e);
        }
    }

    private void initContactListButton() {
        ImageButton contactListButton = findViewById(R.id.contactListButton);
        contactListButton.setOnClickListener(v -> {
            Intent listIntent = new Intent(Chapter4_MapActivity.this, Chapter4_ContactListActivity.class);
            listIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(listIntent);
        });
    }
    private void initMapButton() {
        ImageButton mapButton = findViewById(R.id.contactMapButton);
        mapButton.setEnabled(false);
    }
    private void initSettingsButton() {
        ImageButton settingsButton = findViewById(R.id.contactSettingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent listIntent = new Intent(Chapter4_MapActivity.this, Chapter4_SettingsActivity.class);
            listIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(listIntent);
        });
    }

    private void initGetLocationButton() {
        Button locationButton = findViewById(R.id.buttonLocate);
        locationButton.setOnClickListener(v -> {
            EditText streetText = findViewById(R.id.mapStreetText);
            EditText cityText = findViewById(R.id.mapCityText);
            EditText stateText = findViewById(R.id.mapStateText);
            EditText zipText = findViewById(R.id.mapZipText);

            String address = streetText.getText().toString() + " " +
                    cityText.getText().toString() + " " +
                    stateText.getText().toString() + " " +
                    zipText.getText().toString();

            List<Address> addresses = null;
            Geocoder geo = new Geocoder(Chapter4_MapActivity.this);
            try {
                locationManager = (LocationManager) getBaseContext().getSystemService(LOCATION_SERVICE);

                gpsListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(android.location.Location location) {
                        TextView latitudeText = findViewById(R.id.latitudeText);
                        TextView longitudeText = findViewById(R.id.longitudeText);
                        TextView accuracyText = findViewById(R.id.accuracyText);

                        latitudeText.setText("Latitude: " + location.getLatitude());
                        longitudeText.setText("Longitude: " + location.getLongitude());
                        accuracyText.setText("Accuracy: " + location.getAccuracy() + " meters");
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {}
                    @Override
                    public void onProviderEnabled(String provider) {}
                    @Override
                    public void onProviderDisabled(String provider) {}
                };
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Error. Location not available", Toast.LENGTH_LONG).show();
            }

            TextView latitudeText = findViewById(R.id.latitudeText);
            TextView longitudeText = findViewById(R.id.longitudeText);
            TextView accuracyText = findViewById(R.id.accuracyText);

            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                latitudeText.setText("Latitude: " + location.getLatitude());
                longitudeText.setText("Longitude: " + location.getLongitude());
            } else {
                latitudeText.setText("Latitude: Not Found");
                longitudeText.setText("Longitude: Not Found");
            }
        });
    }




}