package com.example.linson.myweather.domain;

import android.util.Log;

import org.litepal.crud.DataSupport;

import static android.content.ContentValues.TAG;

/**
 * Created by linson on 2017/2/8.
 */

public class City  extends DataSupport {
    private long id;
    private String cityName;
    private String cityCode;
    private String provinceCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
}
