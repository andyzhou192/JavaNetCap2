package com.common.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 
 * 读取properties文件中的value
 *
 */
public class PropertiesUtil {
	private static Properties prop = new Properties();

	public static String getProperty(String fileName, String proName) {
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(fileName + ".properties"));
			InputStreamReader inr = new InputStreamReader(in, "UTF-8");// 解决读取的内容乱码问题
			prop.load(inr);
			return prop.getProperty(proName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean storeProperty(String file, String key, String value, String comments){
		///保存属性到config.properties文件
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(file));
			InputStreamReader inr = new InputStreamReader(in, "UTF-8");// 解决读取的内容乱码问题
			prop.load(inr);
			//FileOutputStream oFile = new FileOutputStream(fileName + ".properties", true);//true表示追加打开
			FileOutputStream fos = new FileOutputStream(file);
			prop.setProperty(key, value);
			prop.store(fos, comments);
			fos.close();
			return true;
		} catch (IOException e) {
			LogUtil.err(PropertiesUtil.class, e);
		}
		return false;
	}
	
//	public static void main(String[] args) {
//		storeProperty("config", "name", "value04", "test");
//	}
}
