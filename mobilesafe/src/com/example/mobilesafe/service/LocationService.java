package com.example.mobilesafe.service;

import android.app.Service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service {

	private MyLocationLister locationLister;
	private SharedPreferences msp;
	private LocationManager ls;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		    msp = getSharedPreferences("config", MODE_PRIVATE);
	          ls = (LocationManager) getSystemService(LOCATION_SERVICE);
		// 创建一个locationprovider过滤条件
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);
		criteria.setAltitudeRequired(true);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度信息
		//获得最佳定位信息
		String bestProvider = ls.getBestProvider(criteria, true);
		   locationLister = new MyLocationLister();
		ls.requestLocationUpdates(bestProvider, 0, 0, locationLister);
		 
         
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		ls.removeUpdates(locationLister);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
     class MyLocationLister implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			msp.edit().putString("location",location.getLongitude()+";"+location.getLatitude()).commit();
		     stopSelf();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
    	 
     }

}
