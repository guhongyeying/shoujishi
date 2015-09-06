package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;

public abstract class BaseStepActivity extends Activity {

	private GestureDetector mdetector;
	public SharedPreferences msp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		msp = getSharedPreferences("config", MODE_PRIVATE);
		mdetector = new GestureDetector(this, new SimpleOnGestureListener(){
			 @Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				// TODO Auto-generated method stub
				 
				 
				 if(Math.abs(e1.getRawY() - e2.getRawY()) >100 ||Math.abs(velocityX) <100){
					 return true;
				 }
				 
				 if(e1.getRawX() - e2.getRawX() >150){
					 showNextPage();
				 }
				 if(e2.getRawX() - e1.getRawX() >150){
					 showPreviousPage();
				 }
				 
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		 });
		    
		}

		
		public void next(View view){
			
		   showNextPage();
		}
		
		public void previous(View view) {
			System.out.println();
			showPreviousPage();
		}
	 public abstract void showNextPage();
		
	public abstract void  showPreviousPage();
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			mdetector.onTouchEvent(event);
			return super.onTouchEvent(event);
		}
		
	}
