package com.example.whereatcegfinal;

/**
 * Created by POORANI on 27-Aug-17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.whereatcegfinal.DatabaseContract;
import com.example.whereatcegfinal.Teacher;


public class dbHandler extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    /*
    private static final String DATABASE_NAME ="WhereatcegDB";
    // Contacts table name
    private static final String TABLE_TEACHERS = "teachers";
    // Shops Table Columns names
    private static final String COL0_ID ="Id";
    private static final String COL1_NAME ="Name";
    private static final String COL2_DEPT ="Dept";
    private static final String COL3_ROOM ="Room";
    private static final String COL4_PHONE ="Phone";
*/
    private static final String CREATE_QUERY=
            "CREATE TABLE "+ DatabaseContract.Teacher_Table.Table_name+" ( "
                    + DatabaseContract.Teacher_Table.Teacher_id +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DatabaseContract.Teacher_Table.Teacher_name+" TEXT, "
                    + DatabaseContract.Teacher_Table.Teacher_dept+" TEXT,"
                    + DatabaseContract.Teacher_Table.Teacher_room+" TEXT, "
                    + DatabaseContract.Teacher_Table.Teacher_phone+" TEXT );";

    private static final String CREATE_QUERY_NoExist=
            "CREATE TABLE IF NOT EXISTS "+ DatabaseContract.Teacher_Table.Table_name+" ( "
                    + DatabaseContract.Teacher_Table.Teacher_id +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DatabaseContract.Teacher_Table.Teacher_name+" TEXT, "
                    + DatabaseContract.Teacher_Table.Teacher_dept+" TEXT,"
                    + DatabaseContract.Teacher_Table.Teacher_room+" TEXT, "
                    + DatabaseContract.Teacher_Table.Teacher_phone+" TEXT );";


    public dbHandler(Context context)
    {
        super(context, DatabaseContract.DatabaseName, null, DATABASE_VERSION);
        Log.e("Database Operations","Database created / opened...");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String CREATE_TEACHERS_TABLE ="CREATE TABLE " + TABLE_TEACHERS +"("
        //+ COL0_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL1_NAME + " TEXT,"
        //+ COL2_DEPT + " TEXT, " + COL3_ROOM + " TEXT, " + COL4_PHONE + " TEXT, " + ");";
        db.execSQL(CREATE_QUERY);
        Log.e("Database Operations","Table created...");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Teacher_Table.Table_name);
// Creating tables again
        onCreate(db);
        Log.e("Database Operations","Table created...");
    }

    public void addTeachers(String name,String dept,String room,String phone,SQLiteDatabase db) {
        //SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(CREATE_QUERY_NoExist);
        ContentValues values = new ContentValues();
        //values.put(COL1_NAME, "0");()
        values.putNull(DatabaseContract.Teacher_Table.Teacher_id);
        values.put(DatabaseContract.Teacher_Table.Teacher_name, name);
        values.put(DatabaseContract.Teacher_Table.Teacher_dept, dept);
        values.put(DatabaseContract.Teacher_Table.Teacher_room,room);
        values.put(DatabaseContract.Teacher_Table.Teacher_phone, phone);

        db.insert(DatabaseContract.Teacher_Table.Table_name, null,values);
        Log.e("Database Operations","Row Inserted.....");
        // Inserting Row
        //db.insert(TABLE_TEACHERS, null, values);
        //db.close(); // Closing database connection
    }

    public Cursor getAllData(SQLiteDatabase db)
    {
        String selectAllQuery = "SELECT * FROM "+ DatabaseContract.Teacher_Table.Table_name+";";
        Cursor res = db.rawQuery(selectAllQuery,null);
        return res;
    }

    public void dropTable(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Teacher_Table.Table_name);
        return;
    }

    public Cursor getSearchResult(String staffKey,String deptKey)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM " + DatabaseContract.Teacher_Table.Table_name
                + " WHERE " + DatabaseContract.Teacher_Table.Teacher_dept + " = '" + deptKey
                + "' OR " + DatabaseContract.Teacher_Table.Teacher_name + " = '" + staffKey + "';";
        Cursor res = db.rawQuery(searchQuery,null);

        return res;
    }
}
