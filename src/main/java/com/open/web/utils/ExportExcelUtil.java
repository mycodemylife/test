package com.open.web.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import com.open.web.anno.ExcelField;
import com.open.web.anno.ExcelHead;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;

public class ExportExcelUtil {

	// 2007 版本以上 最大支持1048576行
	public  final static String  EXCEL_FILE_2007 = "2007";
	// 2003 版本 最大支持65536 行
	public  final static String  EXCEL_FILE_2003 = "2003";
	
	/**
	 * 将对象数据，导出到表格并下载
	 * 
	 * @param fileName 文件名称(没有后缀)
	 * @param datas 数据内容的二维数组
	 * @param clazzs 数据对象对应得字节码数组
	 * @param response 响应对象
	 * @param version 表格版本
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static void exportExcel(String fileName, Object[][] datas,Class[] clazzs,HttpServletResponse response ,String version) throws Exception {
		
		String Suffix = null;
		if(EXCEL_FILE_2003.equals(version)) {
			Suffix = ".xls";
		}else if(EXCEL_FILE_2007.equals(version)) {
			Suffix = ".xlsx";
		}
		
		try {
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8") + Suffix);
			if(StringUtils.isBlank(version) || EXCEL_FILE_2003.equals(version.trim())){
				exportExcel2003(datas,clazzs,response.getOutputStream());
			}else {
				exportExcel2007(datas,clazzs,response.getOutputStream());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将对象数组导出到 2007版 Excel 中,并从网页上下载
	 *
	 * @param datas 数据对象数组
	 * @param clazzs 对象的字节码文件
	 * @param out 文件的输出流
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "resource", "rawtypes" })
	private static void exportExcel2007(Object[][] datas,Class[] clazzs,OutputStream out) throws Exception {
		
		if(datas != null && clazzs != null && datas.length != clazzs.length) {
			exportErrorXSSFWorkbook().write(out);
			closeOutputStream(out);
			throw new RuntimeException("数据数组的长度与字节码数组长度不相同");
		}
		
		// 声明一个工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		// 生成一个样式
		XSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(new XSSFColor(java.awt.Color.white));
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		XSSFFont font = workbook.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体"); 
		font.setColor(new XSSFColor(java.awt.Color.black));
		font.setFontHeightInPoints((short) 11);
		// 把字体应用到当前的样式
		style.setFont(font);
		
		XSSFCellStyle style2 = null;
		if(datas != null && datas.length > 0) {
			// 生成并设置另一个样式
			style2 = workbook.createCellStyle();
			style2.setFillForegroundColor(HSSFColor.WHITE.index);
			style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style2.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style2.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style2.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style2.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			// 生成另一个字体
			XSSFFont font2 = workbook.createFont();
			font2.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
			// 把字体应用到当前的样式
			style2.setFont(font2);
		}
		
		for(int k = 0 ; k < datas.length ; k++) {
			
			Object[] data = datas[k];
			Class clazz = clazzs[k];
			
			if(clazz == null) {
				exportErrorXSSFWorkbook().write(out);
				closeOutputStream(out);
				throw new RuntimeException("缺少字节码文件");
			}
			
			XSSFSheet sheet = null;
			String title = clazz.getName();
			ExcelHead excelHead = (ExcelHead) clazz.getAnnotation(ExcelHead.class);
			if(excelHead != null) {
				title = excelHead.value();
			}
			if(null != workbook.getSheet(title)) {
				sheet = workbook.getSheet(title);
			}else {
				sheet = workbook.createSheet(title);
			}
			// 设置表格默认列宽度为15个字节
			sheet.setDefaultColumnWidth(20);
			
			// 产生表格标题行
			XSSFRow row = sheet.createRow(0);
			XSSFCell cellHeader;
			
			Field[] fields = clazz.getDeclaredFields();
			
			String headTitle = null;
			for (int i = 0; i < fields.length; i++) {
				cellHeader = row.createCell(i);
				cellHeader.setCellStyle(style);
				headTitle = fields[i].getName();
				ExcelField excelField = fields[i].getAnnotation(ExcelField.class);
				if(excelField != null) {
					headTitle = excelField.value();
				}
				cellHeader.setCellValue(new XSSFRichTextString(headTitle));
			}
			
			if(data == null) {
				return;
			}
			
			String fieldName = null;
			String getMethodName = null;
			Object value = null;
			XSSFCell cell = null;
			String textValue = null;
			XSSFRichTextString richString = null;
			Pattern p = Pattern.compile("^(([1-9]\\d*\\.?\\d+)|(0\\.\\d*[1-9])|(\\d+))$");
			Pattern p1 = Pattern.compile("^(([1-9]\\d*\\.?\\d+)|(0\\.\\d*[1-9])|(\\d+))%$");
			Matcher matcher,matcher1;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for(int i = 0 ;i < data.length ; i++) {
				
				row = sheet.createRow(i+1);
				for(int j = 0 ; j < fields.length ; j ++) {
					cell = row.createCell(j);
					cell.setCellStyle(style2);
					
					try {
						fieldName = fields[j].getName();
						getMethodName =  "get" + fieldName.substring(0, 1).toUpperCase()
								+ fieldName.substring(1);
						Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
						value = getMethod.invoke(data[i], new Object[] {});
						
						if (value instanceof Integer) {
							cell.setCellValue((Integer) value);
						} else if (value instanceof Float) {
							textValue = String.valueOf((Float) value);
							cell.setCellValue(textValue);
						} else if (value instanceof Double) {
							textValue = String.valueOf((Double) value);
							cell.setCellValue(textValue);
						} else if (value instanceof Long) {
							cell.setCellValue((Long) value);
						}
						if (value instanceof Boolean) {
							textValue = "√";
							if (!(Boolean) value) {
								textValue = "×";
							}
						} else if (value instanceof Date) {
							textValue = sdf.format((Date) value);
						} else {
							// 其它数据类型都当作字符串简单处理
							if (value != null) {
								textValue = value.toString();
							}
						}
						if (textValue != null) {
							matcher = p.matcher(textValue);
							matcher1 = p1.matcher(textValue);
							if (matcher.find()) {
								// 是数字当作double处理
								cell.setCellValue(Double.parseDouble(textValue));
							} else if(matcher1.find()) {
								richString = new XSSFRichTextString(textValue);
								cell.setCellValue(richString);
							}else {
								richString = new XSSFRichTextString(textValue);
								cell.setCellValue(richString);
							}
						}
					}catch (Exception e) {
						exportErrorXSSFWorkbook().write(out);
						closeOutputStream(out);
						e.printStackTrace();
					}
				}
			}
			
			//调整列宽
			for(int i = 0 ; i < fields.length ; i++) {
				sheet.autoSizeColumn((short)i); 
			}
			
			//设置窗口冻结
			int[] freeze = excelHead.freeze();
			if(null != freeze && freeze.length == 2)
				sheet.createFreezePane(freeze[0],freeze[1]);
			else if(null != freeze && freeze.length == 4)
				sheet.createFreezePane(freeze[0],freeze[1],freeze[2],freeze[3]);
		}
		
		try {
			if(out != null) {
				workbook.write(out);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			closeOutputStream(out);
		}
	}
	
	/**
	 * 将对象数组导出到 2003版 Excel 中,并从网页上下载
	 *
	 * @param datas 数据对象数组
	 * @param clazzs 对象的字节码文件
	 * @param out 输出流
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "resource", "rawtypes" })
	private static void exportExcel2003(Object[][] datas,Class[] clazzs,OutputStream out) throws Exception {
		
		if(datas != null && clazzs != null && datas.length != clazzs.length) {
			exportErrorHSSFWorkbook().write(out);
			closeOutputStream(out);
			throw new RuntimeException("数据数组的长度与字节码数组长度不相同");
		}
		
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.WHITE.index);
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体"); 
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 11);
		// 把字体应用到当前的样式
		style.setFont(font);
		
		HSSFCellStyle style2 = null;
		if(datas != null && datas.length > 0) {
			// 生成并设置另一个样式
			style2 = workbook.createCellStyle();
			style2.setFillForegroundColor(HSSFColor.WHITE.index);
			style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style2.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style2.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style2.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style2.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			// 生成另一个字体
			HSSFFont font2 = workbook.createFont();
			font2.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
			// 把字体应用到当前的样式
			style2.setFont(font2);
		}
		
		for(int k = 0 ; k < datas.length ; k++) {
			
			Object[] data = datas[k];
			Class clazz = clazzs[k];
			
			if(clazz == null) {
				exportErrorHSSFWorkbook().write(out);
				closeOutputStream(out);
				throw new RuntimeException("缺少字节码文件");
			}
			
			HSSFSheet sheet = null;
			String title = clazz.getName();
			ExcelHead excelHead = (ExcelHead) clazz.getAnnotation(ExcelHead.class);
			if(excelHead != null) {
				title = excelHead.value();
			}
			if(null != workbook.getSheet(title)) {
				sheet = workbook.getSheet(title);
			}else {
				sheet = workbook.createSheet(title);
			}
			// 设置表格默认列宽度为15个字节
			sheet.setDefaultColumnWidth(20);
			
			// 产生表格标题行
			HSSFRow row = sheet.createRow(0);
			HSSFCell cellHeader;
			
			Field[] fields = clazz.getDeclaredFields();
			
			String headTitle = null;
			for (int i = 0; i < fields.length; i++) {
				cellHeader = row.createCell(i);
				cellHeader.setCellStyle(style);
				headTitle = fields[i].getName();
				ExcelField excelField = fields[i].getAnnotation(ExcelField.class);
				if(excelField != null) {
					headTitle = excelField.value();
				}
				cellHeader.setCellValue(new HSSFRichTextString(headTitle));
			}
			
			if(data == null) {
				return;
			}
			
			String fieldName = null;
			String getMethodName = null;
			Object value = null;
			HSSFCell cell = null;
			String textValue = null;
			HSSFRichTextString richString = null;
			Pattern p = Pattern.compile("^(([1-9]\\d*\\.?\\d+)|(0\\.\\d*[1-9])|(\\d+))$");
			Pattern p1 = Pattern.compile("^(([1-9]\\d*\\.?\\d+)|(0\\.\\d*[1-9])|(\\d+))%$");
			Matcher matcher,matcher1;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			
			for(int i = 0 ;i < data.length ; i++) {
				row = sheet.createRow(i+1);
				for(int j = 0 ; j < fields.length ; j ++) {
					cell = row.createCell(j);
					cell.setCellStyle(style2);
					
					try {
						fieldName = fields[j].getName();
						getMethodName =  "get" + fieldName.substring(0, 1).toUpperCase()
								+ fieldName.substring(1);
						Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
						value = getMethod.invoke(data[i], new Object[] {});
						
						if (value instanceof Integer) {
							cell.setCellValue((Integer) value);
						} else if (value instanceof Float) {
							textValue = String.valueOf((Float) value);
							cell.setCellValue(textValue);
						} else if (value instanceof Double) {
							textValue = String.valueOf((Double) value);
							cell.setCellValue(textValue);
						} else if (value instanceof Long) {
							cell.setCellValue((Long) value);
						}
						if (value instanceof Boolean) {
							textValue = "√";
							if (!(Boolean) value) {
								textValue = "×";
							}
						} else if (value instanceof Date) {
							textValue = sdf.format((Date) value);
						} else {
							// 其它数据类型都当作字符串简单处理
							if (value != null) {
								textValue = value.toString();
							}
						}
						if (textValue != null) {
							matcher = p.matcher(textValue);
							matcher1 = p1.matcher(textValue);
							if (matcher.find()) {
								// 是数字当作double处理
								cell.setCellValue(Double.parseDouble(textValue));
							} else if(matcher1.find()) {
								richString = new HSSFRichTextString(textValue);
								cell.setCellValue(richString);
							}else {
								richString = new HSSFRichTextString(textValue);
								cell.setCellValue(richString);
							}
						}
					}catch (Exception e) {
						exportErrorHSSFWorkbook().write(out);
						closeOutputStream(out);
						e.printStackTrace();
					}
				}
			}
			
			//调整列宽
			for(int i = 0 ; i < fields.length ; i++) {
				sheet.autoSizeColumn((short)i); 
			}
			
			//设置窗口冻结
			int[] freeze = excelHead.freeze();
			if(null != freeze && freeze.length == 2)
				sheet.createFreezePane(freeze[0],freeze[1]);
			else if(null != freeze && freeze.length == 4)
				sheet.createFreezePane(freeze[0],freeze[1],freeze[2],freeze[3]);
		}
		
		try {
			if(out != null) {
				workbook.write(out);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			closeOutputStream(out);
		}
	}
	
	private static XSSFWorkbook exportErrorXSSFWorkbook() {
		// 声明一个工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		XSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("Excel 导出失败！");
		return workbook;
	}
	
	private static HSSFWorkbook exportErrorHSSFWorkbook() {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("Excel 导出失败！");
		return workbook;
	}
	
	private static void closeOutputStream(OutputStream out) {
		if(out != null) {
			try {
				out.close();
			}catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}
}

















