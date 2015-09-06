package com.example.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.example.mobilesafe.bean.BlackNumberInfo;
import com.lidroid.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangren on 15/8/27.
 */
public class BlachNumberDao {


    public BlackNumberOpenHelper openHelper;

    public BlachNumberDao(Context context) {


        openHelper = new BlackNumberOpenHelper(context);

    }




    public boolean addContacts(String number, String mode) {
        SQLiteDatabase db = openHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("number", number);
        contentValues.put("mode", mode);

        long blacknumber = db.insert("blacknumber", null, contentValues);

        if (blacknumber > 0) {
            return true;
        } else

            return false;

    }

    public boolean deleteContacts(String number) {

        SQLiteDatabase db = openHelper.getWritableDatabase();


        int blacknumber = db.delete("blacknumber", "number = ?", new String[]{number});
        if (blacknumber > 0) {
            return true;
        } else {
            return false;
        }
    }


    public boolean changeNumberMode(String number, String mode) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", mode);
        int blacknumber = db.update("blacknumber", values, "number = ?", new String[]{number});

        if (blacknumber > 0) return true;
        else return false;
    }

    public String findMode(String number) {

        SQLiteDatabase db = openHelper.getReadableDatabase();

        Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "number = ?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {

            String mode = cursor.getString(0);
            cursor.close();
            ;
            db.close();
            return mode;


        } else
            return null;
/*
        String mode = "";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "number=?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {
            mode = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return mode;

        */


    }

    public List<BlackNumberInfo> findAllContacts() {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ArrayList<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();

        Cursor cursor = db.query("blacknumber", new String[]{"number", "mode"}, null,
                null, null, null, null);

        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            String number = cursor.getString(0);
            String mode = cursor.getString(1);

            blackNumberInfo.setNumber(number);
            blackNumberInfo.setMode(mode);

            blackNumberInfos.add(blackNumberInfo);

        }

        db.close();
        cursor.close();
        SystemClock.sleep(2000);
        return blackNumberInfos;

    }

    /**
     *  /**
     * 分页加载数据
     *
     * @param currentPage 表示当前是哪一页
     * @param perSize   表示每一页有多少条数据
     * @return limit 表示限制当前有多少数据
     * offs
     */


    public List<BlackNumberInfo> findPageContacts(int currentPage,int perSize) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ArrayList<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();

        Cursor cursor = db.rawQuery("select number,mode from blacknumber limit ? offset ?", new String[]{String.valueOf(perSize),
                String.valueOf(perSize * currentPage)});

        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            String number = cursor.getString(0);
            String mode = cursor.getString(1);

            blackNumberInfo.setNumber(number);
            blackNumberInfo.setMode(mode);

            blackNumberInfos.add(blackNumberInfo);

        }

        db.close();
        cursor.close();

        return blackNumberInfos;
    }


    public List<BlackNumberInfo> findPageContacts2(int start,int perSize) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ArrayList<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();

        Cursor cursor = db.rawQuery("select number,mode from blacknumber limit ? offset ?", new String[]{String.valueOf(perSize),
                String.valueOf(start)});

        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            String number = cursor.getString(0);
            String mode = cursor.getString(1);

            blackNumberInfo.setNumber(number);
            blackNumberInfo.setMode(mode);

            blackNumberInfos.add(blackNumberInfo);

        }
        db.close();
        cursor.close();
        return blackNumberInfos;
    }


    public int getTotalContacts() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*)  from blacknumber", null);
        cursor.moveToNext();
        int ids = cursor.getInt(0);

        cursor.close();
        db.close();
        return ids;

    }


}
