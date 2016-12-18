package com.bobcob7.untscanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by bobcob7 on 12/17/2016.
 */

public class StudentIdSQLHelper extends SQLiteOpenHelper {

    public static String[] projection = {
            "_id",
            "studentName",
            "active"
    };
    public static String TABLE_NAME = "studentIds";

    private static final String TAG = "com.bobcob7.UNTScanner";

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "ASME.db";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( _id INTEGER PRIMARY KEY, studentName TEXT, active INTEGER );";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public StudentIdSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
        Log.d(TAG, "Upgrading Database " + DATABASE_NAME + " from " + oldVersion + " to " + newVersion);
    }

}
