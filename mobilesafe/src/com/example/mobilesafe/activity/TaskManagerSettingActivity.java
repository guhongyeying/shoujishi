package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.KillProcessService;
import com.example.mobilesafe.utils.SystemUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class TaskManagerSettingActivity extends Activity {

    private SharedPreferences msp;
    private CheckBox cbKillStatus;

    //@ViewInject(R.id.cb_status)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewUtils.inject(this);
        setContentView(R.layout.activity_task_manager_setting);

        msp = getSharedPreferences("config", MODE_PRIVATE);

        initUI();





    }



    public  void initUI(){

        CheckBox cbStatus = (CheckBox) findViewById(R.id.cb_status);
       cbStatus.setChecked(msp.getBoolean("system_status", false));
        cbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // cbStatus.setChecked(false);
                    msp.edit().putBoolean("system_status", true).commit();
                } else {
                    //  cbStatus.setChecked(true);
                    msp.edit().putBoolean("system_status", false).commit();
                }
            }
        });


        final Intent intent = new Intent(TaskManagerSettingActivity.this, KillProcessService.class);

        cbKillStatus = (CheckBox) findViewById(R.id.cb_kill_status);

        cbKillStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    startService(intent);
                } else {

                    System.out.println("你是贱人一个");
                    stopService(intent);
                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        boolean flag = SystemUtils.isRunningService(this, "com.example.mobilesafe.service.KillProcessService");
        if (flag){
            cbKillStatus.setChecked(true);
        }else {
            cbKillStatus.setChecked(flag);
        }

    }
}
