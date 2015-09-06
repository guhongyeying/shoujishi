package com.example.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by wangren on 15/9/3.
 */
public class AntivirusDao {


    private static String desc;

    public  static  String  checkFileVirus(String md5){


      String path = "data/data/com.example.mobilesafe/files/antivirus.db";

      SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

      Cursor cursor = db.rawQuery("select desc from datable where md5 = ?", new String[]{md5});

      if (cursor.moveToNext()){
          desc = cursor.getString(0);
      }

      cursor.close();




      return  desc;
  }
}
