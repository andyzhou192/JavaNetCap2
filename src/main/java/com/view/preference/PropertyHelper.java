package com.view.preference;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.common.Constants;
import com.common.util.LogUtil;
import com.common.util.PropertiesUtil;
import com.common.util.StringUtil;
import com.netcap.captor.NetCaptor;

public class PropertyHelper {
	
	private static Properties prop = new Properties();
	
	// Workspace Setting View
	public static String WORKSPACE = "workspace";
	
	public static String USE_DEFAULT_PROJECT  = "useDefaultMavenProject";
	
	// Script Setting View
	public static String PACKAGE_NAME = "packageName";
	public static String AUTHOR = "author";
	public static String CLASS_DESC = "classDesc";
	public static String CLASS_NAME = "className";
	public static String SUPER_CLASS = "superClass";
	public static String PARAM_NAMES = "paramNames";
	public static String SMOKE_SCRIPT = "smokeScript";
	public static String SCRIPT_OVERWRITE = "scriptOverwrite";
	public static String TEMPLATE_DIR = "templateDir";
	public static String TEMPLATE_FILE = "templateFile";
	
	// Jcapture Setting View
	public static String NET_DEVICES_NAME = "netDevicesName";
	public static String PROTOCOL_TYPE = "protocolType";
	public static String PROMISC = "promisc";
	public static String CAPTURE_URL = "captureUrl";
	public static String CAPTURE_LENGTH = "captureLength";
	
	public static void loadProperties(){
		LogUtil.debug(PropertyHelper.class, (new File(Constants.DEF_SET_PROP_FILE)).getAbsolutePath());
		Constants.PROPS = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(Constants.DEF_SET_PROP_FILE));
			InputStreamReader inr = new InputStreamReader(in, "UTF-8");// 解决读取的内容乱码问题
			Constants.PROPS.load(inr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean storeProperties(){
		boolean isSucc = PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, prop, "");
		if(isSucc)
			prop.clear();
		return isSucc;
	}
	
	/************************* Setter Method *************************/
	/************************* Workspace Setting View *************************/
	public static void setWorkspace(String text){
		Constants.PROPS.put(WORKSPACE, text);
		prop.put(WORKSPACE, text);
		//return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, WORKSPACE, text, "");
	}
	
	public static void setUseDefaultProject(boolean isUse){
		Constants.PROPS.put(USE_DEFAULT_PROJECT, isUse);
		prop.put(SMOKE_SCRIPT, isUse);
		//return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, WORKSPACE, text, "");
	}
	
	/************************* Script Setting View *************************/
	public static void setPackageName(String text){
		Constants.PROPS.put(PACKAGE_NAME, text);
		prop.put(PACKAGE_NAME, text);
		//return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, PACKAGE_NAME, text, "");
	}
	
	public static void setAuthor(String text){
		Constants.PROPS.put(AUTHOR, text);
		prop.put(AUTHOR, text);
		//return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, AUTHOR, text, "");
	}
	 
	public static void setClassDesc(String text){
		Constants.PROPS.put(CLASS_DESC, text);
		prop.put(CLASS_DESC, text);
		//return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, CLASS_DESC, text, "");
	}
	 
	public static void setClassName(String text){
		Constants.PROPS.put(CLASS_NAME, text);
		prop.put(CLASS_NAME, text);
		//return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, CLASS_NAME, text, "");
	}
	 
	public static void setSuperClass(String text){
		Constants.PROPS.put(SUPER_CLASS, text);
		prop.put(SUPER_CLASS, text);
		//return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, SUPER_CLASS, text, "");
	}
	 
	public static void setParamNames(String text){
		Constants.PROPS.put(PARAM_NAMES, text);
		prop.put(PARAM_NAMES, text);
		//return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, PARAM_NAMES, text, "");
	}
	 
	public static void setSmokeScript(boolean isSmoke){
		Constants.PROPS.put(SMOKE_SCRIPT, isSmoke);
		prop.put(SMOKE_SCRIPT, isSmoke);
		//return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, SMOKE_SCRIPT, String.valueOf(isSmoke), "");
	}
	
	public static void setScriptOverwrite(boolean isOverwrite){
		Constants.PROPS.put(SCRIPT_OVERWRITE, isOverwrite);
		prop.put(SCRIPT_OVERWRITE, isOverwrite);
		//return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, SMOKE_SCRIPT, String.valueOf(isSmoke), "");
	}
	 
	public static void setTemplateDir(String text){
		Constants.PROPS.put(TEMPLATE_DIR, text);
		prop.put(TEMPLATE_DIR, text);
		//return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, TEMPLATE_DIR, text, "");
	}
	 
	public static void setTemplateFile(String text){
		Constants.PROPS.put(TEMPLATE_FILE, text);
		prop.put(TEMPLATE_FILE, text);
		//return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, TEMPLATE_FILE, text, "");
	}
	
	/************************* Jcapture Setting View *************************/
	public static void setNetDevicesName(String name){
		Constants.PROPS.put(NET_DEVICES_NAME, name);
		prop.put(NET_DEVICES_NAME, name);
		//return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, NET_DEVICES_INDEX, String.valueOf(index), "");
	}
	
	public static void setProtocolType(String text){
		Constants.PROPS.put(PROTOCOL_TYPE, text);
		prop.put(PROTOCOL_TYPE, text);
		//return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, PROTOCOL_TYPE, text, "");
	}
	
	public static void setPromisc(boolean isPromisc){
		Constants.PROPS.put(PROMISC, isPromisc);
		prop.put(PROMISC, isPromisc);
		//return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, PROMISC, String.valueOf(isPromisc), "");
	}
	 
