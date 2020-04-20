package com.example.excelergo.niceexp;
import android.app.Application;

public class LocationApplication extends Application {
	public LocationService locationService;
    @Override
    public void onCreate() {
        super.onCreate();
        locationService = new LocationService(getApplicationContext());
    }
}
