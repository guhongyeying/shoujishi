package com.example.mobilesafe.engine;

import com.example.mobilesafe.receiver.AdminReceiver;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class DealData extends Activity {
    
	
	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);// 获取设备策略服务
		mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);// 设备管理组件
		
		 activeAdmin();
		 
		

	}
	
	
	// 激活设备管理器, 也可以在设置->安全->设备管理器中手动激活
	public void activeAdmin() {
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
				mDeviceAdminSample);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				"哈哈哈, 我们有了超级设备管理器, 好NB!");
		startActivity(intent);
	}
	
	
	public void lockScreen() {
		if (mDPM.isAdminActive(mDeviceAdminSample)) {// 判断设备管理器是否已经激活
			mDPM.lockNow();// 立即锁屏
			mDPM.resetPassword("123456", 0);
		} else {
			Toast.makeText(this, "必须先激活设备管理器!", Toast.LENGTH_SHORT).show();
		}
	}

}
