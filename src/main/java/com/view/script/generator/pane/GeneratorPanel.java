package com.view.script.generator.pane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;

import com.generator.AbstractGenerator;
import com.generator.bean.DataForJavaBean;
import com.generator.bean.ScriptForJavaBean;
import com.generator.java.ScriptGenerator;
import com.generator.maven.MavenPomHelper;
import com.handler.DataSaveHandler;
import com.protocol.http.HttptHelper;
import com.view.preference.PropertyHelper;
import com.view.util.BaseFrame;
import com.view.util.ScrollPaneTextArea;
import com.view.util.ViewModules;
import com.common.Constants;
import com.common.util.FormatUtil;
import com.common.util.JsonUtil;
import com.common.util.StringUtil;

@SuppressWarnings("serial")
public class GeneratorPanel extends JPanel implements ActionListener {
	
	private JLabel packageNameLabel, classNameLabel, classDescLabel, testMethodNameLabel, testMethodDescLabel, urlLabel, httpMethodLabel, testCaseDescLabel, reqHeaderLabel, reqParamsLabel, statusCodeLabel, reasonPhraseLabel, rspHeaderLabel, rspBodyLabel;
	private JTextField packageNameField, classNameField, methodNameField, urlField, httpMethodField, statusCodeField, reasonPhraseField;
	private ScrollPaneTextArea classDescArea, testMethodDescArea, testCaseDescArea, rspBodyArea;
	private ParameterTablePanel reqParamsArea, reqHeaderArea, rspHeaderArea;
	private JCheckBox smokeScriptCheckBox;
	private JComboBox<Object> typeComboBox;
	
	private String[] types = {"TEXT", "JSON", "HTML"};
	
	private BaseFrame parent;
	private DataForJavaBean dataBean;
	
	public GeneratorPanel(BaseFrame parent, DataForJavaBean dataBean) {
		this.parent = parent;
		this.setBorder(new LineBorder(new Color(255, 200, 0), 2));
		//this.setLayout(ViewModules.getGridBagLayout(15, 10, 5, 5, 1.0, 1.0));
		this.dataBean = dataBean;
		
		defineComponents();
		layoutComponents();
		initData();
	}
	
