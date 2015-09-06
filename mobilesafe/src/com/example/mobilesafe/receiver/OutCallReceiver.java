package com.example.mobilesafe.receiver;

import com.example.mobilesafe.db.dao.AddressDao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
/**
 * 
* @ClassName: OutCallReceiver 
* @Description:用于监听打电话
* @author A18ccms a18ccms_gmail_com 
* @date 2015-8-25 下午9:54:58 
*
 */
public class OutCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
        String number = getResultData();
        String address = AddressDao.getAddress(number);
        
        Toast.makeText(context,address, 0).show();
        
	}

}
