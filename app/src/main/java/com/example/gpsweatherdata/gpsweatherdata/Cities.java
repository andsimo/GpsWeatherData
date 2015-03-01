package com.example.gpsweatherdata.gpsweatherdata;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

public class Cities extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cities);



        // get the listview ss
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        Intent intent = getIntent();
        country = intent.getExtras().getString("country");
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data    //Här ska sedan städers namn i det valda landet listas.
        listDataHeader.add("" + country);
        listDataHeader.add("Göteborg");
        listDataHeader.add("Stockholm");
        listDataHeader.add("Malmö");
        listDataHeader.add("Västerås");
        listDataHeader.add("Örebro");
        listDataHeader.add("Mölndal");
        listDataHeader.add("Umeå");
        listDataHeader.add("Växjö");
        listDataHeader.add("Kalmar");



        // Adding child data        //Gör om så att det skapas en view likt denna under varje stad där all info placeras.
        List<String> top250 = new ArrayList<String>();
        top250.add("Installation date: 2014-02-01, S/V: 1.3, Sensor Value: Hej");


        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("Installation date: 2013-05-20, S/V: 1.2, Sensor Value: Hej2");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("Installation date: 2014-11-10, S/V: 1.4, Sensor Value: Hej3");


        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}