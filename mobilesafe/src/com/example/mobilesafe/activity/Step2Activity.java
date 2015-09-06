package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.R.id;
import com.example.mobilesafe.R.layout;
import com.example.mobilesafe.R.menu;
import com.example.mobilesafe.view.SettingItemView;

import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector.*;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Step2Activity extends BaseStepActivity {

	

	private SettingItemView sivSim;





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step2);
		
		
        
		sivSim = (SettingItemView) findViewById(R.id.siv_sim);
	    
		    String sim = msp.getString("sim", null);
		    if(!TextUtils.isEmpty(sim)){
		    	sivSim.setChecked(true);
		    }else {
		    	sivSim.setChecked(false);
		    }
		
		sivSim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 
				// TODO Auto-generated method stub
				//sivSim.sett
				if(sivSim.isChecked()){
					sivSim.setChecked(false);
					msp.edit().remove("sim").commit();
					// System.out.println("simSerialNumber=fds" );
				}else {
					sivSim.setChecked(true);
	TelephonyManager  tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
	        String simSerialNumber = tm.getSimSerialNumber();
	      //  System.out.println("simSerialNumber=" + simSerialNumber);
	        msp.edit().putString("sim", simSerialNumber).commit();
				}
			}
		});
	}

	

	public void showNextPage(){
		
		String sim = msp.getString("sim", null);
		if(TextUtils.isEmpty(sim)){
			Toast.makeText(this, "SIM没有绑定不能进入下一步",0).show();
			return ;
		}
		startActivity(new Intent(Step2Activity.this,Step3Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
		
	 }
	
	



	@Override
	public void showPreviousPage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(Step2Activity.this, Step1Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}

	
}
