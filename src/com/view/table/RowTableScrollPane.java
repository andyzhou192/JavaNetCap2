package com.view.table;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import com.view.table.component.TableButtonEditor;
import com.view.table.component.TableCheckBoxEditor;
import com.view.util.ViewModules;

@SuppressWarnings("serial")
public class RowTableScrollPane extends JScrollPane {
	
	// data table
	private JTable table = null;
	
	public RowTableScrollPane(Vector<Vector<Object>> data, Vector<Object> header) {
		initTable(data, header);
		this.setViewportView(this.getTable());
		this.setAutoscrolls(true);
		//this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	}
	
	public void addNotifyTable(){
		if(null != this.getTable()){
			this.getTable().addNotify();
			this.getTable().updateUI();
			this.validate();
		} else {
			ViewModules.showMessageDialog(null, "data import failed!");
		}
	}
	
	public void initTable(Vector<Vector<Object>> data, Vector<Object> header) {  
        DefaultTableModel dm = new DefaultTableModel();  
        dm.setDataVector(data, header);  
        this.table = new JTable(dm) {  
            public void tableChanged(TableModelEvent e) {  
                super.tableChanged(e); 
                repaint();  
            }  
        };
        this.table.setRowHeight(20);
        this.table.setGridColor(Color.DARK_GRAY);
        this.table.getTableHeader().setBackground(new Color(200, 200, 200));
        this.table.getTableHeader().setForeground(Color.BLACK);
        this.table.getTableHeader().setFont(new Font(Font.SANS_SERIF, 1, 12));
        new TableCheckBoxEditor(this.table, 0);
        new TableButtonEditor(this.table, header.size() - 1);
    }
	
	public Map<String, Object> getRowData(int rowIndex){
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i = 0; i < this.getTable().getColumnCount(); i++){
			String key = this.getTable().getModel().getColumnName(i);
			Object value = this.getTable().getModel().getValueAt(rowIndex, i);
			map.put(key, value);
		}
		return map;
	}

	public JTable getTable() {
		return this.table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

}
