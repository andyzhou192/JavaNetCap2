package com.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.generator.bean.DataForJavaBean;
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
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					DataForJavaBean dataBean = scrollPane.getRowData(table.getSelectedRow());
					new GeneratorFrame(frame, dataBean);
				}
			});
			break;
		default:
			break;
		}
	}

}
