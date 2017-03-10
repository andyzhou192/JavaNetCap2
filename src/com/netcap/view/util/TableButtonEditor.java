package com.netcap.view.util;

import java.awt.Component;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
  
import javax.swing.AbstractCellEditor;  
import javax.swing.JButton;  
import javax.swing.JTable;  
import javax.swing.table.TableCellEditor;  
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class TableButtonEditor extends AbstractCellEditor implements TableCellEditor {  
    private JTable table;  
    private JButton button;  
    private String text;  
  
    public TableButtonEditor(JTable table, int column, String command, ActionListener listener) {  
        super();  
        this.table = table;  
        button = new JButton();  
        button.addActionListener(listener);  
        button.setActionCommand(command);
  
        TableColumnModel columnModel = table.getColumnModel();  
        columnModel.getColumn(column).setCellEditor(this);  
        columnModel.getColumn(column).setCellRenderer(new TableCellRendererImpl(TableCellRendererImpl.BUTTON));
    }  
  
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {  
        text = (value == null) ? "" : value.toString();  
        button.setText(text);  
        return button;  
    }  
  
    public Object getCellEditorValue() {  
        return text;  
    }  
  
    public void actionPerformed(ActionEvent e) {  
        fireEditingStopped();  
        System.out.println(e.getActionCommand() + " : " + table.getSelectedRow());  
    }  
}  