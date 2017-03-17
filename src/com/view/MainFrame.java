package com.view;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import com.common.Constants;
import com.protocol.http.bean.HttpDataBean;
import com.view.listener.ActionListenerImpl;
import com.view.util.ViewModules;
import com.view.view.RowTableScrollPane;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	public RowTableScrollPane scrollPane = null;
	public JMenuItem startItem, stopItem;
	public Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
	private ActionListenerImpl listener;

	public final String[] title = {"ALL", "url", "method", "reqHeader", "reqParams", "statusCode", "reasonPhrase", "rspHeader", "rspBody", "Operate"};
	
	public MainFrame() {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 600);
		Constants.initProperties(Constants.PROP_FILE);
		listener = new ActionListenerImpl(this);
		initMenu();
		initViewer();
		this.setVisible(true);
	}

	private void initMenu() {
		JMenuBar jMenuBar = new JMenuBar();
		this.setJMenuBar(jMenuBar);
		JMenu fileMenu = ViewModules.addMenu(jMenuBar, "File");
		JMenu mainMenu = ViewModules.addMenu(jMenuBar, "Captor");
		JMenu setMenu = ViewModules.addMenu(jMenuBar, "Setting");
		
		ViewModules.addSimpleMenuItem(fileMenu, "Open", "OPEN", listener);
		ViewModules.addSimpleMenuItem(fileMenu, "Save", "SAVE", listener);
		ViewModules.addSimpleMenuItem(fileMenu, "SaveAs", "SAVEAS", listener);
		ViewModules.addSimpleMenuItem(fileMenu, "Delete", "DELETE", listener);
		ViewModules.addSimpleMenuItem(fileMenu, "Exit", "EXIT", listener);
		startItem = ViewModules.addSimpleMenuItem(mainMenu, "Start", "START", listener);
		stopItem = ViewModules.addSimpleMenuItem(mainMenu, "Stop", "STOP", listener);
		ViewModules.addSimpleMenuItem(setMenu, "Setting", "SETTING", listener);
	}
	
	private void initViewer() {
		Vector<Object> heads = ViewModules.createVector(title);
		scrollPane = new RowTableScrollPane(rows, heads);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JLabel statusLabel = new JLabel("zhouyelin@cmhi.chinamobile.com");
		this.getContentPane().add(statusLabel, BorderLayout.SOUTH);
	}
	
	/**
	 * 在table中新增一行
	 * @param data
	 */
	public void addRowToTable(HttpDataBean data){
		Object[] values = {getCheckBox(String.valueOf(rows.size() + 1)), 
				data.getUrl(), data.getMethod(), data.getReqHeader(), data.getReqParams(), 
				data.getStatusCode(), data.getReasonPhrase(), data.getRspHeader(), data.getRspBody(), 
				getButton()};
		Vector<Object> r = ViewModules.createVector(values);
		rows.addElement(r);
		scrollPane.addNotifyTable();
	}
	
	/**
	 * 从table中删除指定行
	 * @param rowIndex 行索引，为-1时表示删除所有行
	 */
	public void deleteRowFromTable(int rowIndex){
		if(-1 == rowIndex){
			rows.removeAllElements();
		} else {
			rows.removeElementAt(rowIndex);
		}
		scrollPane.addNotifyTable();
	}
	
	public JCheckBox getCheckBox(String text){
		JCheckBox checkBox = new JCheckBox(text);
		checkBox.setEnabled(true);
		checkBox.setVisible(true);
		return checkBox;
	}

	public JButton getButton(){
		JButton button = new JButton("Detail");
		button.addActionListener(listener); 
		button.setEnabled(true);
		button.setActionCommand("DETAIL");
		button.setVisible(true);
		return button;
	}
	
	public JLabel getLabel(String text){
		JLabel label = new JLabel(text);
		label.setEnabled(true);
		label.setVisible(true);
		return label;
	}
}