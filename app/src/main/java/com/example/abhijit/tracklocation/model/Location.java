package com.example.abhijit.tracklocation.model;

import java.io.Serializable;

/**
 * Created by viswas on 9/27/2016.
 */

public class Location implements Serializable{

    public double latitude;
    public double longitude;
    public String date;
    public String time;

    public Location(){

    }

    public Location(double latitude, double longitude, String date, String time){
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
