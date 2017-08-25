package com.example.abhijit.tracklocation.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viswas on 8/24/2017.
 */

public class DBKeys {

    public static final String TEXT_TYPE = "text";
    public static final String REAL_TYPE = "real";

    public static class LocationTable {
        public static final String LAT = "lat";
        public static final String LONG = "long";
        public static final String DATE = "date";
        public static final String TIME = "time";
    }

    public static ArrayList<String> getLocationColumn(){
        ArrayList<String> list = new ArrayList<>();
        list.add(LocationTable.LAT + ","+ REAL_TYPE);
        list.add(LocationTable.LONG + ","+ REAL_TYPE);
        list.add(LocationTable.DATE + ","+ TEXT_TYPE);
        list.add(LocationTable.TIME + ","+ TEXT_TYPE);
        return list;
    }


}

