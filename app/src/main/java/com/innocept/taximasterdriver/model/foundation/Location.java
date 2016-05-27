package com.innocept.taximasterdriver.model.foundation;

import java.io.Serializable;

/**
 * Created by Dulaj on 14-Apr-16.
 */
public class Location implements Serializable {

    private double longitude;
    private double latitude;

    public Location() {
        this.longitude=0;
        this.latitude=0;
    }

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
