package com.example.abhijit.tracklocation.gps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.abhijit.tracklocation.database.TrackLocationDBHelper;
import com.example.abhijit.tracklocation.database.Utils;

/**
 * Created by viswas on 8/24/2017.
 */

public class LocationService extends Service {
    private final static int INTERVAL = 1000 * 10 * 1; // 10 SECONDS

    public LocationService(){
    }

    @Override
    public void onCreate() {
        mHandlerTask.run();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // mHandler.removeCallbacks(mHandlerTask);
    }

    private void getLatLong(){
        /** METHOD TO GET LAT LANG VALUES */
        GpsTracker gpsTrackerNew = new GpsTracker(getApplicationContext());
        Location location = gpsTrackerNew.getLocation();

        if (location!=null) {
            // String address = gpsTrackerNew.getLocationName(location.getLatitude(), location.getLongitude());
            String[] dateTime = Utils.getTimeStamp();
            TrackLocationDBHelper.getInstance().insertLocation(location.getLatitude(),
                    location.getLongitude(), dateTime[0],dateTime[1]);
            // Toast.makeText(this.context, address, Toast.LENGTH_LONG).show();
            // Log.e("LOCATION===", address);
            /*lat = location.getLatitude();
            lng = location.getLongitude();*/
        }
    }

    Handler mHandler = new Handler();
    Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {
            getLatLong();
            mHandler.postDelayed(mHandlerTask, INTERVAL);
        }
    };
}
