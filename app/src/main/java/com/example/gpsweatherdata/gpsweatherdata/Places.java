package com.example.gpsweatherdata.gpsweatherdata;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Wrapperklass för arraylist locations.
 */
public class Places implements Parcelable {

    private ArrayList<Location> locations;

    public Places() {

        locations = new ArrayList<Location>();
    }


    public Places(Parcel source) {
        locations = new ArrayList<Location>();
    }



    public Location get(int i) {
        return locations.get(i);
    }

    public void add(Location location) {
        locations.add(location);
    }

    public int size() {
        return locations.size();
    }

    /*
    NEDAN TILLHÖR PARCELABLE
     */

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(locations);
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {

        @Override
        public Places createFromParcel(Parcel source) {
            return new Places(source);
        }

        @Override
        public Places[] newArray(int size) {
            return new Places[size];
        }
    };
}




