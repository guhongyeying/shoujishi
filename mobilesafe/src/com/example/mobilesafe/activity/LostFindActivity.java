package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.R.id;
import com.example.mobilesafe.R.layout;
import com.example.mobilesafe.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends ActionBarActivity {

	private TextView tvSafePhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lost_find);
		SharedPreferences msp = getSharedPreferences("config", MODE_PRIVATE);

		boolean boolean1 = msp.getBoolean("configed", false);
		if (boolean1) {
			setContentView(R.layout.activity_lost_find);

			// 根据sp更新安全号码
			String phone = msp.getString("safephone", null);
			tvSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
			tvSafePhone.setText(phone);
			// 根据是否保护 查看是否可以锁子
			ImageView ivProtect = (ImageView) findViewById(R.id.iv_protect);
			boolean protect = msp.getBoolean("protect", false);
			if (protect) {
				ivProtect.setImageResource(R.drawable.lock);
			} else {
				ivProtect.setImageResource(R.drawable.unlock);
			}

		} else {
			startActivity(new Intent(LostFindActivity.this, Step1Activity.class));
		}

	}

	public void reEnter(View view) {
		System.out.println("fsdfs===");
		startActivity(new Intent(LostFindActivity.this, Step1Activity.class));
	}

}
