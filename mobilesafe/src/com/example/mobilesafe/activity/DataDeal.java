package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.R.id;
import com.example.mobilesafe.R.layout;
import com.example.mobilesafe.R.menu;
import com.example.mobilesafe.receiver.AdminReceiver;

import android.support.v7.app.ActionBarActivity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DataDeal extends ActionBarActivity {
	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_deal);

		mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);// 获取设备策略服务
		mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);// 设备管理组件
		activeAdmin();
		Bundle extras = getIntent().getExtras();
		String  order = (String) extras.get("order");
	  if(order.equals("#*2*#"))
		 clearData();
	  else 
		   lockScreen();
		// mDPM.lockNow();// 立即锁屏
	 finish();
	
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
	
	/**
	 *  Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
                        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                                mActivity.getString(R.string.add_admin_extra_app_text));
                        startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
	 * @param View
	 */

	// 一键锁屏
	public void lockScreen() {
		if (mDPM.isAdminActive(mDeviceAdminSample)) {// 判断设备管理器是否已经激活
			mDPM.lockNow();// 立即锁屏
			mDPM.resetPassword("123456", 0);
		} else {
			Toast.makeText(this, "必须先激活设备管理器!", Toast.LENGTH_SHORT).show();
		}
	}

	public void clearData() {
		Toast.makeText(this, "必须先激活设备管理器!", Toast.LENGTH_SHORT).show();
		if (mDPM.isAdminActive(mDeviceAdminSample)) {// 判断设备管理器是否已经激活
			mDPM.wipeData(0);// 清除数据,恢复出厂设置
		} else {
			Toast.makeText(this, "必须先激活设备管理器!", Toast.LENGTH_SHORT).show();
		}
	}

	public void unInstall() {
		mDPM.removeActiveAdmin(mDeviceAdminSample);// 取消激活

		// 卸载程序
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setData(Uri.parse("package:" + getPackageName()));
		startActivity(intent);
	}
}
