package com.example.mobilesafe.activity;

import java.util.ArrayList;

import java.util.HashMap;

import com.example.mobilesafe.R;
import com.example.mobilesafe.R.id;
import com.example.mobilesafe.R.layout;
import com.example.mobilesafe.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ContactActivity extends ActionBarActivity {



	private ArrayList<HashMap<String, String>> arrayList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		
		arrayList = returnContact();
		System.out.println("arrayList=" + arrayList.toString());
		ListView view = (ListView) findViewById(R.id.lv_list);
		//view.setAdapter(new SimpleAdapter(this, arrayList, R.layout.list_view_item, new String{"name","phone"},new int{R.id.tv_name,R.id.tv_number}));
//		view.setAdapter(new SimpleAdapter(this, arrayList,
//				R.layout.list_view_item, new String[] { "name", "phone" },
//				new int[] { R.id.tv_name, R.id.tv_number }));
		view.setAdapter(new SimpleAdapter(this, arrayList,
				R.layout.list_view_item, new String[] { "name", "phone" },
				new int[] { R.id.tv_name, R.id.tv_number }));
		//监听listview 子条目的点击事件， 并且用setreuslt返回相应额事件
		view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
		        String phone = arrayList.get(position).get("phone");
		        Intent intent = new Intent();
		        intent.putExtra("phone", phone);
		        setResult(Activity.RESULT_OK, intent);
		        finish();
				
			}
		});
	}

	private ArrayList<HashMap<String, String>> returnContact() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri data_uri = Uri.parse("content://com.android.contacts/data");
		// Cursor cursor = getContentResolver().query(uri, ", null,null,null);
		Cursor cursor = getContentResolver().query(raw_uri,
				new String[] { "_id" }, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {

			while (cursor.moveToNext()) {
				int id = cursor.getInt(0);
				Cursor c = getContentResolver().query(data_uri,
						new String[] { "mimetype", "data1" },
						"raw_contact_id = ?",
						new String[] { String.valueOf(id) }, null);

				HashMap<String, String> per = new HashMap<String, String>();

				if (c != null && c.getCount() > 0) {
					while (c.moveToNext()) {

						String mtype = c.getString(0);
						String data = c.getString(1);
						if ("vnd.android.cursor.item/email_v2".equals(mtype)) {
							Log.i("==", "email" + data);
						}
						if ("vnd.android.cursor.item/name".equals(mtype)) {
							Log.i("==", "名字" + data);
							per.put("name", data);
						}
						if ("vnd.android.cursor.item/postal-address_v2"
								.equals(mtype)) {
							Log.i("==", "手机号码1" + data);
							
						}
						if ("vnd.android.cursor.item/phone_v2".equals(mtype)) {
							Log.i("==", "手机号码" + data);
							per.put("phone", data);
						}
						Log.i("==", "////////");

					}
				}

				list.add(per);
				c.close();
			}

		}
		cursor.close();

		return list;
	}

}
