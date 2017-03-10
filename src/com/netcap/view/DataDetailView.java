package com.netcap.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.codegenerator.generator.java.ScriptGenerator;
import com.common.util.ConstsUtil;
import com.dataprocess.DataSaveHandler;
import com.netcap.view.util.AbstractSettingView;
import com.netcap.view.util.ViewModules;

import freemarker.template.TemplateException;

@SuppressWarnings("serial")
public class DataDetailView extends AbstractSettingView {
	
	private JLabel urlLabel, methodLabel, reqHeaderLabel, reqParamsLabel, statusCodeLabel, reasonPhraseLabel, rspHeaderLabel, rspBodyLabel;
	private JTextField urlField, methodField, reqParamsField, statusCodeField, reasonPhraseField;
	private JTextArea reqHeaderArea, rspHeaderArea, rspBodyArea;
	private JButton applyButton;
	
	private String url, method, reqHeader, reqParams, statusCode, reasonPhrase, rspHeader, rspBody;
	
	public DataDetailView(Map<String, Object> dataMap) {
		super(15, 10);
		this.url = dataMap.get("URL").toString();
		this.method = dataMap.get("Method").toString();
		this.reqHeader = dataMap.get("ReqHeader").toString();
		this.reqParams = dataMap.get("ReqParams").toString();
		this.statusCode = dataMap.get("StatusCode").toString();
		this.reasonPhrase = dataMap.get("ReasonPhrase").toString();
		this.rspHeader = dataMap.get("RspHeader").toString();
		this.rspBody = dataMap.get("RspBody").toString();
		defineComponents();
		layoutComponents();
		initData();
	}
	
