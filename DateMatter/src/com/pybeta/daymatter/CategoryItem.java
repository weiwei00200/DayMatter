package com.pybeta.daymatter;

import com.pybeta.daymatter.db.CategoryTable;

import android.database.Cursor;

public class CategoryItem {

	private int id;
	private String name;
	
	public CategoryItem() {
		id = 0;
		name = "";
	}
	
	public CategoryItem(String value) {
		id = 0;
		name = value;
	}
	
	public CategoryItem(int id, String value) {
		this.id = id;
		this.name = value;
	}

	public CategoryItem(Cursor cursor) {
		this.id = cursor.getInt(cursor.getColumnIndex(CategoryTable._ID));
		this.name = cursor.getString(cursor.getColumnIndex(CategoryTable.NAME));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
