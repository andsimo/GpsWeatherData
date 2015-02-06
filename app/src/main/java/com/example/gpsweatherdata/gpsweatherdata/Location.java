package com.example.gpsweatherdata.gpsweatherdata;

/**
 * Created by Simon on 2015-02-04.
 */
public class Location {


    private double latitude, longitude;
    private int sensors;

    public Location(){
        sensors = 1;
    }

    public Location(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.sensors = 1;
    }

    public void addSensors(){
        sensors++;
    }

    public void addSensors(int i){
        sensors = sensors + i;
    }

    public double getLat(){
        return latitude;
    }

    public double getLong(){
        return longitude;
    }

    public int getNumSensors(){
        return sensors;
    }




}
