package com.view.mainframe.table;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import com.common.Constants;
import com.common.util.JsonUtil;
import com.common.util.LogUtil;
import com.common.util.StringUtil;
import com.generator.bean.DataForJavaBean;
import com.view.mainframe.MainFrame;
import com.view.mainframe.table.component.TableButtonEditor;
import com.view.mainframe.table.component.TableCheckBoxEditor;
import com.view.script.generator.GeneratorFrame;
import com.view.util.ViewModules;

@SuppressWarnings("serial")
public class RowTableScrollPane extends JScrollPane {
	
	private MainFrame parent;
	// data table
	private JTable table = null;
	
	public RowTableScrollPane(MainFrame parent, Vector<Vector<Object>> rows, Vector<Object> header, MainFrame frame) {
		this.parent = parent;
		initTable(rows, header);
		this.setViewportView(this.getTable());
		this.setAutoscrolls(true);
	}
	
	public void addNotifyTable(){
		if(null != this.getTable()){
			this.getTable().addNotify();
		} else {
			ViewModules.showMessageDialog(parent, "data import failed!");
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
		this.table.setAutoscrolls(true);
		this.table.getColumnModel().getColumn(0).setWidth(20);
		new TableCheckBoxEditor(this.table, 0);
		new TableButtonEditor(this.table, header.size() - 1);
		this.table.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				mouseRightButtonClick(evt);
			}
		});
		this.table.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
			boolean isSelectedAll = false;
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if(table.getSelectedColumn() == 0){
					if(isSelectedAll){
						for(Vector<Object> row : parent.getRows()){
							((JCheckBox)(row.get(0))).setSelected(false);
						}
						isSelectedAll = false;
					} else {
						for(Vector<Object> row : parent.getRows()){
							((JCheckBox)(row.get(0))).setSelected(true);
						}
						isSelectedAll = true;
					}
				} 
			}
		});
	}
	
	// 鼠标右键点击事件
	private void mouseRightButtonClick(java.awt.event.MouseEvent evt) {
		// 判断是否为鼠标的BUTTON3按钮，BUTTON3为鼠标右键
		if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
			// 通过点击位置找到点击为表格中的行
			int focusedRowIndex = table.rowAtPoint(evt.getPoint());
			if (focusedRowIndex == -1) {
				return;
			}
			// 将表格所选项设为当前右键点击的行
			table.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
			// 弹出菜单
			createPopupMenu().show(table, evt.getX(), evt.getY());
		}
	}

	private JPopupMenu createPopupMenu() {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem delMenItem = new JMenuItem("Delete");
		delMenItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// 该操作需要做的事
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						parent.getRows().removeElementAt(table.getSelectedRow());
					}
				});
			}
		});
		JMenuItem detMenItem = new JMenuItem("Detail");
		detMenItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// 该操作需要做的事
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						DataForJavaBean dataBean = getRowData(table.getSelectedRow());
						new GeneratorFrame(parent, dataBean);
					}
				});
			}
		});
		popupMenu.add(delMenItem);
		popupMenu.add(detMenItem);
		return popupMenu;
	}
	
	/**
	 * 获取主视图表中数据
	 * @param rowIndex
	 * @return
	 */
	public DataForJavaBean getRowData(int rowIndex){
		DataForJavaBean dataBean = new DataForJavaBean();
		dataBean.setUrl(StringUtil.toString(this.getTable().getModel().getValueAt(rowIndex, 1)));
		dataBean.setMethod(StringUtil.toString(this.getTable().getModel().getValueAt(rowIndex, 2)));
		dataBean.setReqHeader(JsonUtil.getJson(this.getTable().getModel().getValueAt(rowIndex, 3)));
		dataBean.setReqParams(JsonUtil.getJson(this.getTable().getModel().getValueAt(rowIndex, 4)));
		dataBean.setStatusCode(Integer.valueOf(StringUtil.toString(this.getTable().getModel().getValueAt(rowIndex, 5))));
		dataBean.setReasonPhrase(StringUtil.toString(this.getTable().getModel().getValueAt(rowIndex, 6)));
		dataBean.setRspHeader(JsonUtil.getJson(this.getTable().getModel().getValueAt(rowIndex, 7)));
		dataBean.setRspBody(StringUtil.toString(this.getTable().getModel().getValueAt(rowIndex, 8)));
		return dataBean;
	}
	
	/**
	 * 在table中新增一行
	 * @param data
	 */
	public void addRowToTable(DataForJavaBean data){
		Object[] values = {new JCheckBox(String.valueOf(parent.getRows().size() + 1)), 
				data.getUrl(), data.getMethod(), data.getReqHeader(), data.getReqParams(), 
				data.getStatusCode(), data.getReasonPhrase(), data.getRspHeader(), data.getRspBody(), 
				getButton()};
		Vector<Object> r = ViewModules.createVector(values);
		parent.getRows().addElement(r);
		this.addNotifyTable();
	}
	
	public JButton getButton(){
		JButton button = new JButton(new ViewDetailAction(parent, "Detail", Constants.DETAIL_ICON));//ViewModules.createButton("Detail", "SAVE", Constants.DETAIL_ICON, this);
		button.setEnabled(true);
		button.setVisible(true);
		return button;
	}
	
	/**
	 * 从table中删除指定行
	 * @param rowIndex 行索引，为-1时表示删除所有行
	 */
	public void deleteRowFromTable(int rowIndex){
		if(-1 == rowIndex){
			if(parent.getRows().size() > 0)
				parent.getRows().removeAllElements();
			else
				return;
		} else {
			parent.getRows().removeElementAt(rowIndex);
		}
		this.addNotifyTable();
	}

	public JTable getTable() {
		return this.table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}
	

	private class ViewDetailAction extends AbstractAction{
		private MainFrame frame;
		private RowTableScrollPane scrollPane;
		private JTable table;
		
		public ViewDetailAction(MainFrame frame, String name, URL iconUrl){
			putValue(NAME, name);
			putValue(SMALL_ICON, new ImageIcon(iconUrl));
			this.frame = frame;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			this.scrollPane = frame.getScrollPane();
			this.table = frame.getScrollPane().getTable();
			switch(((JButton)e.getSource()).getText()){
			case "Detail":
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						DataForJavaBean dataBean = scrollPane.getRowData(table.getSelectedRow());
						LogUtil.debug(RowTableScrollPane.class, dataBean.toJson());
						new GeneratorFrame(frame, dataBean);
					}
				});
				break;
			default:
				break;
			}
		}
	}

}
