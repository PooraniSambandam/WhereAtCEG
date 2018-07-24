package com.example.whereatcegfinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.whereatcegfinal.dbHandler1;
import com.example.whereatcegfinal.Teacher;

public class InsertLocationActivity extends AppCompatActivity {

    private TextInputEditText textInputEditTextLocation, textInputEditTextLati, textInputEditTextLongi,textInputEditTextCategory;

    Context context;
    dbHandler1 dbHandlerObj;
    SQLiteDatabase db;
    Button btnviewAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_location);
        textInputEditTextLocation=(TextInputEditText) findViewById(R.id.locationEdit);
        textInputEditTextLati =(TextInputEditText) findViewById(R.id.latiEdit);
        textInputEditTextLongi =(TextInputEditText) findViewById(R.id.longiEdit);
        textInputEditTextCategory=(TextInputEditText) findViewById(R.id.categoryEdit);
        btnviewAll = (Button)findViewById(R.id.button_viewAll);
        dbHandlerObj = new dbHandler1(this);


        viewAll();
    }


    public void insertForm(View view) {
        String location = textInputEditTextLocation.getText().toString().trim();
        double latitude = Double.parseDouble(textInputEditTextLati.getText().toString().trim());
        double longitude = Double.parseDouble(textInputEditTextLongi.getText().toString().trim());
        String category = textInputEditTextCategory.getText().toString().trim();

        dbHandlerObj = new dbHandler1(this);
        db = dbHandlerObj.getWritableDatabase();
        dbHandlerObj.addLocation(location,latitude,longitude,category,db);
        Toast.makeText(getBaseContext(),"Data saved",Toast.LENGTH_LONG).show();
        emptyInputEditText();
    }

    private void emptyInputEditText() {
        textInputEditTextLocation.setText(null);
        textInputEditTextLati.setText(null);
        textInputEditTextLongi.setText(null);
        textInputEditTextCategory.setText(null);
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
                            buffer.append("\nLocation : " + res.getString(1));
                            buffer.append("\nLatitude : " + res.getDouble(2));
                            buffer.append("\nLongitude : " + res.getDouble(3));
                            buffer.append("\nCategory : " + res.getString(4));
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

    public void dropTable(View view) {
        dbHandlerObj = new dbHandler1(this);
        db = dbHandlerObj.getWritableDatabase();
        dbHandlerObj.dropTable(db);
        Toast.makeText(getBaseContext(),"Table dropped",Toast.LENGTH_LONG).show();

    }
}
