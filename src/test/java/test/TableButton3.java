package test;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import java.util.Date;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author __USER__
 */
@SuppressWarnings({ "unused", "serial" })
public class TableButton3 extends javax.swing.JFrame {

	public TableButton3() {
		String[] columnNames = { "Date ", "String ", "Integer ", "Number ", " " };
		Object[][] data = { { new Date(), "A ", new Integer(1), new Double(5.1), "Delete0 " },
				{ new Date(), "B ", new Integer(2), new Double(6.2), "Delete1 " },
				{ new Date(), "C ", new Integer(3), new Double(7.3), "Delete2 " },
				{ new Date(), "D ", new Integer(4), new Double(8.4), "Delete3 " } };

		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		JTable table = new JTable(model);

		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane);
		ButtonColumn buttonsColumn = new ButtonColumn(table, 4);
	}

	public static void main(String[] args) {
		TableButton3 frame = new TableButton3();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}

	public class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
		JTable table;
		JButton renderButton;
		JButton editButton;
		String text;

		public ButtonColumn(JTable table, int column) {
			super();
			this.table = table;
			renderButton = new JButton();
			editButton = new JButton();
			editButton.setFocusPainted(false);
			editButton.addActionListener(this);

			TableColumnModel columnModel = table.getColumnModel();
			columnModel.getColumn(column).setCellRenderer(this);
			columnModel.getColumn(column).setCellEditor(this);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (hasFocus) {
				renderButton.setForeground(table.getForeground());
				renderButton.setBackground(UIManager.getColor("Button.background"));
			} else if (isSelected) {
				renderButton.setForeground(table.getSelectionForeground());
				renderButton.setBackground(table.getSelectionBackground());
			} else {
				renderButton.setForeground(table.getForeground());
				renderButton.setBackground(UIManager.getColor("Button.background"));
			}

			renderButton.setText((value == null) ? " " : value.toString());
			return renderButton;
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			text = (value == null) ? " " : value.toString();
			editButton.setText(text);
			return editButton;
		}

		public Object getCellEditorValue() {
			return text;
		}

		public void actionPerformed(ActionEvent e) {
			fireEditingStopped();
			System.out.println(e.getActionCommand() + "   :    " + table.getSelectedRow());
		}
	}
}
