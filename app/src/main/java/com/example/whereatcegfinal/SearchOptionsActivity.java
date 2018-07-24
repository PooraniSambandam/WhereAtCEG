package com.example.whereatcegfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SearchOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_options);
    }

    public void gotoSearchTeachers(View view) {
        Intent SearchOptionsTeachers = new Intent(SearchOptionsActivity.this,SearchTeachersActivity.class);
        startActivity(SearchOptionsTeachers);
    }

    public void gotoInsertForm(View view) {
        Intent SearchOptionsInsertForm= new Intent(SearchOptionsActivity.this,InsertFormActivity.class);
        startActivity(SearchOptionsInsertForm);
    }
}
