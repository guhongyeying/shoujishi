package com.example.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by wangren on 15/9/1.
 */
public class TaskInfo {

    private Drawable icon;

    private String packageName;

    private String appName;

    private long memorySize;



    /**
     * 系统进程
     */
    private boolean userApp;

    /**
     *
     */
    private boolean checked;


    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(long memorySize) {
        this.memorySize = memorySize;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


    @Override
    public String toString() {
        return "TaskInfo{" +
                "icon=" + icon +
                ", packageName='" + packageName + '\'' +
                ", appName='" + appName + '\'' +
                ", memorySize=" + memorySize +
                ", userApp=" + userApp +
                ", checked=" + checked +
                '}';
    }
}
