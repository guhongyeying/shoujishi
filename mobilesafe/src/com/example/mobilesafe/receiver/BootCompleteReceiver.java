package com.example.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		Toast.makeText(context, "开机启动", 0).show();
		System.out.println("phone:" + "dsds");
	        SharedPreferences msp = context.getSharedPreferences("config", context.MODE_PRIVATE);
	        
	       boolean protect = msp.getBoolean("protect", false);
	        if(protect){
	        
	        	String sim = msp.getString("sim", null);
	        	if(!TextUtils.isEmpty(sim)){
	        		TelephonyManager  tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
	    	        String currentSim = tm.getSimSerialNumber() ;
	    	    	System.out.println(sim+"==:"+currentSim);
	    	        if(sim.equals(currentSim)){
	    	            System.out.println("系统安全");
	    	        }else{
	    	        	System.out.println("sim卡已经发生变化");
	    	        	String phone = msp.getString("safephone", "");
	    	        	
	    	        	//发送短信给安全者
	    	        	System.out.println("phone=+++++===:" + phone);
	    	        	SmsManager smsManager = SmsManager.getDefault();
						smsManager.sendTextMessage(phone,null,"sim card changed!", null, null);
					
	    	        	
	    	        }
	        	}
	        }

	}

}
