package com.generator.maven;

import com.common.util.StringUtil;

public class ProjectInfo {

	private String groupId = MavenPomHelper.Default_Group_Id;
	private String projectName = MavenPomHelper.Default_Project_Name;
	private String sourceDir = MavenPomHelper.Default_Source_Dir;
	private String testSourceDir = MavenPomHelper.Default_Test_Source_Dir;
	private String resourceDir = MavenPomHelper.Default_Resource_Dir;
	private String testResourceDir = MavenPomHelper.Default_Test_Resource_Dir;

	public ProjectInfo() {
	}

	public ProjectInfo(MavenPomHelper helper) {
		this.groupId = helper.getText(MavenPomHelper.GroupId_PATH);
		this.projectName = helper.getText(MavenPomHelper.ArtifactId_PATH);
		this.sourceDir = helper.getText(MavenPomHelper.SOURCE_DIR_PATH);
		this.testSourceDir = helper.getText(MavenPomHelper.TEST_SOURCE_DIR_PATH);
		this.resourceDir = helper.getText(MavenPomHelper.RESOURCE_DIR_PATH);
		this.testResourceDir = helper.getText(MavenPomHelper.TEST_RESOURCE_DIR_PATH);
	}

	public ProjectInfo(String groupId, String projectName, String sourceDir, String testSourceDir, String resourceDir,
			String testResourceDir) {
		this.groupId = groupId;
		this.projectName = projectName;
		this.sourceDir = sourceDir;
		this.testSourceDir = testSourceDir;
		this.resourceDir = resourceDir;
		this.testResourceDir = testResourceDir;
	}

	public String getGroupId() {
		return StringUtil.validate(groupId) ? groupId : MavenPomHelper.Default_Group_Id;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getProjectName() {
		return StringUtil.validate(projectName) ? projectName : MavenPomHelper.Default_Project_Name;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSourceDir() {
		return StringUtil.validate(sourceDir) ? sourceDir : MavenPomHelper.Default_Source_Dir;
	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	public String getTestSourceDir() {
		return StringUtil.validate(testSourceDir) ? testSourceDir : MavenPomHelper.Default_Test_Source_Dir;
	}

	public void setTestSourceDir(String testSourceDir) {
		this.testSourceDir = testSourceDir;
	}

	public String getResourceDir() {
		return StringUtil.validate(resourceDir) ? resourceDir : MavenPomHelper.Default_Resource_Dir;
	}

	public void setResourceDir(String resourceDir) {
		this.resourceDir = resourceDir;
	}

	public String getTestResourceDir() {
		return StringUtil.validate(testResourceDir) ? testResourceDir : MavenPomHelper.Default_Test_Resource_Dir;
	}

	public void setTestResourceDir(String testResourceDir) {
		this.testResourceDir = testResourceDir;
	}

}
