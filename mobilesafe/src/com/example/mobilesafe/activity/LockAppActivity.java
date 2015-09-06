package com.example.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.fragment.LockFragment;
import com.example.mobilesafe.fragment.UnLockFragment;

public class LockAppActivity extends FragmentActivity implements View.OnClickListener {
    private FrameLayout fl_content;
    private TextView tv_unlock;
    private TextView tv_lock;
    private FragmentManager fragmentManager;
    private UnLockFragment unLockFragment;
    private LockFragment lockFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);
        iniUI();
    }

    private void iniUI() {

        fl_content = (FrameLayout) findViewById(R.id.fl_content);

        tv_unlock = (TextView) findViewById(R.id.tv_unlock);

        tv_lock = (TextView) findViewById(R.id.tv_lock);

        tv_unlock.setOnClickListener(this);
        tv_lock.setOnClickListener(this);
        //»ñÈ¡µ½fragmentµÄ¹ÜÀíÕß

        fragmentManager = getSupportFragmentManager();
        //¿ªÆôÊÂÎñ
        FragmentTransaction mTransaction = fragmentManager.beginTransaction();

        unLockFragment = new UnLockFragment();

        lockFragment = new LockFragment();

        mTransaction.replace(R.id.fl_content, unLockFragment).commit();
    }


    @Override
    public void onClick(View v) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.tv_unlock:

                tv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
                tv_lock.setBackgroundResource(R.drawable.tab_right_default);

                ft.replace(R.id.fl_content, lockFragment);

                break;

            case R.id.tv_lock:

                tv_unlock.setBackgroundResource(R.drawable.tab_left_default);
                tv_lock.setBackgroundResource(R.drawable.tab_right_pressed);

                ft.replace(R.id.fl_content, unLockFragment);

                break;
        }
        ft.commit();
    }
}
