package com.generator.java;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.common.util.LogUtil;
import com.generator.bean.ScriptForJavaBean;
import com.generator.AbstractGenerator;
import com.view.preference.PropertyHelper;
import com.view.util.ViewModules;

import freemarker.template.TemplateException;

/**
 * 
 * @author zhouyelin
 *
 */
public class ScriptGenerator extends AbstractGenerator {
	
	private Class<?> cl = ScriptGenerator.class;
	
	private String templateFile;

	public ScriptGenerator(String templateFile) {
		super(PropertyHelper.getTemplateDir());
		this.templateFile = templateFile;
	}
	
	public ScriptGenerator(String templateDir, String templateFile) {
		super(templateDir);
		this.templateFile = templateFile;
	}
	
	/**
	 * 
	 * @param bean
	 * @return
	 */
	public boolean generateJavaFile(ScriptForJavaBean bean){
		File targetFile = getJavaFile(bean.getPackageName(), bean.getClassName());
		try {
			if(targetFile.exists()){
//				String msg = targetFile.getAbsolutePath() + "has exist, would you want to cover it?";
//				int option = JOptionPane.showConfirmDialog(null, msg, "ConfirmDialog", JOptionPane.YES_NO_OPTION);
//				if(option != JOptionPane.YES_OPTION) return true;
				if(!PropertyHelper.getScriptOverwrite())
					return true;
			} 
			if(null == templateFile || templateFile.trim().length() == 0){
				ViewModules.showMessageDialog(null, "Template file can not be null.");
				return false;
			} else {
				setTemplate(templateFile);
			}
			generateFile(createDataModel(bean), targetFile);
			return true;
		} catch (TemplateException | IOException e) {
			LogUtil.err(cl, e);
			return false;
		}
	}

	@Override
	public Map<String, Object> createDataModel(ScriptForJavaBean bean) {
//		ScriptForJavaBean scriptEntity = new ScriptForJavaBean();
//		scriptEntity.setPackageName(packageName); // 创建包名
//		scriptEntity.setAuthor(PropertyHelper.getAuthor());
//		scriptEntity.setClassDesc(classDesc);
//		scriptEntity.setClassName(className);  // 创建类名
//		scriptEntity.setSuperclass(PropertyHelper.getSuperClass());
//		scriptEntity.setSmokeScript(isSmoke);
//		scriptEntity.setMethodName(methodName);
//		scriptEntity.setParamNames(params);
		
		// 创建.java类文件
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("entity", bean);
		return root;
	}

	
	
}
