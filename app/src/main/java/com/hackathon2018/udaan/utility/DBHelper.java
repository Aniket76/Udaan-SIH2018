package com.hackathon2018.udaan.utility;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 5/25/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.iso.homecare/databases/";

    private static String DB_NAME = "isoAndroidDB";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    public DBHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
        // checking database and open it if exists
        if (checkDataBase()) {
            openDataBase();
        } else
        {
            try {
                this.getReadableDatabase();
                copyDataBase();
                this.close();
                openDataBase();

            } catch (IOException e) {
                throw new Error("Error copying database");
            }
//            Toast.makeText(context, "Initial database is created", Toast.LENGTH_LONG).show();
        }
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        boolean exist = false;
        try {
            String dbPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(dbPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.v("db log", "database does't exist");
        }

        if (checkDB != null) {
            exist = true;
            checkDB.close();
        }
        return exist;
    }

//    private boolean checkDataBase(){
//
//        SQLiteDatabase checkDB = null;
//
//        try{
//            String myPath = DB_PATH + DB_NAME;
//            //checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
//
//            File dbFile = myContext.getDatabasePath(DB_NAME);
//            return dbFile.exists();
//
//        }catch(SQLiteException e){
//
//            //database does't exist yet.
//        }
//
//        if(checkDB != null){
//
//            checkDB.close();
//
//        }
//
//        return checkDB != null ? true : false;
//    }

    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
