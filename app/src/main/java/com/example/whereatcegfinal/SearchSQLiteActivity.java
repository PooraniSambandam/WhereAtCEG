package com.example.whereatcegfinal;

/**
 * Created by POORANI on 28-Sep-17.
 */


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
public class SearchSQLiteActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> PlacesList = new ArrayList<String>();
    ListAdapter listAdapter;
    dbHandler1 sqLiteHelper;
    EditText editText;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    String category;
    Bundle optionName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sqlite);

        listView = (ListView) findViewById(R.id.listView1);

        editText = (EditText) findViewById(R.id.edittext1);

        listView.setTextFilterEnabled(true);

        sqLiteHelper = new dbHandler1(this);
        optionName = getIntent().getExtras();
        category = optionName.getString("option");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Getting Search ListView clicked item.
                String ListViewClickData = (String) parent.getItemAtPosition(position);

                // printing clicked item on screen using Toast message.
                Toast.makeText(SearchSQLiteActivity.this, ListViewClickData, Toast.LENGTH_LONG).show();


                //Get latitude and longitude for the respective place
                sqLiteDatabase = sqLiteHelper.getWritableDatabase();
                String filterQuery = "SELECT * FROM "+DatabaseContract.Location_Table.Table_name
                +" WHERE " + DatabaseContract.Location_Table.Location_name + " = '"
                + ListViewClickData.trim() +"';";
                Cursor res = sqLiteDatabase.rawQuery(filterQuery, null);
                res.moveToFirst();
                double latitude = res.getDouble(res.getColumnIndex(DatabaseContract.Location_Table.Latitude));
                double longitude = res.getDouble(res.getColumnIndex(DatabaseContract.Location_Table.Longitude));



                //Go to navigation part
                Intent SearchNavigate = new Intent(SearchSQLiteActivity.this,NavigateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("latitudeValue",latitude);
                bundle.putDouble("longitudeValue",longitude);
                bundle.putString("destName",ListViewClickData);

                SearchNavigate.putExtras(bundle);
               // Replace with service :-p
                startActivity(SearchNavigate);

            }
        });


        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence stringVar, int start, int before, int count) {

                listAdapter.getFilter().filter(stringVar.toString());
            }
        });

    }

    public void DisplayDataInToListView() {

        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+DatabaseContract.Location_Table.Table_name
                +" WHERE " + DatabaseContract.Location_Table.Category + " = '"
                + category +"';", null);

        String places;
        PlacesList = new ArrayList<String>();

        if (cursor.moveToFirst()) {
            do {

                String tempLocationName = cursor.getString(cursor.getColumnIndex(DatabaseContract.Location_Table.Location_name));

                //String tempNumber= cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_2_PhoneNumber));

                //Create a class for locations instead of student. With attributes as location name, latitude, longitude
                //places = new Student(tempName, tempNumber);

                PlacesList.add(tempLocationName);

            } while (cursor.moveToNext());
        }

        listAdapter = new ListAdapter(SearchSQLiteActivity.this, R.layout.custom_layout, PlacesList);

        listView.setAdapter(listAdapter);

        cursor.close();
    }

    @Override
    protected void onResume() {

        DisplayDataInToListView() ;

        super.onResume();
    }

}

