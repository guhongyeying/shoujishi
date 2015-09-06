package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.R.id;
import com.example.mobilesafe.R.layout;
import com.example.mobilesafe.R.menu;
import com.example.mobilesafe.service.AddressService;
import com.example.mobilesafe.service.CallSafeService;
import com.example.mobilesafe.utils.ServiceStatusUtils;
import com.example.mobilesafe.view.SettingClickView;
import com.example.mobilesafe.view.SettingItemView;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SettingActivity extends ActionBarActivity {

	private SettingItemView sivUpdate;
	private SharedPreferences ms;
	private SettingItemView sivAddress;
	private SettingItemView sivBlack;
	private SettingClickView scvAddressStyle;
	private SharedPreferences msp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		msp = getSharedPreferences("config", MODE_PRIVATE);
		initUpdate();
		initAddress();
		initAddressStyle();
		initAddressLoacation() ;
		initBlackView();
	}

	private void initUpdate() {
		ms = getSharedPreferences("auto_update", MODE_PRIVATE);
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		// sivUpdate.setTitle("自动更新设置");
		boolean autoUpdate = ms.getBoolean("auto_update", true);

		if (autoUpdate) {
			sivUpdate.setChecked(true);
			// sivUpdate.setDesc("自动更新已经开启");
		} else {
			sivUpdate.setChecked(false);
			// sivUpdate.setDesc("自动更新已经关闭");
		}
		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (sivUpdate.isChecked()) {
					sivUpdate.setChecked(false);
					// sivUpdate.setDesc("自动更新已经关闭");
					msp.edit().putBoolean("auto_update", false).commit();
				} else {
					sivUpdate.setChecked(true);
					// sivUpdate.setDesc("自动更新已经开启");
					msp.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}

	private void initAddress() {

		boolean runningService = ServiceStatusUtils.isRunningService(this,
				"com.example.mobilesafe.service.AddressService");
		System.out.println("runningService:" + runningService);

		sivAddress = (SettingItemView) findViewById(R.id.siv_address);

		sivAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (sivAddress.isChecked()) {
					sivAddress.setChecked(false);
					stopService(new Intent(SettingActivity.this,
							AddressService.class));// 停止归属地服务
				} else {
					sivAddress.setChecked(true);

					startService(new Intent(SettingActivity.this,
							AddressService.class));// 开启归属地服务
				}
			}
		});
	}

	/**
	 * 来电提醒按钮风格
	 */
	final String[] items = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };

	public void initAddressStyle() {
		scvAddressStyle = (SettingClickView) findViewById(R.id.scv_address_style);
		scvAddressStyle.setDesc("归属地提示风格");

		int style = msp.getInt("address_style", 0);
		System.out.println("style" + style);
		scvAddressStyle.setTitle(items[style]);

		scvAddressStyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSingleChooseDailog();
			}
		});
	}

	protected void showSingleChooseDailog() {
		Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("归属地提示框风格");

		builder.setSingleChoiceItems(items, 0,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						System.out.println("address_style==:" + which);
						msp.edit().putInt("address_style", which).commit();
						dialog.dismiss();
						scvAddressStyle.setDesc(items[which]);
					}
				});

		builder.setNegativeButton("取消", null);
		builder.show();
	}

	public void initAddressLoacation() {

		SettingClickView srcAddressLocation = (SettingClickView) findViewById(R.id.scv_address_location);
		srcAddressLocation.setTitle("归属地提示框位置");
		srcAddressLocation.setDesc("设置归属地提示框显示的位置");
		srcAddressLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SettingActivity.this, DragViewActivity.class));
			}
		});
	}

	/**
	 * 黑名单设置
	 */

	public void initBlackView() {


		boolean runningService = ServiceStatusUtils.isRunningService(this,
				"com.example.mobilesafe.service.CallSafeService");
		System.out.println("CallSafeService:" + runningService);

		sivBlack = (SettingItemView) findViewById(id.siv_black);
		if(runningService){
			sivBlack.setChecked(true);
		}else {
			sivBlack.setChecked(false);
		}


		sivBlack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (sivBlack.isChecked()) {
					sivBlack.setChecked(false);
					stopService(new Intent(SettingActivity.this,
							CallSafeService.class));// 停止黑名单地服务
				} else {
					sivBlack.setChecked(true);

					startService(new Intent(SettingActivity.this,
							CallSafeService.class));// 开启黑名单地服务
				}
			}
		});
	}

}
