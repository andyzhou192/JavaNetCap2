package com.generator.bean;

import com.view.preference.PropertyHelper;

/**
 * 实体类
 * @author  zhouyelin
 *
 */
public class ScriptForJavaBean {
	// 实体所在的包名
	private String packageName = PropertyHelper.getPackageName();
	// 作者
	private String author = System.getProperty("user.name");
	// 实体类描述
	private String classDesc = "Description: test class description";
	// 实体类名
	private String className;
	// 测试方法名
	private String methodName;
	// 父类名
	private String superclass;
	// 参数集合
	private String[] paramNames = DataForJavaBean.getFields();
	// 是否冒烟脚本
	private boolean smokeScript = false;
	
	public String getPackageName() {
		return packageName;
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAuthor() {
		return null != author ? author : PropertyHelper.getAuthor();
	}

	public void setAuthor(String author) {
		if(null != author)
			this.author = author;
	}
	
	public String getClassDesc() {
		return classDesc;
	}

	public void setClassDesc(String classDesc) {
		this.classDesc = "Description: " + classDesc;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className.endsWith("Test") ? className : className + "Test";
	}
	
	public String getSuperclass() {
		return superclass;
	}
	
	public void setSuperclass(String superclass) {
		this.superclass = superclass;
	}
	
	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	public boolean getSmokeScript() {
		return smokeScript;
	}

	public void setSmokeScript(boolean smokeScript) {
		this.smokeScript = smokeScript;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName.startsWith("test") ? methodName : "test" + methodName;
	}

}
