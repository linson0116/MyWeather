package com.example.linson.myweather;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.linson.myweather.domain.City;
import com.example.linson.myweather.domain.County;
import com.example.linson.myweather.domain.Province;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ChooseWeatherFragment extends Fragment {

    public static final int PROVINCE = 0;
    public static final int CITY = 1;
    public static final int COUNTY = 2;
    private ListView lv_choose_weather;
    private TextView tv_title;
    private ImageView iv_back;
    private List<Province> mProvinceList = new ArrayList();
    private List<City> mCityList = new ArrayList();
    private List<County> mCountyList = new ArrayList<>();
    private List<String> mDataList = new ArrayList<>();
    private MyAdapter myAdapter;
    private Context context;
    private int list_level;
    private String url = "http://guolin.tech/api/china";
    private String provinceCode = null;
    private String provinceName = null;
    private String cityCode = null;
    private String cityName = null;
    private String countyCode = null;
    private String countyName = null;
    private String weatherId = null;

    public ChooseWeatherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_weather, container, false);
        context = getContext();
        initUI(view);
        initData();
        return view;
    }

    private void initData() {
        String str_province = ToolsUtils.getString(context, "province", null);
        if (str_province == null) {
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.i(TAG, "从网络读取 省 成功");
                    ToolsUtils.setString(context, "province", responseInfo.result);
                    ToolsUtils.saveProvinceToDB(responseInfo.result);
                    mProvinceList = ToolsUtils.getProvince();
                    mDataList.clear();
                    for (Province p : mProvinceList) {
                        mDataList.add(p.getProvinceName());
                    }
                    myAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.i(TAG, "从网络读取 省 失败");
                }
            });
        } else {
            mProvinceList = ToolsUtils.getProvince();
            mDataList.clear();
            for (Province p : mProvinceList) {
                mDataList.add(p.getProvinceName());
            }
            myAdapter.notifyDataSetChanged();
            Log.i(TAG, "从数据库读取 省 成功");
        }
        list_level = PROVINCE;
    }


    private void initUI(View view) {
        lv_choose_weather = (ListView) view.findViewById(R.id.lv_choose_weather);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        myAdapter = new MyAdapter();
        lv_choose_weather.setAdapter(myAdapter);
        lv_choose_weather.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list_level == PROVINCE) {
                    provinceCode = i + 1 + "";
                    provinceName = mDataList.get(i);
                    tv_title.setText(provinceName);
                    iv_back.setVisibility(View.VISIBLE);
                    String city_url = url + "/" + provinceCode;
                    Log.i(TAG, "点击省列表 " + city_url);
                    final List<City> city = ToolsUtils.getCity(provinceCode);
                    if (city.size() == 0) {
                        Log.i(TAG, "初始化 城市");
                        HttpUtils httpUtils = new HttpUtils();
                        httpUtils.send(HttpRequest.HttpMethod.GET, city_url, new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                ToolsUtils.saveCityToDB(responseInfo.result, provinceCode);
                                List<City> db_city = ToolsUtils.getCity(provinceCode);
                                mDataList.clear();
                                for (City city : db_city) {
                                    mDataList.add(city.getCityName());
                                }
                                myAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onFailure(HttpException e, String s) {
                                Log.i(TAG, "从网络读取 城市 失败");
                            }
                        });
                    } else {
                        mDataList.clear();
                        for (City c : city) {
                            mDataList.add(c.getCityName());
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                    list_level = CITY;
                } else if (list_level == CITY) {
                    cityName = mDataList.get(i);
                    cityCode = ToolsUtils.findCityCodeByName(cityName);
                    Log.i(TAG, "点击城市code " + cityCode);
                    tv_title.setText(cityName);
                    //iv_back.setVisibility(View.VISIBLE);
                    String city_url = url + "/" + provinceCode + "/" + cityCode;
                    Log.i(TAG, "点击城市列表" + city_url);
                    final List<County> County = ToolsUtils.getCounty(cityCode);
                    if (County.size() == 0) {
                        Log.i(TAG, "初始化 县城");
                        HttpUtils httpUtils = new HttpUtils();
                        httpUtils.send(HttpRequest.HttpMethod.GET, city_url, new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                ToolsUtils.saveCountyToDB(responseInfo.result, cityCode);
                                List<County> db_county = ToolsUtils.getCounty(cityCode);
                                mDataList.clear();
                                for (County county : db_county) {
                                    mDataList.add(county.getCountyName());
                                }
                                myAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                Log.i(TAG, "从网络读取 城市 失败");
                            }
                        });
                    } else {
                        mDataList.clear();
                        for (County c : County) {
                            mDataList.add(c.getCountyName());
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                    list_level = COUNTY;
                }else if (list_level == COUNTY) {
                    Log.i(TAG, "点击区县 ");
                    countyName = mDataList.get(i);
                    String weather_id = ToolsUtils.findWeatherIdByName(countyName);
                    Log.i(TAG, "weather_id: " + weather_id);
                    Intent intent = new Intent(context, WeatherActivity.class);
                    intent.putExtra("weather_id", weather_id);
                    ToolsUtils.setString(context, "weather_id", weather_id);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list_level == CITY) {
                    List<Province> list = ToolsUtils.getProvince();
                    mDataList.clear();
                    for (Province p : list) {
                        mDataList.add(p.getProvinceName());
                    }
                    myAdapter.notifyDataSetChanged();
                    list_level = PROVINCE;
                    iv_back.setVisibility(View.GONE);
                    tv_title.setText("中国");
                } else if (list_level == COUNTY) {
                    List<City> list = ToolsUtils.getCity(provinceCode);
                    mDataList.clear();
                    for (City c : list) {
                        mDataList.add(c.getCityName());
                    }
                    myAdapter.notifyDataSetChanged();
                    list_level = CITY;
                    iv_back.setVisibility(View.VISIBLE);
                    tv_title.setText(provinceName);
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int i) {
            return mDataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView tv = new TextView(context);
            tv.setPadding(10, 10, 10, 10);
            tv.setText(mDataList.get(i));
            return tv;
        }
    }
}
