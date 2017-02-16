package com.example.linson.myweather;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.linson.myweather.domain.Weather;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "content";
    private ImageView iv_home_choose;
    private ImageView iv_background;
    private Context context;
    private String weather_id;
    private TextView tv_temperature;
    private TextView tv_temperature_status;
    private TextView tv_city_name;
    private TextView tv_update_time;
    private LinearLayout ll_temperature_week;
    private TextView tv_air_quality_aqi;
    private TextView tv_air_quality_pm25;
    private String weather;
    private String image_url = "http://guolin.tech/api/bing_pic";
    private DrawerLayout drawer_layout;
    private SwipeRefreshLayout sr_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        context = this;
        Intent intent = getIntent();
        weather_id = intent.getStringExtra("weather_id");
        weather = intent.getStringExtra("weather");
        initUI();
        getBackgroundImage(image_url);

        if (weather != null) {
            Log.i(TAG, "已保存的数据: " + weather);
            refreshData(weather);
        } else if (weather_id != null) {
            Log.i(TAG, "根据城市Id初始化数据: " + weather_id);
            initData(weather_id);
        } else {
            Log.i(TAG, "数据错误");
        }
    }

    private void initData(String weather_id) {
        //请求天气数据
        final String weathre_url = "http://guolin.tech/api/weather?cityid=" + weather_id + "&key=bc0418b57b2d4918819d3974ac1285d9";
        Log.i(TAG, "请求天气地址 " + weathre_url);

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, weathre_url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                final String result = responseInfo.result;
                ToolsUtils.setString(context, "weather", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshData(result);
                    }
                });
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i(TAG, "请求天气信息 失败 " + s);
            }
        });
    }

    private void refreshData(String result) {
        Log.i(TAG, "要刷新的数据 " + result);
        Gson gson = new Gson();
        Weather weather = gson.fromJson(result, Weather.class);
        Weather.HeWeather mWeather = weather.HeWeather.get(0);
        if (mWeather.now == null) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("type", 1);
            startActivity(intent);
            finish();
        } else {
            String temperature_code = "℃";
            tv_temperature.setText(mWeather.now.tmp + temperature_code);
            tv_temperature_status.setText(mWeather.now.cond.txt);
            tv_city_name.setText(mWeather.basic.city);
            String time = mWeather.basic.update.loc;
            tv_update_time.setText(time.substring(time.length() - 5));
            ll_temperature_week.removeAllViews();
            for (int i = 0; i < mWeather.daily_forecast.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.layout_week_item, null);
                TextView tv_week_date = (TextView) view.findViewById(R.id.tv_week_date);
                TextView tv_week_status = (TextView) view.findViewById(R.id.tv_week_status);
                TextView tv_week_max = (TextView) view.findViewById(R.id.tv_week_max);
                TextView tv_week_min = (TextView) view.findViewById(R.id.tv_week_min);
                tv_week_date.setText(mWeather.daily_forecast.get(i).date);
                tv_week_status.setText(mWeather.daily_forecast.get(i).cond.txt_d);
                tv_week_max.setText(mWeather.daily_forecast.get(i).tmp.max);
                tv_week_min.setText(mWeather.daily_forecast.get(i).tmp.min);
                ll_temperature_week.addView(view);
            }
            tv_air_quality_aqi.setText(mWeather.aqi.city.aqi);
            tv_air_quality_pm25.setText(mWeather.aqi.city.pm25);
            Toast.makeText(context, "天气已刷新", Toast.LENGTH_SHORT).show();
        }

    }

    private void getBackgroundImage(String url) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.i(TAG, "取得背景图片真实地址：" + responseInfo.result);
                Glide.with(context).load(responseInfo.result).into(iv_background);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i(TAG, "获取背景图片失败 " + s);
                Glide.with(context).load(R.drawable.fengjing_1).into(iv_background);
            }
        });

    }

    private void initUI() {
        iv_home_choose = (ImageView) findViewById(R.id.iv_home_choose);
        iv_home_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(GravityCompat.START);
            }
        });
        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_temperature_status = (TextView) findViewById(R.id.tv_temperature_status);
        tv_city_name = (TextView) findViewById(R.id.tv_city_name);
        iv_background = (ImageView) findViewById(R.id.iv_background);
        tv_update_time = (TextView) findViewById(R.id.tv_update_time);
        ll_temperature_week = (LinearLayout) findViewById(R.id.ll_temperature_week);
        tv_air_quality_aqi = (TextView) findViewById(R.id.tv_air_quality_aqi);
        tv_air_quality_pm25 = (TextView) findViewById(R.id.tv_air_quality_pm25);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sr_layout = (SwipeRefreshLayout) findViewById(R.id.sr_layout);
        sr_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(weather_id);
                sr_layout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
