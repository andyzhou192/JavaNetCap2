package com.view.preference.component;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.common.Constants;
import com.view.preference.AbstractPreferencesView;
import com.view.util.ViewModules;

public class WorkspaceSettingView extends AbstractPreferencesView {

	private static final long serialVersionUID = 1L;
	private JTextField workSpaceTextField;
	private JButton browseButton, applyButton;
	private JLabel workSpaceLabel;
	
	public WorkspaceSettingView() {
		super(10, 10);
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
		applyButton = ViewModules.createButton("Apply", "SaveWorkSpaceSetting", this);
	}
	
	/**
	 * 初始化界面数据
	 */
	public void layoutComponents(){
		this.add(workSpaceLabel, ViewModules.getGridBagConstraints(1, 1, 1, 1));
		this.add(workSpaceTextField, ViewModules.getGridBagConstraints(2, 1, 8, 1));
		this.add(browseButton, ViewModules.getGridBagConstraints(10, 1, 1, 1));
		
		this.add(applyButton, ViewModules.getGridBagConstraints(10, 12, 1, 1));
	}
	
	/**
	 * 初始化界面数据
	 */
	public void initData(){
		String workSpaceValue = Constants.PROPS.getProperty("fileStoreDir");
		workSpaceTextField.setText((null != workSpaceValue) ? workSpaceValue : "");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "SaveWorkSpaceSetting":
			saveSettings();
			break;
		case "Browse":
			JFileChooser chooser = new JFileChooser();             //设置选择器
			//chooser.setMultiSelectionEnabled(true);             //设为多选
			chooser.setDialogTitle("Please choose workspace");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = chooser.showOpenDialog(browseButton);        //是否打开文件选择框
			if (returnVal == JFileChooser.APPROVE_OPTION) {          //如果符合文件类型
				String filepath = chooser.getSelectedFile().getAbsolutePath();      //获取绝对路径
				workSpaceTextField.setText(filepath);
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 保存配置
	 */
	public void saveSettings(){
		// 必填：生成的脚本文件包名，如：com.cmcc
		boolean isSucc_01 = storeProperty("fileStoreDir", workSpaceTextField.getText());
		Constants.initProperties(Constants.DEF_SET_PROP_FILE);
		boolean isSucc = isSucc_01;
		ViewModules.showMessageDialog(null, "Properties saved : " + isSucc);
	}
	
}
