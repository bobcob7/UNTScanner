package com.bobcob7.untscanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by bobco on 12/17/2016.
 */



public class SQLManager {

    private static final String TAG = "com.bobcob7.UNTScanner";

    private SQLiteDatabase db;
    private StudentIdSQLHelper helper;
    public boolean isGood;

    SQLManager(Context context) {
        isGood = false;
        helper = new StudentIdSQLHelper(context);
        db = helper.getWritableDatabase();
        if (db.isOpen())
            isGood = true;
    }

    public VALID_RETURN isValidId(int id) {
        if (!isGood) {
            Log.w(TAG, "Trying to access an invalid database");
            return VALID_RETURN.ERROR;
        }

        String selection = "_id" + " = ?";
        String[] selectionArgs = {new Integer(id).toString()};

        String sortOrder = "_id DESC";

        Cursor c = db.query(
                StudentIdSQLHelper.TABLE_NAME,                     // The table to query
                StudentIdSQLHelper.projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c.getCount() > 0) {
            c.moveToFirst();
            int active = c.getInt(c.getColumnIndex("active"));
            if (active > 0)
                return VALID_RETURN.GOOD;
            return VALID_RETURN.BAD;
        }
        return VALID_RETURN.UNKNOWN;
    }

    public void resetTable(Context context)
    {
        db.delete(StudentIdSQLHelper.TABLE_NAME,null,null);
        Toast.makeText(context,"Reset Table",Toast.LENGTH_SHORT).show();
    }

    public void addStudent(int studentId, String studentName)
    {
        ContentValues values = new ContentValues();

        values.put("_id", studentId);
        values.put("studentName", studentName);
        values.put("active", 0);

        db.insert(StudentIdSQLHelper.TABLE_NAME, null, values);
    }

    public void addStudent(int studentId, String studentName, boolean active)
    {
        ContentValues values = new ContentValues();

        values.put("_id", studentId);
        values.put("studentName", studentName);
        if(active)
            values.put("active", 1);
        else
            values.put("active", 0);

        db.insert(StudentIdSQLHelper.TABLE_NAME, null, values);
    }

    String getStudentName(int studentId)
    {
        if (!isGood) {
            Log.w(TAG, "Trying to access an invalid database");
            return "BAD DATABASE";
        }

        String selection = "_id" + " = ?";
        String[] selectionArgs = {new Integer(studentId).toString()};

        String sortOrder = "_id DESC";

        Cursor c = db.query(
                StudentIdSQLHelper.TABLE_NAME,                     // The table to query
                StudentIdSQLHelper.projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c.getCount() > 0) {
            c.moveToFirst();
            return c.getString(c.getColumnIndex("studentName"));
        }
        return "UNKNOWN";
    }

    String getCSVDatabase()
    {
        Cursor c = db.query(StudentIdSQLHelper.TABLE_NAME,
                StudentIdSQLHelper.projection,
                null,
                null,
                null,
                null,
                "_id DESC"
        );

        if(c.getCount() > 0) {
            String output = "_id,studentName,active\n";
            c.moveToFirst();
            do {
                int id = c.getInt(c.getColumnIndex("_id"));
                String name = c.getString(c.getColumnIndex("studentName"));
                int active = c.getInt(c.getColumnIndex("active"));
                output = output.concat(id + "," + name + "," + active + "\n");
            } while (c.moveToNext());
            return output;
        }

        return "";
    }

    public int[] importDb(String output) {
        boolean isHeader = true;
        int width = 0;
        int[] counts = new int[]{0,0};
        String[] projection = new String[0];
        for (String line: output.split("\n")) {
            if(isHeader)
            {
                ArrayList<String> projectionList = new ArrayList<String>();
                for(String token: line.split(","))
                {
                    projectionList.add(token);
                }
                width = countChars(line,',');
                projection = projectionList.toArray(projection);
                isHeader = false;
            }
            else
            {
                if(countChars(line,',') != width)
                {
                    Log.d(TAG,"Bad Table Entry at \"" + line + "\"");
                    counts[0] = -1 * counts[0];
                    counts[1] = -1 * counts[1];
                    return counts;
                }

                ContentValues values = new ContentValues();

                int i = 0;
                for(String token: line.split(","))
                {
                    values.put(projection[i++], token);
                }

                if(db.update(StudentIdSQLHelper.TABLE_NAME, values, "_id = ?", new String[]{values.getAsString("_id")}) < 1)
                {
                    db.insert(StudentIdSQLHelper.TABLE_NAME, null, values);
                    counts[0]++;
                }
                else
                    counts[1]++;

            }
        }
        return counts;
    }

    private int countChars(String line, char s) {
        int count = 0;
        for(int i=0; i<line.length(); ++i)
        {
            if(line.charAt(i) == s)
                ++count;
        }
        return count;
    }
}
