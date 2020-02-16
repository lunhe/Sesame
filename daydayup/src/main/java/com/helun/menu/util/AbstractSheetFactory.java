package com.helun.menu.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;

public abstract class AbstractSheetFactory implements ISheetFactory{
	int currentRow = -1 ; 
	HSSFWorkbook hssfWorkbook = null;
	
	AbstractSheetFactory(HSSFWorkbook hssfWorkbook){
		this.hssfWorkbook = hssfWorkbook;
	}
	
	HSSFCellStyle cellStyle(Short cellGroundColor) {
		HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
		// 字体
        HSSFFont hssfFont = hssfWorkbook.createFont();
        hssfFont.setFontName("微软雅黑");
        hssfFont.setFontHeightInPoints((short) 11);
        
        // 单元格风格
        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        hssfCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        hssfCellStyle.setFillForegroundColor(cellGroundColor);
        hssfCellStyle.setFont(hssfFont);
        
        return hssfCellStyle ;
	}
	
	ISheetFactory cell(HSSFRow row , int index , String value , HSSFCellStyle cellStyle){
		HSSFCell cell = row.createCell(index);
		cell.setCellValue(value);
		cell.setCellStyle(cellStyle);
		return  this ;
	}
	
	ISheetFactory cellBorder(HSSFSheet hssfSheet,int row , int col){
		for(int i = 0  ; i < row ; i++){
			HSSFRow hssfRow = hssfSheet.getRow(i) ;
			for(int j = 0 ; j < col ; j++){
				HSSFCell cell = hssfRow.getCell(j);
				if(cell == null){
					cell = hssfRow.createCell(j);
				}
				HSSFCellStyle hssfCellStyle = cell.getCellStyle() ;
				hssfCellStyle.setBorderBottom((short) 0x1);
				hssfCellStyle.setBorderLeft((short) 0x1);
				hssfCellStyle.setBorderRight((short) 0x1);
				hssfCellStyle.setBorderTop((short) 0x1);
			}
		}
		
		return  this ;
	}
	
	
	int currentRow(int offest){
		this.currentRow += offest ;
		return currentRow ;
	}
	
	ISheetFactory resetCurrentRow(){
		this.currentRow = -1  ;
		return this;
	}
}
