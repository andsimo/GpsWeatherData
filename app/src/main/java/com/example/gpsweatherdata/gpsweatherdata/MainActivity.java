package com.example.gpsweatherdata.gpsweatherdata;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private static final String LOCATION_DATA = "locationdata";
    private static final String LOCATION_LIST = "locationlist";

    //private Places locations;
    private ArrayList<Location> locations;
    private ProgressDialog progressDL;
    private boolean newlyUpdated;
    private long timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locations = new ArrayList<>();

        /*
        Skapar en ruta som indikerar att nedladdning av data görs.
         */
        progressDL = new ProgressDialog(this);
        progressDL.setMessage("Downloading locations");
        progressDL.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDL.setIndeterminate(true);
        newlyUpdated = false;
    }

    @Override
    protected void onResume(){
        super.onResume();
        timeStamp = System.currentTimeMillis() / 1000L;

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void updateFiles(View view){

        new DBConnector().execute();
        newlyUpdated = true;


        //Toast.makeText(this, "" + locations.size() + " files where added", Toast.LENGTH_LONG).show();
    }

    public void showMap(View view){

        Intent intent = new Intent(this, MapActivity.class);
        //intent.putParcelableArrayListExtra("locations", locations);
        intent.putExtra("new", newlyUpdated);

        startActivity(intent);


    }


    /*
    Koppling mot databsen måste ske i bakgrunden med hjälp av AsyncTask för att inte låsa hela GUI't.
    Fundering... byta ut mot intentservice/resultreceiver fördel: kan fortsätta söka efter filer även om aktiviteten byts.
     */
    private class DBConnector extends AsyncTask<Void, Void, Void> {

        private String url = "jdbc:mysql://46.239.117.17:3306/";	//Just nu endast lokalt. min IP 46.239.117.17    localhost
        private String DbName = "GVS";						//Schemats namn satt av Simon
        private String driver ="com.mysql.jdbc.Driver";		//Väljer vilken typ av db vi kopplar upp oss mot. Kräver buildpath.
        private String username = "androidAPP";				//Användarnamn satt av Simon.
        private String password = "parans";                 //Lösen satt av Simon.
        private int error = 0;
       // private int files = 0;



        @Override
        protected void onPreExecute(){
            locations.clear(); //Nollställer hashmapen så att inga dubletter skapas ifall man uppdaterar filerna fler ggr.
            progressDL.show();  //Visar nedladdningsdialog.

        }


        @Override
        protected Void doInBackground(Void... params) {
            try{
                Class.forName(driver).newInstance();
                Connection conn = DriverManager.getConnection(url + DbName, username, password);

                Statement st = conn.createStatement();
                ResultSet result = st.executeQuery("SELECT * FROM locations");          //Skapar ett statement och hämtar allt från locationstable i databasen.

                while(result.next()){       //Hämtar ut rad för rad och sparar undan datan.

                    //FileName används ej just nu men finns ifall vi behöver. Lägga till kolumn för installationsdatum kanske?
                    //String fileName = result.getString("fileName");
                    Double latitude = result.getDouble("latitude");
                    Double longitude = result.getDouble("longitude");

                    boolean found = false;          //Flagga för att se ifall positionen redan finns i listan

                    for(int i = 0; i < locations.size(); i++){          //Går igenom listan och kollar ifall positionen redan finns.
                        Location compLoc = locations.get(i);

                        if(compLoc.getLat() == latitude && compLoc.getLong() == longitude) {
                            locations.get(i).addSensors();          //Ifall den gör det så öka antalet sensorer och sätt flaggan till true så att inte en dublett skapas.
                            found = true;
                        }
                    }
                    if(!found) {        //Hittades ingen befintlig sensor så läggs en ny plats till i listan.
                        Location location = new Location(latitude, longitude);
                        locations.add(location);
                    }
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
        protected void onPostExecute(Void result){
            progressDL.hide(); //Stänger nedladdningsdialog.
            if(error == 0)      //felkod 0 = inget fel, spara.
                saveSP();
            else if (error == 1) {          //felkod 1 = ingen connection till server => felmeddelande.
                progressDL.hide();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("No connection")
                        .setMessage("Connection to database could not be established")
                        .setCancelable(true)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        })
                        .show();
            }
            else{                                               //Felkod något annat => något gick skit.
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("ERROR")
                        .setMessage("Something went wrong!")
                        .setCancelable(true)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        })
                        .show();
            }


        }





    }

    public void saveSP(){
        SharedPreferences data = getSharedPreferences(LOCATION_DATA, 0);
        SharedPreferences.Editor editor = data.edit();

        Gson gson = new Gson();
        String json = gson.toJson(locations);
        editor.putString(LOCATION_LIST, json);
        //editor.putLong("lastTime", System.currentTimeMillis());
        editor.commit();
        System.out.println("Saving...");
    }


}
