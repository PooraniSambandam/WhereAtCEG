package com.example.whereatcegfinal;

/**
 * Created by POORANI on 27-Sep-17.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class dbHandler1 extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_QUERY=
            "CREATE TABLE "+ DatabaseContract.Location_Table.Table_name+" ( "
                    + DatabaseContract.Location_Table.Location_id +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DatabaseContract.Location_Table.Location_name+" TEXT, "
                    + DatabaseContract.Location_Table.Latitude+" DECIMAL, "
                    + DatabaseContract.Location_Table.Longitude+" DECIMAL, "
                    + DatabaseContract.Location_Table.Category+" TEXT );";

    private static final String CREATE_QUERY_NoExist=
            "CREATE TABLE IF NOT EXISTS "+ DatabaseContract.Location_Table.Table_name+" ( "
                    + DatabaseContract.Location_Table.Location_id +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DatabaseContract.Location_Table.Location_name+" TEXT, "
                    + DatabaseContract.Location_Table.Latitude+" DECIMAL, "
                    + DatabaseContract.Location_Table.Longitude+" DECIMAL, "
                    + DatabaseContract.Location_Table.Category+" TEXT );";

    @Override
    public void onCreate(SQLiteDatabase db) {
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
    public dbHandler1(Context context)
    {
        super(context, DatabaseContract.DatabaseName, null, DATABASE_VERSION);
        Log.e("Database Operations","Database created / opened...");
    }

    public void addLocation(String name,double lati, double longi,String category, SQLiteDatabase db) {
        //SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(CREATE_QUERY_NoExist);
        ContentValues values = new ContentValues();
        //values.put(COL1_NAME, "0");()
        values.putNull(DatabaseContract.Location_Table.Location_id);
        values.put(DatabaseContract.Location_Table.Location_name, name);
        values.put(DatabaseContract.Location_Table.Latitude, lati);
        values.put(DatabaseContract.Location_Table.Longitude,longi);
        values.put(DatabaseContract.Location_Table.Category, category);

        db.insert(DatabaseContract.Location_Table.Table_name, null,values);
        Log.e("Database Operations","Row Inserted.....");
        // Inserting Row
        //db.insert(TABLE_TEACHERS, null, values);
        //db.close(); // Closing database connection
    }

    public Cursor getAllData(SQLiteDatabase db)
    {
        String selectAllQuery = "SELECT * FROM "+ DatabaseContract.Location_Table.Table_name+";";
        Cursor res = db.rawQuery(selectAllQuery,null);
        return res;
    }

    public void dropTable(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Location_Table.Table_name);
        return;
    }

    public Cursor retrieve(String searchTerm,SQLiteDatabase db)
    {
        String[] columns={DatabaseContract.Location_Table.Location_id,DatabaseContract.Location_Table.Location_name};
        Cursor c = null;
        if(searchTerm != null && searchTerm.length()>0)
        {
            String sql="SELECT * FROM "+DatabaseContract.Location_Table.Table_name+" WHERE "+DatabaseContract.Location_Table.Location_name
                    +" LIKE '%"+searchTerm+"%'";
            c=db.rawQuery(sql,null);
            return c;
        }
        c=db.query(DatabaseContract.Location_Table.Table_name,columns,null,null,null,null,null);
        return c;
    }
}
