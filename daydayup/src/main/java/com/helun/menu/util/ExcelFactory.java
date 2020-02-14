package com.helun.menu.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelFactory {
	OutputStream output = null ;
	HSSFWorkbook hssfWorkbook = null;
	int sheetCount = -1 ;
	public ExcelFactory(String exportPath,String fileName) {
		try {
			File file = new File(exportPath+fileName);
			output = new FileOutputStream(file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ExcelFactory(OutputStream output) {
		this.output = output;
	}

	public ExcelFactory newExcel() throws IOException {
		hssfWorkbook = new HSSFWorkbook();
		return this;
	}

	public void build() {
		try {
			hssfWorkbook.write(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ExcelFactory addSheet(String sheetName) {
		sheetCount ++ ;
		HSSFSheet hssfSheet = hssfWorkbook.createSheet();
		hssfWorkbook.setSheetName(sheetCount, sheetName);
		
		int rows = 5 ;
		
        hssfSheet.setColumnWidth(3, 12*256);
        hssfSheet.setColumnWidth(4, 12*256);
		
		hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, rows-1)); // 第一行合并
		hssfSheet.addMergedRegion(new CellRangeAddress(1, 1, 0, rows-1)); // 第一行合并
		hssfSheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 1)); // 第一行合并

		HSSFCellStyle hssfCellStyle = cellStyle(IndexedColors.YELLOW.getIndex()) ;
        
        HSSFRow hssfRow1 = hssfSheet.createRow(0);
        HSSFCell lsxx = hssfRow1.createCell(0);
        lsxx.setCellValue("第一单元预习");
        lsxx.setCellStyle(hssfCellStyle);
        
        hssfRow1 = hssfSheet.createRow(1);
        lsxx = hssfRow1.createCell(0);
        lsxx.setCellValue("布置时间: 2020-02-11 14:01 截止时间: 2020-02-12 14:01");
        lsxx.setCellStyle(hssfCellStyle);

        hssfRow1 = hssfSheet.createRow(2);
        lsxx = hssfRow1.createCell(0);
        lsxx.setCellValue("总体答题结果-单题正确率");
        lsxx.setCellStyle(hssfCellStyle);

        hssfRow1 = hssfSheet.createRow(3);
        lsxx = hssfRow1.createCell(0);
        lsxx.setCellValue("题号");
        lsxx.setCellStyle(hssfCellStyle);
        lsxx = hssfRow1.createCell(1);
        lsxx.setCellValue("正确率");
        lsxx.setCellStyle(hssfCellStyle);
        
        // ... 
        int dataSize = 2 ;
        
        
        hssfRow1 = hssfSheet.createRow(dataSize+3);
        lsxx = hssfRow1.createCell(0);
        lsxx.setCellValue("总体答题结果-平均正确率");
        lsxx.setCellStyle(hssfCellStyle);
        
        // ...
        
        
        hssfRow1 = hssfSheet.createRow(5+dataSize);
        lsxx = hssfRow1.createCell(0);
        lsxx.setCellValue("题号");
        lsxx.setCellStyle(hssfCellStyle);
        lsxx = hssfRow1.createCell(1);
        lsxx.setCellValue("正确率");
        lsxx.setCellStyle(hssfCellStyle);
        
        
        
		return this ;
	}
	
	private HSSFCellStyle cellStyle(short cellGroundColor) {
		HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
		// 字体
        HSSFFont hssfFont = hssfWorkbook.createFont();
        // 单元格风格
        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        hssfCellStyle.setFillForegroundColor(cellGroundColor);
        hssfCellStyle.setFont(hssfFont);
        
        return hssfCellStyle ;
	}
	
}