	public static void showDialog(Map<String, Object> map){
		JDialog dialog = new JDialog();
		dialog.setBackground(Color.LIGHT_GRAY);
		dialog.setTitle("DataDetail");
		dialog.setBounds(100, 100, 900, 600);
		dialog.setVisible(true);
		DataDetailView ddv = new DataDetailView(map);
		JScrollPane jsp = new JScrollPane(ddv, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		dialog.getContentPane().add(jsp);
	}

	@Override
	public void defineComponents() {
		urlLabel = ViewModules.createJLabel("URL:", Color.BLACK);
		methodLabel = ViewModules.createJLabel("Method:", Color.BLACK);
		reqHeaderLabel = ViewModules.createJLabel("Request Header:", Color.BLACK);
		reqParamsLabel = ViewModules.createJLabel("Request Params:", Color.BLACK);
		statusCodeLabel = ViewModules.createJLabel("Status Code:", Color.BLACK);
		reasonPhraseLabel = ViewModules.createJLabel("Reason Phrase:", Color.BLACK);
		rspHeaderLabel = ViewModules.createJLabel("Response Header:", Color.BLACK);
		rspBodyLabel = ViewModules.createJLabel("Response Body:", Color.BLACK);
		
		urlField = ViewModules.createTextField(20, "", true);
		methodField = ViewModules.createTextField(20, "", true);
		reqParamsField = ViewModules.createTextField(20, "", true);
		statusCodeField = ViewModules.createTextField(20, "", true);
		reasonPhraseField = ViewModules.createTextField(20, "", true);
		
		reqHeaderArea = ViewModules.createTextArea("");
		rspHeaderArea = ViewModules.createTextArea("");
		rspBodyArea = ViewModules.createTextArea("");
		
		applyButton = ViewModules.createButton("GeneScript", "GENEJAVA", this);
	}

	@Override
	public void layoutComponents() {
		this.add(urlLabel, ViewModules.getGridBagConstraints(1, 1, 1, 1));
		this.add(urlField, ViewModules.getGridBagConstraints(2, 1, 7, 1));
		
		this.add(methodLabel, ViewModules.getGridBagConstraints(9, 1, 1, 1));
		this.add(methodField, ViewModules.getGridBagConstraints(10, 1, 1, 1));
		
		this.add(reqHeaderLabel, ViewModules.getGridBagConstraints(1, 2, 1, 1));
		this.add(reqHeaderArea, ViewModules.getGridBagConstraints(2, 2, 9, 3));
		
		this.add(reqParamsLabel, ViewModules.getGridBagConstraints(1, 5, 1, 1));
		this.add(reqParamsField, ViewModules.getGridBagConstraints(2, 5, 9, 1));
		
		this.add(statusCodeLabel, ViewModules.getGridBagConstraints(1, 6, 1, 1));
		this.add(statusCodeField, ViewModules.getGridBagConstraints(2, 6, 1, 1));
		
		this.add(reasonPhraseLabel, ViewModules.getGridBagConstraints(3, 6, 1, 1));
		this.add(reasonPhraseField, ViewModules.getGridBagConstraints(4, 6, 7, 1));
		
		this.add(rspHeaderLabel, ViewModules.getGridBagConstraints(1, 7, 1, 1));
		this.add(rspHeaderArea, ViewModules.getGridBagConstraints(2, 7, 9, 3));
		
		this.add(rspBodyLabel, ViewModules.getGridBagConstraints(1, 10, 1, 1));
		this.add(rspBodyArea, ViewModules.getGridBagConstraints(2, 10, 9, 4));
		
		this.add(applyButton, ViewModules.getGridBagConstraints(10, 15, 1, 1));
	}

	@Override
	public void initData() {
		urlField.setText(url);
		methodField.setText(method);
		reqParamsField.setText(reqParams);
		statusCodeField.setText(statusCode);
		reasonPhraseField.setText(reasonPhrase);
		reqHeaderArea.setText(reqHeader);
		rspHeaderArea.setText(rspHeader);
		rspBodyArea.setText(rspBody);
	}

	@Override
	public void saveSettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "GENEJAVA":
			createScript();
			createDataFile();
			break;
		default:
			break;
		}
	}

	public void createScript(){
		ScriptGenerator generator = null;
		try {
			String templateDir = ConstsUtil.PROPS.getProperty("templateDir");
			String templateFile = ConstsUtil.PROPS.getProperty("templateFile");
			if(null == templateDir || templateDir.trim().length() == 0){
				ViewModules.showMessageDialog(this, "模板文件存放目录不能为空");
				return;
			} else {
				generator = new ScriptGenerator(ConstsUtil.PROP_FILE, templateDir);
			}
			if(null == templateFile || templateFile.trim().length() == 0){
				ViewModules.showMessageDialog(this, "模板文件不能为空");
				return;
			} else {
				generator.setTemplate(templateFile);
			}
			Map<String, Object> dataModel = generator.createDataModel();
			generator.generateFile(dataModel, generator.targetFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}

	private void createDataFile() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("URL", urlField.getText());
		data.put("Method", methodField.getText());
		data.put("ReqHeader", reqHeaderArea.getText());
		data.put("ReqParams", reqParamsField.getText());
		data.put("StatusCode", statusCodeField.getText());
		data.put("ReasonPhrase", reasonPhraseField.getText());
		data.put("RspHeader", rspHeaderArea.getText());
		data.put("RspBody", rspBodyArea.getText());
		(new DataSaveHandler(data)).writeToExcel();
	}
	
	public static void main(String[] args) {
		java.util.HashMap<String, Object> map = new java.util.HashMap<String, Object>();
		map.put("URL", "11111111111");
		map.put("Method", "22222222222222");
		map.put("ReqHeader", "3333333333333333");
		map.put("ReqParams", "44444444444444444");
		map.put("StatusCode", "55555555555555555555");
		map.put("ReasonPhrase", "6666666666666666666666");
		map.put("RspHeader", "777777777777777777777");
		map.put("RspBody", "8888888888888888888");
		DataDetailView.showDialog(map);
	}

}