	private JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBackground(new Color(216,218,254));
		JButton geneScriptBtn = ViewModules.createButton("GeneScript", "GENEJAVA", Constants.GENE_SCRIPT_ICON, this);
		toolBar.add(geneScriptBtn);
		return toolBar;
	}
	
	public void defineComponents() {
		packageNameLabel = ViewModules.createJLabel("Test Package Name:", Color.BLACK);
		classNameLabel = ViewModules.createJLabel("Test Class Name:", Color.BLACK);
		classDescLabel = ViewModules.createJLabel("Test Class Desc:", Color.BLACK);
		testMethodNameLabel = ViewModules.createJLabel("Test Method Name:", Color.BLACK);
		testMethodDescLabel = ViewModules.createJLabel("Test Method Desc:", Color.BLACK);
		urlLabel = ViewModules.createJLabel("URL:", Color.BLACK);
		httpMethodLabel = ViewModules.createJLabel("Method:", Color.BLACK);
		testCaseDescLabel = ViewModules.createJLabel("Test Case Desc:", Color.BLACK);
		reqHeaderLabel = ViewModules.createJLabel("Request Header:", Color.BLACK);
		reqParamsLabel = ViewModules.createJLabel("Request Params:", Color.BLACK);
		statusCodeLabel = ViewModules.createJLabel("Status Code:", Color.BLACK);
		reasonPhraseLabel = ViewModules.createJLabel("Reason Phrase:", Color.BLACK);
		rspHeaderLabel = ViewModules.createJLabel("Response Header:", Color.BLACK);
		rspBodyLabel = ViewModules.createJLabel("Response Body:", Color.BLACK);
		
		packageNameField = ViewModules.createTextField(20, "", true);
		classNameField = ViewModules.createTextField(20, "", true);
		methodNameField = ViewModules.createTextField(20, "", true);
		urlField = ViewModules.createTextField(20, "", true);
		httpMethodField = ViewModules.createTextField(20, "", true);
		statusCodeField = ViewModules.createTextField(20, "", true);
		reasonPhraseField = ViewModules.createTextField(20, "", true);
		
		reqParamsArea = new ParameterTablePanel(parent, "");
		classDescArea = new ScrollPaneTextArea(2, "");
		testMethodDescArea = new ScrollPaneTextArea(2, "");
		testCaseDescArea = new ScrollPaneTextArea(2, "");
		reqHeaderArea = new ParameterTablePanel(parent, "");
		rspHeaderArea = new ParameterTablePanel(parent, "");
		rspBodyArea = new ScrollPaneTextArea(10, "");
		
		smokeScriptCheckBox = ViewModules.createCheckBox("is Smoke Case", null);
		typeComboBox = ViewModules.createComboBox(types);
		typeComboBox.setBounds(0, 0, 100, 25);
	}
	
	public void layoutComponents() {
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.LEFT);// 设置标签置放位置。
		// script base info
		JPanel scriptInfoPanel = ViewModules.createPanel("Base Info");
		scriptInfoPanel.setLayout(ViewModules.getGridBagLayout(9, 10, 5, 5, 1.0, 1.0));
		
		scriptInfoPanel.add(packageNameLabel, ViewModules.getGridBagConstraints(1, 1, 1, 1));
		scriptInfoPanel.add(packageNameField, ViewModules.getGridBagConstraints(2, 1, 9, 1));
		
		scriptInfoPanel.add(classNameLabel, ViewModules.getGridBagConstraints(1, 2, 1, 1));
		scriptInfoPanel.add(classNameField, ViewModules.getGridBagConstraints(2, 2, 9, 1));
		
		scriptInfoPanel.add(classDescLabel, ViewModules.getGridBagConstraints(1, 3, 1, 1));
		scriptInfoPanel.add(classDescArea, ViewModules.getGridBagConstraints(1, 4, 10, 1));
		
		scriptInfoPanel.add(testMethodNameLabel, ViewModules.getGridBagConstraints(1, 5, 1, 1));
		scriptInfoPanel.add(methodNameField, ViewModules.getGridBagConstraints(2, 5, 8, 1));
		scriptInfoPanel.add(smokeScriptCheckBox, ViewModules.getGridBagConstraints(10, 5, 1, 1));
		
		scriptInfoPanel.add(testMethodDescLabel, ViewModules.getGridBagConstraints(1, 6, 1, 1));
		scriptInfoPanel.add(testMethodDescArea, ViewModules.getGridBagConstraints(1, 7, 10, 1));
		
		scriptInfoPanel.add(testCaseDescLabel, ViewModules.getGridBagConstraints(1, 8, 1, 1));
		scriptInfoPanel.add(testCaseDescArea, ViewModules.getGridBagConstraints(1, 9, 10, 1));
		
		//this.add(scriptInfoPanel, ViewModules.getGridBagConstraints(1, 1, 10, 9));
		tabbedPane.add("Script Base Info", new JScrollPane(scriptInfoPanel));
		// request info
		JPanel requestInfoPanel = ViewModules.createPanel("Request Info");
		requestInfoPanel.setLayout(ViewModules.getGridBagLayout(13, 10, 5, 5, 1.0, 1.0));
		
		requestInfoPanel.add(urlLabel, ViewModules.getGridBagConstraints(1, 1, 1, 1));
		requestInfoPanel.add(urlField, ViewModules.getGridBagConstraints(2, 1, 7, 1));
		
		requestInfoPanel.add(httpMethodLabel, ViewModules.getGridBagConstraints(9, 1, 1, 1));
		requestInfoPanel.add(httpMethodField, ViewModules.getGridBagConstraints(10, 1, 1, 1));
		
		requestInfoPanel.add(reqParamsLabel, ViewModules.getGridBagConstraints(1, 2, 1, 1));
		requestInfoPanel.add(reqParamsArea, ViewModules.getGridBagConstraints(1, 3, 10, 5));
		
		requestInfoPanel.add(reqHeaderLabel, ViewModules.getGridBagConstraints(1, 8, 1, 1));
		requestInfoPanel.add(reqHeaderArea, ViewModules.getGridBagConstraints(1, 9, 10, 5));
		
		//this.add(requestInfoPanel, ViewModules.getGridBagConstraints(1, 10, 10, 13));
		tabbedPane.add("Request Info", new JScrollPane(requestInfoPanel));
		// response info
		JPanel responseInfoPanel = ViewModules.createPanel("Response Info");
		responseInfoPanel.setLayout(ViewModules.getGridBagLayout(13, 10, 5, 5, 1.0, 1.0));
		
		responseInfoPanel.add(statusCodeLabel, ViewModules.getGridBagConstraints(1, 1, 1, 1));
		responseInfoPanel.add(statusCodeField, ViewModules.getGridBagConstraints(2, 1, 1, 1));
		
		responseInfoPanel.add(reasonPhraseLabel, ViewModules.getGridBagConstraints(3, 1, 1, 1));
		responseInfoPanel.add(reasonPhraseField, ViewModules.getGridBagConstraints(4, 1, 7, 1));
		
		responseInfoPanel.add(rspHeaderLabel, ViewModules.getGridBagConstraints(1, 2, 1, 1));
		responseInfoPanel.add(rspHeaderArea, ViewModules.getGridBagConstraints(1, 3, 10, 5));
		
		responseInfoPanel.add(rspBodyLabel, ViewModules.getGridBagConstraints(1, 8, 1, 1));
		responseInfoPanel.add(typeComboBox, ViewModules.getGridBagConstraints(2, 8, 1, 1, GridBagConstraints.HORIZONTAL));
		responseInfoPanel.add(rspBodyArea, ViewModules.getGridBagConstraints(1, 9, 10, 5));
		
		//this.add(responseInfoPanel, ViewModules.getGridBagConstraints(1, 24, 10, 13));
		tabbedPane.add("Response Info", new JScrollPane(responseInfoPanel));
		
		this.add(createToolBar(), BorderLayout.NORTH);
		this.add(tabbedPane, BorderLayout.CENTER);
	}

	public void initData() {
		packageNameField.setText(PropertyHelper.getPackageName());
		if(StringUtil.validate(dataBean.getUrl())){
			String methodName = HttptHelper.getInterfaceMethodName(dataBean.getUrl());
			methodName = methodName.toUpperCase().substring(0, 1) + methodName.substring(1);
			methodNameField.setText("test" + methodName);
			if(null == methodName || methodName.trim().length() < 1){
				classNameField.setText(PropertyHelper.getClassName());
			} else {
				String className = methodName + "Test";
				classNameField.setText(className);
			}
		}
		urlField.setText(dataBean.getUrl());
		httpMethodField.setText(dataBean.getMethod());
		reqParamsArea.setTableValues(dataBean.getReqParams());
		statusCodeField.setText(StringUtil.toString(dataBean.getStatusCode()));
		reasonPhraseField.setText(dataBean.getReasonPhrase());
		
		classDescArea.setText("test class description");
		testMethodDescArea.setText("test method description");
		testCaseDescArea.setText("case description");
		reqHeaderArea.setTableValues(dataBean.getReqHeader());
		rspHeaderArea.setTableValues(dataBean.getRspHeader());
		rspBodyArea.setText(StringUtil.toString(dataBean.getRspBody()));
		
		smokeScriptCheckBox.setSelected(PropertyHelper.getSmokeScript());
		
		typeComboBox.setSelectedIndex(0);
		typeComboBox.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				switch(e.getItem().toString()){
				case "JSON":
					rspBodyArea.setText(FormatUtil.formatJson(rspBodyArea.getText()));
					break;
				case "HTML":
					String text = rspBodyArea.getText();
					try {
						text = FormatUtil.formatHtml(text);
					} catch (Exception e1) {
						
					}
					rspBodyArea.setText(text);
					break;
				case "TEXT":
					rspBodyArea.setText(FormatUtil.formatText(rspBodyArea.getText()));
					break;
				default:
					break;
				}
			}
		});
	}
	
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "GENEJAVA":
			parent.progress.startProgress("Generate script...");
			MavenPomHelper.initMavenProject();
			boolean isSucc = createJavaFile();
			boolean isOK = createExcelFile();
			if(!isSucc){
				ViewModules.showMessageDialog(this, "Generate Script Failed.");
			} else if(!isOK){
				ViewModules.showMessageDialog(this, "Generate Data File Failed.");
			} else {
				ViewModules.showMessageDialog(this, "Generate Script and Data File Success.");
			}
			parent.progress.stopProgress("Script has generated!");
			break;
		default:
			break;
		}
	}

	public boolean createJavaFile(){
		ScriptForJavaBean bean = new ScriptForJavaBean();
		bean.setPackageName(packageNameField.getText());
		bean.setAuthor(PropertyHelper.getAuthor());
		bean.setClassDesc(classDescArea.getText());
		bean.setClassName(classNameField.getText().trim());
		bean.setSuperclass(PropertyHelper.getSuperClass());
		bean.setMethodName(methodNameField.getText().trim());
		bean.setSmokeScript(smokeScriptCheckBox.isSelected());
		
		ScriptGenerator generator = new ScriptGenerator(PropertyHelper.getTemplateDir(), PropertyHelper.getTemplateFile());
		return generator.generateJavaFile(bean);
	}

	private boolean createExcelFile() {
		DataForJavaBean dataBean = new DataForJavaBean();
		dataBean.setCaseDesc(testCaseDescArea.getText());
		dataBean.setUrl(urlField.getText());
		dataBean.setMethod(httpMethodField.getText());
		dataBean.setReqHeader(JsonUtil.getJson(reqHeaderArea.getDataOfJsonString()));
		dataBean.setReqParams(JsonUtil.getJson(reqParamsArea.getDataOfJsonString()));
		dataBean.setStatusCode(Integer.valueOf(statusCodeField.getText()));
		dataBean.setReasonPhrase(reasonPhraseField.getText());
		dataBean.setRspHeader(JsonUtil.getJson(rspHeaderArea.getDataOfJsonString()));
		dataBean.setRspBody(rspBodyArea.getText());
		
		String file = AbstractGenerator.getDataFilePath(packageNameField.getText(), classNameField.getText());
		String sheetName = methodNameField.getText().trim();
		DataSaveHandler.appendToExcel(file, sheetName, dataBean);
		return true;
	}
	
}
