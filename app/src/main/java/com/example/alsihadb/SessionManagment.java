package com.example.alsihadb;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Danish on 4/28/2016.
 */
public class SessionManagment {
    public void setPreferences(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences("MyData", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();

    }


    public String getPreferences(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String position = prefs.getString(key, "");
        return position;
    }
}