package com.example.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by wangren on 15/8/29.
 */
public class AppInfo {

    private Drawable icon;
    private Long sizeApp;
    private  String apkName;
    private String packageName;
    private boolean isRom;
    private  boolean userApp;

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "icon=" + icon +
                ", sizeApp=" + sizeApp +
                ", packageName='" + packageName + '\'' +
                ", isRom=" + isRom +
                '}';
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Long getSizeApp() {
        return sizeApp;
    }

    public void setSizeApp(Long sizeApp) {
        this.sizeApp = sizeApp;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isRom() {
        return isRom;
    }

    public void setIsRom(boolean isRom) {
        this.isRom = isRom;
    }
}
