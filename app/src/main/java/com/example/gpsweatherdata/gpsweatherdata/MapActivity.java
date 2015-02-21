package com.example.gpsweatherdata.gpsweatherdata;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


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
    private ListView mDrawerList;
    private String[] mListContent;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        findViewById(R.id.spinner).setVisibility(View.GONE);

        locations = loadSP();

        initDrawer();
        initMap();




        Intent intent = this.getIntent();               //Plockar ut intenten som kommer från mainactivity.
        newlyUpdated = intent.getExtras().getBoolean("new");


    }


    /*
    Initiates the drawer.
     */
    public void initDrawer() {
        mListContent = new String[]{"one", "two", "three"};
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_layout, mListContent));

        //mDrawerList.setOnClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Toast.makeText(MapActivity.this, "Open", Toast.LENGTH_SHORT).show();
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Toast.makeText(MapActivity.this, "Close", Toast.LENGTH_SHORT).show();
                invalidateOptionsMenu();

            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //mDrawerLayout.bringToFront();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }





    /*
    Initierar kartan med önskade inställningar och skapar en onlongclicklistener som byter mellan maytype hybrid och normal.
    Startar slutligen en asynctask som faktiskt skapar kartan.
     */
    public void initMap(){
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        if(mapFragment != null){
        UiSettings mapSettings = mapFragment.getMap().getUiSettings();

        mapSettings.setRotateGesturesEnabled(false);
        mapSettings.setTiltGesturesEnabled(false);
        mapSettings.setMyLocationButtonEnabled(true);
        mapSettings.setZoomControlsEnabled(true);
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

        mapFragment.getMap().setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){

            @Override
            public void onInfoWindowClick(Marker marker) {
                //DO SOMETHING DIALOG?
                Toast.makeText(MapActivity.this, "infoClicked", Toast.LENGTH_SHORT).show();

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

        timeStamp = data.getLong("lastTime", 0);


        Toast.makeText(this, "Last updated: " + getTimeLastUpdate(), Toast.LENGTH_LONG).show();

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

    /*
    Returnerar tiden då databasen senast uppdaterades.
     */
    public String getTimeStampUTC(Long time){
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm z");
        return sdf.format(date);
    }

    public String getTimeLastUpdate(){
        Date date = new Date(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        return sdf.format(date);
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
        if(newlyUpdated || (timeStamp + 3600) < (System.currentTimeMillis() / 1000L))  //Senaste uppdateringen + 1 timme > just nu
            refreshWeather();
        else
            addMarkers();
    }


    /*
     * Går igenom listan och lägger till en marker för varje position. Klickar man på markören visas antalet sensorer för platsen.
     */
    public void addMarkers(){

        if(locations != null) {
            for (Location location : locations) {
                addMarker(location);
            }
        }
        else{
            Toast.makeText(this, "No locations loaded", Toast.LENGTH_LONG).show();
        }
    }

    public void addMarker(Location location){

        if(location != null) {

            MarkerOptions mark = new MarkerOptions()
                    .position(new LatLng(location.getLat(), location.getLong()))
                    .title("Location")
                    .snippet("Daytime: " + location.getTime() + "\nSunrise: " + getTimeStampUTC(location.getSunrise()) + "  Sunset: " + getTimeStampUTC(location.getSunset()) +
                    " \nLat: " + location.getLat() + "\nLong: " + location.getLong() + "\nCloudiness: " + location.getCloudiness());
                    //.title("sunrise: " + location.getSunrise() + "  sunset: " + location.getSunset() + "  cloudiness: " + location.getCloudiness() + " day: " + location.getTime());
                    /*.title("Sensors: " + location.getNumSensors() +
                            " \n Cloudiness: " + location.getCloudiness() +
                            " \n Lat: " + location.getLat() +
                            " \n Long: " + location.getLong());*/

            if(!location.getTime()){
                mark.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            }
            else {
                if (location.getCloudiness() <= 25)
                    mark.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                else if (location.getCloudiness() >= 50)
                    mark.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                else if ((location.getCloudiness() > 25) && (location.getCloudiness() < 50))
                    mark.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

                else {
                    mark.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    mark.title("Sensors: " + location.getNumSensors() +
                            " \n Cloudiness: ??" +
                            " \n Lat: " + location.getLat() +
                            " \n Long: " + location.getLong());
                }
            }

           // markerArray.add(mark);
            map.addMarker(mark);

        }
    }


    private void renderMarkerArray(){
        //map
        mapFragment.getMap().clear();
        for(MarkerOptions mark : markerArray){
            mapFragment.getMap().addMarker(mark);
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
            long timeNow = System.currentTimeMillis() / 1000L;

            if(locations != null) {
                for (Location location : locations) {
                    if(wc.getWeather(location))


                        //location.setDay(timeNow > location.getSunrise() && timeNow < location.getSunset());
                        //Beräknar ifall det är dagtid
                        if(timeNow > location.getSunrise() && timeNow < location.getSunset() )
                            location.setDay(true);
                        else
                            location.setDay(false);

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
       // markerArray.clear();
        new WeatherTask().execute();
    }




}
