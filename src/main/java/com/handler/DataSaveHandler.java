package com.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.common.helper.excel.ExcelHelper;
import com.common.helper.excel.HssfExcelHelper;
import com.common.helper.excel.JxlExcelHelper;
import com.common.util.FileUtil;
import com.common.util.LogUtil;
import com.generator.bean.DataForJavaBean;

public class DataSaveHandler {
	private static Class<?> cl = DataSaveHandler.class;

	private DataForJavaBean dataBean;
	
	public DataSaveHandler(DataForJavaBean dataBean) {
		this.dataBean = dataBean;
	}
	
	public synchronized void writeToExcel(String file, String sheetName){
		File srcFile = new File(file);
		String parentPath = srcFile.getParent();
		if(null != parentPath && !FileUtil.fileIsExists(parentPath))
			new File(parentPath).mkdirs();
//		ExcelHelper eh = HssfExcelHelper.getInstance(srcFile, sheetName);
		ExcelHelper eh = JxlExcelHelper.getInstance(srcFile, sheetName);
		List<DataForJavaBean> dataList = new ArrayList<DataForJavaBean>();
		dataList.add(dataBean);
        try {
			eh.writeExcel(DataForJavaBean.class, dataList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public synchronized void writeToExcel(String file, String sheetName, String[] columnNames) {
//		try {
//			File srcFile = new File(file);
//			String parentPath = srcFile.getParent();
//			if(null != parentPath && !FileUtil.fileIsExists(parentPath))
//				new File(parentPath).mkdirs();
//			ExcelWriterHelper book = ExcelWriterHelper.getBook(srcFile);
//			WritableSheet sheet = null;
//			if (null == book.getSheet(sheetName)) {
//				sheet = book.createSheet(sheetName, book.getNumberOfSheets());
//			} else {
//				sheet = book.getSheet(sheetName);
//			}
//			int rowCount = sheet.getRows();
//			if (rowCount < 1 || sheet.getColumns() < columnNames.length) {
//				for (int i = 0; i < columnNames.length; i++) {
//					Label label = new Label(i, 0, columnNames[i]);
//					sheet.addCell(label);
//				}
//				rowCount = rowCount + 1;
//			}
//			sheet.addCell(new Label(0, rowCount, dataBean.getCaseId() + rowCount));
//			sheet.addCell(new Label(1, rowCount, dataBean.getCaseDesc()));
//			sheet.addCell(new Label(2, rowCount, dataBean.getMethod()));
//			sheet.addCell(new Label(3, rowCount, dataBean.getUrl()));
//			sheet.addCell(new Label(4, rowCount, StringUtil.toString(dataBean.getReqHeader())));
//			sheet.addCell(new Label(5, rowCount, StringUtil.toString(dataBean.getReqParams())));
//			sheet.addCell(new Label(6, rowCount, StringUtil.toString(dataBean.getStatusCode())));
//			sheet.addCell(new Label(7, rowCount, dataBean.getReasonPhrase()));
//			sheet.addCell(new Label(8, rowCount, StringUtil.toString(dataBean.getRspHeader())));
//			sheet.addCell(new Label(9, rowCount, StringUtil.toString(dataBean.getRspBody())));
//			book.write();
//			book.close();
//			ExcelWriterHelper.deleteTempFile(srcFile);
//		} catch (WriteException | IOException e) {
//			e.printStackTrace();
//		}
//	}

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
