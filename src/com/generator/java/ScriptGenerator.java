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

	/**
	 * 创建.java文件所在路径 和 返回.java文件File对象
	 * @param outDirFile 生成文件路径
	 * @param javaPackage java包名
	 * @param javaClassName java类名
	 * @return
	 */
	public File getJavaFile(String packageName, String className) {
		if(null == packageName || packageName.trim().length() < 1){
			packageName = prop.getProperty("packageName"); 
		} 
		if(null == className || className.trim().length() < 1){
			className = prop.getProperty("className");
		}
		File outDirFile = new File(prop.getProperty("fileStoreDir"));
		if(!outDirFile.exists()){
			outDirFile.mkdir();
		}
        String packageSubPath = packageName.replace('.', '/');
        File packagePath = new File(outDirFile, packageSubPath);
        File file = new File(packagePath, className + ".java");
        if(!packagePath.exists()){
        	packagePath.mkdirs();
        }
        return file;
    }
	
}
