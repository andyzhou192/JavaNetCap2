package com.common.helper.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import com.common.util.DateUtil;
import com.common.util.LogUtil;
import com.common.util.ReflectUtil;
import com.common.util.StringUtil;
 
/**
 * 基于POI实现的Excel工具类
 *
 * @author zhouyelin
 *
 */
public class HssfExcelHelper extends ExcelHelper {
	
	private Class<?> cl = HssfExcelHelper.class;
 
    private static HssfExcelHelper instance = null; // 单例对象
 
    private File file; // 操作文件
    private Object sheetTag; // 目标sheet名称或索引
    private HSSFWorkbook workbook = null;
 
    /**
     * 私有化构造方法
     *
     * @param file
     *            文件对象
     */
    private HssfExcelHelper(File file, Object sheetTag) {
        super();
        this.file = file;
        if(file.exists()){
        	try {
        		// 获取excel工作簿
        		FileInputStream fis = new FileInputStream(file);
				workbook = new HSSFWorkbook(fis);
				fis.close();
			} catch (IOException e) {
				LogUtil.err(cl, e);
			}
        }
        this.sheetTag = sheetTag;
    }
 
    public File getFile() {
        return file;
    }
 
    public void setFile(File file) {
        this.file = file;
    }

    public Object getSheetTag() {
		return sheetTag;
	}

	public void setSheetTag(Object sheetTag) {
		this.sheetTag = sheetTag;
	}
	
    /**
     * 获取单例对象并进行初始化
     *
     * @param file
     *            文件对象
     * @return 返回初始化后的单例对象
     */
    public static HssfExcelHelper getInstance(File file, Object sheetTag) {
        if (instance == null) {
            // 当单例对象为null时进入同步代码块
            synchronized (HssfExcelHelper.class) {
                // 再次判断单例对象是否为null，防止多线程访问时多次生成对象
                if (instance == null) {
                    instance = new HssfExcelHelper(file, sheetTag);
                }
            }
        } else {
            // 如果操作的文件对象不同，则重置文件对象
            if (!file.equals(instance.getFile())) {
                instance.setFile(file);
            }
        }
        return instance;
    }
 
    /**
     * 获取单例对象并进行初始化
     *
     * @param filePath
     *            文件路径
     * @return 返回初始化后的单例对象
     */
    public static HssfExcelHelper getInstance(String filePath, Object sheetTag) {
        return getInstance(new File(filePath), sheetTag);
    }
 
    @Override
    public <T> List<T> readExcel(Class<T> clazz, String[] fieldNames, boolean hasTitle) throws Exception {
        List<T> dataModels = new ArrayList<T>();
        
        try{
        	if(!file.exists() || !file.getName().endsWith(".xls")){
        		LogUtil.err(cl, "file is not exist : " + file.getAbsolutePath());
        		return dataModels;
        	}
        	HSSFSheet sheet = getSheet(sheetTag);
        	if(null == sheet) return dataModels;
        	
        	int start = sheet.getFirstRowNum() + (hasTitle ? 1 : 0); // 如果有标题则从第二行开始
        	for (int i = start; i <= sheet.getLastRowNum(); i++) {
        		HSSFRow row = sheet.getRow(i);
        		if (row == null) {
        			continue;
        		}
        		// 生成实例并通过反射调用setter方法
        		T target = clazz.newInstance();
        		for (int j = 0; j < fieldNames.length; j++) {
        			String fieldName = fieldNames[j];
        			if (fieldName == null || UID.equals(fieldName)) {
        				continue; // 过滤serialVersionUID属性
        			}
        			// 获取excel单元格的内容
        			HSSFCell cell = row.getCell(j);
        			if (cell == null) {
        				continue;
        			}
        			String content = getCellContent(cell);
        			// 如果属性是日期类型则将内容转换成日期对象
        			if (isDateType(clazz, fieldName)) {
        				// 如果属性是日期类型则将内容转换成日期对象
        				ReflectUtil.invokeSetter(target, fieldName, DateUtil.parse(content));
        			} else {
        				Field field = clazz.getDeclaredField(fieldName);
        				ReflectUtil.invokeSetter(target, fieldName, parseValueWithType(content, field.getType()));
        			}
        		}
        		dataModels.add(target);
        	}
        } finally{
        	close();
        }
        return dataModels;
    }
 
