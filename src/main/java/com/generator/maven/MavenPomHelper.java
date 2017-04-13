package com.generator.maven;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import org.dom4j.Element;
import org.dom4j.Node;

import com.common.Constants;
import com.common.helper.CommandHelper;
import com.common.helper.Dom4jXmlHelper;
import com.common.util.FileUtil;
import com.common.util.LogUtil;
import com.common.util.StringUtil;
import com.view.preference.PropertyHelper;

public class MavenPomHelper extends Dom4jXmlHelper {
	
	private static Class<?> cl = MavenPomHelper.class;
	
	public static String POM = "pom.xml";
	
	public static String Default_Group_Id = "com.test";
	public static String Default_Project_Name = "MyTest";
	public static String Default_Source_Dir = "src/main/java";
	public static String Default_Test_Source_Dir = "src/test/java";
	public static String Default_Resource_Dir = "src/main/resources";
	public static String Default_Test_Resource_Dir = "src/test/resources";
	
	public static String GroupId_PATH = "/*[name()='project']/*[name()='groupId']";
	public static String ArtifactId_PATH = "/*[name()='project']/*[name()='artifactId']";
	public static String Version_PATH = "/*[name()='project']/*[name()='version']";
	public static String Name_PATH = "/*[name()='project']/*[name()='name']";
	public static String Url_PATH = "/*[name()='project']/*[name()='url']";
	public static String Props_PATH = "";
	public static String Depends_PATH = "/*[name()='project']/*[name()='dependencies']";
	public static String Build_PATH = "/*[name()='project']/*[name()='build']";
	public static String SOURCE_DIR_PATH = "/*[name()='project']/*[name()='build']/*[name()='sourceDirectory']";
	public static String TEST_SOURCE_DIR_PATH = "/*[name()='project']/*[name()='build']/*[name()='testSourceDirectory']";
	public static String RESOURCE_DIR_PATH = "/*[name()='project']/*[name()='build']/*[name()='resources']/*[name()='resource']/*[name()='directory']";//
	public static String TEST_RESOURCE_DIR_PATH = "/*[name()='project']/*[name()='build']/*[name()='testResources']/*[name()='testResource']/*[name()='directory']";//

	public MavenPomHelper(String sourceDir) {
		super(sourceDir.endsWith(POM) ? sourceDir : StringUtil.assembleRelativeFilePath(sourceDir, POM));
	}
	
	public MavenPomHelper(File xmlFile) {
		super(xmlFile);
	}
	
	/**
	 * 
	 */
	public static String initMavenProject(){
		String projectPath = PropertyHelper.getWorkspace();
		if(!MavenPomHelper.hasPomXml(projectPath)){
			projectPath = MavenPomHelper.createMavenProject(projectPath);
			PropertyHelper.setWorkspace(projectPath);
			PropertyHelper.storeProperties();
		}
		MavenPomHelper.parsePomXml(projectPath);
		return projectPath;
	}
	
	/**
	 * 
	 * @param projectPath
	 */
	public static void parsePomXml(String projectPath){
		if(!StringUtil.validate(projectPath))
			projectPath = Constants.DEFAULT_PROJECT_DIR;
		File file = new File(projectPath, POM);
		LogUtil.debug(cl, file.getAbsolutePath());
		Constants.PROJECT_INFO = new ProjectInfo(new MavenPomHelper(file));
		MavenPomHelper.mergePomXml(file.getAbsolutePath(), Constants.TEMPLATE_POM);
		String targetJar = StringUtil.assembleRelativeFilePath(projectPath, Constants.HTTP_TEST_JAR);
		FileUtil.copyFile(Constants.HTTP_TEST_JAR, targetJar, false);
	}

	/**
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean hasPomXml(String dir){
		File file = new File(dir, POM);
		return (file.exists()) ? true : false;
	}
	
	/**
	 * 
	 */
	public static void mergePomXml(String mainxlm, String subxml){
		MavenPomHelper mainHelper = new MavenPomHelper(mainxlm);
		Element mainDep = (Element) mainHelper.getRootElement().element("dependencies");
		List<Element> mainDeps = mainHelper.getRootElement().element("dependencies").elements();
		
		Dom4jXmlHelper subHelper = new Dom4jXmlHelper(subxml);
		List<Element> subDeps = subHelper.getRootElement().element("dependencies").elements();
		for(int i = 0; i < subDeps.size(); i++){
			String temp_groupId = subDeps.get(i).element("groupId").getTextTrim();
			String temp_artifactId = subDeps.get(i).element("artifactId").getTextTrim();
			String tempKey = temp_groupId + "." + temp_artifactId;
			HashSet<String> set = new HashSet<String>();
			for(int j = 0; j < mainDeps.size(); j++){
				String groupId = mainDeps.get(j).element("groupId").getTextTrim();
				String artifactId = mainDeps.get(j).element("artifactId").getTextTrim();
				set.add(groupId + "." + artifactId);
			}
			if(set.contains(tempKey)){
				continue;
			} else {
				mainHelper.addElement(mainDep, (Node)(subDeps.get(i).clone()));
			}
			mainHelper.save();
		}
	}
	
	/**
	 * 
	 * @param path
	 * @param groupId
	 * @param projectName
	 * @return
	 */
	public static String createMavenProject(String path){
		if(null != Constants.PROJECT_INFO)
			Constants.PROJECT_INFO = new ProjectInfo();
		String groupId = Constants.PROJECT_INFO.getGroupId();
		String projectName = Constants.PROJECT_INFO.getProjectName();
		
		if(!StringUtil.validate(path))
			path = Constants.DEFAULT_PROJECT_DIR;
		String disk = path.substring(0, path.indexOf(":"));
		String paths = path.substring(path.indexOf(":") + 1);
		
		String cmd = StringUtil.assembleStrWithSpace(Constants.MAVEN_BAT, disk, paths, groupId, projectName);
		boolean isSucc = CommandHelper.exec(cmd);
		
		String projectPath = StringUtil.assembleRelativeFilePath(path, projectName);
		if(isSucc){
			MavenPomHelper.parsePomXml(projectPath);
			FileUtil.mkdirs(projectPath, Constants.PROJECT_INFO.getSourceDir(), Constants.PROJECT_INFO.getTestSourceDir(), Constants.PROJECT_INFO.getResourceDir(), Constants.PROJECT_INFO.getTestResourceDir());
		} else {
			projectPath = path;
		}
		return projectPath;
	}
	
//	public static void main(String[] args) {
//		Constants.PROPS = new Properties();
//		PropertyHelper.setWorkspace("D:\\workspace\\JavaNetCap2\\temp-src/Test02");
//		initMavenProject();
//	}
	
}
