package com.common.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.common.util.LogUtil;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.biff.WritableWorkbookImpl;

public class ExcelWriterHelper extends WritableWorkbookImpl{
	private static Class<?> cl = ExcelWriterHelper.class;
	
	public static FileInputStream fins;
	public static Workbook wb;
	
	protected ExcelWriterHelper(OutputStream os, boolean cs, WorkbookSettings ws) throws IOException {
		super(os, cs, ws);
	}
	
	protected ExcelWriterHelper(OutputStream os, Workbook w, boolean cs, WorkbookSettings ws) throws IOException{
		super(os, w, cs, ws);
	}

	public static ExcelWriterHelper getBook(File srcFile){
		File tarFile = getTempFile(srcFile);
		return getBook(srcFile, tarFile);
	}
	
	public static ExcelWriterHelper getBook(File sourcefile, File targetFile){
		LogUtil.debug(cl, "sourceFile:" + sourcefile + "; targetFile:" + targetFile);
		if(sourcefile.exists()){
			return copyBook(sourcefile, targetFile);
		} else {
			return createBook(sourcefile);
		}
	}
	
	public static ExcelWriterHelper createBook(File file){
		ExcelWriterHelper excelWriter = null;
		WorkbookSettings ws = new WorkbookSettings();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			excelWriter = new ExcelWriterHelper(fos, true, ws);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return excelWriter;
	}
	
	public static ExcelWriterHelper copyBook(File sourcefile, File targetFile){
		ExcelWriterHelper excelWriter = null;
		WorkbookSettings ws = new WorkbookSettings();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(targetFile);
			fins = new FileInputStream(sourcefile); 
			wb = Workbook.getWorkbook(fins); // 获得原始文档  
//			ExcelReader wb = ExcelReader.getExcelReader(sourcefile.getPath());
			excelWriter = new ExcelWriterHelper(fos, wb, true, ws); // 创建一个可读写的副本  
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return excelWriter;
	}
	
	public static File getTempFile(File srcFile){
		String fPath = srcFile.getAbsolutePath();
		String prefix = fPath.substring(0, fPath.lastIndexOf("."));
		String suffix = fPath.substring(fPath.lastIndexOf("."));
		return new File(prefix + "-temp" + suffix);
	}
	
	public static void deleteTempFile(File srcFile){
		ExcelWriterHelper.wb.close();
		ExcelWriterHelper.wb = null;
		try {
			ExcelWriterHelper.fins.close();
			ExcelWriterHelper.fins = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		File tempFile = getTempFile(srcFile);
		if(tempFile.exists()){
			boolean isDel = srcFile.delete();
			boolean isSucc = tempFile.renameTo(srcFile);
			LogUtil.debug(cl, srcFile.getAbsolutePath() + " is delete : " + isDel + ";" + tempFile.getAbsolutePath() + " is renamed:" + isSucc);
		}
	}

//	public static void main(String[] args) throws IOException, WriteException {
//		File srcFile = new File("src/data1.xls");
//		ExcelWriterHelper book = getBook(srcFile);
//		WritableSheet sheet = null;
//		if(book.getNumberOfSheets() < 1){
//			sheet = book.createSheet("data", 0);
//		} else {
//			sheet = book.getSheet(0);
//		}
//		if(sheet.getRows() < 1){
//			String[] titles = {"URL","Method","RequestHeader","RequestBody","RequestParameter","ResponseStatus","ResponseMessage","ResponseHeader","ResponseBody"};
//			for(int i = 0; i < titles.length; i++){
//				Label label = new Label(i, 0, titles[i]);
//				sheet.addCell(label);
//			}
//		}
//		WritableCell cell = sheet.getWritableCell(0, 1);
//		Label label01 = new Label(0, 1, cell.getContents() + ".baidu.com");
//		Label label11 = new Label(1, 1, "GET");
//		sheet.addCell(label01);
//		sheet.addCell(label11);
//		book.write();
//		book.close();
//		deleteTempFile(srcFile);
//	}
}
