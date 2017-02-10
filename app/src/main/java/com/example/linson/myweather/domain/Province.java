package com.example.linson.myweather.domain;

import org.litepal.crud.DataSupport;

/**
 * Created by linson on 2017/2/8.
 */

public class Province extends DataSupport{
    private long id;
    private String provinceName;
    private String provinceCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
}
