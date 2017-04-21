package com.view.mainframe;

import java.awt.BorderLayout;
import java.util.Vector;

import com.view.mainframe.menu.FrameMenuBar;
import com.view.mainframe.table.RowTableScrollPane;
import com.view.preference.PropertyHelper;
import com.view.util.BaseFrame;
import com.view.util.StatusProgressPanel;
import com.view.util.ViewModules;

@SuppressWarnings("serial")
public class MainFrame extends BaseFrame {

	private RowTableScrollPane scrollPane = null;
	private FrameMenuBar menuBar;
	public StatusProgressPanel progress;

	private Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
	private String[] tableHead = {"ALL", "url", "method", "reqHeader", "reqParams", "statusCode", "reasonPhrase", "rspHeader", "rspBody", "Operate"};

	public MainFrame() {
		super();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(600, 600);
		PropertyHelper.loadProperties();
		
		menuBar = new FrameMenuBar(this);
		this.setJMenuBar(menuBar);
		
		Vector<Object> heads = ViewModules.createVector(this.getTableHead());
		this.scrollPane = new RowTableScrollPane(this, this.getRows(), heads, this);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		progress = new StatusProgressPanel();
		this.getContentPane().add(menuBar.createToolBar(), BorderLayout.NORTH);
		this.getContentPane().add(progress, BorderLayout.SOUTH);
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