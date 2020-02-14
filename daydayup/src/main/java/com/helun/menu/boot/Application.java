package com.helun.menu.boot;

import java.util.List;
import java.util.Random;

import com.helun.menu.util.ExcelFactory;
import com.helun.menu.util.FileLoader;

public class Application {
	public static void main(String[] args) {
		try {
			ExcelFactory excelFactory = new ExcelFactory("C:\\Users\\Administrator\\Desktop\\","excelTest.xls") ;
			excelFactory.newExcel().addSheet("预习分析").build();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
