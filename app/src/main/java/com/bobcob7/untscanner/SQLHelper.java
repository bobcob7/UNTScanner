package com.bobcob7.untscanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by bobcob7 on 12/17/2016.
 */

public class SQLHelper extends SQLiteOpenHelper {


    public static String[] projection1 = {
            "_id",
            "studentName",
            "active"
    };

    public static String[] projection2 = {
            "_id",
            "studentName",
            "studentId",
            "action",
            "time"
    };

    public static String TABLE_NAME1 = "studentIds";
    public static String TABLE_NAME2 = "logs";

    private static final String TAG = "com.bobcob7.UNTScanner";

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "ASME.db";

    private static final String SQL_CREATE_ENTRIES1 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME1 + " ( _id INTEGER PRIMARY KEY, studentName TEXT, active INTEGER );";
    private static final String SQL_CREATE_ENTRIES2 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME2 + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, studentName TEXT, studentId INTEGER, action TEXT, time TIMESTAMP DEFAULT CURRENT_TIMESTAMP );";

    private static final String SQL_DELETE_ENTRIES1 = "DROP TABLE IF EXISTS " + TABLE_NAME1;
    private static final String SQL_DELETE_ENTRIES2 = "DROP TABLE IF EXISTS " + TABLE_NAME2;

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES1);
        db.execSQL(SQL_CREATE_ENTRIES2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES1);
        db.execSQL(SQL_DELETE_ENTRIES2);
        onCreate(db);
        Log.d(TAG, "Upgrading Database " + DATABASE_NAME + " from " + oldVersion + " to " + newVersion);
    }

}