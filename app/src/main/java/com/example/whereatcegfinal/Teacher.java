package com.example.whereatcegfinal;

/**
 * Created by POORANI on 27-Aug-17.
 */

public class Teacher
{
    int o_id;
    public String o_name;
    public String o_dept;
    public String o_room;
    public String o_phone;
    Teacher()
    {

    }

    void setO_id(int o_id){this.o_id=o_id;}
    void setO_name(String o_name)
    {
        this.o_name=o_name;
    }
    void setO_dept(String o_dept)
    {
        this.o_dept=o_dept;
    }
    void setO_room(String o_room)
    {
        this.o_room=o_room;
    }
    void setO_phone(String o_phone)
    {
        this.o_phone=o_phone;
    }
    int getO_id(){return o_id;}
    String getO_name()
    {
        return o_name;
    }
    String getO_dept()
    {
        return o_dept;
    }
    String getO_room()
    {
        return o_room;
    }
    String getO_phone()
    {
        return o_phone;
    }

}
