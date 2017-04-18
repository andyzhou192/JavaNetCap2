package com.view.preference.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.common.Constants;
import com.common.util.StringUtil;
import com.generator.maven.MavenPomHelper;
import com.generator.maven.ProjectInfo;
import com.view.preference.AbstractPreferencesView;
import com.view.preference.PreferenceFrame;
import com.view.preference.PropertyHelper;
import com.view.util.ViewModules;

public class WorkspaceSettingView extends AbstractPreferencesView {

	private static final long serialVersionUID = 1L;
	private PreferenceFrame parent;
	private JLabel workSpaceLabel;
	private JTextField workSpaceTextField;
	private JButton browseButton, applyButton;
	
	private JLabel groupIdLabel, projectNameLabel, sourceDirLabel, testSourceDirLabel, resourceDirLabel, testResourceDirLabel;
	private JTextField groupIdField, projectNameField, sourceDirField, testSourceDirField, resourceDirField, testResourceDirField;
	
	public WorkspaceSettingView(PreferenceFrame parent) {
		super(10, 10);
		this.parent = parent;
		defineComponents();
		layoutComponents();
		initData();
	}
	
	/**
	 * 定义组件
	 */
	public void defineComponents(){
		workSpaceLabel = ViewModules.createJLabel("WorkSpace : ", Color.RED);
		workSpaceTextField = ViewModules.createTextField(20, "", true);
		browseButton = ViewModules.createButton("browse", "Browse", this);
		
		groupIdLabel = ViewModules.createJLabel("Group Id : ", Color.BLUE);
		groupIdField = ViewModules.createTextField(20, "", true);
		
		projectNameLabel = ViewModules.createJLabel("Project Name : ", Color.BLUE);
		projectNameField = ViewModules.createTextField(20, "", true);
		
		sourceDirLabel = ViewModules.createJLabel("Souece Dir : ", Color.BLUE);
		sourceDirField = ViewModules.createTextField(20, "", true);
	
		testSourceDirLabel = ViewModules.createJLabel("Test Source Dir : ", Color.BLUE);
		testSourceDirField = ViewModules.createTextField(20, "", true);
		
		resourceDirLabel = ViewModules.createJLabel("Resouece Dir : ", Color.BLUE);
		resourceDirField = ViewModules.createTextField(20, "", true);
		
		testResourceDirLabel = ViewModules.createJLabel("Test Resource Dir : ", Color.BLUE);
		testResourceDirField = ViewModules.createTextField(20, "", true);
		
		applyButton = ViewModules.createButton("Apply", "SaveWorkSpaceSetting", this);
	}
	
	/**
	 * 初始化界面数据
	 */
	public void layoutComponents(){
		this.add(workSpaceLabel, ViewModules.getGridBagConstraints(1, 1, 1, 1));
		this.add(workSpaceTextField, ViewModules.getGridBagConstraints(2, 1, 8, 1));
		this.add(browseButton, ViewModules.getGridBagConstraints(10, 1, 1, 1));
		
		// The values of the following fields are obtained by pom.xml parsing and are not stored in the property file
		this.add(groupIdLabel, ViewModules.getGridBagConstraints(1, 3, 1, 1));
		this.add(groupIdField, ViewModules.getGridBagConstraints(2, 3, 4, 1));
		this.add(projectNameLabel, ViewModules.getGridBagConstraints(6, 3, 1, 1));
		this.add(projectNameField, ViewModules.getGridBagConstraints(7, 3, 4, 1));
				        
		this.add(sourceDirLabel, ViewModules.getGridBagConstraints(1, 4, 1, 1));
		this.add(sourceDirField, ViewModules.getGridBagConstraints(2, 4, 4, 1));
		this.add(resourceDirLabel, ViewModules.getGridBagConstraints(6, 4, 1, 1));
		this.add(resourceDirField, ViewModules.getGridBagConstraints(7, 4, 4, 1));
				    
		this.add(testSourceDirLabel, ViewModules.getGridBagConstraints(1, 5, 1, 1));
		this.add(testSourceDirField, ViewModules.getGridBagConstraints(2, 5, 4, 1));
		this.add(testResourceDirLabel, ViewModules.getGridBagConstraints(6, 5, 1, 1));
		this.add(testResourceDirField, ViewModules.getGridBagConstraints(7, 5, 4, 1));
		
		this.add(applyButton, ViewModules.getGridBagConstraints(10, 12, 1, 1));
	}
	
