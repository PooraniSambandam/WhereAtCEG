package com.example.whereatcegfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }

    public void gotoNavigate(View view) {

        Intent OptionsNavigate=new Intent(OptionsActivity.this,NavigateActivity.class);
        startActivity(OptionsNavigate);
    }

    public void gotoSearchOptions(View view) {
        Intent OptionsSearchOptions = new Intent(OptionsActivity.this,SearchOptionsActivity.class);
        startActivity(OptionsSearchOptions);
    }
/*
    public void gotoInsertLocation(View view) {
        Intent OptionsInsertLocation = new Intent(OptionsActivity.this,InsertLocationActivity.class);
        startActivity(OptionsInsertLocation);
    }
*/

}
