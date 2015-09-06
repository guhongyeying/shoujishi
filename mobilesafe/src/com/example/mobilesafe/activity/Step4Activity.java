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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Step4Activity extends BaseStepActivity {

	private SharedPreferences ms;
	private CheckBox cbProtect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step4);

		ms = getSharedPreferences("config", MODE_PRIVATE);

		cbProtect = (CheckBox) findViewById(R.id.cb_protect);

		boolean protect = msp.getBoolean("protect", false);

		if (protect) {
			cbProtect.setText("防盗系统已经开启了");
			cbProtect.setChecked(true);
		} else {
			cbProtect.setText("防盗系统已经关闭了");
			cbProtect.setChecked(false);
		}

		// 更具checkbox的变化回调此方法
		cbProtect.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					cbProtect.setText("防盗系统已经开启了");
					msp.edit().putBoolean("protect", true).commit();
				} else {

					cbProtect.setText("防盗系统已经关闭了");
					msp.edit().putBoolean("protect", false).commit();
				}

			}
		});
	}

	@Override
	public void showNextPage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(Step4Activity.this, LostFindActivity.class));
		finish();
		ms.edit().putBoolean("configed", true).commit();
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}

	@Override
	public void showPreviousPage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, Step3Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);

	}

}
