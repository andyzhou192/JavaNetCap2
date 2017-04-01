package com.common;

import java.io.File;
import java.util.Properties;

import com.common.util.StringUtil;
import com.generator.maven.ProjectInfo;

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
	
	public static String EXPAND_ICON = StringUtil.assembleRelativeFilePath("img", "expand.png");
	public static String COLLAPSE_ICON = StringUtil.assembleRelativeFilePath("img", "collapse.png");
	
	public static String DEF_LOG_PROP_FILE = StringUtil.assembleRelativeFilePath("conf", "log4j.properties");
	public static String DEF_SET_PROP_FILE = StringUtil.assembleRelativeFilePath("conf", "setting.properties");
	
	public static Properties PROPS = null;
	
	public static ProjectInfo PROJECT_INFO = null;
	
	public static String DEFAULT_PROJECT_DIR = new File("projects").getAbsolutePath();
	public static String MAVEN_BAT = StringUtil.assembleRelativeFilePath("maven", "create_maven_project.bat");
	public static String TEMPLATE_POM = StringUtil.assembleRelativeFilePath("maven", "template_pom.xml");
	public static String HTTP_TEST_JAR = StringUtil.assembleRelativeFilePath("libs", "server-interface-ats-1.0.0.jar");
	
	public static String DEFAULT_TEMPLATE_SCRIPT_DIR = new File("template").getAbsolutePath();
	public static String DEFAULT_TEMPLATE_SCRIPT = StringUtil.assembleRelativeFilePath("template", "HttpTestScriptEntity.ftlh");
}