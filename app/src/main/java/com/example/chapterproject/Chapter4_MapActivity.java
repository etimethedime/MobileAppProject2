package com.example.chapterproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.List;

public class Chapter4_MapActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener gpsListener;
    LocationListener networkListener;
    Location currentBestLocation;
    final int PERMISSION_REQUEST_CODE = 101;

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
        initGetLocationButton();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            locationManager.removeUpdates(gpsListener);
            locationManager.removeUpdates(networkListener);
        } catch (SecurityException e) {
            Toast.makeText(getBaseContext(), "Error. Location not available", Toast.LENGTH_LONG).show();
            Log.d("MapActivity", "Location Still Running", e);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startLocationUpdates();
                }
                else{
                    Toast.makeText(getBaseContext(), "Location permission denied", Toast.LENGTH_LONG).show();
                }
            }
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
            try {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(Chapter4_MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(Chapter4_MapActivity.this,
                                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                            Snackbar.make(findViewById(R.id.main), "Location permission is needed to display location", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("OK", v1 -> ActivityCompat.requestPermissions(Chapter4_MapActivity.this, new String[]
                                            {android.Manifest.permission.ACCESS_FINE_LOCATION}, 1)).show();

                        } else {
                            ActivityCompat.requestPermissions(Chapter4_MapActivity.this, new String[]
                                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        }
                    } else {
                        startLocationUpdates();
                    }
                } else {
                    startLocationUpdates();
                }
            } catch (Exception e) {
                Log.d("MapActivity", "Error", e);
                Toast.makeText(getBaseContext(), "Error. Location not available", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
//        EditText streetText = findViewById(R.id.mapStreetText);
//        EditText cityText = findViewById(R.id.mapCityText);
//        EditText stateText = findViewById(R.id.mapStateText);
//        EditText zipText = findViewById(R.id.mapZipText);
//
//        String address = streetText.getText().toString() + " " +
//                cityText.getText().toString() + " " +
//                stateText.getText().toString() + " " +
//                zipText.getText().toString();

        List<Address> addresses = null;
        Geocoder geo = new Geocoder(Chapter4_MapActivity.this);
        try {
            locationManager = (LocationManager) getBaseContext().getSystemService(LOCATION_SERVICE);

            gpsListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (isBetterLocation(location)) {
                        currentBestLocation = location;
//                        TextView latitudeText = findViewById(R.id.latitudeText);
//                        TextView longitudeText = findViewById(R.id.longitudeText);
//                        TextView accuracyText = findViewById(R.id.accuracyText);
//                        latitudeText.setText("" + location.getLatitude());
//                        longitudeText.setText("" + location.getLongitude());
//                        accuracyText.setText("" + location.getAccuracy() + " meters");
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            };
            networkListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (isBetterLocation(location)) {
                        currentBestLocation = location;
//                        TextView latitudeText = findViewById(R.id.latitudeText);
//                        TextView longitudeText = findViewById(R.id.longitudeText);
//                        TextView accuracyText = findViewById(R.id.accuracyText);
//                        latitudeText.setText("" + location.getLatitude());
//                        longitudeText.setText("" + location.getLongitude());
//                        accuracyText.setText("" + location.getAccuracy() + " meters");
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            };
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error. Location not available", Toast.LENGTH_LONG).show();
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,gpsListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,networkListener);
    }

    private boolean isBetterLocation(Location location){
        boolean isBetter = false;
        if(currentBestLocation == null){
            isBetter = true;
        }
        else if(location.getAccuracy() <= currentBestLocation.getAccuracy()){
            isBetter = true;
        }
        else if(location.getTime() - currentBestLocation.getAccuracy() > 5*6*1000){
            isBetter = true;
        }
        return isBetter;
    }





}