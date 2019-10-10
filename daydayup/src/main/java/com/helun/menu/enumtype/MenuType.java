package com.helun.menu.enumtype;

import com.helun.menu.exception.MenuRunTimeException;

public enum MenuType {
	/**
	 * 荤菜
	 */
	MeatDishe("荤菜", 1),
	/**
	 * 素菜
	 */
	VegetableDishe("凉拌菜", 2),
	/**
	 * 凉拌菜
	 */
	ColdDishe("腌菜", 3),
	/**
	 * 腌菜
	 */
	PickledVegetable("汤", 4),
	/**
	 * 汤
	 */
	Soup("", 5),
	/**
	 * 其他主食(面，炒饭)
	 */
	Other("其他主食(面，炒饭)", 6);

	private String name;
	private Integer code;

	private MenuType(String name, Integer code) {
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public Integer getCode() {
		return this.code;
	}

	public static String getNameByCode(Integer code) {
		for (MenuType menuType : MenuType.values()) {
			if (code == menuType.getCode()) {
				return menuType.getName();
			}
		}
		throw new MenuRunTimeException("Unkown menuType code : " + code);
	}
}
