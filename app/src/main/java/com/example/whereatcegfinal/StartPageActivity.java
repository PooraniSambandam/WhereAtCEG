package com.example.whereatcegfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
    }

    public void gotoOptions(View view) {
        Intent StartOptions=new Intent(StartPageActivity.this,OptionsActivity.class);
        startActivity(StartOptions);
    }
}
