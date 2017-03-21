package com.view.detail;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.generator.java.ScriptGenerator;
import com.handler.DataSaveHandler;
import com.protocol.http.HttptHelper;
import com.view.preference.AbstractPreferencesView;
import com.view.util.ViewModules;
import com.common.Constants;
import com.common.asserts.AssertEnum;
import com.common.util.JsonUtil;

import freemarker.template.TemplateException;

@SuppressWarnings("serial")
public class DataDetailView extends AbstractPreferencesView {
	
	private JLabel packageNameLabel, classNameLabel, classDescLabel, methodNameLabel, methodDescLabel, urlLabel, methodLabel, caseDescLabel, reqHeaderLabel, reqParamsLabel, statusCodeLabel, reasonPhraseLabel, rspHeaderLabel, rspBodyLabel;
	private JTextField packageNameField, classNameField, methodNameField, urlField, methodField, reqParamsField, statusCodeField, reasonPhraseField;
	private JTextArea classDescArea, methodDescArea, caseDescArea, reqHeaderArea, rspHeaderArea, rspBodyArea;
	private JCheckBox smokeScriptCheckBox;
	private JButton applyButton;
	
	private String url, method, reqHeader, reqParams, statusCode, reasonPhrase, rspHeader, rspBody;
	private Map<String, String> rspHeadMap;
	
	@SuppressWarnings("unchecked")
	public DataDetailView(Map<String, Object> dataMap) {
		super(20, 10);
		this.url = dataMap.get("url").toString();
		this.method = dataMap.get("method").toString();
		this.reqHeader = dataMap.get("reqHeader").toString();
		this.reqParams = dataMap.get("reqParams").toString();
		this.statusCode = dataMap.get("statusCode").toString();
		this.reasonPhrase = dataMap.get("reasonPhrase").toString();
		this.rspHeader = dataMap.get("rspHeader").toString();
		this.rspBody = dataMap.get("rspBody").toString();
		rspHeadMap = JsonUtil.jsonToMap(this.rspHeader);
		defineComponents();
		layoutComponents();
		initData();
	}
	
