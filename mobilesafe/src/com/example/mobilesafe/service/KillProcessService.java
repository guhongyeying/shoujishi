package com.example.mobilesafe.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;


public class KillProcessService extends Service {

    private LockScreenReceiver lockScreenReceiver;

    public KillProcessService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();


        //创建锁屏的广播

        lockScreenReceiver = new LockScreenReceiver();


        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);

        registerReceiver(lockScreenReceiver,filter);


        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("做人不能靠别人");
            }
        };


        timer.schedule(task,1000,1000);
    }

    @Override
    public void onDestroy() {

        unregisterReceiver(lockScreenReceiver);
        lockScreenReceiver = null;

    }



    private  class  LockScreenReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();

            for (ActivityManager.RunningAppProcessInfo rap : runningAppProcesses) {

                am.killBackgroundProcesses(rap.processName);
            }

        }
    }
}
