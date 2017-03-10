package com.netcap.view;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import com.netcap.view.util.TableButtonEditor;
import com.netcap.view.util.TableCheckBoxEditor;

@SuppressWarnings("serial")
public class TableView extends JTable{

	public static JTable gettable(Object[][] data, Object[] header, String command, ActionListener listener) {  
        DefaultTableModel dm = new DefaultTableModel();  
        dm.setDataVector(data, header);  
  
        JTable table = new JTable(dm) {  
            public void tableChanged(TableModelEvent e) {  
                super.tableChanged(e); 
                System.out.println("table changed:" + e.getColumn() + ":" + e.getLastRow() + ":" + e.getFirstRow());
                repaint();  
            }  
        };  
        new TableCheckBoxEditor(table, 0);
        new TableButtonEditor(table, header.length -1, command, listener);
        return table;  
    }  
	
	public static JTable gettable(Vector<Vector<Object>> data, Vector<Object> header, String command, ActionListener listener) {  
        DefaultTableModel dm = new DefaultTableModel();  
        dm.setDataVector(data, header);  
  
        JTable table = new JTable(dm) {  
            public void tableChanged(TableModelEvent e) {  
                super.tableChanged(e); 
                System.out.println("table changed:" + e.getColumn() + ":" + e.getLastRow() + ":" + e.getFirstRow());
                repaint();  
            }  
        };  
        new TableCheckBoxEditor(table, 0);
        new TableButtonEditor(table, header.size() - 1, command, listener);
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
