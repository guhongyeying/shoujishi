package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.db.dao.AntivirusDao;
import com.example.mobilesafe.utils.Md5Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import java.util.logging.LogRecord;

public class AntivirusActivity extends Activity {

    private static final int BEGING = 0;
    private static final int SCANING = 1;
    private static final int FINISh = 2;
    @ViewInject(R.id.iv_scanning)
    ImageView ivScanning;

    @ViewInject(R.id.progressBar1)
    ProgressBar progressBar1;

    @ViewInject(R.id.ll_content)
    LinearLayout llContent;


    @ViewInject(R.id.scrollView)
    ScrollView scrollView;

    @ViewInject(R.id.tv_init_virus)
    TextView tvInitVirus;




    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case BEGING:
                    tvInitVirus.setText("初始化8核");
                    break;

                case SCANING:

                    TextView textView = new TextView(AntivirusActivity.this);
                    ScanInfo scanInfo = (ScanInfo) msg.obj;

                    if (scanInfo.desc == true) {
                        textView.setTextColor(Color.RED);

                        textView.setText("有毒程序" + scanInfo.appName);
                    } else {
                        textView.setTextColor(Color.BLACK);

                        textView.setText("正常程序" + scanInfo.appName);
                    }

                   llContent.addView(textView, 0);




                    //自动往下动
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(scrollView.FOCUS_DOWN);
                        }
                    });





//                    //自动滚动
//                    scrollView.post(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            //一直往下面进行滚动
//                            scrollView.fullScroll(scrollView.FOCUS_DOWN);
//                            scrollView.
//
//                        }
//                    });
                    break;


                case FINISh:

                    ivScanning.clearAnimation();
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antivirus);
        initUI();
        initData();
    }

    private void initData() {

        new Thread() {

            @Override
            public void run() {
                super.run();

                Message message = Message.obtain();

                message.what = BEGING;

                PackageManager packageManager = getPackageManager();

                List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);

                int count = installedPackages.size();

                progressBar1.setMax(count);
                int progress = 0;
                for (PackageInfo installedPackage : installedPackages) {

                    ScanInfo scanInfo = new ScanInfo();

                    String packageName = installedPackage.applicationInfo.packageName;
                    scanInfo.packageName = packageName;

                    String appName = installedPackage.applicationInfo.loadLabel(packageManager).toString();
                    scanInfo.appName = appName;

                    String sourceDir = installedPackage.applicationInfo.sourceDir;
                    String md5 = Md5Utils.getFileMd5(sourceDir);
                    String desc = AntivirusDao.checkFileVirus(md5);
                    if (desc != null)
                        scanInfo.desc = true;
                    else scanInfo.desc = false;


                    progress++;


                    SystemClock.sleep(100);

                    progressBar1.setProgress(progress);

                    message = Message.obtain();

                    message.what = SCANING;

                    message.obj = scanInfo;

                    handler.sendMessage(message);

                }

                message = Message.obtain();

                message.what = FINISh;

                handler.sendMessage(message);
            }
        }.start();


    }

    static class ScanInfo {
        boolean desc;
        String appName;
        String packageName;
    }

    private void initUI() {
        ViewUtils.inject(this);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);


        rotateAnimation.setDuration(5000);

        rotateAnimation.setRepeatCount(Animation.START_ON_FIRST_FRAME);

        ivScanning.setAnimation(rotateAnimation);


    }


}
