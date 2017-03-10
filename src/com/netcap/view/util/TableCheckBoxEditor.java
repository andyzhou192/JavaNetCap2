package com.netcap.view.util;

import java.awt.Component;  
import java.awt.event.ItemEvent;  
import java.awt.event.ItemListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;  
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")  
public class TableCheckBoxEditor extends AbstractCellEditor implements TableCellEditor, ItemListener {  
	private JTable table; 
	private JCheckBox checkBox;  
  
    public TableCheckBoxEditor(JTable table, int column) {  
        super();  
        this.table = table; 
        TableColumnModel columnModel = table.getColumnModel();  
        columnModel.getColumn(column).setCellEditor(this); 
        columnModel.getColumn(column).setCellRenderer(new TableCellRendererImpl(TableCellRendererImpl.CHECK_BOX));
    }
  
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {  
        if (value == null)  
            return null;  
        checkBox = (JCheckBox) value;  
        checkBox.addItemListener(this);  
        return (Component) value;  
    }  
  
    public Object getCellEditorValue() {  
        checkBox.removeItemListener(this);  
        return checkBox;  
    }  
  
    public void itemStateChanged(ItemEvent e) {  
        super.fireEditingStopped();  
        System.out.println("selected:" + e.getStateChange() + " : "  + table.getSelectedRow()); 
    }  
}  