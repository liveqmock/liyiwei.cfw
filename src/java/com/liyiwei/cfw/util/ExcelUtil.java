/*
 * Created on 2013-04-26 15:42:00
 *
 */
package com.liyiwei.cfw.util;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.liyiwei.common.util.FileUtil;
import com.liyiwei.common.util.StringUtil;

/**
 * @author Liyiwei
 * 
 */
public class ExcelUtil {
	public static Workbook getWorkbook(String filename) throws Exception {
		FileInputStream in = new FileInputStream(new File(filename));
		Workbook workbook = null;
		if (FileUtil.getFileExt(filename).equals("xlsx")) {
			workbook = new XSSFWorkbook(in);
		} else {
			POIFSFileSystem fileSystem = new POIFSFileSystem(in);
			workbook = new HSSFWorkbook(fileSystem);
		}
		return workbook;
	}

	public static String getCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}

		String cellValue = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			cellValue = StringUtil.toString(cell.getRichStringCellValue().getString());
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			cellValue = StringUtil.toString(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			cellValue = "";
			break;
		case Cell.CELL_TYPE_FORMULA:
			cellValue = "";
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				cellValue = StringUtil.formatDate(cell.getDateCellValue());
			} else {
				cellValue = StringUtil.toString(cell.getNumericCellValue());
				if (cellValue.endsWith(".0")) {
					cellValue = cellValue.substring(0, cellValue.length() - 2);
				}
			}
			break;
		case Cell.CELL_TYPE_STRING:
			cellValue = StringUtil.toString(cell.getRichStringCellValue().getString());
			break;
		default:
			cellValue = StringUtil.toString(cell.getRichStringCellValue().getString());
		}

		// System.out.println(cellValue);
		return cellValue.trim();
	}

	public static void createCell(HSSFRow row, int col, HSSFCellStyle cellstyle, String val) {
		HSSFCell cell = row.createCell(col);
		cell.setCellType(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue(new HSSFRichTextString(val));
		cell.setCellStyle(cellstyle);
	}

	public static HSSFCellStyle createCell(HSSFWorkbook wb, int cellType) {
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 10);

		Font font1 = wb.createFont();
		font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font1.setColor(HSSFColor.WHITE.index);
		font1.setFontName("宋体");
		font.setFontHeightInPoints((short) 10);

		HSSFCellStyle cellstyle1 = wb.createCellStyle();
		cellstyle1.setFont(font1);
		cellstyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellstyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellstyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellstyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellstyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellstyle1.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
		cellstyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellstyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		HSSFCellStyle cellstyle2 = wb.createCellStyle();
		cellstyle2.setFont(font);
		cellstyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellstyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellstyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellstyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellstyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFCellStyle cellstyle3 = wb.createCellStyle();
		cellstyle3.setFont(font);
		cellstyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellstyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellstyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellstyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellstyle3.setAlignment(HSSFCellStyle.ALIGN_LEFT);

		HSSFCellStyle cellstyle4 = wb.createCellStyle();
		cellstyle4.setFont(font);
		cellstyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFCellStyle cellstyle5 = wb.createCellStyle();
		cellstyle5.setFont(font);
		cellstyle5.setAlignment(HSSFCellStyle.ALIGN_LEFT);

		if (cellType == 1) {
			return cellstyle1;
		} else if (cellType == 2) {
			return cellstyle2;
		} else if (cellType == 3) {
			return cellstyle3;
		} else if (cellType == 4) {
			return cellstyle4;
		} else if (cellType == 5) {
			return cellstyle5;
		} else {
			return cellstyle1;
		}
	}

	public static String toAlphabet(int value) {
		String alphabet[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		if (value < 1 || value > 26)
			return "";
		return alphabet[value - 1];
	}
}