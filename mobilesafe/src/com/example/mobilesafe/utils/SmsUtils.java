package com.example.mobilesafe.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;
import android.widget.Toast;


import com.example.mobilesafe.Encryption.Crypto;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wangren on 15/8/31.
 */
public class SmsUtils {


    private static FileOutputStream os;
    private static int count;

    public interface BackupSms {

        public void befor(int count);

        public void onBackuoSms(int progress);
    }


    public static boolean backUp(Context context, BackupSms callbackup) {


        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = Uri.parse("content://sms/");

            Cursor cursor = contentResolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);
            count = cursor.getCount();
            callbackup.befor(cursor.getCount());

            File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
            System.out.println("--------------------");
            System.out.println(file.toString());
            try {
                os = new FileOutputStream(file);
                XmlSerializer xmlSerializer = Xml.newSerializer();

                xmlSerializer.setOutput(os, "utf-8");

                xmlSerializer.startDocument("utf-8", true);
                xmlSerializer.startTag(null, "smss");

                //序列化

                int progress = 0;
                while (cursor.moveToNext()) {

                    String address = cursor.getString(0);
                    String date = cursor.getString(1);
                    String type = cursor.getString(2);
                    String body = cursor.getString(3);

                    xmlSerializer.startTag(null, "sms");


                    xmlSerializer.startTag(null, "address");
                    xmlSerializer.text(address);
                    xmlSerializer.endTag(null, "address");

                    xmlSerializer.startTag(null, "date");
                    xmlSerializer.text(date);
                    xmlSerializer.endTag(null, "date");

                    xmlSerializer.startTag(null, "type");
                    xmlSerializer.text(type);
                    xmlSerializer.endTag(null, "type");
                    //加密
                    body = Crypto.encrypt("123", body);

                    xmlSerializer.startTag(null, "body");
                    xmlSerializer.text(body);
                    xmlSerializer.endTag(null, "body");

                    xmlSerializer.endTag(null, "sms");


                   SystemClock.sleep(1000/count);
                    progress++;
                    callbackup.onBackuoSms(progress);
                }

                xmlSerializer.endTag(null, "smss");
                xmlSerializer.endDocument();
                return true;

            } catch (Exception e) {

                e.printStackTrace();
            } finally {


                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cursor.close();

            }


//                System.out.println("--------------------");
//                System.out.println("address  "+address +"  date  " +date+ "type" + type +"  body "  + body);


        } else {

            Toast.makeText(context, "没有插入s卡", Toast.LENGTH_SHORT).show();
        }

        return false;

    }
}
