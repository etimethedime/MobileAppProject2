package com.example.chapterproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Chapter4_MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    final int PERMISSION_REQUEST_CODE = 101;
    GoogleMap gMap;
    SensorManager sensorManager;
    Sensor accelerometer;
    Sensor magnetometer;
    TextView tvDirection;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    ArrayList<Contact> contacts = new ArrayList<>();
    Contact currentContact = null;
    RadioButton rbNormal;
    RadioButton rbSatellite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter4_map);
        Bundle extras = getIntent().getExtras();
        try {
            ContactDataSource ds = new ContactDataSource(this);
            ds.open();
            if (extras != null) {
                currentContact = ds.getSpecificContact(extras.getInt("contactid"));
            } else {
                contacts = ds.getContacts("CONTACTNAME", "ASC");
            }
            ds.close();
        } catch (Exception e) {
            Toast.makeText(this, "Contact's not found", Toast.LENGTH_LONG).show();

        }
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (accelerometer == null && magnetometer == null) {
            sensorManager.registerListener(mySensorEventListener,
                    accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
            sensorManager.registerListener(mySensorEventListener,
                    magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(this, "No sensor available", Toast.LENGTH_LONG).show();
            Log.d("MapActivity", "No sesnor found");
        }
        tvDirection = findViewById(R.id.directionTextView);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        rbNormal = findViewById(R.id.radioButtonNormal);
        rbSatellite = findViewById(R.id.radioButtonSatellite);
        rbNormal.setChecked(true);
        createLocationRequest();
        createLocationCallback();

        initContactListButton();
        initMapButton();
        initSettingsButton();
        initMapTypeButtons();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Point size = new Point();
        WindowManager windowManager = getWindowManager();
        windowManager.getDefaultDisplay().getSize(size);
        int measuredWidth = size.x;
        int measuredHeight = size.y;
        if (contacts.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (int i = 0; i < contacts.size(); i++) {
                currentContact = contacts.get(i);

                Geocoder geocoder = new Geocoder(this);
                List<Address> addresses = null;
                String address = currentContact.getStreetAddress() + ", " +
                        currentContact.getCity() + ", " + currentContact.getState() + " "
                        + currentContact.getZipCode();

                try {
                    addresses = geocoder.getFromLocationName(address, 1);
                } catch (Exception e) {
                    Log.d("MapActivity", "Error", e);
                }
                LatLng point = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                builder.include(point);
                gMap.addMarker(new MarkerOptions().position(point).title(currentContact.getContactName()));
            }
            gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), measuredWidth, measuredHeight, 30));
        } else {
            if (currentContact != null) {
                Geocoder geocoder = new Geocoder(this);
                List<Address> addresses = null;
                String address = currentContact.getStreetAddress() + ", " +
                        currentContact.getCity() + ", " + currentContact.getState() + " "
                        + currentContact.getZipCode();
                try {
                    addresses = geocoder.getFromLocationName(address, 1);
                } catch (Exception e) {
                    Log.d("MapActivity", "Error", e);
                }
                LatLng point = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                gMap.addMarker(new MarkerOptions().position(point).title(currentContact.getContactName()).snippet(address));
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("No Data");
                alertDialog.setMessage("No data is available for the mapping function");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            }

        }

        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Snackbar.make(findViewById(R.id.main), "Location permission is needed to display location", Snackbar.LENGTH_INDEFINITE)
                                .setAction("OK", v1 -> ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE)).show();
                    } else {
                        startLocationUpdates();
                    }
                } else {
                    startLocationUpdates();
                }
            } else {
                startLocationUpdates();
            }
        } catch (Exception e) {
            Log.d("MapActivity", "Error", e);
            Toast.makeText(this, "Error. Location not available", Toast.LENGTH_LONG).show();
        }
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .setMinUpdateIntervalMillis(500)
                .build();
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Toast.makeText(getBaseContext(), "Lat: " + location.getLatitude() + " Long: " + location.getLongitude() + " Accuracy: " + location.getAccuracy(), Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        if (gMap != null) {
            gMap.setMyLocationEnabled(true);
        }
    }

    private void stopLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void initMapTypeButtons() {
        RadioGroup rgMapType = findViewById(R.id.radioGroupMapType);

        rgMapType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == rbNormal.getId()) {
                gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            } else if (checkedId == rbSatellite.getId()) {
                gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });
    }

    private void initContactListButton() {
        ImageButton contactListButton = findViewById(R.id.contactListButton);
        contactListButton.setOnClickListener(v -> {
            Intent listIntent = new Intent(this, Chapter4_ContactListActivity.class);
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
            Intent listIntent = new Intent(this, Chapter4_SettingsActivity.class);
            listIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(listIntent);
        });
    }

    private SensorEventListener mySensorEventListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        float[] accelerometerValues;
        float[] magnetometerValues;

        @Override
        public void onSensorChanged(android.hardware.SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                accelerometerValues = event.values;
            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magnetometerValues = event.values;
            }
            if (accelerometerValues != null && magnetometerValues != null) {
                float[] R = new float[9];
                float[] I = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, accelerometerValues, magnetometerValues);
                if (success) {
                    float[] orientations = new float[3];
                    SensorManager.getOrientation(R, orientations);
                    float azimut = orientations[0];
                    if (azimut < 0.0f) {
                        azimut += 360.f;
                    }
                    String direction;
                    if (azimut >= 315 || azimut < 45) {
                        direction = "N";
                    } else if (azimut >= 45 && azimut < 135) {
                        direction = "E";
                    } else if (azimut >= 135 && azimut < 225) {
                        direction = "S";
                    } else {
                        direction = "W";

                    }
                    tvDirection.setText(direction);
                }
            }
        }




    };
}


