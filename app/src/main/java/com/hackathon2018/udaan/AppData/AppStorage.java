package com.hackathon2018.udaan.AppData;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 5/5/2017.
 */

public class AppStorage {

    private static final String PREFS_NAME = "ERETAILWMS_APP";
    public static String SUPPORTEDVERSIONS="supportedVersions";
    public static String USER_ID="userId";
    public static String NAME="name";
    public static String USERNAME="username";
    public static String TOKEN="token";
    public static String IMAGEURL="imageurl";
    public static String MESSAGEURL="messageurl";



    private static Map<String,String> dataMap = new HashMap<String,String>();

    public static void storeString(Context context, String key, String value) {
        dataMap.put(key, value);
    }

    public static String getString(Context context, String key) {
        if(dataMap.containsKey(key))
            return dataMap.get(key);
        else
            return null;
    }

    public static void removeString(Context context, String key) {
        dataMap.remove(key);
    }
    public static void storeRightMap(Context context,Map<String , String> rightMap){
        for( Map.Entry<String, String> entry : rightMap.entrySet())
        {
            dataMap.put(entry.getKey(), entry.getValue());
        }
    }
    public static void removeRightMap(Context context, String[] key) {
        for(int i=0; i<key.length;i++) {
            dataMap.remove(key[i]);
        }
    }

    public static void removeAll(Context context) {
        dataMap = new HashMap<String,String>();
    }

    public static void storeStringInSF(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringFromSF(Context context, String key) {
        SharedPreferences preferences=context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (preferences.contains(key)) {
            return preferences.getString(key, null);
        } else
            return null;
    }

    public static void removeStringFromSF(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }
}
