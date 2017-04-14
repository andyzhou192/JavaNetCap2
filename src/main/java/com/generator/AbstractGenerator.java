package com.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import com.common.Constants;
import com.common.util.StringUtil;
import com.view.preference.PropertyHelper;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.Version;

/**
 * 
 * @author zhouyelin
 *
 */
public abstract class AbstractGenerator {
	
	public String Java_File_Path = "src/test/java";
	public String Data_File_Path = "src/test/resources";
	
	private Configuration cfg;
	private Template template;
	
	/**
	 * 
	 * @param templateDir 模板文件存放根目录
	 * @throws IOException
	 */
	public AbstractGenerator(String templateDir) throws IOException{
		this.cfg = createConf(templateDir);
	}
	
	public AbstractGenerator() throws IOException{
	}

	/**
	 * 步骤一：指定 模板文件从何处加载的数据源，这里设置一个文件目录
	 * @param templateDir 模板文件存放目录
	 * @return
	 * @throws IOException
	 */
	public Configuration createConf(String templateDir) throws IOException {
		Version version = Configuration.VERSION_2_3_23;
		Configuration cfg = new Configuration(version);	
		cfg.setDirectoryForTemplateLoading(new File(templateDir));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
		cfg.setObjectWrapper(new DefaultObjectWrapper(version));
		return cfg;
	}
	
	public void setConfiguration(Configuration cfg){
		this.cfg = cfg;
	}
	
	/**
	 * 步骤二：获取 模板文件
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws MalformedTemplateNameException 
	 * @throws TemplateNotFoundException 
	 */
	public Template setTemplate(String templateName) throws IOException{
		return this.template = cfg.getTemplate(templateName);
	}
	
	/**
	 * 步骤三：创建 数据模型
	 * @return
	 */
	public abstract Map<String, Object> createDataModel(String packageName, String className, String classDesc, boolean isSmoke,
			String scriptEntity, String[] params);
	
	/**
	 * 步骤四：合并 模板 和 数据模型, 创建.java类文件
	 * @param template 模板
	 * @param dataModel  数据模型
	 * @param targetFile  带生成的目标文件
	 * @throws TemplateException
	 * @throws IOException
	 */
	public void generateFile( Map<String, Object> dataModel, File targetFile) throws TemplateException, IOException{
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile),"UTF-8"));
		//Writer out = new FileWriter(targetFile);
		this.template.process(dataModel, out);
		out.flush();
		out.close();
	}

	public static String getDataFilePath(String packageName, String fileName){
		String outDir = new File(PropertyHelper.getWorkspace(), Constants.PROJECT_INFO.getTestResourceDir()).getAbsolutePath();
		String packagePath = (StringUtil.validate(packageName) ? packageName : Constants.PROJECT_INFO.getGroupId()).replace('.', '/');
		String fileDir = new File(outDir, packagePath).getAbsolutePath();
		fileName = (StringUtil.validate(fileName) ? fileName : (PropertyHelper.getClassName() + "Test"));
		String file = new File(fileDir, fileName + ".xls").getAbsolutePath();
//		if(!FileUtil.fileIsExists(file))
//			FileUtil.createFile(fileName, ".xls", fileDir);
		return file;
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
			packageName = PropertyHelper.getPackageName(); 
		} 
		if(null == className || className.trim().length() < 1){
			className = PropertyHelper.getClassName();
		}
		String filePath = PropertyHelper.getWorkspace();
		File outDirFile = new File(filePath, Constants.PROJECT_INFO.getTestSourceDir());
		if(!outDirFile.exists()){
			outDirFile.mkdir();
		}
        String packageSubPath = packageName.replace('.', '/');
        File packagePath = new File(outDirFile, packageSubPath);
        if(!packagePath.exists()){
        	packagePath.mkdirs();
        }
        File file = new File(packagePath, className + ".java");
        return file;
    }
	
}