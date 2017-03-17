package com.view.preference.component;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.common.Constants;
import com.view.preference.AbstractPreferencesView;
import com.view.util.ViewModules;

/**
 * 脚本配置界面
 * @author sifuma@163.com
 *
 */
@SuppressWarnings("serial")
public class ScriptSettingView extends AbstractPreferencesView {

	private JLabel packageNameLabel, authorLabel, classDescLabel, classNameLabel, superClassLabel, paramNamesLabel, smokeScriptLabel, templateDirLabel, templateFileLabel;
	private JTextField packageNameField, authorField, classDescField, classNameField, superClassField, paramNamesField, templateDirField;
	private JCheckBox smokeScriptCheckBox;
	private JButton browseButton, applyButton;
	private JComboBox<String> templateFileCombBox;
	private DefaultComboBoxModel<String> combBoxModel;
	
	public ScriptSettingView() {
		super(10, 10);
		defineComponents();
		layoutComponents();
		initData();
	}
	
	/**
	 * 初始化界面数据
	 */
	@SuppressWarnings("null")
	public void initData(){
		String packageName = Constants.PROPS.getProperty("packageName");
		String author = Constants.PROPS.getProperty("author");
		String classDesc = Constants.PROPS.getProperty("classDesc");
		String className = Constants.PROPS.getProperty("className");
		String superclass = Constants.PROPS.getProperty("superClass");
		String paramNames = Constants.PROPS.getProperty("paramNames");
		String smokeScript = Constants.PROPS.getProperty("smokeScript");
		boolean isChose = Boolean.valueOf((null != smokeScript || smokeScript.trim().length() > 0) ? smokeScript : "false");
		String templateDir = Constants.PROPS.getProperty("templateDir");
		String templateFile = Constants.PROPS.getProperty("templateFile");
		
		smokeScriptCheckBox.setSelected(isChose);
		packageNameField.setText((null != packageName) ? packageName : "");
		authorField.setText((null != author) ? author : "");
		classDescField.setText((null != classDesc) ? classDesc : "");
		classNameField.setText((null != className) ? className : "");
		superClassField.setText((null != superclass) ? superclass : "");
		paramNamesField.setText((null != paramNames) ? paramNames : "");
		templateDirField.setText((null != templateDir) ? templateDir : "");
		updateFileList();
		templateFileCombBox.setSelectedItem((null != templateFile) ? templateFile : "");
	}
	
	/**
	 * 定义组件
	 */
	public void defineComponents(){
		packageNameLabel = ViewModules.createJLabel("package:", Color.RED);
		packageNameLabel.setToolTipText("生成的测试脚本的所属包名");
		authorLabel = ViewModules.createJLabel("author:", Color.BLACK);
		authorLabel.setToolTipText("生成的测试脚本的作者");
		classDescLabel = ViewModules.createJLabel("classDesc:", Color.BLACK);
		classDescLabel.setToolTipText("生成的测试脚本的描述");
		classNameLabel = ViewModules.createJLabel("className:", Color.RED);
		classNameLabel.setToolTipText("生成的测试脚本的类名，默认为接口方法名+Test");
		superClassLabel = ViewModules.createJLabel("superclass:", Color.BLACK);
		superClassLabel.setToolTipText("生成的测试脚本的父类");
		paramNamesLabel = ViewModules.createJLabel("paramNames:", Color.RED);
		paramNamesLabel.setToolTipText("生成的测试脚本的测试方法的参数名称,多个以','分隔");
		smokeScriptLabel = ViewModules.createJLabel("smokeScript:", Color.BLACK);
		smokeScriptLabel.setToolTipText("生成的测试脚本的测试方法是否是冒烟脚本");
		templateDirLabel = ViewModules.createJLabel("templateDir:", Color.RED);
		templateDirLabel.setToolTipText("生成测试脚本的模板文件存放目录");
		templateFileLabel = ViewModules.createJLabel("templateFile:", Color.RED);
		templateFileLabel.setToolTipText("生成测试脚本的模板文件");
		
		packageNameField = ViewModules.createTextField(20, "", true);
		authorField = ViewModules.createTextField(20, "", true);
		classDescField = ViewModules.createTextField(20, "", true);
		classNameField = ViewModules.createTextField(20, "", true);
		superClassField = ViewModules.createTextField(20, "", true);
		paramNamesField = ViewModules.createTextField(20, "", true);
		smokeScriptCheckBox = ViewModules.createCheckBox("Yes", null);
		templateDirField = ViewModules.createTextField(20, "", true);
		browseButton = ViewModules.createButton("browse", "Browse", this);
//		templateDirField.getDocument().addDocumentListener(new DocumentChangeListener());
		combBoxModel = new DefaultComboBoxModel<String>();
		templateFileCombBox = new JComboBox<String>(combBoxModel);
		templateFileCombBox.addMouseListener(new MouseClickListener());
		
		applyButton = ViewModules.createButton("Apply", "SaveScriptSetting", this);
	}
	
