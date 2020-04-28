package com.example.excelergo.niceexp;

import android.app.Application;
import android.content.Context;
import android.location.LocationListener;

import com.tencent.smtt.sdk.QbSdk;

public class NiceExpApplication extends Application {
    public LocationService locationService;
    @Override
    public void onCreate() {
        super.onCreate();
        locationService=new LocationService(getApplicationContext());
        QbSdk.initX5Environment(getApplicationContext(),null);
}
}
