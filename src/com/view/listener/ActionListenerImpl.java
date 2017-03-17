package com.view.listener;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTable;

import com.common.util.JsonUtil;
import com.common.util.LogUtil;
import com.handler.DataSaveHandler;
import com.netcap.captor.Netcaptor;
import com.protocol.http.bean.HttpDataBean;
import com.view.MainFrame;
import com.view.detail.DataDetailView;
import com.view.preference.PreferenceDialog;
import com.view.table.RowTableScrollPane;
import com.view.util.ViewModules;

import net.sf.json.JSONObject;

public class ActionListenerImpl implements ActionListener {
	private Class<?> cl = ActionListenerImpl.class;
	
	private MainFrame frame;
	private RowTableScrollPane scrollPane;
	private JTable table;

	public ActionListenerImpl(MainFrame frame) {
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		this.scrollPane = frame.getScrollPane();
		this.table = frame.getScrollPane().getTable();
		switch(event.getActionCommand()){
		case "OPEN":
			openFile(frame);
			break;
		case "SAVE":
			saveDataToFile(frame, frame.getTitle(), getDataFromTable(table));
			break;
		case "SAVEAS":
			saveDataToFile(frame, null, getDataFromTable(table));
			break;
		case "DELETE":
			deleteSelectedData(frame);
			break;
		case "EXIT":
			System.exit(0);
			break;
		case "START":
			Netcaptor.startCapture(frame);
			frame.getFrameMenuBar().getStartItem().setEnabled(false);
			frame.getFrameMenuBar().getStopItem().setEnabled(true);
			break;
		case "STOP":
			Netcaptor.stopCapture();
			frame.getFrameMenuBar().getStartItem().setEnabled(true);
			frame.getFrameMenuBar().getStopItem().setEnabled(false);
			break;
		case "SETTING":
			new PreferenceDialog();
			break;
		case "DETAIL":
			Map<String, Object> dataMap = scrollPane.getRowData(table.getSelectedRow());
			DataDetailView.showDialog(dataMap);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 删除选中的行
	 * @param frame
	 */
	private void deleteSelectedData(MainFrame frame) {
		int flag = 0;
		int count = table.getRowCount();
		for(int i = 0; i < count; i++){
			JCheckBox checkBox = ((JCheckBox)(table.getValueAt(i, 0)));
			if(checkBox.isSelected()){
				flag++;
				frame.deleteRowFromTable(Integer.valueOf(checkBox.getText())-1);
				count--;
			}
		}
		if(flag == 0)
			ViewModules.showMessageDialog(frame, "Please choose the data you want to delete...");
	}

	/**
	 * 打开文件并读取数据
	 * @param parent
	 * @return
	 */
	private void openFile(JFrame parent){
		List<String> dataList = null;
		FileDialog fileDialog = new FileDialog(parent, "Open data from file", FileDialog.LOAD);
		fileDialog.setVisible(true); // 创建并显示打开文件对话框
		if ((fileDialog.getDirectory() != null) && (fileDialog.getFile() != null)) { // 单行文本框显示文件路径名
			parent.setTitle(fileDialog.getDirectory() + fileDialog.getFile());
			// 以缓冲区方式读取文件内容
			// 文件对象赋值
			File file = new File(fileDialog.getDirectory(), fileDialog.getFile());
			dataList = readDataFromFile(file);
		} // 结束if文件不为空
		refreshDataView(dataList);
	}
	
	private void refreshDataView(List<String> dataList){
		if(null == dataList) return;
		Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
		classMap.put("reqHeader", JSONObject.class);
		classMap.put("reqParams", JSONObject.class);
		classMap.put("rspHeader", JSONObject.class);
		classMap.put("statusCode", int.class);
		frame.deleteRowFromTable(-1);
		for(String data : dataList){
			HttpDataBean bean = JsonUtil.jsonToBean(data, HttpDataBean.class, classMap);
			if(null == bean)
				ViewModules.showMessageDialog(null, "json To Bean has some error!");
			else
				frame.addRowToTable(bean);
		}
	}

	/**
	 * 获取表中的数据
	 * @return
	 */
	private String getDataFromTable(JTable table) {
		String dataStr = "";
		for(int i = 0; i < table.getRowCount(); i++){
			JCheckBox checkBox = ((JCheckBox)(table.getValueAt(i, 0)));
			if(checkBox.isSelected()){
				Map<String, Object> oneData = scrollPane.getRowData(i);
				String[] tableHead = frame.getTableHead();
				String[] excludes = new String[]{tableHead[0], tableHead[tableHead.length - 1], "protocol"};
				dataStr = dataStr + JsonUtil.mapToJson(oneData, excludes) + "\r\n\r\n";
			}
		}
		return dataStr;
	}
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	private List<String> readDataFromFile(File file){
		List<String> dataList = new ArrayList<String>();
		try {
			// 文件读入通道连向文件对象
			FileReader fr = new FileReader(file);
			// 定义文件缓冲区
			BufferedReader br = new BufferedReader(fr);
			String aline;
			// 按行读取文本，每行附加在多行文本区之后
			String data = "";
			while ((aline = br.readLine()) != null){
				if(data.length() > 0){
					dataList.add(data);
					data = "";
				} else {
					data = data + aline.trim();
				}
			}
			if(data.length() > 0){
				dataList.add(data);
			}
			fr.close();
			br.close(); // 关闭文件缓冲区
		} catch (IOException ioe) {// 输入输出异常捕获
			ViewModules.showMessageDialog(frame, "import data has some error:" + ioe);
			LogUtil.err(cl, ioe);
		}
		return dataList;
	}
	
	/**
	 * 将数据保存到文件中
	 * @param parent
	 * @param filePath
	 * @param content
	 * @return
	 */
	private void saveDataToFile(JFrame parent, String filePath, String content){
		if (filePath == null || filePath.trim().length() < 1){
			String file = "data.dat";
			FileDialog fileDialog = new FileDialog(parent, "Save data to file", FileDialog.SAVE);
			fileDialog.setFile(file); // 缺省文件名
			fileDialog.setFile(new File(file).getName());
			fileDialog.setVisible(true);
			// 创建并显示保存文件对话框
			if ((fileDialog.getDirectory() != null) && (fileDialog.getFile() != null)) { // 单行文本框中显示文件路径和名称
				parent.setTitle(fileDialog.getDirectory() + fileDialog.getFile());
				// 文件对象的赋值
				File f = new File(fileDialog.getDirectory(), fileDialog.getFile());
				file = f.getAbsolutePath();
				DataSaveHandler.save(f, content); // 调用自定义的save方法
				frame.setTitle(file);
				refreshDataView(readDataFromFile(new File(file)));
				ViewModules.showMessageDialog(frame, "Data save is successed!");
			}
		} else {
			refreshDataView(readDataFromFile(new File(filePath)));
			ViewModules.showMessageDialog(frame, "Data save is successed!");
		}
	}

}
