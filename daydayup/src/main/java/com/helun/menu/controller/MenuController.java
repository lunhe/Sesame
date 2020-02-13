package com.helun.menu.controller;

import com.helun.menu.model.Menu;
import com.helun.menu.service.MenuService;

public class MenuController {
	private MenuService menuService ;
	public MenuController() {
		 menuService = new MenuService() ;
	}
	
	public Object listMenu() {
		return menuService.listMenu() ;
	}
	
	public Object getMenu() {
		return menuService.getMenu();
	}
	
	public Object addMenu(Menu menu) {
		return menuService.addMenu(menu);
	}
	
	public Object removeMenu(Menu menu) {
		return menuService.removeMenu(menu);
	}
	
	public Object updateMenu(Menu menu) {
		return menuService.updateMenu(menu);
	}
}
