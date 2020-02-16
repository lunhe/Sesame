package com.helun.menu.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import com.helun.menu.model.BaseEntity;
import com.helun.menu.model.StudentSheetModel;
import com.helun.menu.model.StudentSheetModel.AnswerResult;

public class StudentSheetFactory extends AbstractSheetFactory{
	private static final int COLNUM = 3 ;
	StudentSheetFactory(HSSFWorkbook hssfWorkbook) {
		super(hssfWorkbook);
	}

	@Override
	public ISheetFactory buildSheet(int sheetIndex, String sheetName,BaseEntity sheetData) {
		StudentSheetModel studentSheetModel = (StudentSheetModel)sheetData; 
		resetCurrentRow();

		HSSFSheet hssfSheet = hssfWorkbook.createSheet();
		hssfWorkbook.setSheetName(sheetIndex, sheetName+sheetIndex);
		for(int i = 0 ; i< COLNUM ; i++){
			hssfSheet.setColumnWidth(i, 25 * 256);
		}
		
		HSSFCellStyle titleCellStyle = cellStyle(IndexedColors.LIGHT_GREEN.getIndex()) ;
		HSSFCellStyle valueCellStyle = cellStyle(IndexedColors.WHITE.getIndex()) ;
		
        HSSFRow hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, studentSheetModel.getName(), valueCellStyle);
        hssfSheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, COLNUM-1));
        
        hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, studentSheetModel.getNumber(), valueCellStyle);
        hssfSheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, COLNUM-1));
        
        hssfRow = hssfSheet.createRow(currentRow(1));
        hssfSheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, COLNUM-1));
        cell(hssfRow, 0, studentSheetModel.getSubmitTime(), valueCellStyle);
        
        hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, "学生分数/总分", titleCellStyle);
        
        hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, studentSheetModel.getTotalSocre(), valueCellStyle);
        
        hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, "总正确率", titleCellStyle);
        
        hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, studentSheetModel.getTotalCorrectRate(), valueCellStyle);
        
        hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, "学生答题结果", titleCellStyle);
        hssfSheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, COLNUM-1));
        
        hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, "题号", titleCellStyle);
        cell(hssfRow, 1, "分数/总分", titleCellStyle);
        cell(hssfRow, 2, "正确率", titleCellStyle);
        
        for(AnswerResult answerResult : studentSheetModel.getAnswerResults()){
        	hssfRow = hssfSheet.createRow(currentRow(1));
        	cell(hssfRow, 0, answerResult.getRequestNum(), valueCellStyle);
            cell(hssfRow, 1, answerResult.getScore(), valueCellStyle);
            cell(hssfRow, 2, answerResult.getCorrectRate(), valueCellStyle);
        }
        cellBorder(hssfSheet, currentRow, COLNUM);
		return this ;
	}

}
