package com.example.bustrackingandbookingsystem;

public class LatLng {
    double latitude;
    double longitude;
    int flag;
    int busId;

    public LatLng(double latitude, double longitude,int flag,int busId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.flag = flag;
        this.busId = busId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getFlag() {
        return flag;
    }

    public int getBusId(){return busId; }
}
