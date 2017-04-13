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
	
	
//	public static String EXPAND_ICON = StringUtil.assembleRelativeFilePath("." + File.separator + "img", "expand.png");
//	public static String COLLAPSE_ICON = StringUtil.assembleRelativeFilePath("." + File.separator + "img", "collapse.png");
	public static String EXPAND_ICON = Thread.currentThread().getContextClassLoader().getResource("img/expand.png").getPath();
	public static String COLLAPSE_ICON = Thread.currentThread().getContextClassLoader().getResource("img/collapse.png").getPath();
	public static String BOOKMARK_ICON = Thread.currentThread().getContextClassLoader().getResource("img/bookmark.png").getPath();
	
	//public static String DEF_LOG_PROP_FILE = StringUtil.assembleRelativeFilePath("." + File.separator + "conf", "log4j.properties");
	public static String DEF_SET_PROP_FILE = StringUtil.assembleRelativeFilePath("." + File.separator + "conf", "setting.properties");
	
	public static Properties PROPS = null;
	
	public static ProjectInfo PROJECT_INFO = null;
	
	public static String DEFAULT_PROJECT_DIR = new File("projects").getAbsolutePath();
	public static String MAVEN_BAT = StringUtil.assembleRelativeFilePath("." + File.separator + "maven", "create_maven_project.bat");
	public static String TEMPLATE_POM = StringUtil.assembleRelativeFilePath("." + File.separator + "maven", "template_pom.xml");
	public static String HTTP_TEST_JAR = StringUtil.assembleRelativeFilePath("." + File.separator + "exts", "server-interface-ats-1.0.0.jar");
	
	public static String DEFAULT_TEMPLATE_SCRIPT_DIR = new File("template").getAbsolutePath();
	public static String DEFAULT_TEMPLATE_SCRIPT = StringUtil.assembleRelativeFilePath("." + File.separator + "template", "HttpTestScriptEntity.ftlh");
	
	public static String DEFAULT_THEME = Thread.currentThread().getContextClassLoader().getResource("theme/default.xml").getPath();
	public static String DEFAULT_ALT_THEME = Thread.currentThread().getContextClassLoader().getResource("theme/default-alt.xml").getPath();
	public static String DARK_THEME = Thread.currentThread().getContextClassLoader().getResource("theme/dark.xml").getPath();
	public static String ECLIPSE_THEME = Thread.currentThread().getContextClassLoader().getResource("theme/eclipse.xml").getPath();
	public static String IDEA_THEME = Thread.currentThread().getContextClassLoader().getResource("theme/idea.xml").getPath();
	public static String VS_THEME = Thread.currentThread().getContextClassLoader().getResource("theme/vs.xml").getPath();
	public static String THEME_DTD = Thread.currentThread().getContextClassLoader().getResource("theme/theme.dtd").getPath();
	
	
	
//	public static void main(String[] args) {
//		System.out.println(BOOKMARK_ICON);
//	}
}