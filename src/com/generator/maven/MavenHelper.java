package com.generator.maven;

import java.io.File;

import org.w3c.dom.Element;
import com.common.Constants;
import com.common.helper.CommandHelper;
import com.common.helper.XMLHelper;
import com.common.util.FileUtil;

public class MavenHelper extends XMLHelper {
	
	public static String DefaultGroupId = "com.test";
	public static String DefaultProjectName = "MyTest";
	public static String DefaultSourceDir = "src/main/java";
	public static String DefaultTestSourceDir = "src/test/java";
	public static String DefaultResourceDir = "src/main/resources";
	public static String DefaultTestResourceDir = "src/test/resources";

	public MavenHelper(String sourceDir) {
		File pomFile = new File(sourceDir, "pom.xml");
		if(pomFile.exists()){
			initDocument(pomFile);
		}
	}
	
	public MavenHelper(File sourceFile) {
		if(sourceFile.exists()){
			initDocument(sourceFile);
		}
	}

	public void appendDependency(String groupId, String artifactId, String version, String classifier, String scope, String systemPath) {
		Element dependencyEle = createElementWithoutAttribute("dependency", null);
		Element groupIdEle = createElementWithoutAttribute("groupId", groupId);
		Element artifactIdEle = createElementWithoutAttribute("artifactId", artifactId);
		Element versionEle = createElementWithoutAttribute("version", version);
		appendChildern(dependencyEle, groupIdEle, artifactIdEle, versionEle);
		if(null != classifier){
			Element classifierEle = createElementWithoutAttribute("classifier", classifier);
			dependencyEle.appendChild(classifierEle);
		}
		if(null != scope){
			Element scopeEle = createElementWithoutAttribute("scope", scope);
			dependencyEle.appendChild(scopeEle);
		}
		if(null != systemPath){
			Element systemPathEle = createElementWithoutAttribute("systemPath", systemPath);
			dependencyEle.appendChild(systemPathEle);
		}
		appendChild("/project/dependencies", dependencyEle);
	}

	public static String createMavenProject(String path, String groupId, String projectName){
		String disk = path.substring(0, path.indexOf(":"));
		String paths = path.substring(path.indexOf(":") + 1);
		String cmd = "exts/create_maven_project.bat " + disk + " " + paths + " " + groupId + " " + projectName;
		boolean isSucc = CommandHelper.exec(cmd);
		String projectPath = path + "/" + projectName;
		if(isSucc){
			File tempfile = new File("template/template_pom.xml");
			MavenHelper temp = new MavenHelper(tempfile);
			File targetFile = new File(projectPath, "pom.xml");
			if(targetFile.exists())
				targetFile.delete();
			temp.updateTextContent("/project/groupId", groupId);
			temp.updateTextContent("/project/artifactId", projectName);
			temp.updateTextContent("/project/name", projectName);
			temp.saveAsXml(path + "/" + projectName + "/pom.xml");
			Constants.PROJECT_INFO = new ProjectInfo(temp);
			FileUtil.mkdirs(projectPath, DefaultSourceDir, DefaultTestSourceDir, DefaultResourceDir, DefaultTestResourceDir, "libs");
			FileUtil.copyFile("exts/server-interface-ats-1.0.0.jar", projectPath + "/libs/server-interface-ats-1.0.0.jar", false);
		} else {
			projectPath = path;
		}
		return projectPath;
	}
	
	public static boolean isMavenProject(String dir){
		File file = new File(dir, "pom.xml");
		if(file.exists()){
			return true;
		}
		return false;
	}
	
}
