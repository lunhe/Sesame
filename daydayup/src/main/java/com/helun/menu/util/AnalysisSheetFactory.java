package com.helun.menu.util;

import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import com.helun.menu.model.AnalysisSheetModel;
import com.helun.menu.model.AnalysisSheetModel.StudentResult;
import com.helun.menu.model.BaseEntity;

public class AnalysisSheetFactory extends AbstractSheetFactory{
	private static final int COLNUM = 5 ;
	public AnalysisSheetFactory(HSSFWorkbook hssfWorkbook) {
		super(hssfWorkbook);
	}

	@Override
	public ISheetFactory buildSheet(int sheetIndex, String sheetName,BaseEntity sheetData) {
		AnalysisSheetModel analysisSheetModel = (AnalysisSheetModel)sheetData;
		resetCurrentRow();
		
		HSSFSheet hssfSheet = hssfWorkbook.createSheet();
		hssfWorkbook.setSheetName(sheetIndex, sheetName);
		for(int i = 0 ; i< COLNUM ; i++){
			hssfSheet.setColumnWidth(i, 25 * 256);
		}
		
		HSSFCellStyle titleCellStyle = cellStyle(IndexedColors.LIGHT_GREEN.getIndex()) ;
		HSSFCellStyle valueCellStyle = cellStyle(IndexedColors.WHITE.getIndex()) ;
		
		HSSFRow hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, analysisSheetModel.getTitle(), valueCellStyle);
        hssfSheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, COLNUM-1)); 
        
        hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, analysisSheetModel.getTime(), valueCellStyle);
        hssfSheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, COLNUM-1));
        
        hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, "总体答题结果-单题正确率", titleCellStyle);
        hssfSheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 1)); 
        
        hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, "题号", titleCellStyle);
        cell(hssfRow, 1, "正确率", titleCellStyle);
        for(Entry<String, String> entry : analysisSheetModel.getRequestsToCorrectRate().entrySet()){
            hssfRow = hssfSheet.createRow(currentRow(1));
            cell(hssfRow, 0, entry.getKey(), valueCellStyle);
            cell(hssfRow, 1, entry.getValue(), valueCellStyle);
        }
        
        hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, "总体答题结果-平均正确率", titleCellStyle);
        hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, analysisSheetModel.getAvgCorrectRate(), valueCellStyle);
      
        
        hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, "总体答题结果-知识点掌握情况", titleCellStyle);
		hssfSheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 1)); // 第currentRow行合并
        
        hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, "知识点", titleCellStyle);
        cell(hssfRow, 1, "正确率", titleCellStyle);
        for(Entry<String, String> entry : analysisSheetModel.getPointsToCorrectRate().entrySet()){
            hssfRow = hssfSheet.createRow(currentRow(1));
            cell(hssfRow, 0, entry.getKey(), valueCellStyle);
            cell(hssfRow, 1, entry.getValue(), valueCellStyle);
        }
        
        hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, "学生答题结果", titleCellStyle);
        hssfSheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 4)); // 第currentRow行合并
        
        hssfRow = hssfSheet.createRow(currentRow(1));
        cell(hssfRow, 0, "学生姓名", titleCellStyle);        
        cell(hssfRow, 1, "分数（总分100分）", titleCellStyle);
        cell(hssfRow, 2, "正确率", titleCellStyle);
        cell(hssfRow, 3, "提交时间", titleCellStyle);
        cell(hssfRow, 4, "提交状态", titleCellStyle);
        for(StudentResult studentResult : analysisSheetModel.getStudentResults()){
            hssfRow = hssfSheet.createRow(currentRow(1));
            cell(hssfRow, 0, studentResult.getName(), valueCellStyle);        
            cell(hssfRow, 1, studentResult.getScore(), valueCellStyle);
            cell(hssfRow, 2, studentResult.getCorrectRate(), valueCellStyle);
            cell(hssfRow, 3, studentResult.getSubmitTime(), valueCellStyle);
            cell(hssfRow, 4, studentResult.getSubmitStatus(), valueCellStyle);
        }
        
        cellBorder(hssfSheet, currentRow,COLNUM );
		return this ;
	}

}
