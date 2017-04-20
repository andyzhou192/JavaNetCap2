package com.view.script.editor;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.common.util.JsonUtil;
import com.common.util.StringUtil;
import com.generator.bean.DataForJavaBean;
import com.view.script.generator.pane.ParameterTablePanel;
import com.view.util.BaseFrame;
import com.view.util.ScrollPaneTextArea;
import com.view.util.ViewModules;

@SuppressWarnings("serial")
public class DataEditPane extends JPanel implements ActionListener {

	private JLabel urlLabel, httpMethodLabel, testCaseDescLabel, statusCodeLabel, reasonPhraseLabel;
	private JTextField urlField, httpMethodField, statusCodeField, reasonPhraseField;
	private ScrollPaneTextArea testCaseDescArea, rspBodyArea;
	private ParameterTablePanel reqParamsArea, reqHeaderArea, rspHeaderArea;
	private JComboBox<Object> typeComboBox;
	private JButton saveBtn, deleteBtn;
	
	private JPanel reqParamsPanel, reqHeaderPanel, rspHeaderPanel, rspBodyPanel;
	
	private String[] types = {"TEXT", "JSON", "HTML"};
	
	private BaseFrame parent;
	private DataForJavaBean dataBean;
	
	public DataEditPane(BaseFrame parent, DataForJavaBean dataBean) {
		this.parent = parent;
		this.setBorder(new LineBorder(new Color(255, 200, 0), 2));
		this.setLayout(ViewModules.getGridBagLayout(20, 10, 5, 5, 1.0, 1.0));
		this.dataBean = dataBean;
		
		defineComponents();
		layoutComponents();
		initData();
	}
	
	public void defineComponents() {
		urlLabel = ViewModules.createJLabel("URL:", Color.BLACK);
		httpMethodLabel = ViewModules.createJLabel("Method:", Color.BLACK);
		testCaseDescLabel = ViewModules.createJLabel("Test Case Desc:", Color.BLACK);
		statusCodeLabel = ViewModules.createJLabel("Status Code:", Color.BLACK);
		reasonPhraseLabel = ViewModules.createJLabel("Reason Phrase:", Color.BLACK);
		
		urlField = ViewModules.createTextField(20, "", true);
		httpMethodField = ViewModules.createTextField(20, "", true);
		statusCodeField = ViewModules.createTextField(20, "", true);
		reasonPhraseField = ViewModules.createTextField(20, "", true);
		
		reqParamsArea = new ParameterTablePanel(parent, "");
		testCaseDescArea = new ScrollPaneTextArea(2, "");
		reqHeaderArea = new ParameterTablePanel(parent, "");
		rspHeaderArea = new ParameterTablePanel(parent, "");
		rspBodyArea = new ScrollPaneTextArea(10, "");
		
		typeComboBox = ViewModules.createComboBox(types);
		typeComboBox.setBounds(0, 0, 100, 25);
		
		reqParamsPanel = ViewModules.createPanel("Request Params");
		reqHeaderPanel = ViewModules.createPanel("Request Headers");
		rspHeaderPanel = ViewModules.createPanel("Response Headers");
		rspBodyPanel = ViewModules.createPanel("Response Body");
		
		saveBtn = ViewModules.createButton("Save", "SAVE", this);
		deleteBtn = ViewModules.createButton("Delete", "DELETE", this);
	}
	
	public void layoutComponents() {
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.TOP);// 设置标签置放位置。
		
		// request info
		JPanel requestInfoPanel = new JPanel();
		requestInfoPanel.setLayout(ViewModules.getGridBagLayout(13, 10, 5, 5, 1.0, 1.0));
		
		requestInfoPanel.add(urlLabel, ViewModules.getGridBagConstraints(1, 1, 1, 1));
		requestInfoPanel.add(urlField, ViewModules.getGridBagConstraints(2, 1, 7, 1));
		
		requestInfoPanel.add(httpMethodLabel, ViewModules.getGridBagConstraints(9, 1, 1, 1));
		requestInfoPanel.add(httpMethodField, ViewModules.getGridBagConstraints(10, 1, 1, 1));
		
		requestInfoPanel.add(testCaseDescLabel, ViewModules.getGridBagConstraints(1, 2, 1, 1));
		requestInfoPanel.add(testCaseDescArea, ViewModules.getGridBagConstraints(1, 3, 10, 2));
		
		reqParamsPanel.setLayout(ViewModules.getGridBagLayout(6, 10, 5, 5, 1.0, 1.0));
		reqParamsPanel.add(reqParamsArea, ViewModules.getGridBagConstraints(1, 1, 10, 5));
		requestInfoPanel.add(reqParamsPanel, ViewModules.getGridBagConstraints(1, 6, 10, 5));
		
		reqHeaderPanel.setLayout(ViewModules.getGridBagLayout(6, 10, 5, 5, 1.0, 1.0));
		reqHeaderPanel.add(reqHeaderArea, ViewModules.getGridBagConstraints(1, 2, 10, 5));
		requestInfoPanel.add(reqHeaderPanel, ViewModules.getGridBagConstraints(1, 12, 10, 5));
		
