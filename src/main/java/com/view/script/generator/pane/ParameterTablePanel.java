package com.view.script.generator.pane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.common.Constants;
import com.common.JsonValidator;
import com.view.util.ScrollPaneTextArea;
import com.view.util.ViewModules;

import net.sf.json.JSONObject;

@SuppressWarnings("serial")
public class ParameterTablePanel extends JPanel {
	
	private JFrame parent;
	private DefaultTableModel tableModel; // 表格模型对象
	private JTable table;
	private String[][] tableVales;
	private String[] columnNames = { "Name", "Value" }; // 列名
	
	public ParameterTablePanel(JFrame parent, Object data){
		this.parent = parent;
		this.setLayout(ViewModules.getGridBagLayout(6, 10, 5, 5, 1.0, 1.0));
		setTableValues(data);
		JScrollPane scrollPane = getScrollTable();
		JToolBar toolBar = createToolBar();
		this.add(scrollPane, ViewModules.getGridBagConstraints(1, 1, 10, 5));
		this.add(toolBar, ViewModules.getGridBagConstraints(10, 6, 1, 1));
	}

	@SuppressWarnings("unchecked")
	public void setTableValues(Object data){
		if(null == data) return;
		if(data instanceof Map){
			Map<String, Object> dataMap = (Map<String, Object>)data;
			tableVales = new String[dataMap.size()][2];
			int index = 0;
			for(String name : dataMap.keySet()){
				tableVales[index][0] = name;
				tableVales[index][1] = String.valueOf(dataMap.get(name));
				index++;
			}
		} else if(data instanceof JSONObject){
			JSONObject json = (JSONObject) data;
			tableVales = new String[json.size()][2];
			int index = 0;
			for(Object name : json.keySet()){
				tableVales[index][0] = String.valueOf(name);
				tableVales[index][1] = String.valueOf(json.get(name));
				index++;
			}
		} else {
			if(data instanceof String){
				String str = (String) data;
				if(str.trim().length() < 1) return;
				boolean isJson = new JsonValidator().validate(str);
				if(isJson){
					JSONObject json = JSONObject.fromObject(str);
					tableVales = new String[json.size()][2];
					int index = 0;
					for(Object name : json.keySet()){
						tableVales[index][0] = String.valueOf(name);
						tableVales[index][1] = String.valueOf(json.get(name));
						index++;
					}
				} else {
					JOptionPane.showMessageDialog(parent, "data is not Map or JSONObject");
				}
			}
		}
		tableModel.setDataVector(tableVales, columnNames);
		table.addNotify();
	}
	
	private JScrollPane getScrollTable(){
		tableModel = new DefaultTableModel(tableVales, columnNames);
		table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 单选
		table.setFont(new Font(Font.SERIF, Font.PLAIN, 13));
		table.setRowHeight(15);
		table.setLayout(ViewModules.getGridBagLayout(5, 10, 5, 5, 1.0, 1.0));
		table.setPreferredScrollableViewportSize(new Dimension((int)(parent.getSize().getWidth()), 75));
		JScrollPane scrollPane = new JScrollPane(table); // 支持滚动
		this.setLayout(ViewModules.getGridBagLayout(5, 10, 5, 5, 1.0, 1.0));
		scrollPane.setViewportView(table);
		return scrollPane;
	}
	
	private JToolBar createToolBar(){
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		JButton detailButton = ViewModules.addToolButton(toolBar, new ParamsOperateAction("Detail", "DETAIL", Constants.DETAIL_ICON));
		toolBar.add(detailButton);
		JButton addButton = ViewModules.addToolButton(toolBar, new ParamsOperateAction("Add", "ADD", Constants.ADD_ICON));
		toolBar.add(addButton);
		JButton upButton = ViewModules.addToolButton(toolBar, new ParamsOperateAction("Up", "UP", Constants.UP_ICON));
		toolBar.add(upButton);
		JButton downButton = ViewModules.addToolButton(toolBar, new ParamsOperateAction("Down", "DOWN", Constants.DOWN_ICON));
		toolBar.add(downButton);
		JButton delButton = ViewModules.addToolButton(toolBar, new ParamsOperateAction("Delete", "DELETE", Constants.DELETE_ICON));
		toolBar.add(delButton);
		return toolBar;
	}
	
	private class ParamsOperateAction extends AbstractAction {
		public ParamsOperateAction(String name, String commond, URL iconUrl) {
			if(null != name) putValue(NAME, name);
			if(null != commond) putValue(ACTION_COMMAND_KEY, commond);
			if(null != iconUrl) putValue(SMALL_ICON, new ImageIcon(iconUrl));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = table.getSelectedRow();// 获得选中行的索引
			switch(e.getActionCommand()){
			case "DETAIL":
				if (selectedRow != -1) {// 是否存在选中行
					new NameValueDialog(parent, selectedRow);
				}
				break;
			case "ADD":
				String[] rowValues = {"", ""};
				tableModel.addRow(rowValues); // 添加一行
				Rectangle rect = table.getCellRect(table.getRowCount()-1, 0, true);  
				//table.repaint(); 若需要的话  
				//table.updateUI();若需要的话  
				table.scrollRectToVisible(rect);  
				break;
			case "UP":
				if(selectedRow > 0 && selectedRow < table.getRowCount()){
					tableModel.moveRow(selectedRow, selectedRow, selectedRow-1);
					selectedRow = selectedRow - 1;
					table.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
				}
				break;
			case "DOWN":
				if(selectedRow >= 0 && selectedRow < table.getRowCount()-1){
					tableModel.moveRow(selectedRow, selectedRow, selectedRow+1);
					selectedRow = selectedRow + 1;
					table.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
				}
				break;
			case "DELETE":
				if (selectedRow >= 0 && selectedRow < table.getRowCount()) { // 存在选中行
					tableModel.removeRow(selectedRow); // 删除行
					if(selectedRow < table.getRowCount())
						table.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
				}
				break;
			default:
				break;
			}
		}

	}

	
	
