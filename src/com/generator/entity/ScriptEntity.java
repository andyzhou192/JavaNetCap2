package com.generator.entity;

/**
 * 实体类
 * @author  zhouyelin
 *
 */
public class ScriptEntity {
	// 实体所在的包名
	private String packageName;
	// 作者
	private String author = System.getProperty("user.name");
	// 实体类描述
	private String classDesc = "Description: ";
	// 实体类名
	private String className;
	// 父类名
	private String superclass;
	// 参数集合
	private String[] paramNames;
	// 是否冒烟脚本
	private boolean smokeScript = false;
	
	public String getPackageName() {
		return packageName;
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		if(null != author)
			this.author = author;
		else
			this.author = System.getProperty("user.name");
	}
	
	public String getClassDesc() {
		return classDesc;
	}

	public void setClassDesc(String classDesc) {
		this.classDesc = this.classDesc + classDesc;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
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

}
