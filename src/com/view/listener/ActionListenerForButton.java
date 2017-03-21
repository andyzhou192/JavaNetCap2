package com.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JTable;

import com.view.MainFrame;
import com.view.detail.DataDetailView;
import com.view.table.RowTableScrollPane;

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
			DataDetailView.showDialog(frame, dataMap);
			break;
		default:
			break;
		}
	}

}
