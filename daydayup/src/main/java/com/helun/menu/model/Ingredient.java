package com.helun.menu.model;

import java.math.BigDecimal;

public class Ingredient {

	/**
	 * 编号
	 */
	private Integer id;
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

	public Integer getId() {
		return id;
	}

	public Ingredient setId(Integer id) {
		this.id = id;
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

	public Float getWeight() {
		return weight;
	}

	public Ingredient setWeight(Float weight) {
		this.weight = weight;
		return this;
	}

	public Float getNumber() {
		return number;
	}

	public Ingredient setNumber(Float number) {
		this.number = number;
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

	public String toString() {
		return "[name:" + name + ",price:" + price + ",solaTerms:" + solaTerms + ",type:" + type + ",weight:" + weight
				+ ",number:" + number + ",fat:" + fat + ",fibre:" + fibre + ",protein:" + protein + ",sugar:" + sugar
				+ "]";
	}

}
