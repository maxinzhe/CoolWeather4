package com.example.xinzhe.coolweather4.model;

/**
 * Created by Xinzhe on 2015/10/12.
 */
public class County {
    private String countyCode;
    private String countyName;
    private int CityId;

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }
}