		tabbedPane.add("Request Info", requestInfoPanel);
		// response info
		JPanel responseInfoPanel = new JPanel();
		responseInfoPanel.setLayout(ViewModules.getGridBagLayout(13, 10, 5, 5, 1.0, 1.0));
		
		responseInfoPanel.add(statusCodeLabel, ViewModules.getGridBagConstraints(1, 1, 1, 1));
		responseInfoPanel.add(statusCodeField, ViewModules.getGridBagConstraints(2, 1, 1, 1));
		
		responseInfoPanel.add(reasonPhraseLabel, ViewModules.getGridBagConstraints(3, 1, 1, 1));
		responseInfoPanel.add(reasonPhraseField, ViewModules.getGridBagConstraints(4, 1, 7, 1));
		
		rspHeaderPanel.setLayout(ViewModules.getGridBagLayout(6, 10, 5, 5, 1.0, 1.0));
		rspHeaderPanel.add(rspHeaderArea, ViewModules.getGridBagConstraints(1, 1, 10, 5));
		responseInfoPanel.add(rspHeaderPanel, ViewModules.getGridBagConstraints(1, 2, 10, 5));
		
		rspBodyPanel.setLayout(ViewModules.getGridBagLayout(6, 10, 5, 5, 1.0, 1.0));
		rspBodyPanel.add(typeComboBox, ViewModules.getGridBagConstraints(2, 1, 1, 1, GridBagConstraints.HORIZONTAL));
		rspBodyPanel.add(rspBodyArea, ViewModules.getGridBagConstraints(1, 2, 10, 5));
		responseInfoPanel.add(rspBodyPanel, ViewModules.getGridBagConstraints(1, 9, 10, 6));
		
		tabbedPane.add("Response Info", responseInfoPanel);
		this.add(tabbedPane, ViewModules.getGridBagConstraints(1, 1, 10, 14));
		// button
		this.add(deleteBtn, ViewModules.getGridBagConstraints(8, 20, 1, 1));
		this.add(saveBtn, ViewModules.getGridBagConstraints(10, 20, 1, 1));
	}

	public void initData() {
		urlField.setText(dataBean.getUrl());
		httpMethodField.setText(dataBean.getMethod());
		reqParamsArea.setTableValues(dataBean.getReqParams());
		statusCodeField.setText(StringUtil.toString(dataBean.getStatusCode()));
		reasonPhraseField.setText(dataBean.getReasonPhrase());
		
		testCaseDescArea.setText(dataBean.getCaseDesc());
		reqHeaderArea.setTableValues(dataBean.getReqHeader());
		rspHeaderArea.setTableValues(dataBean.getRspHeader());
		rspBodyArea.setText(StringUtil.toString(dataBean.getRspBody()));
		
		typeComboBox.setSelectedIndex(0);
		typeComboBox.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				rspBodyArea.showWithFormat(e.getItem().toString());
			}
		});
	}
	
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "SAVE":
			parent.progress.startProgress("Data is Saving...");
			//MavenPomHelper.initMavenProject();
			boolean isOK = updateData();
			if(!isOK){
				ViewModules.showMessageDialog(this, "Data Save Failed.");
			} else {
				ViewModules.showMessageDialog(this, "Data Save Success.");
			}
			parent.progress.stopProgress("Data has Saved!");
			break;
		case "DELETE":
			parent.progress.startProgress("Data is Saving...");
			boolean isDel = deleteData();
			if(!isDel){
				ViewModules.showMessageDialog(this, "Data Delete Failed.");
			} else {
				ViewModules.showMessageDialog(this, "Data Delete Success.");
			}
			parent.progress.stopProgress("Data has Deleted!");
			break;
		default:
			break;
		}
	}

	private boolean deleteData() {
		return ((ScriptEditFrame)parent).delCaseData(dataBean.getCaseId());
	}

	private boolean updateData() {
		DataForJavaBean newDataBean = new DataForJavaBean();
		newDataBean.setCaseId(dataBean.getCaseId());
		newDataBean.setCaseDesc(testCaseDescArea.getText());
		newDataBean.setUrl(urlField.getText());
		newDataBean.setMethod(httpMethodField.getText());
		newDataBean.setReqHeader(JsonUtil.getJson(reqHeaderArea.getDataOfJsonString()));
		newDataBean.setReqParams(JsonUtil.getJson(reqParamsArea.getDataOfJsonString()));
		newDataBean.setStatusCode(Integer.valueOf(statusCodeField.getText()));
		newDataBean.setReasonPhrase(reasonPhraseField.getText());
		newDataBean.setRspHeader(JsonUtil.getJson(rspHeaderArea.getDataOfJsonString()));
		newDataBean.setRspBody(rspBodyArea.getText());
		return ((ScriptEditFrame)parent).updateCaseData(newDataBean);
	}

}
