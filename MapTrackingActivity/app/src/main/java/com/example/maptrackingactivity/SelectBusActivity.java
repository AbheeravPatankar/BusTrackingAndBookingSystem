package com.example.maptrackingactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SelectBusActivity extends AppCompatActivity {

    Button backBtn;
    Button trackOnMapBtn ;
    Button bookTicketBtn ;
    String bus;
    ListView busesAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bus2);

        backBtn = findViewById(R.id.backBtn);

        bookTicketBtn =  findViewById(R.id.bookticketBtn);
        String[] buses =   getIntent().getStringArrayExtra("lstOfAvailableBuses");
        busesAvailable = findViewById(R.id.selectBusList);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBackIntent = new Intent(SelectBusActivity.this,MainActivity.class);
                startActivity(goBackIntent);
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,buses);
        busesAvailable.setAdapter(arrayAdapter);

      // trackOnMapBtn.setOnClickListener(new View.OnClickListener() {
      //     @Override
      //     public void onClick(View view) {
      //
      //     }
      // });

        busesAvailable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

              Intent mapIntent= new Intent(SelectBusActivity.this, TrackingActivity.class);
              bus = buses[position];
              mapIntent.putExtra("bus",bus);
              startActivity(mapIntent);
            }
        });
    }


}