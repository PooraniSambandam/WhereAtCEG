package com.example.whereatcegfinal;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SearchResultActivity extends AppCompatActivity {

    dbHandler dbHandlerObj;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        displayResult();
    }

    public void displayResult()
    {
        Bundle bundle = getIntent().getExtras();
        String byStaff = bundle.getString("byStaff");
        String byDept = bundle.getString("byDept");

        dbHandlerObj = new dbHandler(this);
        db = dbHandlerObj.getReadableDatabase();

        Cursor res = dbHandlerObj.getSearchResult(byStaff,byDept);
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
        Toast.makeText(getBaseContext(),byStaff+" "+byDept,Toast.LENGTH_LONG).show();
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
