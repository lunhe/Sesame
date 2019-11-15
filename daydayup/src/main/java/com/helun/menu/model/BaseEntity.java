package com.helun.menu.model;

import java.lang.reflect.Field;

public class BaseEntity {
	public String toString() {
		Field[] fields = this.getClass().getDeclaredFields();
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.getClass().getName()).append("[");
		for (Field field : fields) {
			field.setAccessible(true);
			Object fiedValue = null ;
			try {
				fiedValue = field.get(this);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				
			}
			buffer.append(field.getName()).append("=").append(fiedValue).append(",");
		}
		return buffer.substring(0, buffer.length() - 1) + "]";
	}
}
