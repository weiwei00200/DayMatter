package com.pybeta.daymatter.bean;

import java.io.Serializable;
import java.util.List;

public class HolidayItemBean implements Serializable{
	public String id = ""; 
	public String itemDate = "";
	public List<String> onWorkList = null;
	public List<String> onHolidayList = null;
	
	public void setId(String id) {
		this.id = id;
	}
	public String getItemDate() {
		return itemDate;
	}
	public void setItemDate(String itemDate) {
		this.itemDate = itemDate;
	}
	public String getId() {
		return id;
	}
	public List<String> getOnWorkList() {
		return onWorkList;
	}
	public void setOnWorkList(List<String> onWorkList) {
		this.onWorkList = onWorkList;
	}
	public List<String> getOnHolidayList() {
		return onHolidayList;
	}
	public void setOnHolidayList(List<String> onHolidayList) {
		this.onHolidayList = onHolidayList;
	}
}
