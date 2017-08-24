package com.example.abhijit.tracklocation;

import android.app.Application;
import android.content.Context;

import com.example.abhijit.tracklocation.database.TrackLocationDBHelper;

/**
 * Created by viswas on 8/24/2017.
 */

public class TrackLocationApplication extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        TrackLocationApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return TrackLocationApplication.context;
    }

}
