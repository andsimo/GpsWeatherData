package com.example.gpsweatherdata.gpsweatherdata;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/*
Klass som använder google maps v2 api för att skapa ett fragment som vi kan "måla" prickar på.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap map;
    private ArrayList<Location> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        findViewById(R.id.spinner).setVisibility(View.GONE);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = this.getIntent();               //Plockar ut intenten som kommer från mainactivity.
        locations = intent.getParcelableArrayListExtra("locations");    //Placeras i en arraylist.
        new WeatherTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     *Måste vänta på att kartan är redo innan man får börja måla på den.
     */
    @Override
    public void onMapReady(GoogleMap gMap) {
        map = gMap;
        //addMarkers();
        //addCircles();   //Välj mellan markörer och cirklar. Måste förfinas!


    }


    /*
    Går igenom listan och lägger till en marker för varje position. Klickar man på markören visas antalet sensorer för platsen.
     */
    public void addMarkers(){

        if(locations != null) {
            for (int i = 0; i < locations.size(); i++) {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(locations.get(i).getLat(), locations.get(i).getLong()))
                        .title("Sensors: " + locations.get(i).getNumSensors() + " \n Cloudiness: " + locations.get(i).getCloudiness()));
            }
        }
    }

    public void addMarker(Location location){

        if(location != null) {

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLat(), location.getLong()))
                    .title("Sensors: " + location.getNumSensors() + " \n Cloudiness: " + location.getCloudiness()));
        }
    }

    /*
    Gör samma som ovan fast med cirklar. Får en orange/gul färg. Radiusen på cirkeln beror på antalet sensors med en faktor 0.2
     */
    public void addCircles(){
        for(int i = 0; i < locations.size(); i++) {
            map.addCircle(new CircleOptions()
                    .center(new LatLng(locations.get(i).getLat(), locations.get(i).getLong()))
                    .radius(10000 + 10000 * 0.2* locations.get(i).getNumSensors())
                    .strokeColor(Color.rgb(255, 169, 20))
                    .fillColor(Color.rgb(255, 169, 20)));
        }
    }



    private class WeatherTask extends AsyncTask<Void, Location, Void>{

        @Override
        protected void onPreExecute(){
            findViewById(R.id.spinner).setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            WeatherCollector wc = new WeatherCollector();

            if(locations != null) {
                for (Location location : locations) {
                    if(wc.getWeather(location))
                        publishProgress(location);

                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Location... progress){
            addMarker(progress[0]);
        }

        @Override
        protected void onPostExecute(Void result){
           findViewById(R.id.spinner).setVisibility(View.GONE);
           //addMarkers();
        }
    }



}
