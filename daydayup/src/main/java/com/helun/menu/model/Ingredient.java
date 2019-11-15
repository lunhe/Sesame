package com.helun.menu.model;

import java.math.BigDecimal;

import com.helun.menu.enumtype.IngredientType;
import com.helun.menu.enumtype.SolaTerms;

public class Ingredient extends BaseEntity{

	/**
	 * 编号
	 */
	private String ingredientId;
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
	 * 产地
	 */
	private String location;

	/**
	 * 类型
	 */
	private IngredientType type;

	/**
	 * 脂肪占比
	 */
	private Float fat;
	/**
	 * 纤维占比
	 */
	private Float fibre;
	/**
	 * 蛋白占比
	 */
	private Float protein;
	/**
	 * 糖占比
	 */
	private Float sugar;

	public String getIngredientId() {
		return ingredientId;
	}

	public Ingredient setIngredientId(String ingredientId) {
		this.ingredientId = ingredientId;
		return this;
	}

	public String getLocation() {
		return location;
	}

	public Ingredient setLocation(String location) {
		this.location = location;
		return this;
	}

	public String getName() {
		return name;
	}

	public Ingredient setName(String name) {
		this.name = name;
		return this;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Ingredient setPrice(BigDecimal price) {
		this.price = price;
		return this;
	}

	public SolaTerms getSolaTerms() {
		return solaTerms;
	}

	public Ingredient setSolaTerms(SolaTerms solaTerms) {
		this.solaTerms = solaTerms;
		return this;
	}

	public IngredientType getType() {
		return type;
	}

	public Ingredient setType(IngredientType type) {
		this.type = type;
		return this;
	}

	public Float getFat() {
		return fat;
	}

	public Ingredient setFat(Float fat) {
		this.fat = fat;
		return this;
	}

	public Float getFibre() {
		return fibre;
	}

	public Ingredient setFibre(Float fibre) {
		this.fibre = fibre;
		return this;
	}

	public Float getProtein() {
		return protein;
	}

	public Ingredient setProtein(Float protein) {
		this.protein = protein;
		return this;
	}

	public Float getSugar() {
		return sugar;
	}

	public Ingredient setSugar(Float sugar) {
		this.sugar = sugar;
		return this;
	}

}
