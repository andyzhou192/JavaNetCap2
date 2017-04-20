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

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
public class XssfExcelHelper extends ExcelHelper {
	private Class<?> cl = XssfExcelHelper.class;
 
    private static XssfExcelHelper instance = null; // 单例对象
 
    private File file; // 操作文件
    private Object sheetTag; // 目标sheet名称或索引
    private XSSFWorkbook workbook = null;
 
    /**
     * 私有化构造方法
     *
     * @param file
     *            文件对象
     */
    private XssfExcelHelper(File file, Object sheetTag) {
        super();
        this.file = file;
        if(file.exists()){
        	try {
        		FileInputStream fis = new FileInputStream(file);
				workbook = new XSSFWorkbook(fis);
				fis.close();
			} catch (Exception e) {
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
 
    /**
     * 获取单例对象并进行初始化
     *
     * @param file
     *            文件对象
     * @return 返回初始化后的单例对象
     */
    public static XssfExcelHelper getInstance(File file, Object sheetTag) {
        if (instance == null) {
            // 当单例对象为null时进入同步代码块
            synchronized (XssfExcelHelper.class) {
                // 再次判断单例对象是否为null，防止多线程访问时多次生成对象
                if (instance == null) {
                    instance = new XssfExcelHelper(file, sheetTag);
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
    public static XssfExcelHelper getInstance(String filePath, Object sheetTag) {
        return getInstance(new File(filePath), sheetTag);
    }
 
    @Override
    public <T> List<T> readExcel(Class<T> clazz, String[] fieldNames, boolean hasTitle) throws Exception {
        List<T> dataModels = new ArrayList<T>();
        try{
        	if(null == workbook || !file.getName().endsWith(".xlsx")) return dataModels;
        	
        	XSSFSheet sheet = getSheet(sheetTag);
        	if (null == sheet) {
        		return dataModels;
        	}
        	
        	int start = sheet.getFirstRowNum() + (hasTitle ? 1 : 0); // 如果有标题则从第二行开始
        	for (int i = start; i <= sheet.getLastRowNum(); i++) {
        		XSSFRow row = sheet.getRow(i);
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
        			XSSFCell cell = row.getCell(j);
        			if (cell == null) {
        				continue;
        			}
        			String content = getCellContent(cell);
        			// 如果属性是日期类型则将内容转换成日期对象
        			if (isDateType(clazz, fieldName)) {
        				// 如果属性是日期类型则将内容转换成日期对象
        				ReflectUtil.invokeSetter(target, fieldName,
        						DateUtil.parse(content));
        			} else {
        				Field field = clazz.getDeclaredField(fieldName);
        				ReflectUtil.invokeSetter(target, fieldName,
        						parseValueWithType(content, field.getType()));
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
        	if (null == workbook) {
        		workbook = new XSSFWorkbook();
        	}
        	
        	XSSFSheet sheet = getSheet(sheetTag);
			if (null == sheet) {
				// 根据当前工作表数量创建相应编号的工作表
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
        		T target = dataModels.get(i);
        		int rowIndex = sheet.getLastRowNum() + 1; // 总行数
        		XSSFRow row = sheet.createRow(rowIndex);
        		// 遍历属性列表
        		for (int j = 0; j < fieldNames.length; j++) {
        			// 通过反射获取属性的值域
        			String fieldName = fieldNames[j];
        			if (fieldName == null || UID.equals(fieldName)) {
        				continue; // 过滤serialVersionUID属性
        			}
        			Object result = ReflectUtil.invokeGetter(target, fieldName);
        			XSSFCell cell = row.createCell(j);
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
        	close(); // 不管是否有异常发生都关闭文件输出流
		}
    }
 
    @Override
    protected <T> Object parseValueWithType(String value, Class<?> type) {
        // 由于Excel2007的numeric类型只返回double型，所以对于类型为整型的属性，要提前对numeric字符串进行转换
        if (Byte.TYPE == type || Short.TYPE == type || Short.TYPE == type
                || Long.TYPE == type) {
            value = String.valueOf((long) Double.parseDouble(value));
        }
        return super.parseValueWithType(value, type);
    }
    
    @Override
	public <T> void updateExcelSingleRowData(Class<T> clazz, T dataModel, String content, int columnIndex) throws Exception {
    	try {
        	// 检测文件是否存在，如果存在则修改文件，否则创建文件
        	if (null == workbook) return;
        	
        	XSSFSheet sheet = getSheet(sheetTag);
        	if(null == sheet) return;
			
        	// 更新表格内容
        	int rowIndex = getRowIndex(sheet, content, columnIndex); // 总行数
        	XSSFRow row = sheet.getRow(rowIndex);
        	
        	String[] fieldNames = getFieldNames(clazz);
        	// 遍历属性列表
        	for (int j = 0; j < fieldNames.length; j++) {
        		// 通过反射获取属性的值域
        		String fieldName = fieldNames[j];
        		if (fieldName == null || UID.equals(fieldName)) {
        			continue; // 过滤serialVersionUID属性
        		}
        		Object result = ReflectUtil.invokeGetter(dataModel, fieldName);
        		XSSFCell cell = row.createCell(j);
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
     * 根据单元格内容及所属列索引，获取单元格的行索引
     * @param sheet
     * @param content
     * @param columnIndex
     * @return
     */
    public int getRowIndex(XSSFSheet sheet, String content, int columnIndex){
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
    public XSSFSheet getSheet(Object sheetTag) {
    	XSSFSheet sheet = null;
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
    public void setTableTitle(XSSFWorkbook workbook, XSSFSheet sheet, String...titles){
    	XSSFRow headRow = sheet.createRow(0);
    	// 添加表格标题
    	for (int i = 0; i < titles.length; i++) {
    		XSSFCell cell = headRow.createCell(i);
    		cell.setCellType(CellType.STRING);
    		cell.setCellValue(titles[i]);
    		// 设置字体加粗
    		XSSFCellStyle cellStyle = workbook.createCellStyle();
    		XSSFFont font = workbook.createFont();
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
    private String getCellContent(XSSFCell cell) {
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

	@Override
	public void deleteExcelSingleRowData(String content, int columnIndex) throws Exception {
		try {
        	// 检测文件是否存在，如果存在则修改文件，否则创建文件
        	if (null == workbook) return;
        	
        	XSSFSheet sheet = getSheet(sheetTag);
        	if(null == sheet) return;
			
        	// 更新表格内容
        	int rowIndex = getRowIndex(sheet, content, columnIndex); // 总行数
        	XSSFRow row = sheet.getRow(rowIndex);
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

}

