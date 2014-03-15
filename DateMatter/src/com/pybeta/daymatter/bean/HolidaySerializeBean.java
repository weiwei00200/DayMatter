package com.pybeta.daymatter.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HolidaySerializeBean implements Serializable{

	public Map<String,HolidayItemBean> map = new HashMap<String,HolidayItemBean>();

	public Map<String, HolidayItemBean> getMap() {
		return map;
	}

	public void setMap(Map<String, HolidayItemBean> map) {
		this.map = map;
	}
	
}
