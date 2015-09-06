package com.example.mobilesafe.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

/**
 * Created by wangren on 15/9/2.
 */
public class KillProcessRecevier  extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityManager  activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {


            activityManager.killBackgroundProcesses(runningAppProcess.processName);

        }

        Toast.makeText(context,"清理完毕",Toast.LENGTH_SHORT).show();

    }
}
