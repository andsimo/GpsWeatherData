package com.example.gpsweatherdata.gpsweatherdata;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Klass som hämtar väderdata och kopplar denna till ett location-objekt.
 */
public class WeatherCollector {

    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?"; //Lï¿½nk till vï¿½derserver: http://openweathermap.org/
    private static String API_KEY ="&APPID=0ab3b60b021121ca736c3e9fdc584aa2"; //API_Nyckel som tilldelas vid registrering pï¿½ hemsidan.


    public boolean getWeather(Location location){

        HttpURLConnection conn = null ;
        InputStream is = null;


            String lat = "lat="+location.getLat()+"&";
            String lon = "lon="+location.getLong();

            try{
                conn = (HttpURLConnection) ( new URL(BASE_URL+lat+lon+API_KEY)).openConnection(); //Syr ihop en sträng till URL och skapar en connection till servern.
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();

                StringBuffer buffer = new StringBuffer();
                is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;

                while (  (line = br.readLine()) != null ){ //Läser och printar ut resultatet frï¿½n query.
                    buffer.append(line + "\r\n");
                   // System.out.println("Line = " + line);
                    if(line != null)
                    getClouds(line, location);
                }


                is.close();					//Stänger ned strömmen!
                conn.disconnect();


            }catch (Throwable t){
                t.printStackTrace();
                return false;
            }


        return true;
    }



    public static void getClouds(String data, Location location) throws JSONException {
        //System.out.println("data = " + data);
        JSONObject jObj = new JSONObject(data);


        try {
            JSONObject cOjb = getObject("clouds", jObj);
            location.setCloudiness(getInt("all", cOjb));
        }catch(JSONException e) {
            e.printStackTrace();
            location.setCloudiness(1000);
            System.out.println("KNASIGT CLOUD VALUE!   long  " + location.getLong() +"  lat  " + location.getLat());
        }
        //System.out.println("location = " + location.getCloudiness());

    }



    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException{
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException{
        return jObj.getInt(tagName);
    }



}
