package com.pybeta.daymatter.db;

import com.pybeta.daymatter.R;
import com.pybeta.daymatter.utils.LogUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "daysmatter.db";
	public static final int DATABASE_VERSION = 5;

//	private static final String CREATE_MATTER_TABLE_SQL = "CREATE TABLE " 
//			+ MatterTable.TABLE_NAME + "(" 
//			+ MatterTable._ID + " INTEGER AUTOINCREMENT,"
//			+ MatterTable.UID + " TEXT PRIMARY KEY,"
//			+ MatterTable.MATTER + " TEXT, "
//			+ MatterTable.DATE + " INTEGER, "
//			+ MatterTable.CALENDAR + " INTEGER, "
//			+ MatterTable.CATEGORY + " INTEGER, "
//			+ MatterTable.TOP + " INTEGER, "
//			+ MatterTable.REPEAT + " INTEGER, "
//			+ MatterTable.REMIND + " INTEGER, "
//			+ MatterTable.STATUS + " INTEGER, "
//			+ MatterTable.REMARK + " TEXT, "
//			+ MatterTable.HOUR + " INTEGER, "
//			+ MatterTable.MIN + " INTEGER "
//			+ ");";
	private static final String CREATE_MATTER_TABLE_SQL = "CREATE TABLE " 
			+ MatterTable.TABLE_NAME + "(" 
			+ MatterTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ MatterTable.UID + " TEXT UNIQUE,"
			+ MatterTable.MATTER + " TEXT, "
			+ MatterTable.DATE + " INTEGER, "
			+ MatterTable.NEXTREMINDTIME + " INTEGER, "
			+ MatterTable.CALENDAR + " INTEGER, "
			+ MatterTable.CATEGORY + " INTEGER, "
			+ MatterTable.TOP + " INTEGER, "
			+ MatterTable.REPEAT + " INTEGER, "
			+ MatterTable.REMIND + " INTEGER, "
			+ MatterTable.STATUS + " INTEGER, "
			+ MatterTable.REMARK + " TEXT, "
			+ MatterTable.HOUR + " INTEGER, "
			+ MatterTable.MIN + " INTEGER "
			+ ");";
	
	private static final String CREATE_TIMEZONES_TABLE_SQL = "create table if not exists "
			+ "timezones(id integer primary key autoincrement,time2modify long,gmt text,rawoffset integer,selected integer,cityname_en text,cityname_zh text,cityname_tw text,"
			+ "countryname_en text,countryname_zh text,countryname_tw text)";
	
	private String CREATE_TEMP_MATTER_TBL = "alter table account_matter rename to account_matter_tmp";
	private String DROP_TEMP_MATTER_TBL = "drop table account_matter_tmp";
	private String INSERT_DATA = "insert into account_matter select *,8,0 from account_matter_tmp";
	private String INSERT_DATA_FOR_VERSION4 = "insert into account_matter select * from account_matter_tmp";
	
	private String[] defaultCategory;
	
	private static final String ALTER_MATTER_TABLE_CALENDAR = "ALTER TABLE " 
			+ MatterTable.TABLE_NAME 
			+ " ADD COLUMN "
			+ MatterTable.CALENDAR
			+ " INTEGER;";
	
	private static final String ALTER_MATTER_TABLE_REMARK = "ALTER TABLE " 
			+ MatterTable.TABLE_NAME 
			+ " ADD COLUMN "
			+ MatterTable.REMARK
			+ " TEXT;";
	
	private static final String CREATE_CATEGORY_TABLE_SQL = "CREATE TABLE " + CategoryTable.TABLE_NAME 
			+ " (" + CategoryTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ CategoryTable.NAME + " TEXT);";

	public static String UPDATE_MATTER_TABLE_TOP = "UPDATE " 
			+ MatterTable.TABLE_NAME
			+ " SET " + MatterTable.TOP + "=0 WHERE " + MatterTable.TOP + "=1;";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		defaultCategory = context.getResources().getStringArray(R.array.matter_category);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("database :onCreate 0");
		db.execSQL(CREATE_MATTER_TABLE_SQL);
		
		db.execSQL(CREATE_CATEGORY_TABLE_SQL);
		LogUtil.Sysout("matterdb : "+CREATE_MATTER_TABLE_SQL);
		LogUtil.Sysout("CATEGORY : "+CREATE_CATEGORY_TABLE_SQL);
		
		db.execSQL(CREATE_TIMEZONES_TABLE_SQL);
		System.out.println("database :onCreate ");
		initCategory(db);
	}

	private void initCategory(SQLiteDatabase db) {
		if (defaultCategory != null) {
			for (int index = 0; index < defaultCategory.length; index++) {
				ContentValues initialValues = new ContentValues();
				initialValues.put(CategoryTable._ID, index);
				initialValues.put(CategoryTable.NAME, defaultCategory[index]);
				db.replace(CategoryTable.TABLE_NAME, null, initialValues);
			}
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("database :onUpgrade oldVersion: "+oldVersion);
		if (oldVersion < 2) {
			try{
			db.execSQL(ALTER_MATTER_TABLE_CALENDAR);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if (oldVersion < 3) {
			db.execSQL(CREATE_CATEGORY_TABLE_SQL);
//			db.execSQL(ALTER_MATTER_TABLE_REMARK);
			
			initCategory(db);
		}
		
		if(oldVersion < DATABASE_VERSION){
			try{
				db.execSQL(DROP_TEMP_MATTER_TBL);
			}catch(SQLException e){
				e.printStackTrace();
			}
			db.execSQL(CREATE_TEMP_MATTER_TBL);
			db.execSQL(CREATE_MATTER_TABLE_SQL);
			if(oldVersion ==4){
				db.execSQL(INSERT_DATA_FOR_VERSION4);
			}
			else
			db.execSQL(INSERT_DATA);
		}
		db.execSQL(CREATE_TIMEZONES_TABLE_SQL);
		
		System.out.println("database :onUpgrade 1");
	}

}
