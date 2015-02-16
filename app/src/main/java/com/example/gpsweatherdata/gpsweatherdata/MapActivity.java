package com.example.gpsweatherdata.gpsweatherdata;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/*
Klass som använder google maps v2 api för att skapa ett fragment som vi kan "måla" prickar på.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback{

    private static final String LOCATION_DATA = "locationdata";
    private static final String LOCATION_LIST = "locationlist";

    private GoogleMap map; // <------ DENNA LÄGGER MAN TILL MARKERS PÅ...
    private ArrayList<Location> locations;
    private ArrayList<MarkerOptions> markerArray;
    private boolean newlyUpdated;
    private long timeStamp;
    private MapFragment mapFragment; // <----- DET ÄR DENNA MAN SKALL ÄNDRA INSTÄLLNINGAR PÅ INNAN!


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        findViewById(R.id.spinner).setVisibility(View.GONE);

        initMap();


        Intent intent = this.getIntent();               //Plockar ut intenten som kommer från mainactivity.
        newlyUpdated = intent.getExtras().getBoolean("new");

        locations = loadSP();
    }


    public void initMap(){

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);


        if(mapFragment != null){
        UiSettings mapSettings = mapFragment.getMap().getUiSettings();

        mapSettings.setRotateGesturesEnabled(false);
        mapSettings.setTiltGesturesEnabled(false);
        mapSettings.setMyLocationButtonEnabled(true);
        mapFragment.getMap().setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mapFragment.getMap().setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if(mapFragment.getMap().getMapType() == GoogleMap.MAP_TYPE_NORMAL)
                    mapFragment.getMap().setMapType(GoogleMap.MAP_TYPE_HYBRID);
                else
                    mapFragment.getMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
        }

        mapFragment.getMapAsync(this);


    }


    /*
    Laddar in lagrad data i locations igen.
     */

    public ArrayList<Location> loadSP(){
        System.out.println("Loading...");
        ArrayList<Location> tempList;
        SharedPreferences data = getSharedPreferences(LOCATION_DATA, 0);

        timeStamp = data.getLong("time", 0);

        if(data.contains(LOCATION_LIST)){
            String json = data.getString(LOCATION_LIST, "");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Location>>(){}.getType();
            tempList = gson.fromJson(json, type);

        }else{
            return null;
        }
        return tempList;
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
        if(newlyUpdated || (timeStamp + 3600) > (System.currentTimeMillis() / 1000L))  //Senaste uppdateringen + 1 timme > just nu
            refreshWeather();
        else
            addMarkers();


        //new WeatherTask().execute();
    }


    /*
     * Går igenom listan och lägger till en marker för varje position. Klickar man på markören visas antalet sensorer för platsen.
     */
    public void addMarkers(){


        if(locations != null) {
            for (Location location : locations) {
/*
                MarkerOptions mark = new MarkerOptions()
                        .position(new LatLng(location.getLat(), location.getLong()))
                        .title("Sensors: " + location.getNumSensors() +
                                " \n Cloudiness: " + location.getCloudiness() +
                                " \n Lat: " + location.getLat() +
                                " \n Long: " + location.getLong()));

                markerArray.add(mark);
                map.addMarker(markerArray.get(0));*/  //TESTNING PÅGÅR!




                map.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLat(), location.getLong()))
                        .title("Sensors: " + location.getNumSensors() +
                                " \n Cloudiness: " + location.getCloudiness() +
                                " \n Lat: " + location.getLat() +
                                " \n Long: " + location.getLong()));
            }
        }
    }

    public void addMarker(Location location){

        if(location != null) {

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLat(), location.getLong()))
                    .title("Sensors: " + location.getNumSensors() +
                            " \n Cloudiness: " + location.getCloudiness() +
                            " \n Lat: " + location.getLat() +
                            " \n Long: " + location.getLong())
                    .icon(BitmapDescriptorFactory.defaultMarker(100 - location.getCloudiness())));
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

        int i = 0;
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
                        i++;

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
           if(i > 0)
                Toast.makeText(getApplicationContext(), i+" locations were loaded.", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        saveSP();
    }


    /*
    Sparar undan data från locations, samt en timestamp så att programmet vet när det senast var aktivt.
     */
    public void saveSP(){
        SharedPreferences data = getSharedPreferences(LOCATION_DATA, 0);
        SharedPreferences.Editor editor = data.edit();

        Gson gson = new Gson();
        String json = gson.toJson(locations);
        editor.putString(LOCATION_LIST, json);
        editor.putLong("lastTime", System.currentTimeMillis());
        editor.commit();
        System.out.println("Saving...");
    }

    public void updateWeather(View v){
        refreshWeather();
    }

    public void refreshWeather(){
        map.clear();
        new WeatherTask().execute();
    }




}
