package com.helun.menu.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.helun.menu.model.AnalysisSheetModel;
import com.helun.menu.model.BaseEntity;
import com.helun.menu.model.StudentSheetModel;

public class AnalysisExcelFactory {
	OutputStream output = null ;
	HSSFWorkbook hssfWorkbook = null;
	int sheetCount = -1 ;
	
	public AnalysisExcelFactory(String exportPath,String fileName) {
		try {
			File file = new File(exportPath+fileName);
			output = new FileOutputStream(file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AnalysisExcelFactory(OutputStream output) {
		this.output = output;
	}

	public AnalysisExcelFactory newExcel() throws IOException {
		this.hssfWorkbook = new HSSFWorkbook();
		return this;
	}
	
	public HSSFWorkbook getExcel(){
		return this.hssfWorkbook ;
	}

	public void build() {
		try {
			hssfWorkbook.write(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AnalysisExcelFactory addAnalysisSheet(String sheetName,AnalysisSheetModel analysisSheetModel) throws IOException {
		if(hssfWorkbook == null){
			newExcel();
		}
		ISheetFactory analysisSheetFactory = new AnalysisSheetFactory(hssfWorkbook);
		addSheet(analysisSheetFactory, sheetName,analysisSheetModel);
		return this ;
	}
	
	public AnalysisExcelFactory addStudentSheet(List<StudentSheetModel> studentSheetModels) throws IOException {
		if(hssfWorkbook == null){
			newExcel();
		}
		ISheetFactory studentSheetFactory = new StudentSheetFactory(hssfWorkbook);
		for(StudentSheetModel studentSheetModel: studentSheetModels){
			addSheet(studentSheetFactory, studentSheetModel.getName(),studentSheetModel);
		}
		return this ;
	}
	
	
	public AnalysisExcelFactory addSheet(ISheetFactory sheetFactory , String sheetName,BaseEntity sheetData) {
		sheetCount ++ ;
		sheetFactory.buildSheet(sheetCount, sheetName,sheetData) ;
		return this ;
	}
	
}
