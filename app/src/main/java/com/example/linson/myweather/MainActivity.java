package com.example.linson.myweather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "content";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        String weather = ToolsUtils.getString(context, "weather", null);
        Intent myIntent = getIntent();
        int type = myIntent.getIntExtra("type", 0);
        if (type == 1 || weather == null || weather.length() < 50) {
            Log.i(TAG, "打开城市选择列表");
        } else {
            Intent intent = new Intent(context, WeatherActivity.class);
            intent.putExtra("weather", weather);
            intent.putExtra("weather_id", ToolsUtils.getString(context, "weather_id", null));
            startActivity(intent);
            finish();
        }
        Intent intent = new Intent(context, UpdateService.class);
        startService(intent);
    }
}
