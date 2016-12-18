package com.bobcob7.untscanner;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;

enum VALID_RETURN { GOOD, BAD, UNKNOWN, ERROR };

public class ScanActivity extends AppCompatActivity {

    private static final String TAG = "com.bobcob7.UNTScanner";
    private EditText scannerInput;
    private TextView logView;
    private Calendar calendar;
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

        ActionBar ab = getSupportActionBar();

        manager = new SQLManager(this);

        calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        log("Logs for:\t" + now.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_reset_db:
                manager.resetTable(this);
                return true;
            case R.id.action_export_logs:
                exportLogs();
                return true;
            case R.id.action_export_db:
                exportDb();
            case R.id.action_import_db:
                importDb();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void exportLogs()
    {
        String filename = new Long(calendar.getTimeInMillis()).toString();
        filename = filename.concat(".csv");
        FileOutputStream outputStream;

        File file = new File(this.getFilesDir(), filename);
        file.setReadable(true, false);
        String logText = logView.getText().toString();

        //File file = null;
        //try {
        //    file = File.createTempFile(filename, null, this.getApplicationContext().getCacheDir());
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}

        //File file = new File(this.getFilesDir(), filename);

        try {
            //outputStream = new FileOutputStream(file);
            outputStream = openFileOutput(filename, Context.MODE_WORLD_READABLE);
            outputStream.write(logText.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //file.setReadable(true, false);
        if(file.canRead())
        {
            Log.d(TAG,"Can Read");
        }
        Uri U = Uri.fromFile(file);
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_STREAM, U);
        startActivity(Intent.createChooser(i,"Email:"));
    }

    private void exportDb()
    {

    }

    private void importDb()
    {

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
                            startActivityForResult(intent,0);
                            break;
                        case ERROR:
                            Log.d(TAG, studentId + " caused an error");
                            log("ERROR,"+studentId+","+calendar.getTime().toString());
                            break;
                    }
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int id = data.getIntExtra("id", 0);
        String name = data.getStringExtra("name");
        log("ADDED,"+id+","+name+","+calendar.getTime().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //log("ADDED "+studentId+" "+manager.getStudentName(studentId)+" "+calendar.getTime().toString());
    }

    private void goodStudent(int studentId)
    {
        statusView.setBackgroundColor(GREEN);
        String studentName = manager.getStudentName(studentId);
        statusView.setText(studentName);
        log("ACCEPT,"+studentId+","+studentName+","+calendar.getTime().toString());
    }

    private void badStudent(int studentId)
    {
        statusView.setBackgroundColor(RED);
        String studentName = manager.getStudentName(studentId);
        statusView.setText(studentName);
        log("DENIED,"+studentId+","+studentName+","+calendar.getTime().toString());
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