	/**
	 * 初始化界面数据
	 */
	public void initData(){
		parent.progress.startProgress("Initialization in progress...");
		workSpaceTextField.setText(PropertyHelper.getWorkspace());
		workSpaceTextField.setEnabled(false);
		initProjectInfo(PropertyHelper.getWorkspace());
		parent.progress.stopProgress("Initialization completed!");
	}
	
	private void initProjectInfo(String projectPath){
		if(MavenPomHelper.hasPomXml(projectPath)){
			if(null == Constants.PROJECT_INFO)
				MavenPomHelper.parsePomXml(projectPath);
		} else {
			Constants.PROJECT_INFO = new ProjectInfo();
		}
		String groupId = Constants.PROJECT_INFO.getGroupId();
		String projectName = Constants.PROJECT_INFO.getProjectName();
		String sourceDir = Constants.PROJECT_INFO.getSourceDir();
		String testSourceDir = Constants.PROJECT_INFO.getTestResourceDir();
		String resourceDir = Constants.PROJECT_INFO.getResourceDir();
		String testResourceDir = Constants.PROJECT_INFO.getTestResourceDir();
		groupIdField.setText(groupId);
		projectNameField.setText(projectName);
		sourceDirField.setText(sourceDir);
		testSourceDirField.setText(testSourceDir);
		resourceDirField.setText(resourceDir);
		testResourceDirField.setText(testResourceDir);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "SaveWorkSpaceSetting":
			parent.progress.startProgress("Save data...");
			boolean isSucc = saveSettings();
			parent.progress.stopProgress("Data save status : " + (isSucc ? "Success." : "Failed."));
			break;
		case "Browse":
			parent.progress.startProgress("Open workspace...");
			String filepath = chooseSingleDir(browseButton);
			if(null != filepath){
				workSpaceTextField.setText(filepath);
				initProjectInfo(filepath);
			}
			parent.progress.stopProgress("Open workspace...");
			break;
		default:
			break;
		}
	}
	
	private String chooseSingleDir(Component parent){
		String filepath = null;
		JFileChooser chooser = new JFileChooser();             //设置选择器
//		File defaultDir = FileSystemView.getFileSystemView().getHomeDirectory(); // 获取桌面路径
		File defaultDir = new File(Constants.DEFAULT_PROJECT_DIR);
		if(StringUtil.validate(workSpaceTextField.getText())){
			defaultDir = new File(workSpaceTextField.getText());
			if(!defaultDir.exists())
				defaultDir = new File(Constants.DEFAULT_PROJECT_DIR);
		}
		chooser.setCurrentDirectory(defaultDir); // 设置默认路径
		//chooser.setMultiSelectionEnabled(true);             //设为多选
		chooser.setDialogTitle("Please choose workspace");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(parent);        //是否打开文件选择框
		if (returnVal == JFileChooser.APPROVE_OPTION) {          //如果符合文件类型
			filepath = chooser.getSelectedFile().getAbsolutePath();      //获取绝对路径
		}
		return filepath;
	}
	
	/**
	 * 保存配置
	 */
	public boolean saveSettings(){
		// 必填：生成的脚本文件包名，如：com.cmcc
		String groupId = groupIdField.getText();
		String projectName = projectNameField.getText();
		String sourceDir = sourceDirField.getText();
		String testSourceDir = testSourceDirField.getText();
		String resourceDir = resourceDirField.getText();
		String testResourceDir = testResourceDirField.getText();
		
		Constants.PROJECT_INFO = new ProjectInfo(groupId, projectName, sourceDir, testSourceDir, resourceDir, testResourceDir);
		PropertyHelper.setWorkspace(workSpaceTextField.getText());
		boolean isSucc = PropertyHelper.storeProperties();
		
		String projectPath = MavenPomHelper.initMavenProject();
		workSpaceTextField.setText(projectPath);
		//ViewModules.showMessageDialog(parent, "Properties saved : " + isSucc);
		return isSucc;
	}
	
}
