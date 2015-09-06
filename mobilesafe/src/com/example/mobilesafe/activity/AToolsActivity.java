package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.R.id;
import com.example.mobilesafe.R.layout;
import com.example.mobilesafe.R.menu;
import com.example.mobilesafe.utils.SmsUtils;
import com.example.mobilesafe.utils.SmsUtils.BackupSms;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.ProgressDialog;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AToolsActivity extends ActionBarActivity {

    private ProgressDialog pd;

    @ViewInject(id.pb_progressr)
    ProgressBar pbProgress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
        ViewUtils.inject(this);
    }

    public void numberAddressQuery(View view) {
        startActivity(new Intent(this, AddressActivity.class));
    }

    public void backupSms(View view) {

        pd = new ProgressDialog(AToolsActivity.this);
        pd.setTitle("进度条");
        pd.setMessage("稍安勿躁，尽请等待");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.show();


        new Thread() {
            @Override
            public void run() {
                super.run();
                boolean flag = SmsUtils.backUp(AToolsActivity.this, new BackupSms() {
                    @Override
                    public void befor(int count) {
                        pd.setMax(count);
                        pbProgress.setMax(count);
                    }

                    @Override
                    public void onBackuoSms(int progress) {
                        pd.setProgress(progress);
                        pbProgress.setProgress(progress);
                    }


                });
                pd.dismiss();
                if (flag == true) {
                    Looper.prepare();
                    Toast.makeText(AToolsActivity.this, "短信备份成功", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    Looper.prepare();
                    Toast.makeText(AToolsActivity.this, "短信备份s失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }


            }
        }.start();


    }

    public void appLock(View view){


        startActivity(new Intent(AToolsActivity.this,LockAppActivity.class));
    }

}
