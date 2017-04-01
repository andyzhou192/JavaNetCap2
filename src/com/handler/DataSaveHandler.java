package com.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.common.helper.ExcelWriterHelper;
import com.common.util.FileUtil;
import com.common.util.LogUtil;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

public class DataSaveHandler {
	private static Class<?> cl = DataSaveHandler.class;

	private String caseId, caseDesc, url, method, reqHeader, reqParams, statusCode, reasonPhrase, rspHeader, rspBody;

	public DataSaveHandler(Map<String, String> dataMap) {
		this.caseId = dataMap.get("CaseID").toString();
		this.caseDesc = dataMap.get("CaseDesc").toString();
		this.url = dataMap.get("URL").toString();
		this.method = dataMap.get("Method").toString();
		this.reqHeader = dataMap.get("ReqHeader").toString();
		this.reqParams = dataMap.get("ReqParams").toString();
		this.statusCode = dataMap.get("StatusCode").toString();
		this.reasonPhrase = dataMap.get("ReasonPhrase").toString();
		this.rspHeader = dataMap.get("RspHeader").toString();
		this.rspBody = dataMap.get("RspBody").toString();
	}

	public synchronized void writeToExcel(String file, String sheetName, String[] columnNames) {
		try {
			File srcFile = new File(file);
			String parentPath = srcFile.getParent();
			if(null != parentPath && !FileUtil.fileIsExists(parentPath))
				new File(parentPath).mkdirs();
			ExcelWriterHelper book = ExcelWriterHelper.getBook(srcFile);
			WritableSheet sheet = null;
			if (null == book.getSheet(sheetName)) {
				sheet = book.createSheet(sheetName, book.getNumberOfSheets());
			} else {
				sheet = book.getSheet(sheetName);
			}
			int rowCount = sheet.getRows();
			if (rowCount < 1 || sheet.getColumns() < columnNames.length) {
				for (int i = 0; i < columnNames.length; i++) {
					Label label = new Label(i, 0, columnNames[i]);
					sheet.addCell(label);
				}
				rowCount = rowCount + 1;
			}
			sheet.addCell(new Label(0, rowCount, caseId + rowCount));
			sheet.addCell(new Label(1, rowCount, caseDesc));
			sheet.addCell(new Label(2, rowCount, url));
			sheet.addCell(new Label(3, rowCount, method));
			sheet.addCell(new Label(4, rowCount, reqHeader));
			sheet.addCell(new Label(5, rowCount, reqParams));
			sheet.addCell(new Label(6, rowCount, statusCode));
			sheet.addCell(new Label(7, rowCount, reasonPhrase));
			sheet.addCell(new Label(8, rowCount, rspHeader));
			sheet.addCell(new Label(9, rowCount, rspBody));
			book.write();
			book.close();
			ExcelWriterHelper.deleteTempFile(srcFile);
		} catch (WriteException | IOException e) {
			e.printStackTrace();
		}
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
