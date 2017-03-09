package com.esod.andelaapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Jedidiah on 09/03/2017.
 */

public class SessionManager {

    private static String PAGE_NO = "page_no";

    public static int getPageNo(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(PAGE_NO, 1);
    }

    public static void setPageNo(Context context, int number){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PAGE_NO, number);
        editor.apply();
    }
}
