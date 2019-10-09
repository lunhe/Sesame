package com.helun.menu.model;

public enum TasteLevel {

	NONE("无", 0), 
	LIGHT_lESS("极轻", 1), 
	LIGHT("轻", 2), 
	NORMAL_lESS("偏轻", 3),
	NORMAL("正常", 4),
	NORMAL_MORE("偏重", 5),
	HEAVY("重", 6),
	HEAVY_MORE("严重", 7), 
	OTHER("未知", -1);

	private String levelName;
	private Integer level;

	private TasteLevel(String levelName, Integer level) {
		this.levelName = levelName;
		this.level = level;
	}

	public String getLevelName() {
		return this.levelName;
	}

	public Integer getLevel() {
		return this.level;
	}
	
	public String toString() {
		return "[levelName:"+levelName+",levele:"+level+"]";
	}

}
