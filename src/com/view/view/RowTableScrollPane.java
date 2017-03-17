package com.view.view;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import com.view.util.TableButtonEditor;
import com.view.util.TableCheckBoxEditor;

@SuppressWarnings("serial")
public class RowTableScrollPane extends JScrollPane {
	// data table
	protected JTable table = null;

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public RowTableScrollPane(Vector<Vector<Object>> data, Vector<Object> header) {
		this.table = getTable(data, header);
		this.setViewportView(table);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	}

	public void addNotifyTable(){
		if(null != table)
			table.addNotify();
	}
	
	public JTable getTable(Vector<Vector<Object>> data, Vector<Object> header) {  
        DefaultTableModel dm = new DefaultTableModel();  
        dm.setDataVector(data, header);  
        JTable table = new JTable(dm) {  
            public void tableChanged(TableModelEvent e) {  
                super.tableChanged(e); 
                repaint();  
            }  
        };
        table.setRowHeight(20);
        table.setGridColor(Color.DARK_GRAY);
        table.getTableHeader().setBackground(new Color(200, 200, 200));
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setFont(new Font(Font.SANS_SERIF, 1, 12));
        new TableCheckBoxEditor(table, 0);
        new TableButtonEditor(table, header.size() - 1);
        return table;  
    }
	
	public static Map<String, Object> getRowData(JTable table, int rowIndex){
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i = 0; i < table.getColumnCount(); i++){
			String key = table.getModel().getColumnName(i);
			Object value = table.getModel().getValueAt(rowIndex, i);
			map.put(key, value);
		}
		return map;
	}

}
