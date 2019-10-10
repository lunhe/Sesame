package com.helun.menu.model;

import com.helun.menu.enumtype.TasteLevel;
import com.helun.menu.enumtype.TasteType;
import com.helun.menu.enumtype.TextureType;

public class Taste {
	private String tasteId ;
	private TasteLevel tasteLevel;
	private TasteType tasteType;
	private TextureType textureType;

	
	
	public String getTasteId() {
		return tasteId;
	}

	public Taste setTasteId(String tasteId) {
		this.tasteId = tasteId;
		return this;
	}

	public TasteLevel getTasteLevel() {
		return tasteLevel;
	}

	public Taste setTasteLevel(TasteLevel tasteLevel) {
		this.tasteLevel = tasteLevel;
		return this;
	}

	public TasteType getTasteType() {
		return tasteType;
	}

	public Taste setTasteType(TasteType tasteType) {
		this.tasteType = tasteType;
		return this;
	}

	public TextureType getTextureType() {
		return textureType;
	}

	public Taste setTextureType(TextureType textureType) {
		this.textureType = textureType;
		return this;
	}
	
	public String toString() {
		return "[tasteType:"+tasteType+",tasteLevel:"+tasteLevel+",textureType:"+textureType+"]";
	}

}
