package com.example.mobilesafe.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.fragment.LockFragment;
import com.example.mobilesafe.fragment.UnLockFragment;

/**
 * Created by wangren on 15/9/3.
 */
public class AppLockActivity extends FragmentActivity implements View.OnClickListener {


    private FrameLayout fl_content;
    private TextView tv_unlock;
    private TextView tv_lock;
    private FragmentManager fragmentManager;
    private UnLockFragment unLockFragment;
    private LockFragment lockFragment;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        System.out.println("-----------");
        System.out.println("nihaoma --- fs");
        setContentView(R.layout.activity_app_lock);
        initUI();
    }

    private void initUI() {
//

//
//        fl_content = (FrameLayout) findViewById(R.id.fl_content);
//
//        tv_unlock = (TextView) findViewById(R.id.tv_unlock);
//
//        tv_lock = (TextView) findViewById(R.id.tv_lock);
//
//        tv_unlock.setOnClickListener(this);
//        tv_lock.setOnClickListener(this);
//        //»ñÈ¡µ½fragmentµÄ¹ÜÀíÕß
//
//        fragmentManager = getSupportFragmentManager();
//        //¿ªÆôÊÂÎñ
//        FragmentTransaction mTransaction = fragmentManager.beginTransaction();
//
//        unLockFragment = new UnLockFragment();
//
//        lockFragment = new LockFragment();
//        /**
//         * Ìæ»»½çÃæ
//         * 1 ÐèÒªÌæ»»µÄ½çÃæµÄid
//         * 2¾ßÌåÖ¸Ä³Ò»¸öfragmentµÄ¶ÔÏó
//         */
//        mTransaction.replace(R.id.fl_content, unLockFragment).commit();
    }

    @Override
    public void onClick(View v) {

    }
}
