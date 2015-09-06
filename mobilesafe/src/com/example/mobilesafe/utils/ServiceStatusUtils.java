package com.example.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceStatusUtils {
    public static boolean isRunningService(Context context ,String serviceName){
    	
   ActivityManager systemService = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
  
                 List<RunningServiceInfo> runningServices = systemService.getRunningServices(100);//获得正在运行的100个service
  
                for (RunningServiceInfo runningServiceInfo : runningServices) {
					String className = runningServiceInfo.service.getClassName();
					if(serviceName.equals(className)){
						return true;
					}
				}
                 
                 return false;
    }
}
