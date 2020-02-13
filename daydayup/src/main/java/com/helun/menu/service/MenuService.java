package com.helun.menu.service;

import com.helun.menu.dao.MenuDao;
import com.helun.menu.model.Menu;

public class MenuService {
	MenuDao menuDao = null;
	public MenuService() {
		menuDao = new MenuDao() ;
	}
	
	public Object listMenu() {
		return menuDao.selectMenu();
	}
	
	public Object getMenu() {
		return menuDao.selectMenu();
	}
	
	public Object addMenu(Menu menu) {
		return menuDao.addMenu(menu);
	}
	
	public Object removeMenu(Menu menu) {
		return menuDao.deleteMenu(menu);
	}
	
	public Object updateMenu(Menu menu) {
		return menuDao.saveMenu(menu);
	}
}
