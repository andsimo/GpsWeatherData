package com.example.gpsweatherdata.gpsweatherdata;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import android.app.ListActivity;

import java.util.ArrayList;

public class Menu_countries extends ListActivity{
    private ArrayList<String> locations;
    private ListView lv;
    private ArrayAdapter<String> arrayAdapter;
    String myitem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countries);

        lv = (ListView) findViewById(android.R.id.list);

        // storing string resources into Array brahejhej
        locations = new ArrayList<String>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int cont = extras.getInt("continent");
            if(cont == 1){
                fillNorthAmerica();
            }
            else if(cont == 2){
                fillEurope();
            }
            else if(cont == 3){
                fillAsia();
            }
            else if(cont == 4){
                fillSouthAmerica();
            }
            else if(cont == 5){
                fillAfrica();
            }
            else if(cont == 6){
                fillAustralia();
            }


        }



        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                locations );

        lv.setAdapter(arrayAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object item = lv.getItemAtPosition(position);
                myitem = item.toString();
                startaAct(myitem);
            }
        });


    }


    public void startaAct(String myitem){
            if(myitem == "Sweden"){
            Intent intent = new Intent(this, Cities.class);
            startActivity(intent);
            }

    }

    public void fillNorthAmerica(){
        locations.add("USA");
        locations.add("Canada");
    }

    public void fillEurope(){
        locations.add("Sweden");
        locations.add("Norway");
        locations.add("Finland");
        locations.add("Germany");
        locations.add("Belgium");
        locations.add("Denmark");
    }

    public void fillAsia(){
        locations.add("China");
        locations.add("India");
        locations.add("Malaysia");
    }

    public void fillSouthAmerica(){
        locations.add("Mexico");
        locations.add("Brazil");
    }

    public void fillAfrica(){
        locations.add("Nigeria");
        locations.add("South Africa");
        locations.add("Algeria");
    }

    public void fillAustralia(){
        locations.add("Australia");
    }

}
