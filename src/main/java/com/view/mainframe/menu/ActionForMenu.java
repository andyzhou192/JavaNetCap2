package com.view.mainframe.menu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.common.util.JsonUtil;
import com.common.util.LogUtil;
import com.common.util.StringUtil;
import com.generator.AbstractGenerator;
import com.generator.bean.DataForJavaBean;
import com.generator.bean.ScriptForJavaBean;
import com.generator.java.ScriptGenerator;
import com.generator.maven.MavenPomHelper;
import com.handler.DataSaveHandler;
import com.netcap.captor.CaptureThread;
import com.protocol.http.HttptHelper;
import com.view.mainframe.MainFrame;
import com.view.mainframe.table.RowTableScrollPane;
import com.view.preference.PreferenceFrame;
import com.view.preference.PropertyHelper;
import com.view.script.editor.ScriptEditFrame;
import com.view.script.generator.GeneratorFrame;
import com.view.util.ViewDataHandler;
import com.view.util.ViewModules;

import net.sf.json.JSONObject;

@SuppressWarnings("serial")
public class ActionForMenu extends AbstractAction {
	private Class<?> cl = ActionForMenu.class;
	
	private MainFrame frame;
	private RowTableScrollPane scrollPane;
	private JTable table;
	private CaptureThread captureThread;

	public ActionForMenu(MainFrame frame, String name, String commond, URL iconUrl) {
		if(null != name) putValue(NAME, name);
		if(null != commond) putValue(ACTION_COMMAND_KEY, commond);
		if(null != iconUrl) putValue(SMALL_ICON, new ImageIcon(iconUrl));
		this.frame = frame;
	}
	
	public CaptureThread getCaptureThread(){
		if(null == captureThread)
			captureThread = new CaptureThread("CaptureThread", frame);
		return captureThread;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.scrollPane = frame.getScrollPane();
		this.table = frame.getScrollPane().getTable();
		switch(e.getActionCommand()){
		case "Import":
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					frame.progress.startProgress("Import Data From File...");
					openFile(frame);
					frame.progress.stopProgress("Data has imported!");
				}
			});
			break;
		case "Export":
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					frame.progress.startProgress("Export Data...");
					saveDataToFile(frame, null, "data.json", getDataFromTable(table));
					frame.progress.stopProgress("Data has exported!");
				}
			});
			break;
		case "Delete":
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					frame.progress.startProgress("Delete data...");
					deleteSelectedData(frame);
					frame.progress.stopProgress("Data has deleted!");
				}
			});
			break;
		case "Exit":
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					String title = "Exit Confirm Dialog";
					String content = "Do you really need to exit the applicateion?";
					int option = JOptionPane.showConfirmDialog(null, content, title, JOptionPane.YES_NO_OPTION);
					if(option == JOptionPane.YES_OPTION){
						frame.dispose();
					}
				}
			});
			break;
		case "Start":
			getCaptureThread().start();
			frame.progress.startProgress("Capture Starting...");
			frame.getFrameMenuBar().setCaptureEnabled(false, true, false, true);
			break;
		case "Pause":
			getCaptureThread().suspend();
			frame.progress.startProgress("Capture Paused...");
			frame.getFrameMenuBar().setCaptureEnabled(false, false, true, false);
			break;
		case "Resume":
			getCaptureThread().resume();
			frame.progress.startProgress("Capture Resume...");
			frame.getFrameMenuBar().setCaptureEnabled(false, true, false, true);
			break;
		case "Stop":
			getCaptureThread().stop();
			frame.progress.stopProgress("Capture Stopped!");
			frame.getFrameMenuBar().setCaptureEnabled(true, false, false, false);
			break;
		case "OpenScript":
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					frame.progress.setStatus("Open Script From File!");
					Toolkit.getDefaultToolkit().setDynamicLayout(true);
					new ScriptEditFrame(frame).setVisible(true);
					frame.progress.setStatus("Script has opened");
				}
			});
			break;
		case "BatchGeneScript":
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					frame.progress.setStatus("Batch Generate Scripts...");
					MavenPomHelper.initMavenProject();
					batchGeneScript(getSelectedData(frame));
					frame.progress.setStatus("Scripts has Generated");
				}
			});
			break;
		case "CreateScript":
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					frame.progress.setStatus("Open Script From File!");
					new GeneratorFrame(frame, new DataForJavaBean());
					frame.progress.setStatus("Script has opened");
				}
			});
			break;
		case "Preference":
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
				scrollPane.deleteRowFromTable(i);
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
	 * 批量生成脚本
	 * @param dataList
	 */
	private void batchGeneScript(List<DataForJavaBean> dataList){
		for(DataForJavaBean dataBean : dataList){
			String url = StringUtil.toString(dataBean.getUrl());
			
			String interfaceName = StringUtil.firstCharUpperCase(HttptHelper.getInterfaceMethodName(url));
			ScriptForJavaBean bean = new ScriptForJavaBean();
			bean.setClassName(interfaceName);
			bean.setMethodName(interfaceName);
			ScriptGenerator generator = new ScriptGenerator(PropertyHelper.getTemplateDir(), PropertyHelper.getTemplateFile());
			generator.generateJavaFile(bean);
			
			String file = AbstractGenerator.getDataFilePath(bean.getPackageName(), bean.getClassName());
			String sheetName = bean.getMethodName();
			DataSaveHandler.appendToExcel(file, sheetName, dataBean);
		}
	}
	
	/**
	 * 获取选中行的数据
	 * @param frame
	 * @return
	 */
	private List<DataForJavaBean> getSelectedData(MainFrame frame){
		List<DataForJavaBean> list = new ArrayList<DataForJavaBean>();
		int count = table.getRowCount();
		for(int i = 0; i < count; i++){
			JCheckBox checkBox = ((JCheckBox)(table.getValueAt(i, 0)));
			if(checkBox.isSelected()){
				DataForJavaBean dataBean = scrollPane.getRowData(table.getSelectedRow());
				list.add(dataBean);
			} else {
				if(Integer.valueOf(checkBox.getText()) != (i + 1))
					checkBox.setText(String.valueOf(i + 1));
			}
		}
		if(list.isEmpty())
			ViewModules.showMessageDialog(frame, "Please choose the data you want to delete...");
		return list;
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
		frame.getScrollPane().deleteRowFromTable(-1);
		for(String data : dataList){
			DataForJavaBean bean = JsonUtil.jsonToBean(data, DataForJavaBean.class, classMap);
			if(null == bean)
				ViewModules.showMessageDialog(null, "json To Bean has some error!");
			else
				frame.getScrollPane().addRowToTable(bean);
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
				DataForJavaBean dataBean = scrollPane.getRowData(i);
//				String[] tableHead = frame.getTableHead();
//				String[] excludes = new String[]{tableHead[0], tableHead[tableHead.length - 1], "protocol"};
//				dataStr = dataStr + JsonUtil.mapToJson(dataBean, excludes) + "\r\n\r\n";
				dataStr = dataStr + JsonUtil.beanToJson(dataBean) + "\r\n\r\n";
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
		if(!StringUtil.validate(content)){
			JOptionPane.showMessageDialog(parent, "Please choose the data that you want to export..");
			return;
		}
		filePath = ViewDataHandler.saveDataToFile(parent, filePath, defaultFileName, content);
		refreshDataView(readDataFromFile(new File(filePath)));
		ViewModules.showMessageDialog(parent, "Data save is successed!");
	}

}
