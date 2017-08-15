package com.example.android.quakereport;

class Earthquake{

    private double magnitude;
    private String place;
    private long timeInMilliseconds;
    private String url;

    Earthquake(double magnitude, String place, long time, String url){
        this.magnitude = magnitude;
        this.place = place;
        this.timeInMilliseconds = time;
        this.url = url;
    }

    String getUrl() {
        return url;
    }

    long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    double getMagnitude() {
        return magnitude;
    }

    String getPlace() {
        return place;
    }
}