    @Override
    public <T> void writeExcel(Class<T> clazz, List<T> dataModels, String[] fieldNames, String[] titles) throws Exception {
        try {
        	// 检测文件是否存在，如果存在则修改文件，否则创建文件
        	if (null == workbook) workbook = new HSSFWorkbook();
        	
        	HSSFSheet sheet = getSheet(sheetTag);
        	if(null == sheet){
        		sheet = workbook.createSheet(StringUtil.toString(sheetTag));
        	}
			
			int rowCount = sheet.getLastRowNum(); // 最后一行的索引
            int colCount = 0; // 总列数
            if(sheet.getRow(0) != null){
            	colCount = sheet.getRow(0).getPhysicalNumberOfCells(); // 总列数
            }
            if(rowCount <= 0 || colCount != titles.length){
            	setTableTitle(workbook, sheet, titles);
            }
        	// 添加表格内容
        	for (int i = 0; i < dataModels.size(); i++) {
        		int rowIndex = sheet.getLastRowNum() + 1; // 总行数
        		HSSFRow row = sheet.createRow(rowIndex);
        		// 遍历属性列表
        		for (int j = 0; j < fieldNames.length; j++) {
        			// 通过反射获取属性的值域
        			String fieldName = fieldNames[j];
        			if (fieldName == null || UID.equals(fieldName)) {
        				continue; // 过滤serialVersionUID属性
        			}
        			Object result = ReflectUtil.invokeGetter(dataModels.get(i), fieldName);
        			HSSFCell cell = row.createCell(j);
        			cell.setCellValue(StringUtil.toString(result));
        			// 如果是日期类型则进行格式化处理
        			if (isDateType(clazz, fieldName)) {
        				cell.setCellValue(DateUtil.format((Date) result));
        			} else if("caseId-".equalsIgnoreCase(StringUtil.toString(result))){
        				cell.setCellValue(StringUtil.toString(result) + rowIndex);
                    }
        		}
        	}
        	FileOutputStream fos = new FileOutputStream(file);
        	// 将数据写到磁盘上
            workbook.write(fos);
            fos.flush();
            fos.close();
        } finally {
        	close();
		}
    }
    
    /**
     * 
     * @param clazz
     * @param dataModel
     * @param rowIndex
     * @throws Exception 
     */
    @Override
    public <T> void updateExcelSingleRowData(Class<T> clazz, T dataModel, String content, int columnIndex) throws Exception {
        try {
        	// 检测文件是否存在，如果存在则修改文件，否则创建文件
        	if (null == workbook) return;
        	
        	HSSFSheet sheet = getSheet(sheetTag);
        	if(null == sheet) return;
			
        	// 更新表格内容
        	int rowIndex = getRowIndex(sheet, content, columnIndex); // 总行数
        	HSSFRow row = sheet.getRow(rowIndex);
        	
        	String[] fieldNames = getFieldNames(clazz);
        	// 遍历属性列表
        	for (int j = 0; j < fieldNames.length; j++) {
        		// 通过反射获取属性的值域
        		String fieldName = fieldNames[j];
        		if (fieldName == null || UID.equals(fieldName)) {
        			continue; // 过滤serialVersionUID属性
        		}
        		Object result = ReflectUtil.invokeGetter(dataModel, fieldName);
        		HSSFCell cell = row.createCell(j);
        		cell.setCellValue(StringUtil.toString(result));
        		// 如果是日期类型则进行格式化处理
        		if (isDateType(clazz, fieldName)) {
        			cell.setCellValue(DateUtil.format((Date) result));
        		}
        	}
        	FileOutputStream fos = new FileOutputStream(file);
        	// 将数据写到磁盘上
            workbook.write(fos);
            fos.flush();
            fos.close();
        } finally {
        	close();
		}
    }
    
