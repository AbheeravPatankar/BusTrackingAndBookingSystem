package com.example.maptrackingactivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.maptrackingactivity.databinding.ActivityTrackingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TrackingActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityTrackingBinding binding;


    Marker userLocationMarker;
    CameraUpdate cameraUpdate;
    DatabaseReference databaseReference;
    private Handler mHandler = new Handler();

    String result;
    String latitude;
    String longitude;
    LatLng latlng;
    String bus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTrackingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Buses");


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latlng = new LatLng(0, 0);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng);
        bus = getIntent().getStringExtra("bus");
        userLocationMarker = mMap.addMarker(markerOptions);
        int flag = 1;
        if (flag == 1) {
            runnable.run();
        }else{
            stopRepeating();
        }
    }


    public void stopRepeating(){
        mHandler.removeCallbacks(runnable);

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            databaseReference.child(bus).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    DataSnapshot dataSnapshot = task.getResult();
                    result = String.valueOf(dataSnapshot.child("flag").getValue());
                    latitude = String.valueOf(dataSnapshot.child("latitude").getValue());
                    longitude = String.valueOf(dataSnapshot.child("longitude").getValue());
                    // Toast.makeText(TrackingActivity.this, "complete", Toast.LENGTH_LONG).show();
                    trackBus1();
                    mHandler.postDelayed(runnable,1000);


                }
            });

        }
    };




    private void trackBus1(){
            double lat = Double.parseDouble(latitude);
            double lng = Double.parseDouble(longitude);
            latlng = new LatLng(lat,lng);
            if(userLocationMarker == null){
                //create new marker
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latlng);
                userLocationMarker = mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17));
       }else{
                userLocationMarker.setPosition(latlng);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17));
            }
    }


}