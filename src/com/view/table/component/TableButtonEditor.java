package com.view.table.component;

import java.awt.Component;  
import javax.swing.AbstractCellEditor;  
import javax.swing.JButton;  
import javax.swing.JTable;  
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class TableButtonEditor extends AbstractCellEditor implements TableCellEditor {  
    private JButton button;  
  
    public TableButtonEditor(JTable table, int columnIndex) {  
        super(); 
        //button = new JButton();
        TableColumnModel columnModel = table.getColumnModel();  
        columnModel.getColumn(columnIndex).setCellEditor(this);  
        columnModel.getColumn(columnIndex).setCellRenderer(new TableCellRendererImpl(button));
    } 
  
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {  
    	if(null == value) return null;
    	button = (JButton) value;
    	return button;
    }  
  
    @Override
    public Object getCellEditorValue() {  
        return button;  
    }  
  
}  