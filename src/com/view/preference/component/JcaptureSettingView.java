package com.view.preference.component;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.common.Constants;
import com.view.preference.AbstractPreferencesView;
import com.view.util.ViewModules;

import jpcap.NetworkInterface;
import jpcap.JpcapCaptor;

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
@SuppressWarnings({ "restriction", "serial" })
public class JcaptureSettingView extends AbstractPreferencesView {

	private JFrame parent;
	private JLabel ethernetLabel, protocolLabel, promiscLabel, urlLabel, maxLengthLabel;
	private JTextField caplenTextField, urlFilterField;
	private JCheckBox promiscCheckBox;
	private JComboBox<?> netJComboBox, proJComboBox;
	private JRadioButton wholeRadioButton, headRadioButton, otherRadioButton;
	private JButton applyButton;
	
	public JcaptureSettingView(JFrame parent) {
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
		ethernetLabel = ViewModules.createJLabel("Ethernet:", Color.RED);
		ethernetLabel.setToolTipText("请选择网卡");
		protocolLabel = ViewModules.createJLabel("Capture Protocol:", Color.BLACK);
		protocolLabel.setToolTipText("请选择捕获的协议");
		promiscLabel = ViewModules.createJLabel("Promisc:", Color.BLACK); 
		protocolLabel.setToolTipText("请选择是否采用混杂模式");
		urlLabel = ViewModules.createJLabel("Capture Url:", Color.BLACK);
		protocolLabel.setToolTipText("请填写待捕获的URL");
		maxLengthLabel = ViewModules.createJLabel("Max Length:", Color.BLACK);
		protocolLabel.setToolTipText("请选择每次捕获数据的最大长度，大小在68~1514之间");
		
		netJComboBox = ViewModules.createComboBox(getNetDeviceList());
		proJComboBox = ViewModules.createComboBox(new String[]{"TCP"});
		promiscCheckBox = ViewModules.createCheckBox("Yes", null);
		urlFilterField = ViewModules.createTextField(20, "", true);
		wholeRadioButton = ViewModules.createRadioButton("Whole Data", "WHOLE", this);
		headRadioButton = ViewModules.createRadioButton("Only Head", "HEAD", this);
		otherRadioButton = ViewModules.createRadioButton("Other", "OTHER", this);
		// 把单选框加到一个组中以确保一个组中的单选框只能单选
		ViewModules.createButtonGroup(wholeRadioButton, headRadioButton, otherRadioButton);
		caplenTextField = ViewModules.createTextField(20, "1514", false);
		
		applyButton = ViewModules.createButton("Apply", "SaveCaptureSetting", this);
	}
	
	/**
	 * 初始化界面数据
	 */
	@SuppressWarnings("null")
	public void initData(){
		String indexValue = Constants.PROPS.getProperty("net_devices_index");
		int index = (null != indexValue || indexValue.trim().length() > 0) ? Integer.valueOf(indexValue.trim()) : 0;
		netJComboBox.setSelectedIndex(index);
		
		String protocol = Constants.PROPS.getProperty("protocol_type");
		proJComboBox.setSelectedItem((null != protocol || protocol.trim().length() > 0) ? protocol : "TCP");
		
		String promisc = Constants.PROPS.getProperty("promisc");
		boolean isChose = Boolean.valueOf((null != promisc || promisc.trim().length() > 0) ? promisc : "false");
		promiscCheckBox.setSelected(isChose);
		
		String url = Constants.PROPS.getProperty("capture_url");
		urlFilterField.setText(null != url ? url : "");
		
		String len = Constants.PROPS.getProperty("capture_length");
		int caplen = Integer.parseInt((null != len || len.trim().length() > 0) ? len : "1514");
		if (caplen < 68 || caplen > 1514) {
			caplenTextField.setText(String.valueOf(caplen));
			otherRadioButton.setSelected(true);
		} else if(caplen == 68){
			caplenTextField.setText(String.valueOf(caplen));
			headRadioButton.setSelected(true);
		} else {
			caplenTextField.setText(String.valueOf(1514));
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
		this.add(urlFilterField, ViewModules.getGridBagConstraints(2, 4, 9, 1));
		
		this.add(maxLengthLabel, ViewModules.getGridBagConstraints(1, 5, 2, 1));
		this.add(wholeRadioButton, ViewModules.getGridBagConstraints(3, 5, 2, 1));
		this.add(headRadioButton, ViewModules.getGridBagConstraints(5, 5, 2, 1));
		this.add(otherRadioButton, ViewModules.getGridBagConstraints(7, 5, 2, 1));
		this.add(caplenTextField, ViewModules.getGridBagConstraints(9, 5, 2, 1));
		
		this.add(applyButton, ViewModules.getGridBagConstraints(10, 10, 1, 1));
	}

	public void actionPerformed(ActionEvent evt) {
		switch(evt.getActionCommand()){
		case "WHOLE":
			caplenTextField.setText("1514");
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
			saveSettings();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 保存配置
	 */
	public void saveSettings(){
		int caplen = Integer.parseInt(caplenTextField.getText());
		if (caplen < 68 || caplen > 1514) {
			ViewModules.showMessageDialog(null, "捕获长度必须介于 68 和 1514之间");
			return;
		}
		// 网卡序号，默认为0
		boolean isSucc_01 = storeProperty("net_devices_index", String.valueOf(netJComboBox.getSelectedIndex()));
		// 是否混杂模式:true/false，默认false
		boolean isSucc_02 = storeProperty("promisc", String.valueOf(promiscCheckBox.isSelected()));
		// 待捕获的协议类型，默认为tcp
		boolean isSucc_03 = storeProperty("protocol_type", proJComboBox.getSelectedItem().toString());
		// 待捕获的数据长度 ,捕获长度必须介于 68和1514之间的整数，默认为1514
		boolean isSucc_04 = storeProperty("capture_length", String.valueOf(caplen));
		// 待捕获的URL，不含参数
		boolean isSucc_05 = storeProperty("capture_url", urlFilterField.getText());
		Constants.initProperties(Constants.DEF_SET_PROP_FILE);
		boolean isSucc = isSucc_01 && isSucc_02 && isSucc_03 && isSucc_04 && isSucc_05;
		ViewModules.showMessageDialog(parent, "Properties saved : " + isSucc);
	}
	
	/**
	 * 获取所有网卡的名称
	 * @return
	 */
	private String[] getNetDeviceList(){
		NetworkInterface[] devices = JpcapCaptor.getDeviceList();
		if (devices == null) {
			return new String[0];
		} else {
			String[] names = new String[devices.length];
			for (int i = 0; i < names.length; i++) {
				names[i] = (devices[i].description == null ? devices[i].name : devices[i].description);
			}
			return names;
		}
	}
	
//	public static void main(String[] args) {
//		JFrame frame = new JFrame();
//		JcaptureSettingView inst = new JcaptureSettingView(frame);
//		inst.setVisible(true);
//	}

}