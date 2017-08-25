package com.example.abhijit.tracklocation.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.abhijit.tracklocation.R;
import com.example.abhijit.tracklocation.database.TrackLocationDBHelper;
import com.example.abhijit.tracklocation.database.Utils;
import com.example.abhijit.tracklocation.gps.GpsTracker;
import com.example.abhijit.tracklocation.gps.LocationService;
import com.example.abhijit.tracklocation.model.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String date, time;
    private EditText edtDate, edtTime, edtAddress, edtLat, edtLong;
    private TextInputLayout edtAddressLayout;
    private LinearLayout layoutLatLong;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

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
            // startLocationService();
        }

        edtDate = (EditText) findViewById(R.id.edtDate);
        edtTime = (EditText) findViewById(R.id.edtTime);
        edtLat = (EditText) findViewById(R.id.edtLat);
        edtLong = (EditText) findViewById(R.id.edtLong);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        layoutLatLong = (LinearLayout) findViewById(R.id.layoutLatLong);
        edtAddressLayout = (TextInputLayout) findViewById(R.id.edtAddressLayout);
        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        edtDate.setOnClickListener(this);
        edtTime.setOnClickListener(this);

        getLatLong();
        copyDatabase(this, "TrackLocationDB");
    }

    private void getLatLong(){
        /** METHOD TO GET LAT LANG VALUES */
        GpsTracker gpsTrackerNew = new GpsTracker(getApplicationContext());
        android.location.Location location = gpsTrackerNew.getLocation();

        if (location!=null) {
            // String address = gpsTrackerNew.getLocationName(location.getLatitude(), location.getLongitude());
            String[] dateTime = Utils.getTimeStamp();
            Log.e("TIME==",dateTime[1]);
            TrackLocationDBHelper.getInstance().insertLocation(location.getLatitude(),
                    location.getLongitude(), dateTime[0],dateTime[1]);
            // Toast.makeText(this.context, address, Toast.LENGTH_LONG).show();
            // Log.e("LOCATION===", address);
            /*lat = location.getLatitude();
            lng = location.getLongitude();*/
        }
    }

    public static void copyDatabase(Context c, String DATABASE_NAME) {
        // THIS IS TO COPY LOCAL DB IN THE MOBILE DEVICE ITS FOR JUST TESTING PURPOSE NEED NOT TO BE
        // CALLED IN PRODUCTION
        String databasePath = c.getDatabasePath(DATABASE_NAME).getPath();
        File f = new File(databasePath);
        OutputStream myOutput = null;
        InputStream myInput = null;
        Log.d("testing", " testing db path " + databasePath);
        Log.d("testing", " testing db exist " + f.exists());
        if (f.exists()) {
            try {
                File directory = new File(Environment.getExternalStorageDirectory(), "MyDirName");
                if (!directory.exists())
                    directory.mkdir();
                myOutput = new FileOutputStream(directory.getAbsolutePath()
                        + "/" + DATABASE_NAME);
                myInput = new FileInputStream(databasePath);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
            } catch (Exception e) {
            } finally {
                try {
                    if (myOutput != null) {
                        myOutput.close();
                        myOutput = null;
                    }
                    if (myInput != null) {
                        myInput.close();
                        myInput = null;
                    }
                } catch (Exception e) {
                }
            }
        }
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
            date = edtDate.getText().toString().trim();
            // time = edtTime.getText().toString().trim();
            if(isValidate()){
                GpsTracker gpsTrackerNew = new GpsTracker(getApplicationContext());
                Location location = TrackLocationDBHelper.getInstance().getLocationByDateTime(date, time);
                if (location!=null){
                    edtAddressLayout.setVisibility(View.VISIBLE);
                    layoutLatLong.setVisibility(View.VISIBLE);
                    String address = gpsTrackerNew.getLocationName(location.getLatitude(), location.getLongitude());
                    edtAddress.setText(address);
                    edtLat.setText(location.getLatitude()+"");
                    edtLong.setText(location.getLongitude()+"");
                }else{
                    Toast.makeText(this,getString(R.string.no_record), Toast.LENGTH_SHORT).show();
                }
            }
        }else if (v.getId()==R.id.edtDate){
            datePicker(R.string.select_date, edtDate);
        }else if (v.getId()==R.id.edtTime){
            timePicker(R.string.select_time, edtTime);
        }
    }

    private void timePicker(int dialogTitle, final EditText editText){
        final TimePickerDialog timePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String format;
                Log.e("TIME==HOUR",hourOfDay+"");
                String min="";
                if(minute<10) min = "0"+minute;
                else min = minute+"";
                time = hourOfDay + ":" + min;
                if (hourOfDay == 0) {
                    hourOfDay += 12;
                    format = "AM";
                } else if (hourOfDay == 12) {
                    format = "PM";
                } else if (hourOfDay > 12) {
                    hourOfDay -= 12;
                    format = "PM";
                } else {
                    format = "AM";
                }
                editText.setText(new StringBuilder().append(hourOfDay).append(":")
                        .append(min).append(":00").append(" ").append(format));
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false);
        timePicker.setTitle(dialogTitle);
        timePicker.show();
    }

    private void datePicker(int dialogTitle, final EditText editText) {
        final DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String strMonth="";
                String strDay="";
                monthOfYear++;
                if (monthOfYear<10) strMonth = "0"+monthOfYear;
                else strMonth = monthOfYear+"";
                if (dayOfMonth<10) strDay = "0"+dayOfMonth;
                else strDay = dayOfMonth+"";
                editText.setText(new StringBuilder().append(strDay)
                        .append("/").append(strMonth).append("/")
                        .append(year));
                timePicker(R.string.select_time, edtTime);
            }

        }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePicker.setTitle(dialogTitle);
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1);
        datePicker.show();
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
                        //startLocationService();
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
