package com.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.common.helper.excel.ExcelHelper;
import com.common.helper.excel.HssfExcelHelper;
import com.common.helper.excel.XssfExcelHelper;
//import com.common.helper.excel.JxlExcelHelper;
import com.common.util.FileUtil;
import com.common.util.LogUtil;
import com.generator.bean.DataForJavaBean;

public class DataSaveHandler {
	private static Class<?> cl = DataSaveHandler.class;

	public static synchronized boolean appendToExcel(String file, Object sheetTag, DataForJavaBean dataBean){
		LogUtil.debug(cl, "data file : " + file);
		File srcFile = new File(file);
		String parentPath = srcFile.getParent();
		if(null != parentPath && !FileUtil.fileIsExists(parentPath))
			new File(parentPath).mkdirs();
		ExcelHelper eh = null;
		if(srcFile.getName().endsWith(".xls"))
			eh = HssfExcelHelper.getInstance(srcFile, sheetTag);
		else
			eh = XssfExcelHelper.getInstance(srcFile, sheetTag);
		List<DataForJavaBean> dataList = new ArrayList<DataForJavaBean>();
		dataList.add(dataBean);
        try {
			eh.writeExcel(DataForJavaBean.class, dataList);
		} catch (Exception e) {
			LogUtil.err(cl, e);
			return false;
		}
        return true;
	}
	
	public static synchronized boolean updateExcelSingleRowData(String file, Object sheetTag, DataForJavaBean dataBean, int columnIndex){
		LogUtil.debug(cl, "data file : " + file);
		File srcFile = new File(file);
		ExcelHelper eh = null;
		if(file.endsWith(".xls")){
			eh = HssfExcelHelper.getInstance(srcFile, sheetTag);
		} else {
			eh = XssfExcelHelper.getInstance(srcFile, sheetTag);
		}
        try {
			eh.updateExcelSingleRowData(DataForJavaBean.class, dataBean, dataBean.getCaseId(), columnIndex);
		} catch (Exception e) {
			LogUtil.err(cl, e);
			return false;
		}
        return true;
	}
	
	public static synchronized boolean deleteExcelSingleRowData(String file, Object sheetTag, String content, int columnIndex){
		LogUtil.debug(cl, "data file : " + file);
		File srcFile = new File(file);
		ExcelHelper eh = null;
		if(file.endsWith(".xls")){
			eh = HssfExcelHelper.getInstance(srcFile, sheetTag);
		} else {
			eh = XssfExcelHelper.getInstance(srcFile, sheetTag);
		}
        try {
			eh.deleteExcelSingleRowData(content, columnIndex);
		} catch (Exception e) {
			LogUtil.err(cl, e);
			return false;
		}
        return true;
	}
	
	public static synchronized List<DataForJavaBean> readExcel(String file, Object sheetTag){
		LogUtil.debug(cl, "data file : " + file);
		List<DataForJavaBean> dataList = new ArrayList<DataForJavaBean>();
		File srcFile = new File(file);
		String parentPath = srcFile.getParent();
		if(null != parentPath && !FileUtil.fileIsExists(parentPath))
			return dataList;
		ExcelHelper eh = null;
		if(srcFile.getName().endsWith(".xls"))
			eh = HssfExcelHelper.getInstance(srcFile, sheetTag);
		else
			eh = XssfExcelHelper.getInstance(srcFile, sheetTag);
		try {
			dataList = eh.readExcel(DataForJavaBean.class, true);
		} catch (Exception e) {
			LogUtil.err(cl, e);
			e.printStackTrace();
		}
		return dataList;
	}

	// 自定义的save方法，参数为文件对象
	public static boolean save(File file, String content) {
		LogUtil.debug(cl, file.getAbsolutePath());
		// 将文本区内容写入字符输出流
		try { // 文件写入通道连向文件对象
			FileWriter fw = new FileWriter(file, false);
			fw.write(content);// 写入多行文本框的内容
			fw.close(); // 关闭通道
			LogUtil.debug(cl, "properties is saved:" + true);
			return true;
		} catch (IOException ioe) { // 异常处理
			LogUtil.err(cl, ioe);
		}
		LogUtil.debug(cl, "properties is saved:" + false);
		return false;
	}

	// public static void main(String[] args) {
	// java.util.HashMap<String, Object> map = new java.util.HashMap<String,
	// Object>();
	// map.put("URL", "11111111111");
	// map.put("Method", "22222222222222");
	// map.put("ReqHeader", "3333333333333333");
	// map.put("ReqParams", "44444444444444444");
	// map.put("StatusCode", "55555555555555555555");
	// map.put("ReasonPhrase", "6666666666666666666666");
	// map.put("RspHeader", "777777777777777777777");
	// map.put("RspBody", "8888888888888888888");
	// }
}
