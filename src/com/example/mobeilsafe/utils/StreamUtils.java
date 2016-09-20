package com.example.mobeilsafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 读取流的工具类
 */
public class StreamUtils {
	/**
	 * 将输入流读取为String返回
	 * @param in
	 * @return
	 * @throws IOException 
	 */
	
	public static String readFromStream(InputStream in) throws IOException{
		//创建一个字节数组输出流
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		byte[] bt = new byte[1024];
		while((len = in.read(bt))!= -1){
			out.write(bt,0,len);
		}
		String result = out.toString();
		//关闭各种流
		in.close();
		out.close();
		return result;
	}
}
