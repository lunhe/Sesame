package com.helun.menu.model;

import com.helun.menu.enumtype.MenuType;

public class Menu  extends BaseEntity{
	/**
	 * 编号
	 */
	private String menuId;
	/**
	 * 菜名
	 */
	private String name;
	/**
	 * 菜类别
	 */
	private MenuType menuType;
	
	/**
	 * 描述
	 */
	private String description ;
	
	/**
	 * 味道
	 */
	private String tasteId;
	
}
 