	public static void showDialog(JFrame frame, Map<String, Object> map){
		JDialog dialog = new JDialog(frame, "Data Detail");
		dialog.setBackground(Color.LIGHT_GRAY);
		dialog.setTitle("DataDetail");
		dialog.setBounds(100, 100, 900, 600);
		dialog.setVisible(true);
		DataDetailView ddv = new DataDetailView(map);
		JScrollPane jsp = new JScrollPane(ddv, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		dialog.getContentPane().add(jsp);
		dialog.setModal(true);
		dialog.pack();
	}

	@Override
	public void defineComponents() {
		packageNameLabel = ViewModules.createJLabel("Test Package Name:", Color.BLACK);
		classNameLabel = ViewModules.createJLabel("Test Class Name:", Color.BLACK);
		classDescLabel = ViewModules.createJLabel("Test Class Desc:", Color.BLACK);
		methodNameLabel = ViewModules.createJLabel("Test Method Name:", Color.BLACK);
		methodDescLabel = ViewModules.createJLabel("Test Method Desc:", Color.BLACK);
		urlLabel = ViewModules.createJLabel("URL:", Color.BLACK);
		methodLabel = ViewModules.createJLabel("Method:", Color.BLACK);
		caseDescLabel = ViewModules.createJLabel("Test Case Desc:", Color.BLACK);
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
		methodField = ViewModules.createTextField(20, "", true);
		reqParamsField = ViewModules.createTextField(20, "", true);
		statusCodeField = ViewModules.createTextField(20, "", true);
		reasonPhraseField = ViewModules.createTextField(20, "", true);
		
		classDescArea = ViewModules.createTextArea("");
		methodDescArea = ViewModules.createTextArea("");
		caseDescArea = ViewModules.createTextArea("");
		reqHeaderArea = ViewModules.createTextArea("");
		rspHeaderArea = ViewModules.createTextArea("");
		rspBodyArea = ViewModules.createTextArea("");
		
		smokeScriptCheckBox = ViewModules.createCheckBox("is Smoke Case", null);
		
		applyButton = ViewModules.createButton("GeneScript", "GENEJAVA", this);
	}

	@Override
	public void layoutComponents() {
		this.add(packageNameLabel, ViewModules.getGridBagConstraints(1, 1, 1, 1));
		this.add(packageNameField, ViewModules.getGridBagConstraints(2, 1, 9, 1));
		
		this.add(classNameLabel, ViewModules.getGridBagConstraints(1, 2, 1, 1));
		this.add(classNameField, ViewModules.getGridBagConstraints(2, 2, 9, 1));
		
		this.add(classDescLabel, ViewModules.getGridBagConstraints(1, 3, 1, 1));
		this.add(classDescArea, ViewModules.getGridBagConstraints(2, 3, 9, 1));
		
		this.add(methodNameLabel, ViewModules.getGridBagConstraints(1, 4, 1, 1));
		this.add(methodNameField, ViewModules.getGridBagConstraints(2, 4, 8, 1));
		
		this.add(smokeScriptCheckBox, ViewModules.getGridBagConstraints(10, 4, 1, 1));
		
		this.add(methodDescLabel, ViewModules.getGridBagConstraints(1, 5, 1, 1));
		this.add(methodDescArea, ViewModules.getGridBagConstraints(2, 5, 9, 1));
		
		this.add(urlLabel, ViewModules.getGridBagConstraints(1, 6, 1, 1));
		this.add(urlField, ViewModules.getGridBagConstraints(2, 6, 7, 1));
		
		this.add(methodLabel, ViewModules.getGridBagConstraints(9, 6, 1, 1));
		this.add(methodField, ViewModules.getGridBagConstraints(10, 6, 1, 1));
		
		this.add(caseDescLabel, ViewModules.getGridBagConstraints(1, 7, 1, 1));
		this.add(caseDescArea, ViewModules.getGridBagConstraints(2, 7, 9, 1));
		
		this.add(reqHeaderLabel, ViewModules.getGridBagConstraints(1, 8, 1, 1));
		this.add(reqHeaderArea, ViewModules.getGridBagConstraints(2, 8, 9, 3));
		
		this.add(reqParamsLabel, ViewModules.getGridBagConstraints(1, 11, 1, 1));
		this.add(reqParamsField, ViewModules.getGridBagConstraints(2, 11, 9, 1));
		
		this.add(statusCodeLabel, ViewModules.getGridBagConstraints(1, 12, 1, 1));
		this.add(statusCodeField, ViewModules.getGridBagConstraints(2, 12, 1, 1));
		
		this.add(reasonPhraseLabel, ViewModules.getGridBagConstraints(3, 12, 1, 1));
		this.add(reasonPhraseField, ViewModules.getGridBagConstraints(4, 12, 7, 1));
		
		this.add(rspHeaderLabel, ViewModules.getGridBagConstraints(1, 13, 1, 1));
		this.add(rspHeaderArea, ViewModules.getGridBagConstraints(2, 13, 9, 3));
		
		this.add(rspBodyLabel, ViewModules.getGridBagConstraints(1, 16, 1, 1));
		this.add(rspBodyArea, ViewModules.getGridBagConstraints(2, 16, 9, 4));
		
		this.add(applyButton, ViewModules.getGridBagConstraints(10, 20, 1, 1));
	}

	@Override
	public void initData() {
		packageNameField.setText(Constants.PROPS.getProperty("packageName"));
		String methodName = HttptHelper.getInterfaceMethodName(url);
		methodName = methodName.toUpperCase().substring(0, 1) + methodName.substring(1);
		methodNameField.setText("test" + methodName);
		if(null == methodName || methodName.trim().length() < 1){
			classNameField.setText(Constants.PROPS.getProperty("className"));
		} else {
			String className = methodName + "Test";
			classNameField.setText(className);
		}
		urlField.setText(url);
		methodField.setText(method);
		reqParamsField.setText(reqParams);
		statusCodeField.setText(statusCode);
		reasonPhraseField.setText(reasonPhrase);
		
		classDescArea.setText("test class description");
		methodDescArea.setText("test method description");
		caseDescArea.setText("case description");
		reqHeaderArea.setText(reqHeader);
		rspHeaderArea.setText(rspHeader);
		rspBodyArea.setText(rspBody);
		
		String smokeScript = (null == Constants.PROPS.getProperty("smokeScript")) ? "false" : Constants.PROPS.getProperty("smokeScript");
		boolean isChose = Boolean.valueOf((smokeScript.trim().length() > 0) ? smokeScript : "false");
		smokeScriptCheckBox.setSelected(isChose);
	}
	
	@Override
	public void saveSettings() {}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "GENEJAVA":
			boolean isSucc = createScript();
			boolean isOK = createDataFile();
			if(!isSucc){
				ViewModules.showMessageDialog(this, "生成脚本文件失败");
			} else if(!isOK){
				ViewModules.showMessageDialog(this, "生成数据文件失败");
			} else {
				ViewModules.showMessageDialog(this, "脚本及数据文件生成成功");
			}
			break;
		default:
			break;
		}
	}

	public boolean createScript(){
		String packageName = packageNameField.getText();
		String className = classNameField.getText();
		String classDesc = classDescArea.getText();
		boolean isSmoke = smokeScriptCheckBox.isSelected();
		String[] params = HttptHelper.PARAM_NAMES;
		boolean isSucc = false;
		ScriptGenerator generator = null;
		try {
			String templateDir = Constants.PROPS.getProperty("templateDir");
			String templateFile = Constants.PROPS.getProperty("templateFile");
			if(null == templateDir || templateDir.trim().length() == 0){
				ViewModules.showMessageDialog(this, "模板文件存放目录不能为空");
				return isSucc;
			} else {
				generator = new ScriptGenerator(templateDir, packageName, className);
			}
			if(null == templateFile || templateFile.trim().length() == 0){
				ViewModules.showMessageDialog(this, "模板文件不能为空");
				return isSucc;
			} else {
				generator.setTemplate(templateFile);
			}
			Map<String, Object> dataModel = generator.createDataModel(packageName, className, classDesc, isSmoke, params);
			generator.generateFile(dataModel, generator.targetFile);
			isSucc = true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return isSucc;
	}

	private boolean createDataFile() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("CaseID", "case-");
		data.put("CaseDesc", caseDescArea.getText());
		data.put("URL", urlField.getText());
		data.put("Method", methodField.getText());
		data.put("ReqHeader", reqHeaderArea.getText());
		data.put("ReqParams", reqParamsField.getText());
		data.put("StatusCode", statusCodeField.getText());
		data.put("ReasonPhrase", reasonPhraseField.getText());
		data.put("RspHeader", rspHeaderArea.getText());
		data.put("RspBody", rspBodyArea.getText());
		String file = getFilePath(packageNameField.getText(), classNameField.getText());
		String sheetName = methodNameField.getText().trim();
		(new DataSaveHandler(data)).writeToExcel(file, sheetName, HttptHelper.PARAM_NAMES);
		return true;
	}
	
	private String getFilePath(String packageName, String fileName){
		String outDir = Constants.PROPS.getProperty("fileStoreDir").trim();
		outDir = outDir.endsWith(File.separator) ? outDir : (outDir + File.separator);
		packageName = (null != packageName && packageName.trim().length() > 0) ? packageName : Constants.PROPS.getProperty("packageName");
		String packagePath = packageName.replace('.', '/').trim();
		packagePath = packagePath.endsWith(File.separator) ? packagePath : (packagePath + File.separator);
		fileName = (null != fileName && fileName.trim().length() > 0) ? fileName : (Constants.PROPS.getProperty("className") + "Test");
		return outDir + packagePath + fileName + ".xls";
	}
	
//	public static void main(String[] args) {
//		java.util.HashMap<String, Object> map = new java.util.HashMap<String, Object>();
//		map.put("URL", "11111111111");
//		map.put("Method", "22222222222222");
//		map.put("ReqHeader", "3333333333333333");
//		map.put("ReqParams", "44444444444444444");
//		map.put("StatusCode", "55555555555555555555");
//		map.put("ReasonPhrase", "6666666666666666666666");
//		map.put("RspHeader", "777777777777777777777");
//		map.put("RspBody", "8888888888888888888");
//		DataDetailView.showDialog(map);
//	}

}
