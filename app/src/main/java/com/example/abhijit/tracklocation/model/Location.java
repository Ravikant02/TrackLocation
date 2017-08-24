package com.example.abhijit.tracklocation.model;

import java.io.Serializable;

/**
 * Created by viswas on 9/27/2016.
 */

public class Location implements Serializable{

    public String latitude;
    public String longitude;
    public String date;
    public String time;

    public Location(){

    }

    public Location(String latitude, String longitude, String date, String time){
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.time = time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
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
