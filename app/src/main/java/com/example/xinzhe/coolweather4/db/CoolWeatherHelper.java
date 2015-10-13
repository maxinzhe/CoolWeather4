package com.example.xinzhe.coolweather4.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Xinzhe on 2015/10/12.
 */
public class CoolWeatherHelper extends SQLiteOpenHelper{
    private final String CREATE_PROVINCE="create table Province (" +
            "id integer primary key autoincrement," +
            "province_code text," +
            "province_name text)";
    private final String CREATE_CITY="create table City(" +
            "id integer primary key autoincrement," +
            "city_code text," +
            "city_name text," +
            "province_id integer)";
    private final String CREATE_COUNTY="create table County(" +
            "id integer primary key autoincrement," +
            "county_code text," +
            "county_name text," +
            "city_id integer)";

    public CoolWeatherHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
