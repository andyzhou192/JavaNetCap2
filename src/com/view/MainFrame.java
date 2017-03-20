package com.view;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import com.common.Constants;
import com.view.menu.FrameMenuBar;
import com.view.table.RowTableScrollPane;
import com.view.util.ViewModules;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private RowTableScrollPane scrollPane = null;
	private FrameMenuBar menuBar;

	private Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
	private String[] tableHead = {"ALL", "url", "method", "reqHeader", "reqParams", "statusCode", "reasonPhrase", "rspHeader", "rspBody", "Operate"};


	public MainFrame() {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 600);
		Constants.initProperties(Constants.DEF_SET_PROP_FILE);
		
		menuBar = new FrameMenuBar(this);
		this.setJMenuBar(menuBar);
		
		Vector<Object> heads = ViewModules.createVector(this.getTableHead());
		this.scrollPane = new RowTableScrollPane(this.getRows(), heads, this);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JLabel statusLabel = new JLabel("zhouyelin@cmhi.chinamobile.com");
		this.getContentPane().add(statusLabel, BorderLayout.SOUTH);
		this.setVisible(true);
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