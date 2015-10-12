package com.example.xinzhe.coolweather4.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.xinzhe.coolweather4.model.City;
import com.example.xinzhe.coolweather4.model.County;
import com.example.xinzhe.coolweather4.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xinzhe on 2015/10/12.
 */
public class CoolWeatherDB {
    private final static int VERSION=1;
    private CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    private CoolWeatherDB(Context context) {
        CoolWeatherHelper coolWeatherHelper=new CoolWeatherHelper(context,"CoolWeatherDB",null,VERSION);
        db=coolWeatherHelper.getWritableDatabase();
    }
    public synchronized CoolWeatherDB getInstancce(Context context){
        if(coolWeatherDB!=null){
            coolWeatherDB=new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }
    public List<Province> loadProvinces(){
        List<Province> provinceList=new ArrayList<>();
        Cursor cursor =db.query("Province",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do{
                Province province=new Province();
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                provinceList.add(province);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return provinceList;
    }
    public List<City> loadCities(Province province){//????
        List<City> cityList=new ArrayList<>();
        Cursor cursor=db.query("City",null,"province_id=?",new String[]{String.valueOf(province.getProvinceCode())},null,null,null);
        if (cursor.moveToFirst()){
            do{
                City city=new City();
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
                cityList.add(city);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return cityList;
    }
    public List<County> loadCounty(City city){
        List<County> countyList=new ArrayList<>();
        Cursor cursor=db.query("County",null,"city_id=?",new String []{String.valueOf(city.getCityCode())},null,null,null);
        if(cursor.moveToFirst()){
            do{
                County county=new County();
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                countyList.add(county);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return countyList;
    }

    public void saveProvince(Province province){
        ContentValues values=new ContentValues();
        values.put("province_code",province.getProvinceCode());
        values.put("province_name",province.getProvinceName());
        db.insert("Province", null, values);
    }
    public void saveCity(City city){
        ContentValues values=new ContentValues();
        values.put("city_code",city.getCityCode());
        values.put("city_name",city.getCityName());
        values.put("province_id",city.getProvinceId());
        db.insert("City",null,values);
    }
    public void saveCounty(County county){
        ContentValues values=new ContentValues();
        values.put("county_code",county.getCountyCode());
        values.put("county_name",county.getCountyName());
        values.put("city_id",county.getCountyName());
        db.insert("County",null,values);
    }

}
