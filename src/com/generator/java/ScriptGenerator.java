package com.generator.java;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.generator.entity.ScriptEntity;
import com.common.Constants;
import com.generator.AbstractGenerator;

/**
 * 
 * @author zhouyelin
 *
 */
public class ScriptGenerator extends AbstractGenerator {
	
	private Properties prop = Constants.PROPS;
	public File targetFile;

	public ScriptGenerator(String packageName, String className) throws IOException {
		super();
		this.targetFile = getJavaFile(packageName, className);
	}
	
	public ScriptGenerator(String templateDir, String packageName, String className) throws IOException {
		super(templateDir);
		this.targetFile = getJavaFile(packageName, className);
	}

	@Override
	public Map<String, Object> createDataModel(String packageName, String className, String classDesc, boolean isSmoke, String[] params) {
		ScriptEntity scriptEntity = new ScriptEntity();
		scriptEntity.setPackageName(packageName); // 创建包名
		scriptEntity.setAuthor(prop.getProperty("author"));
		scriptEntity.setClassDesc(classDesc);
		scriptEntity.setClassName(className);  // 创建类名
		scriptEntity.setSuperclass(prop.getProperty("superClass"));
		scriptEntity.setParamNames(params);
		scriptEntity.setSmokeScript(isSmoke);
		
		// 创建.java类文件
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("entity", scriptEntity);
		return root;
	}

	
	
}
