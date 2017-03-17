package com.view.table.component;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class TableCellRendererImpl implements TableCellRenderer {
	
	private Component comp;
	
	public TableCellRendererImpl(Component comp){
		this.comp = comp;
	}
	
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if(null == value) return null;
		this.comp = (Component)value;
		if (hasFocus) {
			this.comp.setForeground(table.getForeground());
			this.comp.setBackground(UIManager.getColor("Button.background"));
		} else if (isSelected) {
			this.comp.setForeground(table.getSelectionForeground()); //table.getSelectionForeground()
			this.comp.setBackground(table.getSelectionBackground()); //table.getSelectionBackground()
		} else {
			this.comp.setForeground(table.getForeground()); //table.getForeground()
			this.comp.setBackground(UIManager.getColor("Button.background")); //UIManager.getColor("Button.background")
		}
		if(this.comp instanceof JButton){
			JButton button = (JButton) this.comp;
			return button;
		} else if(this.comp instanceof JCheckBox){
			return (JCheckBox) this.comp;
		} else {
			JLabel label = (JLabel) this.comp;
			label.setText((value == null) ? "" : value.toString());
			return label;
		}
	}
}  