package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import com.example.mobilesafe.R.id;
import com.example.mobilesafe.R.layout;
import com.example.mobilesafe.R.menu;
import com.example.mobilesafe.db.dao.AddressDao;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

public class AddressActivity extends ActionBarActivity {

	private EditText etNumber;
	private TextView tvContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		etNumber = (EditText) findViewById(R.id.et_number);
		tvContent = (TextView) findViewById(R.id.tv_content);
		
		etNumber.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				 String address = AddressDao.getAddress(s.toString());
				 tvContent.setText(address);
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void onQuery(View view){
	      String number = etNumber.getText().toString();
		if(!TextUtils.isEmpty(number)){
			// AddressDao addressDao = new AddressDao();
			 String address = AddressDao.getAddress(number);
			 tvContent.setText(address);
		}else {
			//输入框左右换晃动的效果
			 Animation shake = AnimationUtils.loadAnimation(AddressActivity.this, R.anim.shake);
		
			etNumber.startAnimation(shake);
			vibrate();
		}
	}
	/**
	 * 震动效果
	 */
	public void vibrate(){
		  
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// vibrator.vibrate(2000);震动两秒
		vibrator.vibrate(new long[] { 1000, 2000, 1000, 3000 }, -1);
	}

}
