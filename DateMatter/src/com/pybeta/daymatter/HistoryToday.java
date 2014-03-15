package com.pybeta.daymatter;

public class HistoryToday {

	private String content;
	private String mmdd;
	private long id;
	private long year;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMmdd() {
		return mmdd;
	}

	public void setMmdd(String mmdd) {
		this.mmdd = mmdd;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getYear() {
		return year;
	}

	public void setYear(long year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return String.format("id=%d, mmdd=%s, year=%d, content=%s", id, mmdd, year, content);
	}
}
