package com.helun.menu.performance;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.helun.menu.model.BaseEntity;

public class MergeSETaskBO extends BaseEntity {
	private static final long serialVersionUID = -8276026134234666013L;
	/**
	 * 入参属性集合
	 */
	Map<String,Object> propertiesMap = Maps.newHashMap() ;
	
	public Object getProperty(String propertyName) {
		if(StringUtils.isBlank(propertyName)) {
			return null ;
		}
		return propertiesMap.get(propertyName) ;
	}
	
	public Boolean addProperty(String propertyName,Object propertyValue) {
		if(StringUtils.isBlank(propertyName)) {
			return false ;
		}
		propertiesMap.put(propertyName, propertyValue) ;
		return true ;
	}
	
	public Boolean addPropertys(Map<String,Object> propertiesMap) {
		if(propertiesMap != null) {
			propertiesMap.putAll(propertiesMap) ;
			return true ;
		}
		return false ;
	}

}

