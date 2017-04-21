package com.view.script.editor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;

import com.common.Constants;
import com.common.util.FileUtil;
import com.common.util.StringUtil;
import com.view.util.BaseFrame;
import com.view.util.ViewDataHandler;
import com.view.util.ViewModules;

@SuppressWarnings("serial")
public class ScriptBaseInfoPane extends JPanel implements ActionListener {

	private JLabel sourceFileLabel, resourceFileLabel;
	private JTextField sourceFileField, resourceFileField;
	private JButton sourceFileBrowseBtn, resourceFileBrowseBtn;

	private BaseFrame parent;

	public ScriptBaseInfoPane(BaseFrame parent) {
		this.parent = parent;
		this.setBorder(new LineBorder(new Color(255, 200, 0), 2));
		this.setLayout(ViewModules.getGridBagLayout(15, 10, 5, 5, 1.0, 1.0));

		defineComponents();
		layoutComponents();
	}
	
	private JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		JButton reloadBtn = ViewModules.createButton("Reload", "RELOAD", Constants.RELOAD_ICON, this);
		toolBar.add(reloadBtn);
		return toolBar;
	}

	private void defineComponents() {
		sourceFileLabel = ViewModules.createJLabel("Source File : ", Color.BLACK);
		sourceFileField = ViewModules.createTextField(20, "", true);
		sourceFileBrowseBtn = ViewModules.createButton("Browse", "BROWSESOURCE", Constants.BROWSE_ICON, this);
		
		resourceFileLabel = ViewModules.createJLabel("Resource File : ", Color.BLACK);
		resourceFileField = ViewModules.createTextField(20, "", true);
		resourceFileBrowseBtn = ViewModules.createButton("Browse", "BROWSERESOURCE", Constants.BROWSE_ICON, this);
	}

	private void layoutComponents() {
		this.add(sourceFileLabel, ViewModules.getGridBagConstraints(1, 2, 1, 1));
		this.add(sourceFileField, ViewModules.getGridBagConstraints(2, 2, 8, 1));
		this.add(sourceFileBrowseBtn, ViewModules.getGridBagConstraints(10, 2, 1, 1));

		this.add(resourceFileLabel, ViewModules.getGridBagConstraints(1, 3, 1, 1));
		this.add(resourceFileField, ViewModules.getGridBagConstraints(2, 3, 8, 1));
		this.add(resourceFileBrowseBtn, ViewModules.getGridBagConstraints(10, 3, 1, 1));

		this.add(createToolBar(), ViewModules.getGridBagConstraints(10, 1, 1, 1));
	}

	private void initData(String file) {
		if(file.endsWith(".java") && !StringUtil.validate(resourceFileField.getText())){
			String resourceFile = file.replace(".java", ".xlsx").replace("java", "resources");
			if(!FileUtil.fileIsExists(resourceFile))
				resourceFile = resourceFile.replace(".xlsx", ".xls");
			resourceFileField.setText(resourceFile);
		} else if((file.endsWith(".xls") || file.endsWith(".xlsx")) && !StringUtil.validate(sourceFileField.getText())){
			String sourceFile = file.replace(".xlsx", ".java").replace("resources", "java");
			if(!FileUtil.fileIsExists(sourceFile))
				sourceFile = sourceFile.replace(".xls", ".java");
			sourceFileField.setText(sourceFile);
		}
			
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "BROWSESOURCE":
			String sourceFile = ViewDataHandler.openFile(parent);
			if (null != sourceFile) {
				sourceFileField.setText(sourceFile);
				initData(sourceFile);
			}
			break;
		case "BROWSERESOURCE":
			String resourceFile = ViewDataHandler.openFile(parent);
			if (null != resourceFile) {
				resourceFileField.setText(resourceFile);
				initData(resourceFile);
			}
			break;
		case "RELOAD":
			Map<String, String> dataMap = new TreeMap<String, String>();
			dataMap.put("source_file", sourceFileField.getText());
			dataMap.put("resource_file", resourceFileField.getText());
			((ScriptEditFrame)parent).initSourceFile(dataMap);
			break;
		default:
			break;
		}
	}
}
