package com.example.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {

	public static String getAddress(String number) {
        System.out.println("number ; " + number );
		String address = "未知号码";
		String path = "data/data/com.example.mobilesafe/files/address.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
          
		if (number.matches("^1[3-8]\\d{9}$")) {
			String sql = "select location from data2 where id=(select outkey from data1 where id = ?)";
			// db.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)",
			// number.subSequence(0, 7));
			Cursor cursor = db.rawQuery(sql,
					new String[] { number.substring(0, 7) });

			
				if (cursor.moveToNext()) {
					address = cursor.getString(0);
					System.out.println("cursor.index" + cursor.getString(0));

				}
				cursor.close();
			
		} else if (number.matches("^\\d+$")) {
			switch (number.length()) {
			case 3:
				address = "报警电话";
				System.out.println("报警电话");
				break;
			case 4:
				address = "模拟器";
				System.out.println("模拟器");
				break;

			case 5:
				address = "客服电话";
				System.out.println("客服电话");
				break;

			case 8:
				address = "本地电话";
				System.out.println("本地电话");
				break;

			default:

				if (number.startsWith("0") && number.length() > 10) {

					Cursor cursor = db.rawQuery(
							"select location from data2 where area = ?",
							new String[] { number.substring(1, 4) });

					if (cursor.moveToNext()) {
						address = cursor.getString(0);
					} else {
						cursor.close();
						Cursor cs = db.rawQuery(
								"select location from data2 where area = ?",
								new String[] { number.substring(1, 4) });
						if (cs.moveToNext()) {
							address = cursor.getString(0);
							cs.close();
						}

					}
				}
				break;
			}
		}
		db.close();
		return address;
	}
}
