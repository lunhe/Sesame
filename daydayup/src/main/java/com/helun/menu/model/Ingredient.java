package com.helun.menu.model;

import java.math.BigDecimal;

public class Ingredient {
	/**
	 * 名称
	 */
	private String name;

	/**
	 * 单价 *元/500g
	 */
	private BigDecimal price;

	/**
	 * 节气
	 */
	private SolaTerms solaTerms;

	/**
	 * 类型
	 */
	private IngredientType type;

	/**
	 * 重量 g
	 */
	private Float weight;

	/**
	 * 数量
	 */
	private Float number;

	/**
	 * 脂肪占比
	 */
	private Float Fat;
	/**
	 * 纤维占比
	 */
	private Float Fibre;
	/**
	 * 蛋白占比
	 */
	private Float protein;
	/**
	 * 糖占比
	 */
	private Float sugar;

}
