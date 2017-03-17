package com.view;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import com.common.Constants;
import com.protocol.http.bean.HttpDataBean;
import com.view.listener.ActionListenerImpl;
import com.view.menu.FrameMenuBar;
import com.view.table.RowTableScrollPane;
import com.view.util.ViewModules;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private RowTableScrollPane scrollPane = null;
	private FrameMenuBar menuBar;

	private ActionListenerImpl listener;
	private Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
	private String[] tableHead = {"ALL", "url", "method", "reqHeader", "reqParams", "statusCode", "reasonPhrase", "rspHeader", "rspBody", "Operate"};


	public MainFrame() {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 600);
		Constants.initProperties(Constants.DEF_SET_PROP_FILE);
		this.listener = new ActionListenerImpl(this);
		
		menuBar = new FrameMenuBar(listener);
		this.setJMenuBar(menuBar);
		
		Vector<Object> heads = ViewModules.createVector(this.getTableHead());
		this.scrollPane = new RowTableScrollPane(this.getRows(), heads);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JLabel statusLabel = new JLabel("zhouyelin@cmhi.chinamobile.com");
		this.getContentPane().add(statusLabel, BorderLayout.SOUTH);
		this.setVisible(true);
	}
	
	/**
	 * 在table中新增一行
	 * @param data
	 */
	public void addRowToTable(HttpDataBean data){
		Object[] values = {getCheckBox(String.valueOf(this.getRows().size() + 1)), 
				data.getUrl(), data.getMethod(), data.getReqHeader(), data.getReqParams(), 
				data.getStatusCode(), data.getReasonPhrase(), data.getRspHeader(), data.getRspBody(), 
				getButton()};
		Vector<Object> r = ViewModules.createVector(values);
		this.getRows().addElement(r);
		scrollPane.addNotifyTable();
	}
	
	/**
	 * 从table中删除指定行
	 * @param rowIndex 行索引，为-1时表示删除所有行
	 */
	public void deleteRowFromTable(int rowIndex){
		if(-1 == rowIndex){
			if(this.getRows().size() > 0)
				this.getRows().removeAllElements();
			else
				return;
		} else {
			this.getRows().removeElementAt(rowIndex);
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
	
	public FrameMenuBar getFrameMenuBar() {
		return menuBar;
	}
	
	public RowTableScrollPane getScrollPane() {
		return scrollPane;
	}

	public Vector<Vector<Object>> getRows() {
		return this.rows;
	}

	public void setRows(Vector<Vector<Object>> rows) {
		this.rows = rows;
	}

	public String[] getTableHead() {
		return this.tableHead;
	}

	public void setTableHead(String[] tableHead) {
		this.tableHead = tableHead;
	}
	
	public static void main(String[] args) {
		new MainFrame();
	}
}