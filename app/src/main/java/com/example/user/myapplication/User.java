package com.example.user.myapplication;

/**
 * Created by user on 18-Dec-17.
 */

public  class User {

    public User(String name, String mobnum, double latitude, double longitude) {
        this.name = name;
        this.mobnum = mobnum;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobnum() {
        return mobnum;
    }

    public void setMobnum(String mobnum) {
        this.mobnum = mobnum;
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

    public String name;
    public String mobnum;
    public double latitude;
    public  double longitude;
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


}