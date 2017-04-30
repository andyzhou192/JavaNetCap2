package com.view.preference.pane;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.netcap.captor.NetCaptor;
import com.view.preference.AbstractPreferencesView;
import com.view.preference.PreferenceFrame;
import com.view.preference.PropertyHelper;
import com.view.util.ScrollPaneTextArea;
import com.view.util.ViewModules;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
@SuppressWarnings({ "serial" })
public class CaptorSettingView extends AbstractPreferencesView {

	private PreferenceFrame parent;
	private JLabel ethernetLabel, protocolLabel, promiscLabel, urlLabel, maxLengthLabel;
	private JTextField caplenTextField;
	private ScrollPaneTextArea urlFilterArea;
	private JCheckBox promiscCheckBox;
	private JComboBox<?> netJComboBox, proJComboBox;
	private JRadioButton wholeRadioButton, headRadioButton, otherRadioButton;
	private JButton applyButton;
	
	public CaptorSettingView(PreferenceFrame parent) {
		super(11, 10);
		this.parent = parent;
		defineComponents();
		layoutComponents();
		initData();
	}
	
	/**
	 * 定义组件
	 */
	public void defineComponents(){
		ethernetLabel = ViewModules.createJLabel("Ethernet:", Color.RED);
		ethernetLabel.setToolTipText("请选择网卡");
		protocolLabel = ViewModules.createJLabel("Capture Protocol:", Color.BLACK);
		protocolLabel.setToolTipText("请选择捕获的协议");
		promiscLabel = ViewModules.createJLabel("Promisc:", Color.BLACK); 
		promiscLabel.setToolTipText("请选择是否采用混杂模式");
		urlLabel = ViewModules.createJLabel("Capture Url:(Multiple addresses are separated by ',')", Color.BLACK);
		urlLabel.setToolTipText("请填写待捕获的URL");
		maxLengthLabel = ViewModules.createJLabel("Max Length:", Color.BLACK);
		maxLengthLabel.setToolTipText("请选择每次捕获数据的最大长度，大小在68~65536之间");
		
		netJComboBox = ViewModules.createComboBox(NetCaptor.devicesMap.keySet().toArray());
		proJComboBox = ViewModules.createComboBox(new String[]{"TCP"});
		promiscCheckBox = ViewModules.createCheckBox("Yes", null);
		urlFilterArea = new ScrollPaneTextArea(5, "");
		wholeRadioButton = ViewModules.createRadioButton("Whole Data", "WHOLE", this);
		headRadioButton = ViewModules.createRadioButton("Only Head", "HEAD", this);
		otherRadioButton = ViewModules.createRadioButton("Other", "OTHER", this);
		// 把单选框加到一个组中以确保一个组中的单选框只能单选
		ViewModules.createButtonGroup(wholeRadioButton, headRadioButton, otherRadioButton);
		caplenTextField = ViewModules.createTextField(20, "65536", false);
		
		applyButton = ViewModules.createButton("Apply", "SaveCaptureSetting", this);
	}
	
	/**
	 * 初始化界面数据
	 */
	public void initData(){
		String driverName = PropertyHelper.getNetDevicesName();
		netJComboBox.setSelectedItem(driverName);
		proJComboBox.setSelectedItem(PropertyHelper.getProtocolType());
		promiscCheckBox.setSelected(PropertyHelper.getPromisc());
		urlFilterArea.setText(PropertyHelper.getCaptureUrl());
		
		int caplen = PropertyHelper.getCaptureLength();
		if (caplen < 68 || caplen > 65536) {
			caplenTextField.setText(String.valueOf(caplen));
			otherRadioButton.setSelected(true);
		} else if(caplen == 68){
			caplenTextField.setText(String.valueOf(caplen));
			headRadioButton.setSelected(true);
		} else {
			caplenTextField.setText(String.valueOf(65536)); // 65535为ip数据包支持的最大报文长度
			wholeRadioButton.setSelected(true);
		}
	}
	
	/**
	 * 对界面进行布局
	 */
	public void layoutComponents(){
		this.add(ethernetLabel, ViewModules.getGridBagConstraints(1, 1, 1, 1));
		this.add(netJComboBox, ViewModules.getGridBagConstraints(2, 1, 9, 1));
		
		this.add(protocolLabel, ViewModules.getGridBagConstraints(1, 2, 1, 1));
		this.add(proJComboBox, ViewModules.getGridBagConstraints(2, 2, 9, 1));
		
		this.add(promiscLabel, ViewModules.getGridBagConstraints(1, 3, 1, 1));
		this.add(promiscCheckBox, ViewModules.getGridBagConstraints(2, 3, 9, 1));
		
		this.add(urlLabel, ViewModules.getGridBagConstraints(1, 4, 1, 1));
		this.add(urlFilterArea, ViewModules.getGridBagConstraints(1, 5, 10, 5));
		
		this.add(maxLengthLabel, ViewModules.getGridBagConstraints(1, 10, 2, 1));
		this.add(wholeRadioButton, ViewModules.getGridBagConstraints(3, 10, 2, 1));
		this.add(headRadioButton, ViewModules.getGridBagConstraints(5, 10, 2, 1));
		this.add(otherRadioButton, ViewModules.getGridBagConstraints(7, 10, 2, 1));
		this.add(caplenTextField, ViewModules.getGridBagConstraints(9, 10, 2, 1));
		
		this.add(applyButton, ViewModules.getGridBagConstraints(10, 11, 1, 1));
	}

	public void actionPerformed(ActionEvent evt) {
		switch(evt.getActionCommand()){
		case "WHOLE":
			caplenTextField.setText("65536");
			caplenTextField.setEnabled(false);
			break;
		case "HEAD":
			caplenTextField.setText("68");
			caplenTextField.setEnabled(false);
			break;
		case "OTHER":
			caplenTextField.setText("");
			caplenTextField.setEnabled(true);
			caplenTextField.requestFocus();
			break;
		case "SaveCaptureSetting":
			parent.progress.startProgress("Save data...");
			boolean isSucc = saveSettings();
			parent.progress.stopProgress("Data save status : " + (isSucc ? "Success." : "Failed."));
			break;
		default:
			break;
		}
	}
	
	/**
	 * 保存配置
	 */
	public boolean saveSettings(){
		int caplen = Integer.parseInt(caplenTextField.getText());
		if (caplen < 68 || caplen > 65536) {
			ViewModules.showMessageDialog(null, "捕获长度必须介于 68 和 65536之间");
			return false;
		}
		// 网卡序号，默认为0
		PropertyHelper.setNetDevicesName(String.valueOf(netJComboBox.getSelectedItem()));
		// 是否混杂模式:true/false，默认false
		PropertyHelper.setPromisc(promiscCheckBox.isSelected());
		// 待捕获的协议类型，默认为tcp
		PropertyHelper.setProtocolType(proJComboBox.getSelectedItem().toString());
		// 待捕获的数据长度 ,捕获长度必须介于 68和65536之间的整数，默认为65536
		PropertyHelper.setCaptureLength(caplen);
		// 待捕获的URL，不含参数
		PropertyHelper.setCaptureUrl(urlFilterArea.getText());
		boolean isSucc = PropertyHelper.storeProperties();
		//ViewModules.showMessageDialog(parent, "Properties saved : " + isSucc);
		return isSucc;
	}
	
}