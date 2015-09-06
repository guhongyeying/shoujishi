package com.example.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {

	public static String readStream(InputStream is) throws IOException {
		// TODO Auto-generated method stub
		
		//ByteArrayOutputStream
       ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
       byte[] buffer = new byte[1024];
      int len = 0;
       while((len = is.read(buffer)) != -1){
    	   outputStream.write(buffer, 0, len);
       }
       String rt = outputStream.toString();
       is.close();
       outputStream.close();
       
		return rt;
	}
   

}
