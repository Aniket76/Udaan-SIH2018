package com.hackathon2018.udaan.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by anubhav.chandra on 10/9/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static volatile DatabaseHandler sInstance;
    private static final int DATABASE_VERSION = 10;
    private static final String DATABASE_NAME = "CashCart";

    public static DatabaseHandler getInstance(Context context) {

        if (sInstance == null) {
            synchronized (DatabaseHandler.class){
                if (null == sInstance) {
                    sInstance = new DatabaseHandler(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
//    public void deleteDuplicates(){
//        String CREATE_LOGIN_TABLE = "delete from pushnotification where message not in (SELECT MIN(message ) FROM post GROUP BY message)";
//        db.execSQL(CREATE_LOGIN_TABLE);
////        getWritableDatabase().execSQL("delete from pushnotification where message not in (SELECT MIN(message ) FROM post GROUP BY message)");
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE pushnotification ( message TEXT , imgurl TEXT)";
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void inserDataintoPushNotification(String message, String imgurl) {
        System.out.println("ANUBHAV INPUT : "+message +", "+imgurl);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("message", message);
        values.put("imgurl", imgurl);
        db.insert("pushnotification", null, values);
    }

    /**
     * fetching C2 Table data in database
     */
    public ArrayList<String> fetchDatafromPushNotification() {
        ArrayList<String> user = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        getWritableDatabase().execSQL("delete from pushnotification where message not in (SELECT MIN(message ) FROM pushnotification GROUP BY message)");
//        Cursor cursor = db.query(false, "pushnotification", new String[]{"message","imgurl"}, null, null, null, null,null,null);
        Cursor cursor = db.rawQuery("select * from pushnotification" , null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
//            user.add(cursor.getString(0));
            user.add(cursor.getString(1));
            cursor.moveToNext();
        }
//        if (cursor.getCount() > 0) {
//            user.add(cursor.getString(0));
//        }
        cursor.close();
        System.out.println("ANUBHAV OUTPUT : "+user.toString());
        return user;

    }

    public ArrayList<String> fetchDatafromPushNotification2() {
        ArrayList<String> user = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(false, "pushnotification", new String[]{"message","imgurl"}, null, null, null, null,null,null);
        Cursor cursor = db.rawQuery("select * from pushnotification" , null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            user.add(cursor.getString(0));
//            user.add(cursor.getString(1));
            cursor.moveToNext();
        }
//        if (cursor.getCount() > 0) {
//            user.add(cursor.getString(0));
//        }
        cursor.close();
        System.out.println("ANUBHAV OUTPUT : "+user.toString());
        return user;

    }

}