	public void updateFileList(){
		String path = templateDirField.getText().trim();
		path = path.endsWith(File.separator) ? path : path + File.separator;
		String[] fileNameArray = traverseFolder(path);
		combBoxModel.removeAllElements();
		for(String file : fileNameArray){
			if(null == file) continue;
			combBoxModel.addElement(file.substring(file.indexOf(path) + path.length()));
		}
		templateFileCombBox.updateUI();
	}
	
	/**
	 * 对界面进行布局
	 */
	public void layoutComponents(){
		this.add(packageNameLabel, ViewModules.getGridBagConstraints(1, 1, 1, 1));
		this.add(packageNameField, ViewModules.getGridBagConstraints(2, 1, 9, 1));
		
		this.add(authorLabel, ViewModules.getGridBagConstraints(1, 2, 1, 1));
		this.add(authorField, ViewModules.getGridBagConstraints(2, 2, 9, 1));

		this.add(classDescLabel, ViewModules.getGridBagConstraints(1, 3, 1, 1));
		this.add(classDescField, ViewModules.getGridBagConstraints(2, 3, 9, 1));

		this.add(classNameLabel, ViewModules.getGridBagConstraints(1, 4, 1, 1));
		this.add(classNameField, ViewModules.getGridBagConstraints(2, 4, 9, 1));

		this.add(superClassLabel, ViewModules.getGridBagConstraints(1, 5, 1, 1));
		this.add(superClassField, ViewModules.getGridBagConstraints(2, 5, 9, 1));

		this.add(paramNamesLabel, ViewModules.getGridBagConstraints(1, 6, 1, 1));
		this.add(paramNamesField, ViewModules.getGridBagConstraints(2, 6, 9, 1));
		
		this.add(smokeScriptLabel, ViewModules.getGridBagConstraints(1, 7, 1, 1));
		this.add(smokeScriptCheckBox, ViewModules.getGridBagConstraints(2, 7, 9, 1));

		this.add(templateDirLabel, ViewModules.getGridBagConstraints(1, 8, 1, 1));
		this.add(templateDirField, ViewModules.getGridBagConstraints(2, 8, 8, 1));
		this.add(browseButton, ViewModules.getGridBagConstraints(10, 8, 1, 1));
		
		this.add(templateFileLabel, ViewModules.getGridBagConstraints(1, 9, 1, 1));
		this.add(templateFileCombBox, ViewModules.getGridBagConstraints(2, 9, 9, 1));
		
		this.add(applyButton, ViewModules.getGridBagConstraints(10, 10, 1, 1));
	}

	public void actionPerformed(ActionEvent evt) {
		switch(evt.getActionCommand()){
		case "SaveScriptSetting":
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
				templateDirField.setText(filepath);
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
		storeProperty("packageName", packageNameField.getText());
		// 选填：生成的脚本作者，不填时使用电脑当前用户
		storeProperty("author", authorField.getText());
		// 选填：生成的脚本描述
		storeProperty("classDesc", classDescField.getText());
		// 必填：生成的脚本类名，建议首字母大写
		storeProperty("className", classNameField.getText());
		// 选填：生成的脚本继承的父类，设置是请加上包名，如：org.apache.http.HttpResponse
		storeProperty("superClass", superClassField.getText());
		// 必填，测试方法的变量名称，多个值时用,分隔
		storeProperty("paramNames", paramNamesField.getText());
		// 选填，测试方法是否为冒烟测试脚本，值为true或false,默认false
		storeProperty("smokeScript", String.valueOf(smokeScriptCheckBox.isSelected()));
		// 必填，模板文件的存储目录，可以是项目中的相对路径
		storeProperty("templateDir", templateDirField.getText());
		// 必填，模板文件名称，包括后缀名
		storeProperty("templateFile", templateFileCombBox.getSelectedItem().toString());
		Constants.initProperties(Constants.DEF_SET_PROP_FILE);
	}
	
	public class MouseClickListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if(e.getComponent() instanceof JComboBox)
				updateFileList();
		}
	}
	
	public class DocumentChangeListener implements DocumentListener{
		@Override
		public void insertUpdate(DocumentEvent e) {
			updateFileList();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			updateFileList();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			updateFileList();
		}
		
	}
	
//	public static void main(String[] args) {
//		JFrame jf=new JFrame();
//		ScriptSettingView panel = new ScriptSettingView();
//		jf.add(panel.getJPanel(), BorderLayout.CENTER);
//		jf.setSize(600,600);
//		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		jf.setVisible(true);
//	}

}