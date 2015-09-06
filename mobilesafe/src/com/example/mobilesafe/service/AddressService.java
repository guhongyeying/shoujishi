package com.example.mobilesafe.service;

import com.example.mobilesafe.R;
import com.example.mobilesafe.db.dao.AddressDao;

import com.example.mobilesafe.receiver.OutCallReceiver;



import android.R.color;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.location.LocationManager;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class AddressService extends Service {

	private TelephonyManager tm;
	private MyListen myListen;
	private OutCallReceiver outCallReceiver;
	private WindowManager mWM;
	private View view;
	private SharedPreferences msp;
	private TextView tvNumber;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
       
		msp = getSharedPreferences("config", MODE_PRIVATE);
		System.out.println("电话响起来了。。。。");
		// TODO Auto-generated method stub
		super.onCreate();
	//	tm = (TelephonyManager) getSystemServiceCoLOCATION_SERVICE);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		myListen = new MyListen();
		tm.listen(myListen, PhoneStateListener.LISTEN_CALL_STATE);
 
		outCallReceiver = new OutCallReceiver();
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(outCallReceiver, intentFilter);
		
	}
	
	 @Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//停用到来电服务
		tm.listen(myListen, PhoneStateListener.LISTEN_NONE);//停止监听
		unregisterReceiver(outCallReceiver);//注销广播
	}
	 /**
	  * 
	 * @ClassName: MyListen 
	 * @Description: 自定义的toast类
	 * @author A18ccms a18ccms_gmail_com 
	 * @date 2015-8-25 下午11:03:07 
	 *
	  */
  private void 	showToast(String text){
	  
	  mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
	  
	  
	 WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
	  final WindowManager.LayoutParams params = mParams;
      params.height = WindowManager.LayoutParams.WRAP_CONTENT;
      params.width = WindowManager.LayoutParams.WRAP_CONTENT;
      params.format = PixelFormat.TRANSLUCENT;

      params.type = WindowManager.LayoutParams.TYPE_TOAST;
      params.setTitle("Toast");
      params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
              | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
              | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
     params.gravity = Gravity.LEFT + Gravity.TOP;
     int lastx = msp.getInt("lastx", 0);
     int lasty = msp.getInt("lasty", 0);
      params.x = lastx;
      params.y = lasty;
      int[] bgs = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		view =  View.inflate(this, R.layout.toast_address, null);
		int style = msp.getInt("address_style", 0);
		System.out.println("style=" + style);
		view.setBackgroundResource(bgs[style]);

		 tvNumber = (TextView) view.findViewById(R.id.tv_number);
         tvNumber.setText(text);
   
      mWM.addView(view, mParams);
  }
	class MyListen extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			

			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// 电话响起
			     
                  String address = AddressDao.getAddress(incomingNumber);
                  Toast.makeText(AddressService.this,address, Toast.LENGTH_LONG).show();
                  showToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE:// 电话闲置状态
           if(mWM != null && view != null)
           {   mWM.removeView(view);
              view = null;
           }
              System.out.println("mWM； " + mWM);
				break;

			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);

		}
	}
}
