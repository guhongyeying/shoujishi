package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobilesafe.R;

public class Step3Activity extends BaseStepActivity {




	private EditText etPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step3);
         etPhone = (EditText) findViewById(R.id.et_phone);
        String safePhone = msp.getString("safephone", null);
        etPhone.setText(safePhone);
	}


	@Override
	public void showNextPage() {
		// TODO Auto-generated method stub
		String phone = etPhone.getText().toString().trim();
		
		
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "必须选择联系号码", 0).show();
			return ;
		}
		
		  msp.edit().putString("safephone", phone).commit();
		
		startActivity(new Intent(Step3Activity.this,Step4Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}


	@Override
	public void showPreviousPage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, Step2Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
		
	}
	
	public void seletContact(View view){
		
		startActivityForResult(new Intent(Step3Activity.this,ContactActivity.class), 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == Activity.RESULT_OK){
			String phone = data.getStringExtra("phone");
			  phone = phone.replace(" ", "");
			  etPhone.setText(phone);
		
			
		}
	}
	

}
