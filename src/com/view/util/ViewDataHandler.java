package com.view.util;

import java.awt.FileDialog;
import java.io.File;
import javax.swing.JFrame;

import com.common.util.LogUtil;
import com.handler.DataSaveHandler;

public class ViewDataHandler {
	
	private static Class<?> cl = ViewDataHandler.class;
	
	/**
	 * 将数据保存到文件中
	 * @param parent
	 * @param filePath
	 * @param content
	 * @return
	 */
	public static String saveDataToFile(JFrame parent, String filePath, String defaultFileName, String content){
		boolean isSucc = false;
		if (filePath == null || filePath.trim().length() < 1){
			FileDialog fileDialog = new FileDialog(parent, "Save data to file", FileDialog.SAVE);
			fileDialog.setVisible(true);
			// 创建并显示保存文件对话框
			if ((fileDialog.getDirectory() != null) && (fileDialog.getFile() != null)) { // 单行文本框中显示文件路径和名称
				parent.setTitle(fileDialog.getDirectory() + fileDialog.getFile());
				// 文件对象的赋值
				File f = new File(fileDialog.getDirectory(), fileDialog.getFile());
				filePath = f.getAbsolutePath();
				isSucc = DataSaveHandler.save(f, content); // 调用自定义的save方法
				parent.setTitle(filePath);
				ViewModules.showMessageDialog(parent, "Data save : " + isSucc);
				LogUtil.debug(cl, "Saved:" + isSucc);
			}
		} else {
			isSucc = DataSaveHandler.save(new File(filePath), content); // 调用自定义的save方法
			ViewModules.showMessageDialog(parent, "Data save : " + isSucc);
			LogUtil.debug(cl, "Saved:" + isSucc);
		}
		return filePath;
	}
	
	/**
	 * 打开文件并读取数据
	 * @param parent
	 * @return
	 */
	public static String openFile(JFrame parent){
		String fileName = null;
		FileDialog fileDialog = new FileDialog(parent, "Open data from file", FileDialog.LOAD);
		fileDialog.setVisible(true); // 创建并显示打开文件对话框
		if ((fileDialog.getDirectory() != null) && (fileDialog.getFile() != null)) { // 单行文本框显示文件路径名
			parent.setTitle(fileDialog.getDirectory() + fileDialog.getFile());
			// 以缓冲区方式读取文件内容
			// 文件对象赋值
			File file = new File(fileDialog.getDirectory(), fileDialog.getFile());
			fileName = file.getAbsolutePath();
		} // 结束if文件不为空
		return fileName;
	}

}
