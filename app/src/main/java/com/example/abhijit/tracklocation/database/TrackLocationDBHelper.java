package com.example.abhijit.tracklocation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.abhijit.tracklocation.TrackLocationApplication;
import com.example.abhijit.tracklocation.model.Location;

/**
 * Created by viswas on 8/24/2017.
 */

public class TrackLocationDBHelper extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "TrackLocationDB";
    private final static String TABLE_LOCATIONS = "Locations";
    private static TrackLocationDBHelper instance;

    private TrackLocationDBHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static TrackLocationDBHelper getInstance() {
        if (instance == null) {
            return instance = new TrackLocationDBHelper(TrackLocationApplication.getAppContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utils.getQueryString(TABLE_LOCATIONS, DBKeys.getLocationColumn()));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertLocation(String lat, String lng, String date, String time) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBKeys.LocationTable.LAT, lat);
        contentValues.put(DBKeys.LocationTable.LONG, lng);
        contentValues.put(DBKeys.LocationTable.TIME, date);
        contentValues.put(DBKeys.LocationTable.LAT, time);
        Cursor cursor = db.query(TABLE_LOCATIONS, new String[]{DBKeys.LocationTable.DATE}, DBKeys.LocationTable.TIME + "= ? and " + DBKeys.LocationTable.DATE + "= ?", new String[]{date, time}, null, null, null);

        long insert;
        if (cursor.moveToFirst()) {
            insert = db.update(TABLE_LOCATIONS, contentValues, DBKeys.LocationTable.DATE + "= ? and " + DBKeys.LocationTable.TIME + "=?", new String[]{date, time});
        } else {
            insert = db.insert(TABLE_LOCATIONS, null, contentValues);
        }
        cursor.close();
        return insert > 0;
    }

    public Location getLocationByDateTime(String date, String time) {
        Location location = null;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_LOCATIONS, null, DBKeys.LocationTable.DATE+ "= ? and "+ DBKeys.LocationTable.TIME+"=? ", new String[]{date,time}, null, null, null);
        if(cursor.moveToFirst()) {
            location = new Location(cursor.getString(cursor.getColumnIndex(DBKeys.LocationTable.LAT)),
                    cursor.getString(cursor.getColumnIndex(DBKeys.LocationTable.LONG)),
                    cursor.getString(cursor.getColumnIndex(DBKeys.LocationTable.DATE)),
                    cursor.getString(cursor.getColumnIndex(DBKeys.LocationTable.TIME)));
        }
        cursor.close();
        return location;
    }

}
