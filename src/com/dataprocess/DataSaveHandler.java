package com.dataprocess;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.http.util.Asserts;

import com.db.excel.ExcelWriter;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

public class DataSaveHandler {
	
	private String[] titles = {"ID", "URL","Method","ReqHeader","ReqParams","StatusCode","ReasonPhrase","RspHeader","RspBody"};
	
	private String url, method, reqHeader, reqParams, statusCode, reasonPhrase, rspHeader, rspBody, file;
	
	public DataSaveHandler(Map<String, String> dataMap){
		this.url = dataMap.get("URL").toString();
		this.method = dataMap.get("Method").toString();
		this.reqHeader = dataMap.get("ReqHeader").toString();
		this.reqParams = dataMap.get("ReqParams").toString();
		this.statusCode = dataMap.get("StatusCode").toString();
		this.reasonPhrase = dataMap.get("ReasonPhrase").toString();
		this.rspHeader = dataMap.get("RspHeader").toString();
		this.rspBody = dataMap.get("RspBody").toString();
		this.file = getFileName();
	}
	
	private String getFileName(){
		Asserts.notEmpty(url, "url");
		String fileName = "";
		if(url.contains("?")){
			fileName = url.substring(url.lastIndexOf("/"), url.indexOf("?")).trim();
		} else if(url.trim().endsWith("/")){
			String temp = url.substring(0, url.lastIndexOf("/"));
			fileName = temp.substring(temp.lastIndexOf("/")).trim();
		} else {
			fileName = url.substring(url.lastIndexOf("/")).trim();
		}
		return "data" + fileName.replace(":", "-") + ".xls";
	}
	
	public synchronized void writeToExcel() {
		try{
			File srcFile = new File(file);
			ExcelWriter book = ExcelWriter.getBook(srcFile);
			WritableSheet sheet = null;
			if(book.getNumberOfSheets() < 1){
				sheet = book.createSheet("data", 0);
			} else {
				sheet = book.getSheet(0);
			}
			int rowCount = sheet.getRows();
			if(rowCount < 1 || !"ID".equals(sheet.getCell(0, 0).getContents())){
				for(int i = 0; i < titles.length; i++){
					Label label = new Label(i, 0, titles[i]);
					sheet.addCell(label);
				}
				rowCount = rowCount + 1;
			}
			sheet.addCell(new Label(0, rowCount, "Case" + rowCount)); 
			sheet.addCell(new Label(1, rowCount, url)); 
			sheet.addCell(new Label(2, rowCount, method)); 
			sheet.addCell(new Label(3, rowCount, reqHeader)); 
			sheet.addCell(new Label(4, rowCount, reqParams)); 
			sheet.addCell(new Label(5, rowCount, statusCode)); 
			sheet.addCell(new Label(6, rowCount, reasonPhrase)); 
			sheet.addCell(new Label(7, rowCount, rspHeader)); 
			sheet.addCell(new Label(8, rowCount, rspBody)); 
			book.write();
			book.close();
			ExcelWriter.deleteTempFile(srcFile);
		} catch(WriteException | IOException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		java.util.HashMap<String, Object> map = new java.util.HashMap<String, Object>();
		map.put("URL", "11111111111");
		map.put("Method", "22222222222222");
		map.put("ReqHeader", "3333333333333333");
		map.put("ReqParams", "44444444444444444");
		map.put("StatusCode", "55555555555555555555");
		map.put("ReasonPhrase", "6666666666666666666666");
		map.put("RspHeader", "777777777777777777777");
		map.put("RspBody", "8888888888888888888");
	}
}