	public static void setCaptureUrl(String text){
		Constants.PROPS.put(CAPTURE_URL, text);
		prop.put(CAPTURE_URL, text);
		//return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, CAPTURE_URL, text, "");
	}
	
	public static void setCaptureLength(int length){
		Constants.PROPS.put(CAPTURE_LENGTH, length);
		prop.put(CAPTURE_LENGTH, length);
		//return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, CAPTURE_LENGTH, String.valueOf(length), "");
	}
	
	
	/************************* Getter Method *************************/
	/************************* Workspace Setting View *************************/
	public static String getWorkspace(){
		if(null == Constants.PROPS)
			PropertyHelper.loadProperties();
		String value = String.valueOf(Constants.PROPS.get(WORKSPACE));
		if(StringUtil.validate(value)){
			return value;
		} else {
			PropertyHelper.setWorkspace(Constants.DEFAULT_PROJECT_DIR);
			return Constants.DEFAULT_PROJECT_DIR;
		}
	}
	
	public static boolean getUseDefaultProject(){
		if(null == Constants.PROPS)
			PropertyHelper.loadProperties();
		String value = String.valueOf(Constants.PROPS.get(USE_DEFAULT_PROJECT));
		return (StringUtil.validate(value)) ? Boolean.valueOf(value) : true;
	}
	
	/************************* Script Setting View *************************/
	public static String getPackageName(){
		if(null == Constants.PROPS)
			PropertyHelper.loadProperties();
		String value = String.valueOf(Constants.PROPS.get(PACKAGE_NAME));
		return (null != value) ? value : "";
	}
	 
	public static String getAuthor(){
		if(null == Constants.PROPS)
			PropertyHelper.loadProperties();
		String value = String.valueOf(Constants.PROPS.get(AUTHOR));
		if(StringUtil.validate(value)){
			return value;
		} else {
			PropertyHelper.setWorkspace(System.getProperty("user.name"));
			return System.getProperty("user.name");
		}
	}
	 
	public static String getClassDesc(){
		if(null == Constants.PROPS)
			PropertyHelper.loadProperties();
		String value = String.valueOf(Constants.PROPS.get(CLASS_DESC));
		return (null != value) ? value : "";
	}
	 
	public static String getClassName(){
		if(null == Constants.PROPS)
			PropertyHelper.loadProperties();
		return String.valueOf(Constants.PROPS.get(CLASS_NAME));
	}
	 
	public static String getSuperClass(){
		if(null == Constants.PROPS)
			PropertyHelper.loadProperties();
		return String.valueOf(Constants.PROPS.get(SUPER_CLASS));
	}
	 
	public static String getParamNames(){
		if(null == Constants.PROPS)
			PropertyHelper.loadProperties();
		return String.valueOf(Constants.PROPS.get(PARAM_NAMES));
	}
	 
	public static boolean getSmokeScript(){
		if(null == Constants.PROPS)
			PropertyHelper.loadProperties();
		String value = String.valueOf(Constants.PROPS.get(SMOKE_SCRIPT));
		return (StringUtil.validate(value)) ? Boolean.valueOf(value) : false;
	}
	
	public static boolean getScriptOverwrite(){
		if(null == Constants.PROPS)
			PropertyHelper.loadProperties();
		String value = String.valueOf(Constants.PROPS.get(SCRIPT_OVERWRITE));
		return (StringUtil.validate(value)) ? Boolean.valueOf(value) : false;
	}
	 
	public static String getTemplateDir(){
		if(null == Constants.PROPS)
			PropertyHelper.loadProperties();
		String value = String.valueOf(Constants.PROPS.get(TEMPLATE_DIR));
		return (StringUtil.validate(value)) ? value : Constants.DEFAULT_TEMPLATE_SCRIPT_DIR;
	}
	 
	public static String getTemplateFile(){
		if(null == Constants.PROPS)
			PropertyHelper.loadProperties();
		String value = String.valueOf(Constants.PROPS.get(TEMPLATE_FILE));
		return (StringUtil.validate(value)) ? value : Constants.DEFAULT_TEMPLATE_SCRIPT;
	}
	
	/************************* Jcapture Setting View *************************/
	public static String getNetDevicesName(){
		if(null == Constants.PROPS)
			PropertyHelper.loadProperties();
		String value = String.valueOf(Constants.PROPS.get(NET_DEVICES_NAME));
		return (StringUtil.validate(value)) ? value : NetCaptor.devicesMap.keySet().iterator().next();
	}
	
	public static String getProtocolType(){
		if(null == Constants.PROPS)
			PropertyHelper.loadProperties();
		String value = String.valueOf(Constants.PROPS.get(PROTOCOL_TYPE));
		return (StringUtil.validate(value)) ? value : "TCP";
	}
	
	public static boolean getPromisc(){
		if(null == Constants.PROPS)
			PropertyHelper.loadProperties();
		String value = String.valueOf(Constants.PROPS.get(PROMISC));
		return (StringUtil.validate(value)) ? Boolean.valueOf(value) : false;
	}
	
	public static String getCaptureUrl(){
		if(null == Constants.PROPS)
			PropertyHelper.loadProperties();
		String value = String.valueOf(Constants.PROPS.get(CAPTURE_URL));
		return (StringUtil.validate(value)) ? value : "";
	}
	
	public static int getCaptureLength(){
		if(null == Constants.PROPS)
			PropertyHelper.loadProperties();
		String value = String.valueOf(Constants.PROPS.get(CAPTURE_LENGTH));
		return (StringUtil.validate(value)) ? Integer.valueOf(value) : 1514;
	}
	 
}
