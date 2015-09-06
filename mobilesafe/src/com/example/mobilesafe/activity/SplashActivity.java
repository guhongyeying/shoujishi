package com.example.mobilesafe.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class SplashActivity extends ActionBarActivity {

    protected static final int CODE_UPDATE_DIALOG = 0;
    protected static final int CODE_NET_DIALOG = 1;
    protected static final int CODE_IO_ERROR = 2;
    protected static final int CODE_JSON_ERROR = 3;
    protected static final int CODE_NET_HOME = 4;
    private TextView tvVersion;
    private String mDesc;
    private int mVersionCode;
    private String mVersionName;
    private String mDownloadUrl;
    private TextView tvProgress;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == CODE_UPDATE_DIALOG) {
                showUpdateDailog();
            } else if (msg.what == CODE_NET_DIALOG) {
                Toast.makeText(SplashActivity.this, "URL报错", 0).show();
                enterHome();
            } else if (msg.what == CODE_IO_ERROR) {
                Toast.makeText(SplashActivity.this, "网络异常", 0).show();
                enterHome();
            } else if (msg.what == CODE_JSON_ERROR) {
                Toast.makeText(SplashActivity.this, "JSON报错", 0).show();
                ;
                enterHome();
            } else if (msg.what == CODE_NET_HOME) {
                enterHome();
            }
        }

        ;
    };
    private SharedPreferences mPref;
    private RelativeLayout rlRoot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        String v = getVersionName();
        tvVersion.setText("版本号：" + v);
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_roots);

        mPref = getSharedPreferences("auto_update", MODE_PRIVATE);
        //拷贝数据adddres.db库
        copyDB("address.db");
       copyDB("antivirus.db");
        // 判断是否需要自动更新
        boolean autoUpdate = mPref.getBoolean("auto_update", true);
        if (autoUpdate) {
            checkUpdate();
        } else {
            handler.sendEmptyMessageDelayed(CODE_NET_HOME, 2000);
        }

        AlphaAnimation anim = new AlphaAnimation(0.3f, 1f);
        anim.setDuration(2000);
        rlRoot.startAnimation(anim);

        createShortcut();

    }


    private void checkUpdate() {

        // /搞一个子线程
        new Thread(new Runnable() {

            @Override
            public void run() {

                long startTime = System.currentTimeMillis();
                HttpURLConnection openConnection = null;
                Message msg = Message.obtain();

                try {
                    URL url = new URL("http://2.shoujishi.sinaapp.com/update.json");
                    openConnection = (HttpURLConnection) url.openConnection();
                    openConnection.setRequestMethod("GET");
                    openConnection.setReadTimeout(5000);
                    openConnection.setConnectTimeout(5000);
                    openConnection.connect();

                    int responseCode = openConnection.getResponseCode();

                    if (responseCode == 200) {
                        // 获得数据流
                        InputStream is = openConnection.getInputStream();
                        // 解析流并且返回数据
                        String result = StreamUtils.readStream(is);
                        // System.out.println("result== :" + result);
                        JSONObject json = new JSONObject(result);
                        mVersionName = json.getString("versionName");
                        mVersionCode = json.getInt("versionCode");
                        mVersionName = json.getString("description");
                        mDownloadUrl = json.getString("downloadUrl");

                        if (mVersionCode > getVersionCode()) {
                            msg.what = CODE_UPDATE_DIALOG;
                        } else {
                            msg.what = CODE_NET_HOME;
                        }

                    }

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    // 连接url
                    msg.what = CODE_NET_DIALOG;
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    msg.what = CODE_IO_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    msg.what = CODE_JSON_ERROR;
                    e.printStackTrace();
                } finally {

                    long endTime = System.currentTimeMillis();
                    long userTime = endTime - startTime;
                    if (userTime < 2000) {
                        try {
                            Thread.sleep(2000 - userTime);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if (openConnection != null)
                        openConnection.disconnect();
                    handler.sendMessage(msg);
                }
            }
        }).start();

    }

    protected void enterHome() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    private int getVersionCode() {
        // TODO Auto-generated method stub

        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            // System.out.println("getPackageName()=="+getPackageName());
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            System.out.println(versionCode + versionName);
            return versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    private String getVersionName() {
        // TODO Auto-generated method stub

        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            // System.out.println("getPackageName()=="+getPackageName());
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            // System.out.println(versionCode + versionName);
            return versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 升级的对话框
     */

    private void showUpdateDailog() {

        Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("最新版本：" + mVersionName);
        builder.setMessage(mDesc);
        builder.setPositiveButton("立即更新", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                System.out.println("立即更新");

                download();

            }
        });

        builder.setNegativeButton("以后再说", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                enterHome();
            }
        });

        builder.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                enterHome();
            }
        });
        builder.show();
    }

    /**
     * 下载apk
     */

    private void download() {
        System.out.println("下载进度+current+total");
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            tvProgress.setVisibility(View.VISIBLE);
            String target = Environment.getExternalStorageDirectory()
                    + "/mobilesafe.apk";
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.download("http://2.shoujishi.sinaapp.com/mobilesafe-debug.apk", target,
                    new RequestCallBack<File>() {

                        @Override
                        public void onLoading(long total, long current,
                                              boolean isUploading) {
                            // TODO Auto-generated method stub
                            super.onLoading(total, current, isUploading);

                            System.out.println("下载进度" + current + total);
                            tvProgress.setText("下载进度" + current * 100 / total
                                    + "%");
                        }

                        @Override
                        public void onSuccess(ResponseInfo<File> arg0) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);

                            intent.setDataAndType(Uri.fromFile(arg0.result),
                                    "application/vnd.android.package-archive");

                            startActivityForResult(intent, 0);// 如果用户取消安装的话,

                            // 会返回结果,回调方法onActivityResult
                        }

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            // TODO Auto-generated method stub

                        }
                    });
        } else {
            Toast.makeText(this, "没找到SDcard", 0).show();
            enterHome();
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        enterHome();
        super.onActivityResult(arg0, arg1, arg2);
    }

    private void copyDB(String dbName) {
        // TODO Auto-generated method stub

        InputStream in = null;
        FileOutputStream out = null;
        File destFile = new File(getFilesDir(), dbName);

        if (destFile.exists()) {
            System.out.println("数据库" + dbName + "数据库已经存在");

            return;
        }

        try {
            in = getAssets().open(dbName);
            out = new FileOutputStream(destFile);
            int len = 0;
            byte[] buffer = new byte[1024];

            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (in != null && out != null) {
                    in.close();
                    out.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }


    private void createShortcut() {


        Intent intent = new Intent();

        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");


        /**
         * 做什么事情
         * 什么名字
         * 什么样子
         */


        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "哼哈");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

        //打电话

        //不重复操作
        intent.putExtra("duplicate", false);


        Intent Shortcut_intent = new Intent();

        Shortcut_intent.setAction("aa.bb.cc");
        Shortcut_intent.addCategory("android.intent.category.DEFAULT");

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, Shortcut_intent);

        sendBroadcast(intent);

    }

}
