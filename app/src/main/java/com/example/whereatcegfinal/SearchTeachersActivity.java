package com.example.whereatcegfinal;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SearchTeachersActivity extends AppCompatActivity {
    private TextInputEditText textInputEditTextStaff;
    private TextInputEditText textInputEditTextDept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_teachers);
        textInputEditTextStaff=(TextInputEditText) findViewById(R.id.byStaffEdit);
        textInputEditTextDept=(TextInputEditText) findViewById(R.id.byDeptEdit);
    }

    public void searchByButton(View view) {

        Intent SearchTeachersResult= new Intent(this,SearchResultActivity.class);
        String staffName=textInputEditTextStaff.getText().toString().trim();
        String deptName=textInputEditTextDept.getText().toString().trim();

        Bundle bundle = new Bundle();

        bundle.putString("byStaff",staffName);
        bundle.putString("byDept",deptName);
        SearchTeachersResult.putExtras(bundle);
        emptyInputEditText();
        startActivity(SearchTeachersResult);
    }

    private void emptyInputEditText() {
        textInputEditTextStaff.setText(null);
        textInputEditTextDept.setText(null);
    }
}
