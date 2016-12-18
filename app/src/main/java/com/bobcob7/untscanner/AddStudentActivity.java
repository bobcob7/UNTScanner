package com.bobcob7.untscanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class AddStudentActivity extends Activity {

    private int studentId;
    private TextView idView;
    private EditText nameView;
    private Switch activeView;
    private SQLManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        Intent intent = getIntent();
        studentId = intent.getIntExtra("id", 0);

        idView = (TextView)findViewById(R.id.idView);
        nameView = (EditText) findViewById(R.id.nameView);
        activeView = (Switch) findViewById(R.id.activeView);

        CharSequence idMsg = "Student ID: " + studentId;
        idView.setText(idMsg);

        nameView.requestFocus();

        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        manager = new SQLManager(this);
    }

    public void onCancel(View view) {
        this.finish();
    }

    public void onSubmit(View view) {
        manager.addStudent(studentId,nameView.getText().toString(),activeView.isChecked());

        Intent output = new Intent(AddStudentActivity.this, ScanActivity.class);
        output.putExtra("id",studentId);
        output.putExtra("name",nameView.getText().toString());

        setResult(0,output);

        this.finish();
    }
}