    /**
     * 
     * @param clazz
     * @param dataModel
     * @param rowIndex
     * @throws Exception 
     */
    @Override
    public void deleteExcelSingleRowData(String content, int columnIndex) throws Exception {
        try {
        	// 检测文件是否存在，如果存在则修改文件，否则创建文件
        	if (null == workbook) return;
        	
        	HSSFSheet sheet = getSheet(sheetTag);
        	if(null == sheet) return;
			
        	// 更新表格内容
        	int rowIndex = getRowIndex(sheet, content, columnIndex); // 总行数
        	HSSFRow row = sheet.getRow(rowIndex);
        	sheet.removeRow(row);
        	
        	FileOutputStream fos = new FileOutputStream(file);
        	// 将数据写到磁盘上
            workbook.write(fos);
            fos.flush();
            fos.close();
        } finally {
        	close();
		}
    }
    
    /**
     * 根据单元格内容及所属列索引，获取单元格的行索引
     * @param sheet
     * @param content
     * @param columnIndex
     * @return
     */
    public int getRowIndex(HSSFSheet sheet, String content, int columnIndex){
    	for(int i = 0; i <= sheet.getLastRowNum(); i++){
    		String cellContent = getCellContent(sheet.getRow(i).getCell(columnIndex));
    		if(cellContent.equalsIgnoreCase(content))
    			return i;
    	}
    	return -1;
    }
    
    /**
     * 
     * @throws IOException
     */
    public void close() throws IOException{
    	if (workbook != null)
    		workbook.close();
    	instance = null;
    }
    
    /**
     * 
     * @param workbook
     * @param sheetTag
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public HSSFSheet getSheet(Object sheetTag) {
    	HSSFSheet sheet = null;
    	if(null == workbook) return null;
    	int sheetNo = workbook.getNumberOfSheets();
		if (null == sheetTag) {
			// 根据当前工作表数量创建相应编号的工作表
			return null;
		} else if (sheetTag instanceof Integer){
			int sheetIndex = (Integer) sheetTag;
			if(sheetNo > 0 && sheetNo >= sheetIndex)
				sheet = workbook.getSheetAt((Integer) sheetTag);
			else
				return null;;
		} else if (sheetTag instanceof String){
			sheet = workbook.getSheet((String) sheetTag);
		}
		return sheet;
    }
    
    /**
     * 
     * @param workbook
     * @param sheet
     * @param titles
     */
    public void setTableTitle(HSSFWorkbook workbook, HSSFSheet sheet, String...titles){
    	HSSFRow headRow = sheet.createRow(0);
    	// 添加表格标题
    	for (int i = 0; i < titles.length; i++) {
    		HSSFCell cell = headRow.createCell(i);
    		cell.setCellType(CellType.STRING);
    		cell.setCellValue(titles[i]);
    		// 设置字体加粗
    		HSSFCellStyle cellStyle = workbook.createCellStyle();
    		HSSFFont font = workbook.createFont();
    		font.setBold(true);
    		cellStyle.setFont(font);
    		// 设置自动换行
    		cellStyle.setWrapText(true);
    		cell.setCellStyle(cellStyle);
    		// 设置单元格宽度
    		sheet.setColumnWidth(i, titles[i].length() * 1000);
    	}
    }
    
    /**
     * 获取单元格的内容
     *
     * @param cell
     *            单元格
     * @return 返回单元格内容
     */
    private String getCellContent(HSSFCell cell) {
        StringBuffer buffer = new StringBuffer();
        switch (cell.getCellTypeEnum()) {
            case NUMERIC : // 数字
                buffer.append(cell.getNumericCellValue());
                break;
            case BOOLEAN : // 布尔
                buffer.append(cell.getBooleanCellValue());
                break;
            case FORMULA : // 公式
                buffer.append(cell.getCellFormula());
                break;
            case STRING : // 字符串
                buffer.append(cell.getStringCellValue());
                break;
            case BLANK : // 空值
            case ERROR : // 故障
            default :
                break;
        }
        return buffer.toString();
    }

}

