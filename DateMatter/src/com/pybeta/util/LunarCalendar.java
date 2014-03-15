package com.pybeta.util;

import java.util.Calendar;
import java.util.Locale;

public class LunarCalendar {

	private int year;
	private int monthOfYear;
	private int dayOfMonth;

	public LunarCalendar() {
		Calendar now = Calendar.getInstance(Locale.CHINA);
		
		ChineseCalendarGB lunar = new ChineseCalendarGB();
		lunar.setGregorian(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));
		lunar.computeChineseFields();
		
		this.year = lunar.getChineseYear();
		this.monthOfYear = lunar.getChineseMonth() - 1;
		this.dayOfMonth = lunar.getChineseDate();
	}
	
	public void setDate(int year, int monthOfYear, int dayOfMonth) {
		this.year = year;
		this.monthOfYear = monthOfYear;
		this.dayOfMonth = dayOfMonth;
	}
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonthOfYear() {
		return monthOfYear;
	}

	public void setMonthOfYear(int monthOfYear) {
		this.monthOfYear = monthOfYear;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public int getActualMaximum() {
		return ChineseCalendarGB.daysInChineseMonth(year, monthOfYear + 1);
	}
}
