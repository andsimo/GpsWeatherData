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


import android.app.ListActivity;

import java.util.ArrayList;

public class Menu_countries extends ListActivity{
    private ArrayList<String> locations;
    private ListView lv;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countries);

        lv = (ListView) findViewById(android.R.id.list);

        // storing string resources into Array
        locations = new ArrayList<String>();

        locations.add("1");
        locations.add("2");
        locations.add("3");
        locations.add("4");
        locations.add("5");
        locations.add("6");
        locations.add("7");
        locations.add("8");
        locations.add("9");
        locations.add("10");
        locations.add("10");
        locations.add("10");
        locations.add("10");
        locations.add("10");
        locations.add("10");
        locations.add("10");
        locations.add("10");
        locations.add("10");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                locations );

        lv.setAdapter(arrayAdapter);
    }

}
