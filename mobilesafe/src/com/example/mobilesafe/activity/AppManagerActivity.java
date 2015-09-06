package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import android.os.Message;
import android.text.format.Formatter;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.AppInfo;
import com.example.mobilesafe.engine.AppInfoProvider;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.List;


public class AppManagerActivity extends Activity implements View.OnClickListener {

    public List<AppInfo> appInfos;
    ArrayList<AppInfo> UserAppInfos;
    ArrayList<AppInfo> SystemInfos;
    public PopupWindow popupWindow;
    private  AppInfo infoitme;
    @ViewInject(R.id.list_view)
    ListView listView;
    @ViewInject(R.id.tv_ram)
    TextView tvRam;
    @ViewInject(R.id.tv_sd)
    TextView tvSd;
    @ViewInject(R.id.tv_data_type)
    TextView tvDataType;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            AppManagerAdapter appManagerAdapter = new AppManagerAdapter();
            listView.setAdapter(appManagerAdapter);

        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        closePopupWindow();
    }

    public void initData() {

        long freeSpaceRam = Environment.getDataDirectory().getFreeSpace();
        long freeSpaceSd = Environment.getExternalStorageDirectory().getFreeSpace();
        tvRam.setText(Formatter.formatFileSize(AppManagerActivity.this, freeSpaceRam));
        tvSd.setText(Formatter.formatFileSize(AppManagerActivity.this, freeSpaceSd));

        new Thread() {
            @Override
            public void run() {
                super.run();

                appInfos = AppInfoProvider.getAppInfos(AppManagerActivity.this);

                UserAppInfos = new ArrayList<AppInfo>();

                SystemInfos = new ArrayList<AppInfo>();


                for (AppInfo appInfo : appInfos) {

                    if (appInfo.isUserApp()) {


                        UserAppInfos.add(appInfo);
                    } else {

                        SystemInfos.add(appInfo);
                    }
                }

                handler.sendEmptyMessage(0);
            }
        }.start();


    }

    public void initUI() {
        setContentView(R.layout.activity_app_manager);
        // ViewUtils.inject(this); //注入view和事件
        ViewUtils.inject(this);
//设置Listview 滚动事件

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstView, int i1, int i2) {

                closePopupWindow();
                if (UserAppInfos != null && SystemInfos != null) {
                    if (firstView > UserAppInfos.size()) {
                        tvDataType.setText("系统程序（" + SystemInfos.size() + ")");
                    } else {
                        tvDataType.setText("系统程序（" + UserAppInfos.size() + ")");
                    }
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Object obj = listView.getItemAtPosition(i);
                if (obj != null && obj instanceof AppInfo) {

               infoitme = (AppInfo) obj;

                    closePopupWindow();
                    View ContentView = View.inflate(AppManagerActivity.this, R.layout.popup_item, null);

                    popupWindow = new PopupWindow(ContentView, -2, -2);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    int[] location = new int[2];
                    //让在item显示位置
                    view.getLocationInWindow(location);
                    popupWindow.showAtLocation(adapterView, Gravity.LEFT + Gravity.TOP, 90, location[1]);

                    //设置动画，必须设置透明背景

                    AnimationSet set = new AnimationSet(false);
                    ScaleAnimation sa = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

          sa.setDuration(2500);


//                    AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
//                    aa.setDuration(500);



                    AlphaAnimation aa = new AlphaAnimation(0.5f,1.0f);

                    aa.setDuration(500);

                    set.addAnimation(aa);
                    set.addAnimation(sa);
                  ContentView.startAnimation(sa);

                    LinearLayout llUninstall = (LinearLayout) ContentView.findViewById(R.id.ll_uninstall);
                    LinearLayout    llStart = (LinearLayout) ContentView.findViewById(R.id.ll_start);
                    LinearLayout llShare = (LinearLayout) ContentView.findViewById(R.id.ll_share);

                    llShare.setOnClickListener(AppManagerActivity.this);
                    llUninstall.setOnClickListener(AppManagerActivity.this);
                    llStart.setOnClickListener(AppManagerActivity.this);


                }


            }
        });

    }

    public void closePopupWindow() {


        if (popupWindow != null && popupWindow.isShowing())

        {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
    //分享，启动，卸载的点击事件

    @Override
    public void onClick(View view) {
//infoitme
        switch (view.getId()){

            case R.id.ll_uninstall:

                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("package:" + infoitme.getPackageName()));
                startActivity(intent);
                closePopupWindow();;
                break;
            case R.id.ll_start:
//                Intent intent2 = new Intent();
//                PackageManager pm = getPackageManager();
//                try {
//                    PackageInfo packInfo = pm.getPackageInfo(infoitme.getPackageName(), 0);
//                    ActivityInfo[] acivityInfos = packInfo.activities;
//                    if(acivityInfos != null&&acivityInfos.length>0){
//                        ActivityInfo activityInfo = acivityInfos[0];
//                        intent2.setClassName(infoitme.getPackageName(), activityInfo.name);
//                        startActivity(intent2);
//                    }else{
//                        Toast.makeText(this, "这个程序没有界面", 0).show();
//                    }
//                } catch (PackageManager.NameNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                    Toast.makeText(this, "这个应用无法启动", 0).show();
//                }

                Intent start_localIntent = this.getPackageManager().getLaunchIntentForPackage(infoitme.getPackageName());
                this.startActivity(start_localIntent);
             closePopupWindow();

                break;
            case R.id.ll_share:
                Intent intent1 = new Intent();
//	<action android:name="android.intent.action.SEND" />
//<category android:name="android.intent.category.DEFAULT" />
//  <data android:mimeType="text/plain" />
                intent1.setAction("android.intent.action.SEND");
                intent1.addCategory("android.intent.category.DEFAULT");
                intent1.setType("text/plain");
                intent1.putExtra(Intent.EXTRA_TEXT, "推荐一款软件名叫：" + infoitme.getApkName() + ",下载地址：ccc"+infoitme.getPackageName());
                startActivity(intent1);
                closePopupWindow();
                break;
         //   查看信息
//            case R.id.:
//
//                Intent share_localIntent = new Intent("android.intent.action.SEND");
//                share_localIntent.setType("text/plain");
//                share_localIntent.putExtra("android.intent.extra.SUBJECT", "f分享");
//                share_localIntent.putExtra("android.intent.extra.TEXT",
//                        "Hi！推荐您使用软件：" + clickAppInfo.getApkName()+"下载地址:"+"https://play.google.com/store/apps/details?id="+clickAppInfo.getApkPackageName());
//                this.startActivity(Intent.createChooser(share_localIntent, "分享"));
//                  popupWindowDismiss();
//
//                break;


        }

    }


    public class AppManagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return appInfos.size() + 2;
        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return null;
            } else if (position == UserAppInfos.size() + 1) {
                return null;
            }
            AppInfo appInfo;

            if (position < UserAppInfos.size() + 1) {
                //把多出来的特殊的条目减掉
                appInfo = UserAppInfos.get(position - 1);

            } else {

                int location = UserAppInfos.size() + 2;

                appInfo = SystemInfos.get(position - location);
            }

            return appInfo;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {


            AppInfo appInfo = null;
            ViewHolder holder = null;

            if (i == 0) {
                TextView textView = new TextView(AppManagerActivity.this);
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.GRAY);
                textView.setText("应用程序（" + UserAppInfos.size() + ")");
                return textView;

            } else if (i == (UserAppInfos.size() + 1)) {
                TextView textView = new TextView(AppManagerActivity.this);
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.GRAY);
                textView.setText("系统程序（" + SystemInfos.size() + ")");
                return textView;
            }


            if (i < UserAppInfos.size() + 1) {
                appInfo = UserAppInfos.get(i - 1);
            } else {
                appInfo = SystemInfos.get(i - 2 - UserAppInfos.size());
            }


            if (convertView != null && convertView instanceof LinearLayout) {
                holder = (ViewHolder) convertView.getTag();
            } else {

                holder = new ViewHolder();
                convertView = View.inflate(AppManagerActivity.this, R.layout.list_app_item, null);

                holder.ivApp = (ImageView) convertView.findViewById(R.id.iv_app);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvLocation = (TextView) convertView.findViewById(R.id.tv_location);
                holder.tvRam = (TextView) convertView.findViewById(R.id.tv_ram);

                convertView.setTag(holder);
            }

            // Drawable icon = appInfo.getIcon();
            //   System.out.print("appInfo.getIcon()=" + icon.toString() );


            holder.tvRam.setText(Formatter.formatFileSize(AppManagerActivity.this, appInfo.getSizeApp()));

            holder.tvName.setText(appInfo.getApkName());

            holder.ivApp.setImageDrawable(appInfo.getIcon());

            if (appInfo.isRom()) {
                holder.tvLocation.setText("手机内存");
            } else {
                holder.tvLocation.setText("外部存储");
            }


            return convertView;
        }
    }

    class ViewHolder {
        ImageView ivApp;
        TextView tvName;
        TextView tvLocation;
        TextView tvRam;
    }

}
