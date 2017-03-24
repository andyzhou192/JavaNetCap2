package com.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JTable;

import com.view.mainframe.MainFrame;
import com.view.mainframe.table.RowTableScrollPane;
import com.view.script.generator.GeneratorFrame;

public class ActionListenerForButton implements ActionListener {

	private MainFrame frame;
	private RowTableScrollPane scrollPane;
	private JTable table;
	
	public ActionListenerForButton(MainFrame frame) {
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.scrollPane = frame.getScrollPane();
		this.table = frame.getScrollPane().getTable();
		switch(e.getActionCommand()){
		case "DETAIL":
			Map<String, Object> dataMap = scrollPane.getRowData(table.getSelectedRow());
			new GeneratorFrame(frame, dataMap);
			break;
		default:
			break;
		}
	}

}
