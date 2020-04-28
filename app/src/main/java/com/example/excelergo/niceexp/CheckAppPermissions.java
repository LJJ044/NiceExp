package com.example.excelergo.niceexp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

public class CheckAppPermissions {
    public static void geFilePermission(Activity activity){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            int hasReadPermission = activity.checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int hasWritePermission=activity.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasReadPermission != PackageManager.PERMISSION_GRANTED&&hasWritePermission!=PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        }

    }
}
