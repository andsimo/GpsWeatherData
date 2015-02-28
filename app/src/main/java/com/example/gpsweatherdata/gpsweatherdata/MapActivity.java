package com.example.gpsweatherdata.gpsweatherdata;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
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
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/*
Klass som använder google maps v2 api för att skapa ett fragment som vi kan "måla" prickar på.
Latitud Longitud alltid i den ordningen!
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback{

    private static final String LOCATION_DATA = "locationdata";
    private static final String LOCATION_LIST = "locationlist";
    private static final String LAST_ACTIVE = "lastActive";
    private static final String PARANS_URL = "http://www.parans.com";

    private GoogleMap map; // <------ DENNA LÄGGER MAN TILL MARKERS PÅ...
    private ArrayList<Location> locations;
    private ArrayList<MarkerOptions> markerArray;
    private long timeStamp;
    private MapFragment mapFragment; // <----- DET ÄR DENNA MAN SKALL ÄNDRA INSTÄLLNINGAR PÅ INNAN!
    private ListView mDrawerList;
    private String[] mListContent;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ProgressDialog progressDL;
    private ShowcaseView sv;
    private int value;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        findViewById(R.id.spinner).setVisibility(View.GONE);
        locations = loadSP();






        initDrawer();
        initMap();
        initProgressDialog();
        //initShowcase();

    }


    //**********************TUTORIAL **********************//

    public void initShowcase(){
        //mDrawerLayout.openDrawer(Gravity.START);
        //ViewTarget target = new ViewTarget(R.id.left_drawer, this);
        sv = new ShowcaseView.Builder(this, true).setTarget(Target.NONE).setContentTitle("Menu").setContentText("Slide from left to right to\n open the navgation menu").build();
        //sv = new ShowcaseView.Builder(this, true).setTarget(new ViewTarget(mDrawerLayout.getChildAt(0).findViewById(R.id.list_item))).setContentTitle("Menu").setContentText("Slide from left to right to\n open the navgation menu").build();
        sv.setAlpha(90f);
        sv.setHideOnTouchOutside(true);



    }


    //****************************** DRAWER GUI **************************//

    /*
    Initiates the drawer.
     */
    public void initDrawer() {

        mListContent = getResources().getStringArray(R.array.drawer_menu);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_layout, mListContent));

        //mDrawerList.setOnClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //Toast.makeText(MapActivity.this, "Open", Toast.LENGTH_SHORT).show();
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //Toast.makeText(MapActivity.this, "Close", Toast.LENGTH_SHORT).show();
                invalidateOptionsMenu();

            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //mDrawerLayout.bringToFront();
    }

/* Vet ej om behövs
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }
*/

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


    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }




    private void selectItem(int position) {

            switch(position) {
                case 0://Update
                    refreshWeather();
                    break;

                case 1:         //menu_cont
                    /*Intent intent = new Intent(this, Menu_Cont.class);
                    startActivity(intent);*/
                    Fragment fragment = new continentFragment();
                    Bundle bundle = new Bundle();
                    fragment.setArguments(bundle);

                    FragmentManager fm = getFragmentManager();

                    fm.beginTransaction().replace(R.id.content_frame, fragment).commit();


                    break;

                case 2://Parans Website

                    new AlertDialog.Builder(MapActivity.this)
                            .setTitle("Http://www.Parans.com")
                            .setMessage("Clicking ok will take you to Parans website ")
                            .setCancelable(true)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);         //Besöker hemsidan via telefonens default webläsare.
                                    intent.setData(Uri.parse(PARANS_URL));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();//Do nothing
                                }
                            })
                            .show();


                    //Toast.makeText(MapActivity.this, "DrawerLayout 3", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(MapActivity.this, "Not found", Toast.LENGTH_SHORT).show();
            }

        mDrawerLayout.closeDrawer(Gravity.LEFT);
            }



    @Override
    public void onBackPressed(){
        if(mDrawerLayout.isDrawerOpen(mDrawerList)){
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
        else
            super.onBackPressed();
    }

    //***************************** DATABAS ************************************ //

    /*
   Koppling mot databsen måste ske i bakgrunden med hjälp av AsyncTask för att inte låsa hela GUI't.
   Fundering... byta ut mot intentservice/resultreceiver fördel: kan fortsätta söka efter filer även om aktiviteten byts.
    */
    private class DBConnector extends AsyncTask<Void, Location, Void> {

        private String url = "jdbc:mysql://46.239.117.17:3306/";	//Just nu endast lokalt. min IP 46.239.117.17    localhost
        private String DbName = "GVS";						//Schemats namn satt av Simon
        private String driver ="com.mysql.jdbc.Driver";		//Väljer vilken typ av db vi kopplar upp oss mot. Kräver buildpath.
        private String username = "androidAPP";				//Användarnamn satt av Simon.
        private String password = "parans";                 //Lösen satt av Simon.
        private int error = 0;                              //Felkod. 0 = inget fel, 1 = ingen connection, 2 = något annat fel.
        private ArrayList<Location> tempLocations;
        private int i = 0;
        // private int files = 0;



        @Override
        protected void onPreExecute(){
            //locations.clear(); //Nollställer hashmapen så att inga dubletter skapas ifall man uppdaterar filerna fler ggr.
            map.clear();
            tempLocations = new ArrayList<>();
            progressDL.show();  //Visar nedladdningsdialog.

        }


        @Override
        protected Void doInBackground(Void... params) {
            try{
                Class.forName(driver).newInstance();
                Connection conn = DriverManager.getConnection(url + DbName, username, password);

                long timeNow = System.currentTimeMillis() / 1000L;

                Statement st = conn.createStatement();
                ResultSet result = st.executeQuery("SELECT * FROM getweather");          //Skapar ett statement och hämtar allt från locationstable i databasen.

                while(result.next()){       //Hämtar ut rad för rad och sparar undan datan.

                    //FileName används ej just nu men finns ifall vi behöver. Lägga till kolumn för installationsdatum kanske?
                    //String fileName = result.getString("fileName");
                    Double latitude = result.getDouble("latitude");
                    Double longitude = result.getDouble("longitude");
                    int cloudiness = result.getInt("cloudiness");
                    int sensors = result.getInt("sensors");
                    long sunrise = parseToLong(result.getString("sunrise"));
                    long sunset = parseToLong(result.getString("sunset"));

                    Location location = new Location(latitude, longitude, sensors, cloudiness, sunrise, sunset);
                    tempLocations.add(location);

                    if(timeNow > location.getSunrise() && timeNow < location.getSunset() )
                        location.setDay(true);
                    else
                        location.setDay(false);

                    publishProgress(location);
                    i++;



                }
                error = 0;
                conn.close();       //stäng kopplingen.
            }
            catch(CommunicationsException exc){ //behöver ingen stacktrace för detta fel.
                error = 1;
            }
            catch(Exception e){
                error = 2;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Location... progress){
            addMarker(progress[0]);
        }


        @Override
        protected void onPostExecute(Void result){
            progressDL.hide(); //Stänger nedladdningsdialog.
            if(error == 0) {      //felkod 0 = visa på kartan.
                if(tempLocations.size() == 0 && locations.size() > 0){
                    new AlertDialog.Builder(MapActivity.this)
                            .setTitle("No connections in database!")
                            .setMessage("Database is empty do you wish to remove exisiting markers?")
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_email)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    locations = tempLocations;
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();            //Do nothing
                                }
                            })
                            .show();
                }
                else
                    locations = tempLocations;

            }
            else if (error == 1) {          //felkod 1 = ingen connection till server => felmeddelande.
                progressDL.hide();
                new AlertDialog.Builder(MapActivity.this)
                        .setTitle("No connection")
                        .setMessage("Connection to database could not be established")
                        .setCancelable(true)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();//Do nothing
                            }
                        })
                        .show();
            }
            else{                                               //Felkod något annat => något gick skit.
                new AlertDialog.Builder(MapActivity.this)
                        .setTitle("ERROR")
                        .setMessage("Something went wrong!")
                        .setCancelable(true)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();//do nothing
                            }
                        })
                        .show();
            }


        }


    }

    private long parseToLong(String str){
        try{
            return Long.parseLong(str);
        }catch (NumberFormatException e){
            return 0;
        }
    }

    private void initProgressDialog(){
        progressDL = new ProgressDialog(this);
        progressDL.setMessage("Downloading locations");
        progressDL.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDL.setIndeterminate(true);
    }


