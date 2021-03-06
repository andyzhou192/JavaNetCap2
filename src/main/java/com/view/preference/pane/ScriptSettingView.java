package com.view.preference.pane;

import java.awt.Color;
import java.awt.event.ActionEvent;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.io.File;

//import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
//import javax.swing.event.DocumentEvent;
//import javax.swing.event.DocumentListener;

import com.view.preference.AbstractPreferencesView;
import com.view.preference.PreferenceFrame;
import com.view.preference.PropertyHelper;
import com.view.util.ViewModules;

/**
 * 脚本配置界面
 * @author sifuma@163.com
 *
 */
@SuppressWarnings("serial")
public class ScriptSettingView extends AbstractPreferencesView {

	private PreferenceFrame parent;
	private JLabel packageNameLabel, authorLabel, classDescLabel, classNameLabel, superClassLabel, paramNamesLabel, smokeScriptLabel, scriptOverwriteLabel/**, templateDirLabel**/, templateFileLabel;
	private JTextField packageNameField, authorField, classDescField, classNameField, superClassField, paramNamesField/**, templateDirField**/;
	private JCheckBox smokeScriptCheckBox, scriptOverwriteCheckBox;
	private JButton /**browseButton,**/ applyButton;
	private JComboBox<String> templateFileCombBox;
//	private DefaultComboBoxModel<String> combBoxModel;
	
	public ScriptSettingView(PreferenceFrame parent) {
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
		scriptOverwriteLabel = ViewModules.createJLabel("scriptOverwrite:", Color.BLACK);
		scriptOverwriteLabel.setToolTipText("当待生成的测试脚本已经存在时，是否覆盖");
//		templateDirLabel = ViewModules.createJLabel("templateDir:", Color.RED);
//		templateDirLabel.setToolTipText("生成测试脚本的模板文件存放目录");
		templateFileLabel = ViewModules.createJLabel("templateFile:", Color.RED);
		templateFileLabel.setToolTipText("生成测试脚本的模板文件");
		
		packageNameField = ViewModules.createTextField(20, "", true);
		authorField = ViewModules.createTextField(20, "", true);
		classDescField = ViewModules.createTextField(20, "", true);
		classNameField = ViewModules.createTextField(20, "", true);
		superClassField = ViewModules.createTextField(20, "", true);
		paramNamesField = ViewModules.createTextField(20, "", true);
		smokeScriptCheckBox = ViewModules.createCheckBox("Yes", null);
		scriptOverwriteCheckBox = ViewModules.createCheckBox("Yes", null);
//		templateDirField = ViewModules.createTextField(20, "", true);
//		browseButton = ViewModules.createButton("browse", "Browse", this);
//		templateDirField.getDocument().addDocumentListener(new DocumentChangeListener());
//		combBoxModel = new DefaultComboBoxModel<String>();
		//templateFileCombBox = new JComboBox<String>(combBoxModel);
		templateFileCombBox = new JComboBox<String>(getTemplateFiles());
//		templateFileCombBox.addMouseListener(new MouseClickListener());
		
		applyButton = ViewModules.createButton("Apply", "SaveScriptSetting", this);
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
		this.add(smokeScriptCheckBox, ViewModules.getGridBagConstraints(2, 7, 4, 1));
		this.add(scriptOverwriteLabel, ViewModules.getGridBagConstraints(6, 7, 1, 1));
		this.add(scriptOverwriteCheckBox, ViewModules.getGridBagConstraints(7, 7, 4, 1));

//		this.add(templateDirLabel, ViewModules.getGridBagConstraints(1, 8, 1, 1));
//		this.add(templateDirField, ViewModules.getGridBagConstraints(2, 8, 8, 1));
//		this.add(browseButton, ViewModules.getGridBagConstraints(10, 8, 1, 1));
		
		this.add(templateFileLabel, ViewModules.getGridBagConstraints(1, 8, 1, 1));
		this.add(templateFileCombBox, ViewModules.getGridBagConstraints(2, 8, 9, 1));
		
		this.add(applyButton, ViewModules.getGridBagConstraints(10, 10, 1, 1));
	}
	
