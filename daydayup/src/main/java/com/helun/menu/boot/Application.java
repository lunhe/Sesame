package com.helun.menu.boot;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.helun.menu.model.AnalysisSheetModel;
import com.helun.menu.model.StudentSheetModel;
import com.helun.menu.util.AnalysisExcelFactory;
import com.helun.menu.util.FileLoader;

public class Application {
	public static void main(String[] args) {
		try {
			
			AnalysisSheetModel analysisSheetModel = buildAnalysisDate();
			StudentSheetModel studentSheetModel = buildStudentDate();
			List<StudentSheetModel> studentSheetModels = Lists.newArrayList();
			studentSheetModels.add(studentSheetModel);
			studentSheetModels.add(studentSheetModel);
			studentSheetModels.add(studentSheetModel);
			String filePath = "C:\\Users\\茗德\\Desktop\\" ;
			String fileName = "预习分析_" + analysisSheetModel.getTitle()+".xls" ;
			
			AnalysisExcelFactory analysisExcelFactory = new AnalysisExcelFactory(filePath,fileName) ;
			analysisExcelFactory.newExcel().addAnalysisSheet("预习分析",analysisSheetModel)
			.addStudentSheet(studentSheetModels).build();;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static AnalysisSheetModel buildAnalysisDate() {
		AnalysisSheetModel analysisSheetModel = new AnalysisSheetModel() ;
		analysisSheetModel.setAvgCorrectRate("80%").setTime("布置时间: 2020-02-11 14:01 截止时间: 2020-02-12 14:01")
		.setTitle("第一单元预习").addPointsToCorrectRate("整数","89%").addRequestsToCorrectRate("第1题", "78%")
		.addRequestsToCorrectRate("第2题", "待批改").addStudentResult("李明明", "91", "91%", "2020/2/11 15:01:00", "已交")
		.addStudentResult("王晓红", "待批改", "待批改", "2020/2/11 15:01:00", "迟交")
		.addStudentResult("李明明", "", "", "", "未交");
		return analysisSheetModel ;
		
	}
	
	public static StudentSheetModel buildStudentDate(){
		StudentSheetModel studentSheetModel = new StudentSheetModel() ;
		studentSheetModel.setName("李明明").setNumber("学号：1123").setSubmitTime("提交时间：2020/2/11 15:01:00")
		.setTotalSocre("80/100").setTotalCorrectRate("80%")
		.addAnswerResult("第1題", "5/6", "80%")
		.addAnswerResult("第2題", "5/7", "80%");
		return studentSheetModel ;
	}
	
	public static void testMenu() {
		List<String> menus = FileLoader.readFileByLineOnResources("src/main/resources/menu.json");
		Random random = new Random();
		if (menus.size() > 0) {
			for (int i = 0; i < 3; i++) {
				System.out.println(random.nextInt(menus.size()));
				System.out.println(menus.get(random.nextInt(menus.size())));
			}
		}
	}
}
