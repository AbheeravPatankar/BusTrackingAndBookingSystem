package com.example.bustrackingandbookingsystem;

//import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.annotation.SuppressLint;
//import android.content.Intent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;


//import android.location.Location;

import android.os.Bundle;
import android.os.Looper;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class TrackingActivity extends AppCompatActivity {

    LocationRequest locationRequest;

    String busNo ;

    Button backToSelectBus;
    int requestCode = 1003;
    DatabaseReference firebaseDatabase;
    FusedLocationProviderClient fusedLocationProviderClient;
    LatLng initial,updated ;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            if (locationResult == null)
            {
                    return;
            }
            else{
               Toast.makeText((TrackingActivity.this), "your current updated"+locationResult.getLocations(), Toast.LENGTH_LONG).show();
                updated = new LatLng(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude(),1,123);

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("busId", 123);
                map.put("flag",1);
                map.put("latitude", locationResult.getLastLocation().getLatitude());
                map.put("longitude", locationResult.getLastLocation().getLongitude());
                firebaseDatabase.child(busNo).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(TrackingActivity.this, "completed", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(TrackingActivity.this, "success", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TrackingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);


        HashMap initialLatLng = new HashMap();
        initialLatLng.put("latitude",0.000);
        initialLatLng.put("longitude",0.000);
        initialLatLng.put("flag",0.000);
        initialLatLng.put("busId",456);

        busNo =  getIntent().getStringExtra("busNum");//"Bus4";
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Buses");
        firebaseDatabase.child(busNo).updateChildren(initialLatLng).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TrackingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        backToSelectBus = findViewById(R.id.backBtn2);
        locationRequest = new LocationRequest.Builder(5000)
                .setGranularity(Granularity.GRANULARITY_FINE)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();

        backToSelectBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selectBusIntent = new Intent(TrackingActivity.this, MainActivity.class);
                startActivity(selectBusIntent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            //permission granted
            changeLocationSettings();
        } else {
            askLocationPermission();
        }
    }
    protected void onStop() {

        super.onStop();

       // LatLng newLatLng = new LatLng(00.00,0000.000,0,123);
        stopLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback , Looper.getMainLooper());
    }

    private void stopLocationUpdates(){

        Toast.makeText(TrackingActivity.this, "in here", Toast.LENGTH_LONG).show();
        HashMap updateLatLng = new HashMap();
        updateLatLng.put("latitude",updated.getLatitude());
        updateLatLng.put("longitude",updated.getLongitude());
        updateLatLng.put("flag",0);
        updateLatLng.put("busId",123);
        firebaseDatabase.child(busNo).updateChildren(updateLatLng).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TrackingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void changeLocationSettings(){

        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> locationSettingsResponseTask = settingsClient.checkLocationSettings(locationSettingsRequest);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });
        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException){
                    ResolvableApiException resolvableApiException =(ResolvableApiException)e;
                    try {
                        resolvableApiException.startResolutionForResult(TrackingActivity.this,22);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });
    }
    public void askLocationPermission () {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this, "you need this permission because ......", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},requestCode);
            }else{
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},requestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode >= 0 && grantResults[0]== 0){
            //permission granted
            changeLocationSettings();

        }

    }
}


















