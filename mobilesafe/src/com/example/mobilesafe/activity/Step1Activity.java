package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.R.id;
import com.example.mobilesafe.R.layout;
import com.example.mobilesafe.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Step1Activity extends BaseStepActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step1);
		
		//startActivity(intent)
	}

	@Override
	public void showNextPage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(Step1Activity.this,Step2Activity.class));
		finish();
		
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}

	@Override
	public void showPreviousPage() {
		// TODO Auto-generated method stub
		
	}

	
}
