package com.example.mobeilsafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ��ȡ���Ĺ�����
 */
public class StreamUtils {
	/**
	 * ����������ȡΪString����
	 * @param in
	 * @return
	 * @throws IOException 
	 */
	
	public static String readFromStream(InputStream in) throws IOException{
		//����һ���ֽ����������
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		byte[] bt = new byte[1024];
		while((len = in.read(bt))!= -1){
			out.write(bt,0,len);
		}
		String result = out.toString();
		//�رո�����
		in.close();
		out.close();
		return result;
	}
}
