package com.example.maptrackingactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    String[] busStops = {"A","B","C"};
    String srcBusStop;
    String destBusStop,key;
    Button findBusesBtn;

    Map<String, List<String>> mapSrcDestBusStops_Buses = new HashMap<String, List<String>>();


    Spinner spinner1,spinner2;


    private void populate(){
        List<String> buses = new ArrayList<String>();
        buses.add("Bus1");
        buses.add("Bus3");
        buses.add("Bus5");
        mapSrcDestBusStops_Buses.put("A_C",buses);

        List<String> buses2 = new ArrayList<String>();
        buses2.add("Bus2");
        buses2.add("Bus4");
        mapSrcDestBusStops_Buses.put("A_B",buses2);

        List<String> buses3 = new ArrayList<String>();
        buses3.add("Bus1");
        buses3.add("Bus2");
        buses3.add("Bus5");
        mapSrcDestBusStops_Buses.put("B_C",buses3);

        List<String> buses4 = new ArrayList<String>();
        buses4.add("Bus1");
        buses4.add("Bus2");
        buses4.add("Bus4");
        mapSrcDestBusStops_Buses.put("C_B",buses4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findBusesBtn= findViewById(R.id.findBuses);

        spinner1 = findViewById(R.id.ddSourceBusSTop);
        spinner2 = findViewById(R.id.ddDestBusSTop);

        populate();

        ArrayAdapter<String> adapterSourceBusStops = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item,busStops);
        adapterSourceBusStops.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapterSourceBusStops);

        spinner2.setAdapter(adapterSourceBusStops);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                srcBusStop = spinner1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                destBusStop = spinner2.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        findBusesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent( MainActivity.this , SelectBusActivity.class);
                key = srcBusStop+"_"+destBusStop;
                List<String> lstOfAvailableBuses = mapSrcDestBusStops_Buses.get(key);
                String arrayBuses[] = lstOfAvailableBuses.toArray(new String[0]);
                intent1.putExtra("lstOfAvailableBuses",arrayBuses);

                startActivity(intent1);
            }
        });
    }
}


















