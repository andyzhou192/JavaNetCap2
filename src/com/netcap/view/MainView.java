package com.netcap.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.codegenerator.generator.java.ScriptGenerator;
import com.common.util.ConstsUtil;
import com.netcap.captor.Netcaptor;
import com.netcap.view.util.MenuEnum;
import com.netcap.view.util.ViewModules;
import com.protocol.http.bean.HttpDataBean;

import freemarker.template.TemplateException;

public class MainView extends JFrame implements ActionListener {

	private static final long serialVersionUID = -6936337706153702144L;

	private JTable tabledisplay = null;
	private Vector<Object> rows;

	private static List<HttpDataBean> dataList = new ArrayList<HttpDataBean>();
	protected final String propFile = "setting";

	private static int id = 1;

	/**
	 * Auto-generated main method to display this JFrame
	 */
//	public static void main(String[] args) {
//		new MainView();
//	}

	public MainView() {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initGUI();
		this.setVisible(true);
	}

	private void initGUI() {
		try {
			this.setSize(600, 600);
			setMenu();
			rows = new Vector<Object>();
			
			String[] httpTitles = {"Select", "ID", "URL", "Method", "ReqHeader", "ReqParams", "RspCode", "RspMsg", "RspHeader", "RspBody", "Operate"};
			Vector<Object> columns = ViewModules.createVector(httpTitles);

			tabledisplay = ViewModules.createTable(rows, columns);
			this.getContentPane().add(new JScrollPane(tabledisplay), BorderLayout.CENTER);

			JLabel statusLabel = new JLabel("zhouyelin@cmhi.chinamobile.com");
			this.getContentPane().add(statusLabel, BorderLayout.SOUTH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setMenu() {
		JMenuBar jMenuBar = new JMenuBar();
		this.setJMenuBar(jMenuBar);
		JMenu mainMenu = ViewModules.addMenu(jMenuBar, "Captor");
		JMenu confMenu = ViewModules.addMenu(jMenuBar, "Setting");
		JMenu geneMenu = ViewModules.addMenu(jMenuBar, "Generator");
		for(MenuEnum menu : MenuEnum.values()){
			if(MenuEnum.SETTING == menu)
				ViewModules.addSimpleMenuItem(confMenu, MenuEnum.SETTING, this);
			else if(MenuEnum.GENEJAVA == menu)
				ViewModules.addSimpleMenuItem(geneMenu, MenuEnum.GENEJAVA, this);
			else
				ViewModules.addSimpleMenuItem(mainMenu, menu, this);
		}
	}

	public void actionPerformed(ActionEvent event) {
		MenuEnum cmd = MenuEnum.getMenuEnum(event.getActionCommand());
		switch(cmd){
		case START:
			ConstsUtil.initProperties("setting");
			Netcaptor.startCapture(this);
			break;
		case STOP:
			Netcaptor.stopCapture();
			break;
		case SAVE:
			dataList.toString();
			break;
		case SAVEAS:
			break;
		case EXIT:
			System.exit(0);
			break;
		case SETTING:
			new PreferenceDialog();
			break;
		case GENEJAVA:
			ScriptGenerator generator = null;
			try {
				String templateDir = ConstsUtil.PROPS.getProperty("templateDir");
				String templateFile = ConstsUtil.PROPS.getProperty("templateFile");
				if(null == templateDir || templateDir.trim().length() == 0){
					ViewModules.showMessageDialog(this.getContentPane(), "模板文件存放目录不能为空");
					break;
				} else {
					generator = new ScriptGenerator(propFile, templateDir);
				}
				if(null == templateFile || templateFile.trim().length() == 0){
					ViewModules.showMessageDialog(this.getContentPane(), "模板文件不能为空");
					break;
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
			break;
		default:
			break;
		}
	}

	public void updateView(HttpDataBean dataBean){
		dataList.add(dataBean);
		Object[] values = getValues(dataBean);
		Vector<Object> r = ViewModules.createVector(values);

		rows.addElement(r);
		tabledisplay.addNotify();
	}

	private Object[] getValues(HttpDataBean data) {
		Object[] values = {ViewModules.createCheckBox("", null), id ++, data.getUrl(), data.getMethod(), data.getRequestHeader(), data.getRequestParam().toString(), data.getStatusCode(), data.getReasonPhrase(), data.getResponseHeader(), data.getResponseBody(), ViewModules.createButton("GeneScript", "GENEJAVA", this)};
		return values;
	}
}