package com.bobcob7.untscanner;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;

enum VALID_RETURN { GOOD, BAD, UNKNOWN, ERROR };

public class ScanActivity extends Activity {

    private static final String TAG = "com.bobcob7.UNTScanner";
    private EditText scannerInput;
    private TextView logView;
    SQLManager manager;

    private boolean keyboardListenersAttached = false;
    private TextView statusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        scannerInput = (EditText) findViewById(R.id.inputView);
        statusView = (TextView) findViewById(R.id.statusView);
        logView = (TextView) findViewById(R.id.logView);
        manager = new SQLManager(this);

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        log("Logs for:\t" + now.toString());
    }

    String FSM = "";
    int FSMState = 0;
    ArrayList states = new ArrayList();
    int[] digits = new int[8];

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        states.add(keyCode);
        scannerInput.setText("");
        switch(FSMState)
        {
            case 0:
                if(keyCode == 12)
                    ++FSMState;
                break;
            case 1:
                if(keyCode == 15)
                    ++FSMState;
                else
                    FSMState = 0;
                break;
            case 2:
                if(keyCode >= 7 || keyCode <= 16) {
                    ++FSMState;
                    digits[0] = keyCode-7;
                }
                else
                    FSMState = 0;
                break;
            case 3:
                if(keyCode >= 7 || keyCode <= 16) {
                    ++FSMState;
                    digits[1] = keyCode-7;
                }
                else
                    FSMState = 0;
                break;
            case 4:
                if(keyCode >= 7 || keyCode <= 16) {
                    ++FSMState;
                    digits[2] = keyCode-7;
                }
                else
                    FSMState = 0;
                break;
            case 5:
                if(keyCode >= 7 || keyCode <= 16) {
                    ++FSMState;
                    digits[3] = keyCode-7;
                }
                else
                    FSMState = 0;
                break;
            case 6:
                if(keyCode >= 7 || keyCode <= 16) {
                    ++FSMState;
                    digits[4] = keyCode-7;
                }
                else
                    FSMState = 0;
                break;
            case 7:
                if(keyCode >= 7 || keyCode <= 16) {
                    ++FSMState;
                    digits[5] = keyCode-7;
                }
                else
                    FSMState = 0;
                break;
            case 8:
                if(keyCode >= 7 || keyCode <= 16) {
                    ++FSMState;
                    digits[6] = keyCode-7;
                }
                else
                    FSMState = 0;
                break;
            case 9:
                if(keyCode >= 7 || keyCode <= 16) {
                    ++FSMState;
                    digits[7] = keyCode-7;
                }
                else
                    FSMState = 0;
                break;
            case 10:
                if(keyCode == 76)
                    ++FSMState;
                else
                    FSMState = 0;
                break;
            case 11:
                if(keyCode == 74)
                    ++FSMState;
                else
                    FSMState = 0;
                break;
            default:
                if(FSMState > 10)
                    ++FSMState;
                break;
            case 27:
                //Print out the results
                Log.d(TAG,digits.toString());
                FSMState = 0;

                int studentId = 0;
                studentId += digits[0]*10000000;
                studentId += digits[1]*1000000;
                studentId += digits[2]*100000;
                studentId += digits[3]*10000;
                studentId += digits[4]*1000;
                studentId += digits[5]*100;
                studentId += digits[6]*10;
                studentId += digits[7]*1;

                Log.d(TAG,"Student ID: " + studentId);

                if(studentId < 0 || studentId > 99999999)
                {
                    Log.d(TAG, "Bad Id Format");
                }
                else
                {
                    VALID_RETURN response = manager.isValidId(studentId);

                    switch(response)
                    {
                        case GOOD:
                            Log.d(TAG,studentId + " is an active member");
                            goodStudent(studentId);
                            break;
                        case BAD:
                            Log.d(TAG,studentId + " is an inactive member");
                            badStudent(studentId);
                            break;
                        case UNKNOWN:
                            Log.d(TAG,studentId + " is not a member");
                            statusView.setBackgroundColor(WHITE);
                            Intent intent = new Intent(ScanActivity.this, AddStudentActivity.class);
                            intent.putExtra("id",studentId);
                            ScanActivity.this.startActivity(intent);
                            break;
                        case ERROR:
                            Log.d(TAG, studentId + " caused an error");
                            break;
                    }
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goodStudent(int studentId)
    {
        statusView.setBackgroundColor(GREEN);
        String studentName = manager.getStudentName(studentId);
        statusView.setText(studentName);
    }

    private void badStudent(int studentId)
    {
        statusView.setBackgroundColor(RED);
        String studentName = manager.getStudentName(studentId);
        statusView.setText(studentName);
    }

    private void log(String msg)
    {
        logView.append(msg + "\n");
    }

    private void checkStudentId(int id)
    {
        if(id < 0 || id > 99999999)
        {
            Log.d(TAG, "Bad Id Format");
        }
        else
        {
            switch( manager.isValidId(id))
            {
                case GOOD:
                    Log.d(TAG,id + " is an active member");
                    break;
                case BAD:
                    Log.d(TAG,id + " is an inactive member");
                    break;
                case UNKNOWN:
                    Log.d(TAG,id + " is not a member");
                    Intent intent = new Intent(ScanActivity.this, AddStudentActivity.class);
                    intent.putExtra("id",id);
                    ScanActivity.this.startActivity(intent);
                    break;
                case ERROR:
                    Log.d(TAG, id + " caused an error");
                    break;
            }
        }
    }

    public void onSubmit(View view)
    {
        String idText = scannerInput.getText().toString();
        scannerInput.setText("");
        if(idText.length() != 8)
        {
            Log.d(TAG, "ID isn't the right length");
        }
        else
        {
            Integer id = new Integer(idText);
            checkStudentId(id);
        }
    }
}
