package com.example.mobilesafe.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.example.mobilesafe.R;
import com.example.mobilesafe.receiver.MyAppWidget;
import com.example.mobilesafe.utils.SystemUtils;

import java.util.Timer;
import java.util.TimerTask;

public class KillProcesWidgetService extends Service {

    private AppWidgetManager appWidgetManager;

    public KillProcesWidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();


        //AppWidgetManager
        appWidgetManager = AppWidgetManager.getInstance(this);

        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.process_widget);

               int count  = SystemUtils.getProcessCount(getApplicationContext());
                remoteViews.setTextViewText(R.id.process_count, "进程个数" + String.valueOf(count));

                int availMem = SystemUtils.getAvailMem(getApplicationContext());

                remoteViews.setTextViewText(R.id.process_memory, "运行内存" + Formatter.formatFileSize(getApplicationContext(), availMem));


                Intent intent = new Intent();

        //发送一个隐试意图

                intent.setAction("com.example.mobilesafe");

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);

                remoteViews.setOnClickPendingIntent(R.id.btn_clear,pendingIntent);


                //第二个参数表示小控件的布局文件
                ComponentName componentName = new ComponentName(getApplicationContext(), MyAppWidget.class);

                appWidgetManager.updateAppWidget(componentName,remoteViews);







            }
        };

        timer.schedule(timerTask,0,5000);

    }
}
