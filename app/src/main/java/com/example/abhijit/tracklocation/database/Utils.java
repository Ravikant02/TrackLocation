package com.example.abhijit.tracklocation.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by viswas on 8/24/2017.
 */

public class Utils {

    public static String[] getTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date).split(" ");
    }

    public static String getQueryString(String tableName, ArrayList<String> columns) {

        String queryString = "create table if not exists " + tableName + "(";
        StringBuilder attrString = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {

            String[] coType = columns.get(i).split(",");

            attrString.append(coType[0] + " " + coType[1] + ",");
        }

        String normalString = attrString + "";

        String finalString = normalString.substring(0, normalString.length() - 1);

        queryString = queryString + finalString + ")";
        return queryString;
    }

}
