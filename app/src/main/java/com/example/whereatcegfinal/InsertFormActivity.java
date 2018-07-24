package com.example.whereatcegfinal;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.common.collect.Range;

import com.example.whereatcegfinal.dbHandler;
import com.example.whereatcegfinal.Teacher;

//public class InsertFormActivity extends AppCompatActivity implements View.OnClickListener {
public class InsertFormActivity extends AppCompatActivity{
    /*
    private final AppCompatActivity activity=InsertFormActivity.this;
    private Teacher teach;
    private dbHandler dbHelp;
    private AppCompatButton appCompatButtonAdd;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutDept;
    private TextInputLayout textInputLayoutRoom;
    private TextInputLayout textInputLayoutPhone;
*/
    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextDept;
    private TextInputEditText textInputEditTextRoom;
    private TextInputEditText textInputEditTextPhone;

    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;


    Context context;
    dbHandler dbHandlerObj;
    SQLiteDatabase db;
    Teacher teacherObj;
    Button btnviewAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_form);
        textInputEditTextName=(TextInputEditText) findViewById(R.id.nameEdit);
        textInputEditTextDept=(TextInputEditText) findViewById(R.id.deptEdit);
        textInputEditTextRoom=(TextInputEditText) findViewById(R.id.roomEdit);
        textInputEditTextPhone=(TextInputEditText) findViewById(R.id.phoneEdit);
        btnviewAll = (Button)findViewById(R.id.button_viewAll);
        dbHandlerObj = new dbHandler(this);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        //adding validation to edittexts
        awesomeValidation.addValidation(this, R.id.nameEdit, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.deptEdit, "^[A-Za-z\\s]{1,}$", R.string.depterror);
        awesomeValidation.addValidation(this, R.id.roomEdit, "^[1-3]{1}[0-9]{2}$", R.string.roomerror);
        awesomeValidation.addValidation(this, R.id.phoneEdit, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);


        viewAll();

/*
        initViews();
        initListeners();
        initObjects();
        */
    }
    /*

    private void initObjects() {

        teach = new Teacher();
        dbHelp= new dbHandler(activity);
    }

    private void initViews() {

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutName=(TextInputLayout) findViewById(R.id.nameInput);
        textInputLayoutDept=(TextInputLayout) findViewById(R.id.deptInput);
        textInputLayoutRoom=(TextInputLayout) findViewById(R.id.roomInput);
        textInputLayoutPhone=(TextInputLayout) findViewById(R.id.phoneInput);

        textInputEditTextName=(TextInputEditText) findViewById(R.id.nameEdit);
        textInputEditTextDept=(TextInputEditText) findViewById(R.id.deptEdit);
        textInputEditTextRoom=(TextInputEditText) findViewById(R.id.roomEdit);
        textInputEditTextPhone=(TextInputEditText) findViewById(R.id.phoneEdit);

        appCompatButtonAdd = (AppCompatButton) findViewById(R.id.appCompatButtonAdd);
    }

    private void initListeners() {
        appCompatButtonAdd.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        postDataToSqlite();
    }

    private void postDataToSqlite() {

        teach.setO_name(textInputEditTextName.getText().toString().trim());
        teach.setO_dept(textInputEditTextDept.getText().toString().trim());
        teach.setO_room(textInputEditTextRoom.getText().toString().trim());
        teach.setO_phone(textInputEditTextPhone.getText().toString().trim());

        dbHelp.addTeachers(teach);
        Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
        emptyInputEditText();
    }
*/
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextDept.setText(null);
        textInputEditTextRoom.setText(null);
        textInputEditTextPhone.setText(null);
    }


    public void insertForm(View view) {
        String name = textInputEditTextName.getText().toString().trim();
        String dept = textInputEditTextDept.getText().toString().trim();
        String room = textInputEditTextRoom.getText().toString().trim();
        String phone = textInputEditTextPhone.getText().toString().trim();

        dbHandlerObj = new dbHandler(this);
        db = dbHandlerObj.getWritableDatabase();
        if (awesomeValidation.validate()) {
            dbHandlerObj.addTeachers(name,dept,room,phone,db);
            Toast.makeText(getBaseContext(),"Data saved",Toast.LENGTH_LONG).show();
            emptyInputEditText();
        }
    }

    public void viewAll()
    {
        btnviewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db = dbHandlerObj.getReadableDatabase();
                        Cursor res = dbHandlerObj.getAllData(db);
                        if (res.getCount() == 0) {//Returns number of rows in cursor.
                            //show message
                            showMessage("Error", "Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {//moves to next row the cursor holds
                            buffer.append("\n\nId : " + res.getString(0));
                            buffer.append("\nName : " + res.getString(1));
                            buffer.append("\nDept : " + res.getString(2));
                            buffer.append("\nRoom : " + res.getString(3));
                            buffer.append("\nPhone : " + res.getString(4));
                        }
                        //Show all data
                        showMessage("Data ", buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void dropTable(View view) jvjbdf{
        dbHandlerObj = new dbHandler(this);
        db = dbHandlerObj.getWritableDatabase();
        dbHandlerObj.dropTable(db);
        Toast.makeText(getBaseContext(),"Table dropped",Toast.LENGTH_LONG).show();

    }
}
