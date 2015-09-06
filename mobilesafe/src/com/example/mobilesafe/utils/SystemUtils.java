package com.example.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangren on 15/9/1.
 */
public class SystemUtils {


    private String readLine;



    public  static  int getProcessCount(Context context){


        ActivityManager  activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

      return runningAppProcesses.size();
    }



    public  static  int getAvailMem(Context context){

        ActivityManager  activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();

        activityManager.getMemoryInfo(memoryInfo);

        int availMem = (int) memoryInfo.availMem;


        return availMem;
    }
    public static int getTotalRam(Context context) {
        int total = 0;


        try {

            File file = new File("/proc/meminfo");
            FileInputStream os = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(os));

            String readLine = bufferedReader.readLine();

            StringBuffer stringBuffer = new StringBuffer();


            for (char c : readLine.toCharArray()) {

                if(c>'0' && c<='9')

                    stringBuffer.append(c);
            }

            return  Integer.valueOf(stringBuffer.toString())*1024;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return total;
    }


    public  static  boolean isRunningService(Context context ,String name){


        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> infos = am.getRunningServices(200);

        for (ActivityManager.RunningServiceInfo info:infos){
            String className = info.service.getClassName();
            if(name.equals(className))
                return  true;

        }


        return false;

    }


}