	/**
	 * 初始化界面数据
	 */
	public void initData(){
		packageNameField.setText(PropertyHelper.getPackageName());
		authorField.setText(PropertyHelper.getAuthor());
		classDescField.setText(PropertyHelper.getClassDesc());
		classNameField.setText(PropertyHelper.getClassName());
		superClassField.setText(PropertyHelper.getSuperClass());
		paramNamesField.setText(PropertyHelper.getParamNames());
		smokeScriptCheckBox.setSelected(PropertyHelper.getSmokeScript());
		scriptOverwriteCheckBox.setSelected(PropertyHelper.getScriptOverwrite());
		//templateDirField.setText(PropertyHelper.getTemplateDir());
//		updateFileList();
		templateFileCombBox.setSelectedItem(PropertyHelper.getTemplateFile());
	}
	
//	public void updateFileList(){
//		String path = Constants.DEFAULT_TEMPLATE_SCRIPT_DIR;//templateDirField.getText().trim();
//		String[] fileNameArray = traverseFolder(path);
//		combBoxModel.removeAllElements();
//		for(String file : fileNameArray){
//			if(null == file) continue;
//			combBoxModel.addElement(file.substring(file.indexOf(path) + path.length()));
//		}
//		templateFileCombBox.updateUI();
//	}
	

	public void actionPerformed(ActionEvent evt) {
		switch(evt.getActionCommand()){
		case "SaveScriptSetting":
			parent.progress.startProgress("Save data...");
			boolean isSucc = saveSettings();
			parent.progress.stopProgress("Data save status : " + (isSucc ? "Success." : "Failed."));
			break;
//		case "Browse":
//			JFileChooser chooser = new JFileChooser();             //设置选择器
//			//chooser.setMultiSelectionEnabled(true);             //设为多选
//			chooser.setDialogTitle("Please choose workspace");
//			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//			int returnVal = chooser.showOpenDialog(browseButton);        //是否打开文件选择框
//			if (returnVal == JFileChooser.APPROVE_OPTION) {          //如果符合文件类型
//				String filepath = chooser.getSelectedFile().getAbsolutePath();      //获取绝对路径
//				templateDirField.setText(filepath);
//			}
//			break;
		default:
			break;
		}
	}
	
	/**
	 * 保存配置
	 */
	public boolean saveSettings(){
		// 必填：生成的脚本文件包名，如：net.andy
		PropertyHelper.setPackageName(packageNameField.getText());
		// 选填：生成的脚本作者，不填时使用电脑当前用户
		PropertyHelper.setAuthor(authorField.getText());
		// 选填：生成的脚本描述
		PropertyHelper.setClassDesc(classDescField.getText());
		// 必填：生成的脚本类名，建议首字母大写
		PropertyHelper.setClassName(classNameField.getText());
		// 选填：生成的脚本继承的父类，设置是请加上包名，如：org.apache.http.HttpResponse
		PropertyHelper.setSuperClass(superClassField.getText());
		// 必填，测试方法的变量名称，多个值时用,分隔
		PropertyHelper.setParamNames(paramNamesField.getText());
		// 选填，测试方法是否为冒烟测试脚本，值为true或false,默认false
		PropertyHelper.setSmokeScript(smokeScriptCheckBox.isSelected());
		// 选填，待生成的测试脚本存在时，是否覆盖，值为true或false,默认true
		PropertyHelper.setScriptOverwrite(scriptOverwriteCheckBox.isSelected());
		// 必填，模板文件的存储目录，可以是项目中的相对路径
//		PropertyHelper.setTemplateDir(templateDirField.getText());
		// 必填，模板文件名称，包括后缀名
		PropertyHelper.setTemplateFile(templateFileCombBox.getSelectedItem().toString());
		return PropertyHelper.storeProperties();
	}
	
//	public class MouseClickListener extends MouseAdapter {
//		public void mouseClicked(MouseEvent e) {
//			if(e.getComponent() instanceof JComboBox)
//				updateFileList();
//		}
//	}
//	
//	public class DocumentChangeListener implements DocumentListener{
//		@Override
//		public void insertUpdate(DocumentEvent e) {
//			updateFileList();
//		}
//
//		@Override
//		public void removeUpdate(DocumentEvent e) {
//			updateFileList();
//		}
//
//		@Override
//		public void changedUpdate(DocumentEvent e) {
//			updateFileList();
//		}
//		
//	}
	
//	public static void main(String[] args) {
//		JFrame jf=new JFrame();
//		ScriptSettingView panel = new ScriptSettingView();
//		jf.add(panel.getJPanel(), BorderLayout.CENTER);
//		jf.setSize(600,600);
//		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		jf.setVisible(true);
//	}

}