//********************************** TIME *************************************//

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
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd  z");
        return sdf.format(date);
    }




//***************************** MARKERS ************************************//

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
                    .title("Lat: " + location.getLat() + "  Long:" + location.getLong())
                    .snippet("Sensors: " + location.getNumSensors() + "  Daytime: " + location.getTime() + "  Sunrise: " + getTimeStampUTC(location.getSunrise()) + "  Sunset: " + getTimeStampUTC(location.getSunset()) +
                            "  Cloudiness: " + location.getCloudiness());
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


//****************************** MAP ****************************************//

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
                public void onInfoWindowClick(final Marker marker) {
                    new AlertDialog.Builder(new ContextThemeWrapper(MapActivity.this, android.R.style.Theme_Holo_Dialog))
                            .setTitle(marker.getTitle())
                            .setMessage(marker.getSnippet() + "\n Delete?")
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_map)
                            .setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    marker.remove();

                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    marker.hideInfoWindow();
                                    dialog.cancel();//Do nothing
                                }
                            })
                            .show();
                }
            });
        }

        mapFragment.getMapAsync(this);
    }

    /*
        *Måste vänta på att kartan är redo innan man får börja måla på den.
        */
    @Override
    public void onMapReady(GoogleMap gMap) {
        map = gMap;
        if((timeStamp/1000L + 3600) < (System.currentTimeMillis() / 1000L))  //Senaste uppdateringen + 1 timme < just nu
            refreshWeather();
        else
            addMarkers();
    }



    public void refreshWeather(){
        map.clear();
       // markerArray.clear();
        //new WeatherTask().execute();
        new DBConnector().execute();
    }



    //*********************************** LAGRING *******************************//


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

        Gson gson = new Gson();                 //Lagrar i Gsonformat
        String json = gson.toJson(locations);
        editor.putString(LOCATION_LIST, json);
        editor.putLong(LAST_ACTIVE, System.currentTimeMillis());
        editor.commit();
        System.out.println("Saving...");
    }

    /*
    Laddar den sparade datan
     */
    public ArrayList<Location> loadSP(){
        System.out.println("Loading...");
        ArrayList<Location> tempList;
        SharedPreferences data = getSharedPreferences(LOCATION_DATA, 0);

        timeStamp = data.getLong(LAST_ACTIVE, 0);


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



    public void showNA(View v){
        value = 1;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", value);
        startActivity(intent);


    }

    public void showEU(View v){
        value = 2;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", value);
        startActivity(intent);


    }

    public void showAS(View v){
        value = 3;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", value);
        startActivity(intent);


    }


    public void showSA(View v){
        value = 4;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", value);
        startActivity(intent);


    }

    public void showAF(View v){
        value = 5;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", value);
        startActivity(intent);


    }


    public void showAUS(View v){
        value = 6;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", value);
        startActivity(intent);


    }


}
