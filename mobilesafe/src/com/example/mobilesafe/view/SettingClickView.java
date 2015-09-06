package com.example.mobilesafe.view;

import com.example.mobilesafe.R;

import android.R.bool;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingClickView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.example.mobilesafe";
	private TextView tvTitle;
	private TextView tvDesc;
	

	// style
	public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		initView();
	}

	// 属性
	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);

		
		initView();
	}

	// 代码new
	public SettingClickView(Context context) {
		super(context);
		initView();
		// TODO Auto-generated constructor stub
	}

	private void initView() {
		View.inflate(getContext(), R.layout.view_setting_click, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDesc = (TextView) findViewById(R.id.tv_desc);

	
		
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setDesc(String desc) {

		tvDesc.setText(desc);
	}



}
