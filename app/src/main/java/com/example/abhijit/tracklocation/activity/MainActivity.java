package com.example.abhijit.tracklocation.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.abhijit.tracklocation.R;
import com.example.abhijit.tracklocation.database.TrackLocationDBHelper;
import com.example.abhijit.tracklocation.gps.LocationService;
import com.example.abhijit.tracklocation.model.Location;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String date, time;
    private EditText edDate, edTime;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission. ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission. ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission. ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }else{
            startLocationService();
        }

        edDate = (EditText) findViewById(R.id.edtDate);
        edTime = (EditText) findViewById(R.id.edtDate);
        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
    }

    private void startLocationService(){
        /*LocService locService = new LocService(this);
        this.startService(new Intent(this, locService.getClass()));*/
        LocationService locationService = new LocationService();
        Intent serviceIntent = new Intent(getApplicationContext(), locationService.getClass());
        if (!isMyServiceRunning(serviceIntent.getClass())) startService(serviceIntent);
    }

    private boolean isValidate(){
        if (date.isEmpty()){
            Toast.makeText(this, getString(R.string.error_date), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (date.isEmpty()){
            Toast.makeText(this, getString(R.string.error_time), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        /** TO GET SERVICE RUNNING STATUS */
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btnSearch){
            date = edDate.getText().toString().trim();
            time = edTime.getText().toString().trim();
            if(isValidate()){
                Location location = TrackLocationDBHelper.getInstance().getLocationByDateTime(date, time);
                if (location!=null){

                }else{
                    Toast.makeText(this,getString(R.string.no_record), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission. ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        startLocationService();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}
