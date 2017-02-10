package com.example.linson.myweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.linson.myweather.domain.City;
import com.example.linson.myweather.domain.County;
import com.example.linson.myweather.domain.Province;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by linson on 2017/2/8.
 */

public class ToolsUtils {
    private static String backgroundImage;
//    static HttpUtils httpUtils = new HttpUtils();

    public static void saveProvince(final Context context, String url) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                setString(context, "province", responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i(TAG, "onFailure: " + s);
            }
        });
    }

    public static void saveProvinceToDB(String content) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Province province = new Province();
                province.setProvinceCode(obj.getString("id"));
                province.setProvinceName(obj.getString("name"));
                province.save();
            }
            Log.i(TAG, "保存" + jsonArray.length() + "条数据");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveCity(String url, final String provinceCode) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                saveCityToDB(responseInfo.result,provinceCode);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    public static void saveCityToDB(String content,String provinceCode) {
        JSONArray jsonArray = null;
        List<City> cityList = getCity();
        try {
            jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                City city = new City();
                city.setCityName(obj.getString("name"));
                city.setProvinceCode(provinceCode);
                city.setCityCode(obj.getString("id"));
                if (!cityList.contains(city)) {
                    city.save();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveCounty(String url,final String cityCode) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                saveCountyToDB(responseInfo.result,cityCode);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    public static void saveCountyToDB(String content,String cityCode) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                County county = new County();
                county.setCountyCode(obj.getString("id"));
                county.setCountyName(obj.getString("name"));
                county.setCityCode(cityCode);
                county.setWeather_id(obj.getString("weather_id"));
                county.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<Province> getProvince() {
        List<Province> list = null;
        list = DataSupport.findAll(Province.class);
        Log.i(TAG, "getProvince: " + list.size());
        return list;
    }

    public static List<City> getCity() {
        List<City> list = null;
        list = DataSupport.findAll(City.class);
        return list;
    }
    public static List<City> getCity(String provinceCode) {
        List<City> list = null;
        list = DataSupport.where("provinceCode = ? ",provinceCode).find(City.class);
        return list;
    }

//    public static List<County> getCounty() {
//        List<County> list = null;
//        list = DataSupport.findAll(County.class);
//        return list;
//    }
    public static List<County> getCounty(String cityCode) {
        List<County> list = null;
        list = DataSupport.where("cityCode = ?",cityCode).find(County.class);
        return list;
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        String string = sharedPreferences.getString(key, defaultValue);
        return string;
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String findCityCodeByName(String cityName) {
        List<City> list = DataSupport.where("cityName = ?", cityName).find(City.class);
        if (list.size() > 0) {
            return list.get(0).getCityCode();
        }
        return null;
    }

    public static String findWeatherIdByName(String countyName) {
        Log.i(TAG, "findWeatherIdByName: " + countyName);
        List<County> list = DataSupport.where("countyName = ?", countyName).find(County.class);
        if (list.size() > 0) {
            return list.get(0).getWeather_id();
        }
        return null;
    }


}
