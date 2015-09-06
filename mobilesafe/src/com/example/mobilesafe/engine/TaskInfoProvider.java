package com.example.mobilesafe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangren on 15/9/1.
 */
public class TaskInfoProvider {

    public static List<TaskInfo> getTaskInfo(Context context) {
        ArrayList<TaskInfo> taskInfos = new ArrayList<TaskInfo>();


        PackageManager pm = context.getPackageManager();

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo rap : runningAppProcesses) {
            TaskInfo taskInfo = new TaskInfo();
            //获得内存大小的数组
            Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{rap.pid});

            long memory = processMemoryInfo[0].getTotalPrivateDirty();
            memory *= 1024;

            //内存
            taskInfo.setMemorySize(memory);
            String processName = rap.processName;
            //包名
            taskInfo.setPackageName(processName);

            try {
                PackageInfo packageInfo = pm.getPackageInfo(processName, 0);

                Drawable drawable = packageInfo.applicationInfo.loadIcon(pm);
                //图标
                taskInfo.setIcon(drawable);

//                软件名

                String name = (String) packageInfo.applicationInfo.loadLabel(pm);

                  taskInfo.setAppName(name);

                int flags = packageInfo.applicationInfo.flags;


                if((flags & ApplicationInfo.FLAG_SYSTEM) == 0 ){
                    //用户进程
                    taskInfo.setUserApp(true);
                }else {
                    //用户进程
                    taskInfo.setUserApp(false);
                }



            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();

                taskInfo.setAppName(processName);
                taskInfo.setIcon(context.getResources().getDrawable(
                        R.drawable.ic_launcher));
            }

            taskInfos.add(taskInfo);


        }


        return taskInfos;

    }
}
