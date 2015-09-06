package com.example.mobilesafe.receiver;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.example.mobilesafe.service.KillProcesWidgetService;

/**
 * Created by wangren on 15/9/2.
 */
public class MyAppWidget extends AppWidgetProvider {


    public MyAppWidget() {
        super();
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);


        Intent intent = new Intent(context,KillProcesWidgetService.class);
        context.startService(intent);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        Intent intent = new Intent(context,KillProcesWidgetService.class);
        context.startService(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}
