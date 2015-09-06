package com.example.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;


import com.example.mobilesafe.bean.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangren on 15/8/29.
 */
public class AppInfoProvider {


    public static List<AppInfo> getAppInfos(Context context){

        ArrayList<AppInfo> infos = new ArrayList<AppInfo>();


        PackageManager pM = context.getPackageManager();

        //获得报名集合
        List<PackageInfo> installedPackages = pM.getInstalledPackages(0);

        for (PackageInfo installedPackage : installedPackages) {

            AppInfo appInfo = new AppInfo();


            Drawable icon = installedPackage.applicationInfo.loadIcon(pM);

            String apkName = (String) installedPackage.applicationInfo.loadLabel(pM);

            String packageName = installedPackage.packageName;

            //获取apk资源路径

            String sourceDir = installedPackage.applicationInfo.sourceDir;

            File file = new File(sourceDir);

            long sizeApp = file.length();

            appInfo.setIcon(icon);
            appInfo.setApkName(apkName);
            appInfo.setPackageName(packageName);
            appInfo.setSizeApp(sizeApp);

            //获取y是否是系统程序

            int flags = installedPackage.applicationInfo.flags;

            if((flags & ApplicationInfo.FLAG_SYSTEM) != 0){
                //系统app
                appInfo.setUserApp(false);
            }else {
                appInfo.setUserApp(true);
            }
            //内存关系
            if((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                   //sd
                appInfo.setIsRom(false);
            }else {
                appInfo.setIsRom(true);
            }
            infos.add(appInfo);

        }

        return infos;
    }
}
