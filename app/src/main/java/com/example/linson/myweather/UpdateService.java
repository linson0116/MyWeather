package com.example.linson.myweather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import static android.content.ContentValues.TAG;

public class UpdateService extends Service {
    Context context;

    public UpdateService() {
        context = this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "服务更新启动");
        String weather_id = ToolsUtils.getString(this, "weather_id", null);
        if (weather_id != null) {
            String weathre_url = "http://guolin.tech/api/weather?cityid=" + weather_id + "&key=bc0418b57b2d4918819d3974ac1285d9";
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.send(HttpRequest.HttpMethod.GET, weathre_url, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    ToolsUtils.setString(context, "weather", responseInfo.result);
                    Log.i(TAG, "服务更新成功 ");
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.i(TAG, "服务更新失败 ");
                }
            });

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            int time = 1000 * 60 * 60;
            long triggerAtMillis = SystemClock.elapsedRealtime() + time;
            Intent intent1 = new Intent(context, UpdateService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent1, 0);
            alarmManager.cancel(pendingIntent);
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, pendingIntent);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
