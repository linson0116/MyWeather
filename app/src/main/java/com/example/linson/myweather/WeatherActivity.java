package com.example.linson.myweather;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "content";
    private ImageView iv_background;
    private Context context;

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
        String weather_id = intent.getStringExtra("weather_id");
        Log.i(TAG, "onCreate: " + weather_id);
        initUI();
        initData();
    }

    private void initData() {
        String url = "http://guolin.tech/api/bing_pic";
        getBackgroundImage(url);
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
                Log.i(TAG, "获取背景图片失败 " +s);
                Glide.with(context).load(R.drawable.fengjing_1).into(iv_background);
            }
        });

    }

    private void initUI() {
        iv_background = (ImageView) findViewById(R.id.iv_background);
    }
}
