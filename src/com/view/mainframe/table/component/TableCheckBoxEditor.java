package com.view.mainframe.table.component;

import java.awt.Component;  
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;  
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")  
public class TableCheckBoxEditor extends AbstractCellEditor implements TableCellEditor {  
	private JCheckBox checkBox;  
  
    public TableCheckBoxEditor(JTable table, int columnIndex) {  
        super();  
        TableColumnModel columnModel = table.getColumnModel();  
        columnModel.getColumn(columnIndex).setCellEditor(this);  
        columnModel.getColumn(columnIndex).setCellRenderer(new TableCellRendererImpl(checkBox));
    }
  
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {  
    	if(null == value) return null;
    	checkBox = (JCheckBox) value;
    	return checkBox;
    }  
  
    @Override
    public Object getCellEditorValue() {  
        return checkBox;
    }  
  
}  