package com.example.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.database.ContentObserver;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.activity.DataDeal;
import com.example.mobilesafe.db.dao.BlachNumberDao;

import com.android.internal.telephony.ITelephony;
import java.lang.reflect.Method;

public class CallSafeService extends Service {


    BlachNumberDao blachNumberDao;
    private TelephonyManager tm;


    public CallSafeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();



        //获取到电话的系统的服务


        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        MyPhoneStateListener myPhoneStateListener = new MyPhoneStateListener();
        tm.listen(myPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);



//new  PhoneStateListen
        //tm.listen();


        System.out.print("初始化短信的广播");
        blachNumberDao = new BlachNumberDao(this);
        //初始化短信的广播
        InnerReceiver innerReceiver = new InnerReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

        intentFilter.setPriority(Integer.MAX_VALUE);
         registerReceiver(innerReceiver, intentFilter);




/*
        InnerReceiver innerReceiver = new InnerReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(innerReceiver, intentFilter);
        */
    }

   public class MyPhoneStateListener extends PhoneStateListener{


       @Override
       public void onCallStateChanged(int state, String incomingNumber) {
           super.onCallStateChanged(state, incomingNumber);


           switch (state) {
               //电话响起
               case TelephonyManager.CALL_STATE_RINGING:

           System.out.println("电话响了");
           String mode = blachNumberDao.findMode(incomingNumber);
 if(!TextUtils.isEmpty(mode))
           if (mode.equals("3") || mode.equals("2")) {


               Uri uri = Uri.parse("content://call_log/calls");

               getContentResolver().registerContentObserver(uri, true, new MyContentObserver(new Handler(), incomingNumber));

               //挂断电话
               endCall();
               System.out.println("阻断电话了 ");

           }
                   break;
       }


       }
   }


    private class MyContentObserver extends ContentObserver {
        String incomingNumber;
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         * @param incomingNumber
         */
        public MyContentObserver(Handler handler, String incomingNumber) {
            super(handler);
            this.incomingNumber = incomingNumber;
        }

        //当数据改变的时候调用的方法
        @Override
        public void onChange(boolean selfChange) {

            getContentResolver().unregisterContentObserver(this);

            deleteCallLog(incomingNumber);

            super.onChange(selfChange);
        }
    }

    /**
     * 挂断电话
     */
    private void endCall() {

        try {
            //通过类加载器加载ServiceManager
            Class<?> clazz = getClassLoader().loadClass("android.os.ServiceManager");
            //通过反射得到当前的方法
            Method method = clazz.getDeclaredMethod("getService", String.class);

            IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);

            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);

            iTelephony.endCall();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //删掉电话号码
    private void deleteCallLog(String incomingNumber) {

        Uri uri = Uri.parse("content://call_log/calls");

        getContentResolver().delete(uri,"number=?",new String[]{incomingNumber});

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private class InnerReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("短信来了!!!!!!!!!!");

            Object[] objects = (Object[]) intent.getExtras().get("pdus");

              //kaishi

            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                // 通过pdus 获得短信内容
                Object[] pdus = (Object[]) bundle.get("pdus");

                for (Object pdu : pdus) {
                    // 获得短信内容
                    SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);

                    // 短信内容
                    String number = message.getOriginatingAddress();

                    // 短信号码
                    String messageBody = message.getDisplayMessageBody();

                    String mode = blachNumberDao.findMode(number);
                    System.out.println(number + messageBody+mode);
             if(!TextUtils.isEmpty(mode))
                    if(mode.equals("1")){
                        abortBroadcast();
                    }else if (mode.equals("3")){
                        abortBroadcast();
                    }
                }
            }


            //end
        }
    }
}
