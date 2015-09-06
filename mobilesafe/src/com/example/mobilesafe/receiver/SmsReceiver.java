package com.example.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.activity.DataDeal;
import com.example.mobilesafe.engine.DealData;
import com.example.mobilesafe.service.LocationService;

public class SmsReceiver extends BroadcastReceiver {

	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		StringBuilder sb = new StringBuilder();
		// 接受sms的数据
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

				//
				// if ("#*alarm*#".equals(messageBody)) {
				// else if ("#*location*#".equals(messageBody))
				// }
				//
				// if ("#*wipedata*#".equals(messageBody))
				// if ("#*lockscreen*#".equals(messageBody))
				// mediaPlayer = MediaPlayer.create(getApplicationContext(),
				// R.raw.heightgirl);
				// mediaPlayer.start();

				if ("#*alarm*#".equals(messageBody)) {
					MediaPlayer player = MediaPlayer
							.create(context, R.raw.ylzs);
					player.setVolume(1f, 1f);
					player.setLooping(true);
					player.start();
					// player.setDataSource(R.);
					abortBroadcast();
				}
				if ("#*location*#".equals(messageBody)) {
					context.startService(new Intent(context,
							LocationService.class));

					SharedPreferences msp = context.getSharedPreferences(
							"config", context.MODE_PRIVATE);
					String lc = msp.getString("location", "location is ...");
					System.out.println("location :" + lc);
					abortBroadcast();
				}

				/**
				 * public void activeAdmin(View view) { Intent intent = new
				 * Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
				 * intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
				 * mDeviceAdminSample);
				 * intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				 * "哈哈哈, 我们有了超级设备管理器, 好NB!"); startActivity(intent); }
				 */

				if ("#*lockscreen*#".equals(messageBody)) {
					System.out.println("#*lockscreen*#");
					  Intent intent1=new Intent(context,DataDeal.class);
					  intent1.putExtra("order", "#*1*#");
					  intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					  
					  context.startActivity(intent1);
				}
				 if ("#*wipedata*#".equals(messageBody)){
					  Intent intent2=new Intent(context,DataDeal.class);
					  intent2.putExtra("order", "#*2*#");
					  intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					  context.startActivity(intent2);
				 }

			}
		}

	}



}
