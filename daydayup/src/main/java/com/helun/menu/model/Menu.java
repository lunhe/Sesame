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

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MenuType getMenuType() {
		return menuType;
	}

	public void setMenuType(MenuType menuType) {
		this.menuType = menuType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
 