package com.example.android.moviesrus2;

public class TrailerInfo {
    String trailerId;
    String trailerKey;
    String trailerName;


    public TrailerInfo(){

    }

    public TrailerInfo(String id, String key, String name) {
        this.trailerId = id;
        this.trailerKey = key;
        this.trailerName = name;
    }
}
