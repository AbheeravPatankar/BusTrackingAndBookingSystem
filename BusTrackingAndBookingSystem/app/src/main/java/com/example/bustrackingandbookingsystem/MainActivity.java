package com.example.bustrackingandbookingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    TextView description;
    Spinner selectBus;
    Button startUpdates;
    String selectedBus;
    DatabaseReference firebaseDatabase;
    int ifSelected;
    int isBusInAction;
    String[] routes = {"Bus1","Bus2","Bus3","Bus4","Bus5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        description = findViewById(R.id.textView);
        selectBus = findViewById(R.id.selectRoute);
        startUpdates = findViewById(R.id.startUpdates);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Buses");

        ArrayAdapter<String> adapterSourceBusStops = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item,routes);
        adapterSourceBusStops.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectBus.setAdapter(adapterSourceBusStops);

        selectBus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               selectedBus = selectBus.getSelectedItem().toString();

               firebaseDatabase.child(selectedBus).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DataSnapshot> task) {
                    DataSnapshot dataSnapshot = task.getResult();
                    isBusInAction = Integer.parseInt(String.valueOf(dataSnapshot.child("flag").getValue()));
                    if(isBusInAction == 0)
                    {
                        Toast.makeText(MainActivity.this, selectedBus, Toast.LENGTH_LONG).show();
                        ifSelected = 1;
                    }else
                        Toast.makeText(MainActivity.this,"This Bus is already selected.Select another bus",Toast.LENGTH_SHORT).show();

                   }
               });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        startUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ifSelected == 1){
                    Intent intent = new Intent(MainActivity.this,TrackingActivity.class);
                    intent.putExtra("busNum",selectedBus);
                    startActivity(intent);
                }else
                    Toast.makeText(MainActivity.this, "please select a bus", Toast.LENGTH_LONG).show();
            }
        });


    }
}