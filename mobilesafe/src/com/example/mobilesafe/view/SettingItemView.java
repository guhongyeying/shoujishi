package com.example.mobilesafe.view;

import com.example.mobilesafe.R;

import android.R.bool;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.example.mobilesafe";
	private TextView tvTitle;
	private TextView tvDesc;
	private CheckBox cbStatus;
	private String mtitle;
	private String mDescOff;
	private String mDescOn;

	// style
	public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		initView();
	}

	// 属性
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mtitle = attrs.getAttributeValue(NAMESPACE, "title_text");
		mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
		mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
		System.out.println("mtitle" + mtitle);
		initView();
	}

	// 代码new
	public SettingItemView(Context context) {
		super(context);
		initView();
		// TODO Auto-generated constructor stub
	}

	private void initView() {
		View.inflate(getContext(), R.layout.view_setting_item, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDesc = (TextView) findViewById(R.id.tv_desc);
		cbStatus = (CheckBox) findViewById(R.id.cb_status);
		
		setTitle(mtitle);
		
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setDesc(String desc) {

		tvDesc.setText(desc);
	}

	public boolean isChecked() {
		return cbStatus.isChecked();
	}

	public void setChecked(boolean check) {
		cbStatus.setChecked(check);
		
		if(check){
			setDesc(mDescOn);
		}else {
			setDesc(mDescOff);
		}
	}

}
