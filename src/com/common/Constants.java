package com.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 
 * @author zhouyelin@chinamobile.com
 *
 */
public final class Constants {

	public final static String UTF8 = "UTF-8"; //Charset UTF_8 = Charset.forName("UTF-8");
	
	public final static String SP = " "; // 空格
	
	public final static String CRLF = "\r\n"; // 回车换行（carriage return/line feed）
	
	public final static String CR = "\r"; //回车
	
	public final static String LF = "\n"; // 换行
	
	public final static String CASEID = "caseId";
	
	public final static String GLOBAL_MSG = "msg";
	
	public final static String COMMA = ",";
	
	public final static String COLON = ":";
	
	public final static String QUES = "\\?";
	
	public final static String EQUAL = "=";
	
	public final static String AND = "&";
	
	public final static String VERTICAL_LINE = "\\|";
	
	public static Properties PROPS = new Properties();
	
	public static String DEF_LOG_PROP_FILE = "./conf/log4j.properties";
	
	public static String DEF_SET_PROP_FILE = "./conf/setting.properties";
	
	public static void initProperties(String fileName){
		DEF_SET_PROP_FILE = fileName;
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(fileName));
			InputStreamReader inr = new InputStreamReader(in, "UTF-8");// 解决读取的内容乱码问题
			PROPS.load(inr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}