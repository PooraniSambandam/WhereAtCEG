package com.example.whereatcegfinal;

/**
 * Created by POORANI on 07-Sep-17.
 */

public class DatabaseContract {
    public static final String DatabaseName = "WhereatcegDB";
    public static abstract class Teacher_Table
    {
        public static final String Table_name = "teachers";
        public static final String Teacher_id = "teach_Id";
        public static final String Teacher_name = "Name";
        public static final String Teacher_dept = "Dept";
        public static final String Teacher_room = "Room";
        public static final String Teacher_phone = "Phone";

    }

    public static abstract class Location_Table {
        public static final String Table_name = "location_table";
        public static final String Location_id = "loc_id";
        public static final String Location_name = "loc_name";
        public static final String Latitude = "latitude";
        public static final String Longitude = "longitude";
        public static final String Category = "category";

    }
}
