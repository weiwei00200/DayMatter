package com.pybeta.daymatter;

import java.io.Serializable;

public class ApkInfo implements Serializable {

	private int worksid;
	private String name;
	private String desc;
	private String keyword;
	private String photo;
	private String url;
	private String size;
	private int works_type;
	private String packageName;
	private boolean isDescShowing = false;

	public void setDescShowing(boolean isDescShowing) {
		this.isDescShowing = isDescShowing;
	}

	public boolean getDescShowing() {
		return isDescShowing;
	}

	public int getWorksType() {
		return works_type;
	}

	public String getPackageName() {
		return packageName;
	}

	public int getWorksId() {
		return worksid;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public String getKeyWord() {
		return keyword;
	}

	public String getPhoto() {
		return photo;
	}

	public String getUrl() {
		return url;
	}

	public String getSize() {
		return size;
	}

	public void setWorksType(int works_type) {
		this.works_type = works_type;
	}

	public void setWorksId(int worksid) {
		this.worksid = worksid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setKeyWord(String keyword) {
		this.keyword = keyword;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String toString() {
		return "ApkInfo- worksid: " + worksid + ",name: " + name + " ,desc: "
				+ desc + " ,keyword: " + keyword + " ,photo: " + photo
				+ " , url: " + url + " ,size: " + size + " ,works_type : "
				+ works_type + " ,packageName: " + packageName;

	}

}
