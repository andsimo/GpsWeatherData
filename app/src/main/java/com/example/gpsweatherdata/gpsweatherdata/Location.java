package com.example.gpsweatherdata.gpsweatherdata;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Objekt för att spara varje plats. Innehåller sensorernas latitud och longitud,
 * samt sparar antalet sensorer som har samma lokalisering.
 *
 * Implementerar Parcelable för att kunna skicka dessa som intent till andra aktiviteter.
 */
public class Location implements Parcelable, Serializable{


    private double latitude, longitude;
    private int sensors = 1;
    private boolean day;
    private int cloudiness;
    private long sunrise, sunset; //Tid i UNIX UTC


    /*
    Måste ha samma ordning som writeToParcel för att kunna återskapa objektet.
     */
    public Location(Parcel in){
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.sensors = in.readInt();
        this.cloudiness = 1000;
    }

    public Location(double latitude, double longitude){
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.cloudiness = 1000;
    }

    public void addSensors(){
        sensors++;
    }
/*
    public void addSensors(int i){
        sensors = sensors + i;
    }
*/
    public void setCloudiness(int clouds){
        cloudiness = clouds;
    }

    public void setSunrise (long time){sunrise = time; }

    public void setSunset (long time) {sunset = time; }

    public void setDay(boolean time ) {day = time; }

    public double getLat(){
        return latitude;
    }

    public double getLong(){
        return longitude;
    }

    public int getNumSensors(){
        return sensors;
    }

    public int getCloudiness(){
        return cloudiness;
    }

    public long getSunrise(){ return sunrise; }

    public long getSunset () { return sunset; }

    public boolean getTime() {return day; }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeInt(this.sensors);
    }

    public static final Parcelable.Creator<Location> CREATOR
            = new Parcelable.Creator<Location>() {

        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };



}
