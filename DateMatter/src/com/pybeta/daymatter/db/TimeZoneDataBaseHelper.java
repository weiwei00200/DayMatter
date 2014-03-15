package com.pybeta.daymatter.db;

import com.pybeta.daymatter.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TimeZoneDataBaseHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "daysmatter.db.new";
	public static final int DATABASE_VERSION = 4;
	
	private static final String CREATETAB = "create table if not exists "
			+ "timezones(id integer primary key autoincrement,time2modify long,gmt text,rawoffset integer,selected integer,cityname_en text,cityname_zh text,cityname_tw text,"
			+ "countryname_en text,countryname_zh text,countryname_tw text)";
	public TimeZoneDataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATETAB);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
