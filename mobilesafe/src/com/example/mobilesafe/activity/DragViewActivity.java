package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import com.example.mobilesafe.R.id;
import com.example.mobilesafe.R.layout;
import com.example.mobilesafe.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class DragViewActivity extends Activity {

	private TextView tvTop;
	private TextView tvBottom;
	private ImageView ivDrag;
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private SharedPreferences msp;
	private int winWidth;
	private int winHeight;
	long[] mHits = new long[2];// 数组长度表示要点击的次数
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_view);
		
		msp = getSharedPreferences("config", MODE_PRIVATE);
		tvTop = (TextView) findViewById(R.id.tv_top);
		tvBottom = (TextView) findViewById(R.id.tv_bottom);

		ivDrag = (ImageView) findViewById(R.id.iv_drag);
		
		//设置双击居中
		ivDrag.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();// 开机后开始计算的时间
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
					// 把图片居中z
					System.out.println("把图片居中");
					ivDrag.layout(winWidth / 2 - ivDrag.getWidth() / 2,
							ivDrag.getTop(), winWidth / 2 + ivDrag.getWidth()
									/ 2, ivDrag.getBottom());
					}
			}
		});
		
		winWidth = getWindowManager().getDefaultDisplay().getWidth();
	       winHeight = getWindowManager().getDefaultDisplay().getHeight();
		   
		// onMeasure(测量view), onLayout(安放位置), onDraw(绘制)
				// ivDrag.layout(lastX, lastY, lastX + ivDrag.getWidth(),
				// lastY + ivDrag.getHeight());//不能用这个方法,因为还没有测量完成,就不能安放位置

		int lastX = msp.getInt("lastX", 0); 
		int lastY = msp.getInt("lastY", 0);
		
		
		if(lastY < winHeight/2){
			tvTop.setVisibility(View.INVISIBLE);
			tvBottom.setVisibility(View.VISIBLE);
		}else {
			tvTop.setVisibility(View.VISIBLE);
			tvBottom.setVisibility(View.INVISIBLE);
		}
		

	RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag.getLayoutParams();

	    layoutParams.leftMargin = lastX;
	    layoutParams.topMargin = lastY;
	    ivDrag.setLayoutParams(layoutParams);
	    //设置触摸监听并且回调数据
	   
	    
	    
		ivDrag.setOnTouchListener(new OnTouchListener() {

		

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					 endX = (int) event.getRawX();
					endY = (int) event.getRawY();
					
					int dX = endX - startX;
					int dY = endY - startY;
					
					int l = ivDrag.getLeft() + dX;
					int r =  ivDrag.getRight() + dX;
					
					int t = ivDrag.getTop() + dY;
					int b = ivDrag.getBottom() + dY;
					
					if(l < 0 || r> winWidth || t < 0 || b > winHeight - 20){
						break;
					}
					
					if(t < winHeight/2){
						tvTop.setVisibility(View.INVISIBLE);
						tvBottom.setVisibility(View.VISIBLE);
					}else {
						tvTop.setVisibility(View.VISIBLE);
						tvBottom.setVisibility(View.INVISIBLE);
					}
					
					ivDrag.layout(l, t, r, b);
					
					 startX = (int) event.getRawX();
	                  startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					// 记录坐标点
					Editor edit = msp.edit();
					edit.putInt("lastX", ivDrag.getLeft());
					edit.putInt("lastY", ivDrag.getTop());
					edit.commit();
					break;

				default:
					break;
				}
				return false;
				
			}
		});
	}

}