	class NameValueDialog extends JDialog implements ActionListener {
		private JTextField nameField;
		private ScrollPaneTextArea valueArea;
		private int rowIndex;
		public NameValueDialog(JFrame parent, int rowIndex) {
			super(parent);
			this.rowIndex = rowIndex;
			this.setLayout(ViewModules.getGridBagLayout(12, 4, 5, 5, 0.5, 1.0));
			this.setBounds(0, 0, 450, 500);
			
			JLabel nameLabel = ViewModules.createJLabel("Name:", Color.BLACK);
			nameField = ViewModules.createTextField(20, String.valueOf(table.getValueAt(rowIndex, 0)), true);
			this.add(nameLabel, ViewModules.getGridBagConstraints(0, 0, 1, 1));
			this.add(nameField, ViewModules.getGridBagConstraints(1, 0, 3, 1));
			
			JLabel valueLabel = ViewModules.createJLabel("Value:", Color.BLACK);
			valueArea = new ScrollPaneTextArea(10, String.valueOf(table.getValueAt(rowIndex, 1)));
			this.add(valueLabel, ViewModules.getGridBagConstraints(0, 1, 1, 1));
			this.add(valueArea, ViewModules.getGridBagConstraints(0, 2, 4, 9));
			
			JButton updateBtn = ViewModules.createButton("Update", "UPDATE", this);
			JButton previousBtn = ViewModules.createButton("Previous", "PREVIOUS", this);
			JButton nextBtn = ViewModules.createButton("Next", "NEXT", this);
			JButton closeBtn = ViewModules.createButton("Close", "CLOSE", this);
			this.add(updateBtn, ViewModules.getGridBagConstraints(0, 12, 1, 1));
			this.add(previousBtn, ViewModules.getGridBagConstraints(1, 12, 1, 1));
			this.add(nextBtn, ViewModules.getGridBagConstraints(2, 12, 1, 1));
			this.add(closeBtn, ViewModules.getGridBagConstraints(3, 12, 1, 1));
			this.setVisible(true);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			switch(e.getActionCommand()){
			case "UPDATE":
				table.setValueAt(nameField.getText(), rowIndex, 0);
				table.setValueAt(valueArea.getText(), rowIndex, 1);
				break;
			case "PREVIOUS":
				if(rowIndex > 0){
					rowIndex = rowIndex-1;
					nameField.setText(String.valueOf(table.getValueAt(rowIndex, 0)));
					valueArea.setText(String.valueOf(table.getValueAt(rowIndex, 1)));
				}
				break;
			case "NEXT":
				if((rowIndex+1) < table.getRowCount()){
					rowIndex = rowIndex+1;
					nameField.setText(String.valueOf(table.getValueAt(rowIndex, 0)));
					valueArea.setText(String.valueOf(table.getValueAt(rowIndex, 1)));
				}
				break;
			case "CLOSE":
				this.dispose();
				break;
			default:
				break;
			}
		}

	}
	
	/**
	 * 获取table中的数据
	 * @return
	 */
	public Map<String, String> getDataOfMap(){
		Map<String, String> data = new HashMap<String, String>();
		for(int i = 0; i < table.getRowCount(); i++){
			Object key = tableModel.getValueAt(i, 0);
			if(null != key && String.valueOf(key).trim().length() > 0){
				String value = String.valueOf(tableModel.getValueAt(i, 1));
				data.put(String.valueOf(key), value);
			}
		}
		return data;
	}
	
	/**
	 * 获取table中的数据
	 * @return
	 */
	public String getDataOfJsonString(){
		JSONObject json = new JSONObject();
		for(int i = 0; i < table.getRowCount(); i++){
			Object key = tableModel.getValueAt(i, 0);
			if(null != key && String.valueOf(key).trim().length() > 0){
				String value = String.valueOf(tableModel.getValueAt(i, 1));
				json.put(key, value);
			}
		}
		return json.toString();
	}
	
//	public static void main(String[] args) {
//		javax.swing.JFrame frame = new javax.swing.JFrame();
//		frame.setTitle("表格");
//		frame.setBounds(100, 100, 500, 400);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		ParameterTablePanel tablePanel = new ParameterTablePanel(frame, "{\"name\":\"test\",\"password\":\"123\"}");
//		frame.getContentPane().add(tablePanel, BorderLayout.CENTER);
//		frame.setVisible(true);
//	}

}
