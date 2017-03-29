package com.generator.maven;

public class ProjectInfo {
	
	private String groupId, projectName, sourceDir, testSourceDir, resourceDir, testResourceDir;

	public ProjectInfo(MavenHelper helper) {
		this.groupId = helper.getTextContent("/project/groupId");
		this.projectName = helper.getTextContent("/project/artifactId");
		this.sourceDir = helper.getTextContent("/project/build/sourceDirectory");
		this.testSourceDir = helper.getTextContent("/project/build/testSourceDirectory");
		this.resourceDir = helper.getTextContent("/project/build/resources/resource/directory");
		this.testResourceDir = helper.getTextContent("/project/build/testResources/testResource/directory");
	}
	
	public ProjectInfo(String groupId, String projectName, String sourceDir, String testSourceDir, String resourceDir, String testResourceDir) {
		this.groupId = groupId;
		this.projectName = projectName;
		this.sourceDir = sourceDir;
		this.testSourceDir = testSourceDir;
		this.resourceDir = resourceDir;
		this.testResourceDir = testResourceDir;
	}
	
	public String getGroupId() {
		return (null != groupId) ? groupId : "";
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getProjectName() {
		return (null != projectName) ? projectName : "";
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSourceDir() {
		if(null != sourceDir)
			return sourceDir;
		return MavenHelper.DefaultSourceDir;
	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	public String getTestSourceDir() {
		if(null != testSourceDir)
			return testSourceDir;
		return MavenHelper.DefaultTestSourceDir;
	}

	public void setTestSourceDir(String testSourceDir) {
		this.testSourceDir = testSourceDir;
	}

	public String getResourceDir() {
		if(null != resourceDir)
			return resourceDir;
		return MavenHelper.DefaultResourceDir;
	}

	public void setResourceDir(String resourceDir) {
		this.resourceDir = resourceDir;
	}

	public String getTestResourceDir() {
		if(null != testResourceDir)
			return testResourceDir;
		return MavenHelper.DefaultTestResourceDir;
	}

	public void setTestResourceDir(String testResourceDir) {
		this.testResourceDir = testResourceDir;
	}

}
