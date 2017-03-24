package com.view.listener;

import java.awt.Toolkit;
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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import com.common.util.JsonUtil;
import com.common.util.LogUtil;
import com.netcap.captor.Netcaptor;
import com.protocol.http.bean.HttpDataBean;
import com.view.mainframe.MainFrame;
import com.view.mainframe.table.RowTableScrollPane;
import com.view.preference.PreferenceFrame;
import com.view.script.editor.ScriptEditFrame;
import com.view.util.ViewDataHandler;
import com.view.util.ViewModules;

import net.sf.json.JSONObject;

public class ActionListenerForMenu implements ActionListener {
	private Class<?> cl = ActionListenerForMenu.class;
	
	private MainFrame frame;
	private RowTableScrollPane scrollPane;
	private JTable table;

	public ActionListenerForMenu(MainFrame frame) {
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		this.scrollPane = frame.getScrollPane();
		this.table = frame.getScrollPane().getTable();
		switch(event.getActionCommand()){
		case "IMPORT":
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					openFile(frame);
				}
			});
			break;
//		case "SAVE":
//			saveDataToFile(frame, frame.getTitle(), getDataFromTable(table));
//			break;
		case "EXPORT":
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					saveDataToFile(frame, null, "data.dat", getDataFromTable(table));
				}
			});
			break;
		case "DELETE":
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					deleteSelectedData(frame);
				}
			});
			break;
		case "EXIT":
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					String title = "Exit Confirm Dialog";
					String content = "Do you really need to exit the applicateion?";
					int option = JOptionPane.showConfirmDialog(null, content, title, JOptionPane.YES_NO_CANCEL_OPTION);
					if(option == JOptionPane.YES_OPTION){
						frame.dispose();
					}
				}
			});
			break;
		case "START":
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Netcaptor.startCapture(frame);
					frame.getFrameMenuBar().getStartItem().setEnabled(false);
					frame.getFrameMenuBar().getStopItem().setEnabled(true);
				}
			});
			break;
		case "STOP":
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Netcaptor.stopCapture();
					frame.getFrameMenuBar().getStartItem().setEnabled(true);
					frame.getFrameMenuBar().getStopItem().setEnabled(false);
				}
			});
			break;
		case "OPENSCRIPT":
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Toolkit.getDefaultToolkit().setDynamicLayout(true);
					new ScriptEditFrame(frame).setVisible(true);
				}
			});
			break;
		case "PREFERENCE":
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new PreferenceFrame(frame);
				}
			});
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
		for(int i = 0; i < count; ){
			JCheckBox checkBox = ((JCheckBox)(table.getValueAt(i, 0)));
			if(checkBox.isSelected()){
				frame.getScrollPane().deleteRowFromTable(frame.getRows(), i);
				flag++;
			} else {
				if(Integer.valueOf(checkBox.getText()) != (i + 1))
					checkBox.setText(String.valueOf(i + 1));
				i++;
			}
			count = table.getRowCount();
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
		String fileName = ViewDataHandler.openFile(parent);
		List<String> dataList = null;
		if (fileName != null && fileName.length()>0) { // 单行文本框显示文件路径名
			File file = new File(fileName);
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
		classMap.put("rspBody", JSONObject.class);
		frame.getScrollPane().deleteRowFromTable(frame.getRows(), -1);
		for(String data : dataList){
			HttpDataBean bean = JsonUtil.jsonToBean(data, HttpDataBean.class, classMap);
			if(null == bean)
				ViewModules.showMessageDialog(null, "json To Bean has some error!");
			else
				frame.getScrollPane().addRowToTable(frame.getRows(), bean);
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
	private void saveDataToFile(JFrame parent, String filePath, String defaultFileName, String content){
		filePath = ViewDataHandler.saveDataToFile(parent, filePath, defaultFileName, content);
		refreshDataView(readDataFromFile(new File(filePath)));
		ViewModules.showMessageDialog(parent, "Data save is successed!");
	}

}
