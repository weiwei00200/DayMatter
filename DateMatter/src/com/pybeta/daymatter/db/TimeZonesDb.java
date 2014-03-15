// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.pybeta.daymatter.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pybeta.daymatter.WorldTimeZone;
import com.pybeta.daymatter.utils.LogUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeZonesDb {

	public TimeZonesDb() {
	}

	public static void delete(SQLiteDatabase sqlitedatabase,
			WorldTimeZone worldtimezone) {
		String s = (new StringBuilder("delete from timezones where id="))
				.append(worldtimezone.getId()).toString();
		LogUtil.Sysout((new StringBuilder("delete sql : ")).append(s)
				.toString());
		sqlitedatabase.execSQL(s);
		sqlitedatabase.close();
	}

	public static ArrayList<WorldTimeZone> getMultWorldTimeOriginalData(
			SQLiteDatabase sqlitedatabase) {
		Cursor cursor;
		int i;
		ArrayList<WorldTimeZone> mWorldTimeZones = null;
		cursor = sqlitedatabase.rawQuery(
				"select * from timezones where selected=0", null);
		i = cursor.getCount();

		if (i < 0)
			return mWorldTimeZones;
		mWorldTimeZones = new ArrayList<WorldTimeZone>();
		while (cursor.moveToNext()) {

			WorldTimeZone worldtimezone = new WorldTimeZone();
			String s = cursor.getString(cursor.getColumnIndex("cityname_en"))
					.trim();
			worldtimezone.setCityName(s);
			String s1 = cursor.getString(
					cursor.getColumnIndex("countryname_en")).trim();
			worldtimezone.setCountryName(s1);
			worldtimezone.setCityNameZH(cursor.getString(cursor
					.getColumnIndex("cityname_zh")));
			worldtimezone.setCityNameTW(cursor.getString(cursor
					.getColumnIndex("cityname_tw")));
			worldtimezone.setCountryNameTW(cursor.getString(cursor
					.getColumnIndex("countryname_tw")));
			worldtimezone.setCountryNameZH(cursor.getString(cursor
					.getColumnIndex("countryname_zh")));
			worldtimezone
					.setGMT(cursor.getString(cursor.getColumnIndex("gmt")));
			worldtimezone.setRawOffset(cursor.getInt(cursor
					.getColumnIndex("rawoffset")));
			worldtimezone.setId(cursor.getInt(cursor.getColumnIndex("id")));
			worldtimezone.setTime2Modify(cursor.getLong(cursor
					.getColumnIndex("time2modify")));
			mWorldTimeZones.add(worldtimezone);
		}
		cursor.close();
		sqlitedatabase.close();
		LogUtil.Sysout("AddWorldTimeActivity getMultWorldTime 3");
		return mWorldTimeZones;

	}

	public static ArrayList<WorldTimeZone> getMultWorldTime(
			SQLiteDatabase sqlitedatabase) {
		Cursor cursor;
		int i;
		ArrayList<WorldTimeZone> mWorldTimeZones = null;
		cursor = sqlitedatabase.rawQuery(
				"select * from timezones where selected=0", null);
		i = cursor.getCount();

		if (i < 0)
			return mWorldTimeZones;
		mWorldTimeZones = new ArrayList<WorldTimeZone>();
		while (cursor.moveToNext()) {

			WorldTimeZone worldtimezone = new WorldTimeZone();
			String s = cursor.getString(cursor.getColumnIndex("cityname_en"))
					.trim();
			worldtimezone.setCityName(Pattern.compile("_").matcher(s)
					.replaceAll("'"));
			String s1 = cursor.getString(
					cursor.getColumnIndex("countryname_en")).trim();
			worldtimezone.setCountryName(Pattern.compile("_").matcher(s1)
					.replaceAll("'"));
			worldtimezone.setCityNameZH(cursor.getString(cursor
					.getColumnIndex("cityname_zh")));
			worldtimezone.setCityNameTW(cursor.getString(cursor
					.getColumnIndex("cityname_tw")));
			worldtimezone.setCountryNameTW(cursor.getString(cursor
					.getColumnIndex("countryname_tw")));
			worldtimezone.setCountryNameZH(cursor.getString(cursor
					.getColumnIndex("countryname_zh")));
			worldtimezone
					.setGMT(cursor.getString(cursor.getColumnIndex("gmt")));
			worldtimezone.setRawOffset(cursor.getInt(cursor
					.getColumnIndex("rawoffset")));
			worldtimezone.setId(cursor.getInt(cursor.getColumnIndex("id")));
			worldtimezone.setTime2Modify(cursor.getLong(cursor
					.getColumnIndex("time2modify")));
			mWorldTimeZones.add(worldtimezone);
		}
		cursor.close();
		sqlitedatabase.close();
		LogUtil.Sysout("AddWorldTimeActivity getMultWorldTime 3");
		return mWorldTimeZones;

	}

	public static ArrayList<WorldTimeZone> getMultWorldTimeSelected(
			SQLiteDatabase sqlitedatabase) {
		ArrayList<WorldTimeZone> mWorldTimeZones = null;
		Cursor cursor = sqlitedatabase.rawQuery(
				"select * from timezones where selected=1", null);
		int i = cursor.getCount();
		LogUtil.Sysout("AddWorldTimeActivity getMultWorldTime 0");

		LogUtil.Sysout("AddWorldTimeActivity getMultWorldTime 1 pos : "
				+ cursor.getPosition());
		if (i < 0)
			return mWorldTimeZones;
		mWorldTimeZones = new ArrayList<WorldTimeZone>();
		while (cursor.moveToNext()) {

			LogUtil.Sysout("AddWorldTimeActivity getMultWorldTime 2");
			WorldTimeZone worldtimezone = new WorldTimeZone();
			String s = cursor.getString(cursor.getColumnIndex("cityname_en"))
					.trim();
			worldtimezone.setCityName(Pattern.compile("_").matcher(s)
					.replaceAll("'"));
			String s1 = cursor.getString(
					cursor.getColumnIndex("countryname_en")).trim();
			worldtimezone.setCountryName(Pattern.compile("_").matcher(s1)
					.replaceAll("'"));
			worldtimezone.setCityNameZH(cursor.getString(cursor
					.getColumnIndex("cityname_zh")));
			worldtimezone.setCityNameTW(cursor.getString(cursor
					.getColumnIndex("cityname_tw")));
			worldtimezone.setCountryNameTW(cursor.getString(cursor
					.getColumnIndex("countryname_tw")));
			worldtimezone.setCountryNameZH(cursor.getString(cursor
					.getColumnIndex("countryname_zh")));
			worldtimezone
					.setGMT(cursor.getString(cursor.getColumnIndex("gmt")));
			worldtimezone.setRawOffset(cursor.getInt(cursor
					.getColumnIndex("rawoffset")));
			worldtimezone.setId(cursor.getInt(cursor.getColumnIndex("id")));
			worldtimezone.setTime2Modify(cursor.getLong(cursor
					.getColumnIndex("time2modify")));
			mWorldTimeZones.add(worldtimezone);
		}
		cursor.close();
		sqlitedatabase.close();
		LogUtil.Sysout("AddWorldTimeActivity getMultWorldTime 3");
		return mWorldTimeZones;

	}

	public static void insertBatch(SQLiteOpenHelper mHelper,
			ArrayList<WorldTimeZone> mWorldTimes) {
		if (mWorldTimes != null && mWorldTimes.size() > 0) {
			SQLiteDatabase db = mHelper.getWritableDatabase();
			String sql = "delete  from " + TAB_NAME;
			db.execSQL(sql);
			db.beginTransaction();
			try {
				for (WorldTimeZone t : mWorldTimes) {
					String tmp = t.getCityName();
					String cityname = tmp.trim();
					Pattern p = Pattern.compile("\'");
					Matcher m = p.matcher(cityname);
					cityname = m.replaceAll("_");

					tmp = t.getCountryName();
					String countryname = tmp.trim();
					p = Pattern.compile("\'");
					m = p.matcher(countryname);
					countryname = m.replaceAll("_");

					sql = "insert into "
							+ TAB_NAME
							+ " (time2modify,rawoffset,selected,gmt,cityname_en,cityname_zh,cityname_tw,countryname_en,countryname_zh,countryname_tw) values("
							+ 0 + "," + t.getRawOffset() + "," + 0 + ",'"
							+ t.getGMT() + "','" + cityname + "','"
							+ t.getCityNameZH() + "','" + t.getCityNameTW() + "','"+ countryname + "','"+ t.getCountryNameZH() + "','"
							+ t.getCountryNameTW() + "' )";
					db.execSQL(sql);
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

				db.endTransaction();
				db.close();
			}
		}

	}

	public static void update(SQLiteDatabase sqlitedatabase,
			WorldTimeZone worldtimezone, int i) {
		String s = (new StringBuilder("update timezones set selected="))
				.append(i).append(" ,time2modify=")
				.append(worldtimezone.getTime2Modify()).append(" where id=")
				.append(worldtimezone.getId()).toString();
		LogUtil.Sysout((new StringBuilder("update sql : ")).append(s)
				.toString());
		sqlitedatabase.execSQL(s);
		sqlitedatabase.close();
	}

	// public static void updateBatch(SQLiteOpenHelper sqliteopenhelper,
	// ArrayList arraylist)
	// {
	// if(arraylist == null || arraylist.size() <= 0) goto _L2; else goto _L1
	// _L1:
	// SQLiteDatabase sqlitedatabase;
	// sqlitedatabase = sqliteopenhelper.getWritableDatabase();
	// sqlitedatabase.beginTransaction();
	// Iterator iterator = arraylist.iterator();
	// _L5:
	// if(iterator.hasNext()) goto _L4; else goto _L3
	// _L3:
	// sqlitedatabase.setTransactionSuccessful();
	// sqlitedatabase.endTransaction();
	// sqlitedatabase.close();
	// _L2:
	// return;
	// _L4:
	// WorldTimeZone worldtimezone = (WorldTimeZone)iterator.next();
	// String s = worldtimezone.getCityName().trim();
	// String s1 = Pattern.compile("'").matcher(s).replaceAll("_");
	// String s2 = worldtimezone.getCountryName().trim();
	// Pattern.compile("'").matcher(s2).replaceAll("_");
	// sqlitedatabase.execSQL((new
	// StringBuilder("update timezones set cityname_tw='")).append(worldtimezone.getCityNameZH()).append("',countryname_tw='").append(worldtimezone.getCountryNameZH()).append("' where cityname_en='").append(s1).append("'").toString());
	// goto _L5
	// Exception exception1;
	// exception1;
	// exception1.printStackTrace();
	// sqlitedatabase.endTransaction();
	// sqlitedatabase.close();
	// goto _L2
	// Exception exception;
	// exception;
	// sqlitedatabase.endTransaction();
	// sqlitedatabase.close();
	// throw exception;
	// goto _L5
	// }
	public static void updateBatch(SQLiteOpenHelper mHelper,
			ArrayList<WorldTimeZone> mWorldTimes) {
		if (mWorldTimes != null && mWorldTimes.size() > 0) {
			SQLiteDatabase db = mHelper.getWritableDatabase();
			db.beginTransaction();
			try {
				for (WorldTimeZone t : mWorldTimes) {
					String tmp = t.getCityName();
					String cityname = tmp.trim();
					Pattern p = Pattern.compile("\'");
					Matcher m = p.matcher(cityname);
					cityname = m.replaceAll("_");

					tmp = t.getCountryName();
					String countryname = tmp.trim();
					p = Pattern.compile("\'");
					m = p.matcher(countryname);
					countryname = m.replaceAll("_");

					String sql = "update " + TAB_NAME + " set cityname_tw='"
							+ t.getCityNameZH() + "',countryname_tw='"
							+ t.getCountryNameZH() + "' where cityname_en='"
							+ cityname + "'";
					db.execSQL(sql);
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

				db.endTransaction();
				db.close();
			}
		}

	}

	public static final String TAB_NAME = "timezones";
}
