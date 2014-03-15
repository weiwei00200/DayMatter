
package com.pybeta.daymatter.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pybeta.daymatter.CategoryItem;
import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.WorldTimeZone;
import com.pybeta.daymatter.utils.LogUtil;
import com.pybeta.daymatter.utils.UtilityHelper;

public class DatabaseManager {

	public static String LOCK = "dblock";
	
	public static String UPDATE_MATTER_TABLE_CATEGORY = "UPDATE " 
			+ MatterTable.TABLE_NAME
			+ " SET " + MatterTable.CATEGORY + "=0 WHERE " +  MatterTable.CATEGORY + "=%d";;
	
	private static DatabaseManager sDatabaseMgr;
	private static DatabaseHelper sDatabaseHelper = null;
	private TimeZoneDataBaseHelper mTimeZoneDataBaseHelper = null;
    
	public synchronized static DatabaseManager getInstance(Context context) {
		if (sDatabaseMgr == null) {
			sDatabaseMgr = new DatabaseManager(context);
            sDatabaseHelper = new DatabaseHelper(context);
            sDatabaseMgr.checkDB(context);
		}
		return sDatabaseMgr;
	}
	
	private DatabaseManager(Context context) {
//		checkDB(context);
	}
	
	
	private void checkDB(Context context){
		
		String dateBasePath = 
				context.getApplicationInfo().dataDir + "/databases/"
						+ DatabaseHelper.DATABASE_NAME;
		String dateBaseTmpPath = 
				context.getApplicationInfo().dataDir + "/databases/"
						+ DatabaseHelper.DATABASE_NAME+".new";
		File file = new File(dateBasePath);
		int datebase_version = UtilityHelper.getDatabaseVersion(context, IContants.KEY_DATABASE_VERSION);
//		if(datebase_version < 4)
//		if(file == null || !file.exists() ||datebase_version < 4){
		if(datebase_version < 4){
			File dir = new File(context.getApplicationInfo().dataDir + "/databases/");
			dir.mkdirs();
			InputStream fin = null;
			FileOutputStream fout = null;
			try {
				fin = context.getAssets().open(DatabaseHelper.DATABASE_NAME);
				fout = new FileOutputStream(dateBaseTmpPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				if (fin != null && fout != null) {
					byte[] buf = new byte[1024 * 10];
					int sum = 0;
					while ((sum = fin.read(buf)) != -1) {
						fout.write(buf, 0, sum);
					}
					fout.flush();
					fout.close();
					fin.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String path = context.getDatabasePath(DatabaseHelper.DATABASE_NAME+".new").getPath();
			try {
				cpVersion4DataInDataBase(context);
//				String command = "chmod " + "666" + " " + path;
//				Runtime runtime = Runtime.getRuntime();
//				runtime.exec(command);
				File tmpFile = new File(dateBaseTmpPath);
				if(tmpFile.exists())
					tmpFile.delete();
				UtilityHelper.setDatabaseVersion(context, IContants.KEY_DATABASE_VERSION, DatabaseHelper.DATABASE_VERSION);
				
			} catch( Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void cpVersion4DataInDataBase(Context context){
		ArrayList<WorldTimeZone> mWorldTimeZones = queryAllWorldTimeOriginalData(context);
		insertBatch(mWorldTimeZones);
	}
	
	public ArrayList<WorldTimeZone> queryAllWorldTimeOriginalData(Context context) {
		ArrayList<WorldTimeZone> mWorldTimeZones = null;
		mTimeZoneDataBaseHelper = new TimeZoneDataBaseHelper(context);
		SQLiteDatabase rsd = mTimeZoneDataBaseHelper.getReadableDatabase();
//		mWorldTimeZones = TimeZonesDb.getMultWorldTimeOriginalData(rsd);
		mWorldTimeZones = TimeZonesDb.getMultWorldTime(rsd);
		return mWorldTimeZones;	
	}
	
	public void insertBatch(ArrayList<WorldTimeZone> mWorldTimeZones) {
		TimeZonesDb.insertBatch(sDatabaseHelper, mWorldTimeZones);
	}
	
	
	public void add(DayMatter matter) {
		SQLiteDatabase wsd = sDatabaseHelper.getWritableDatabase();
		//System.out.println("vk2013 add matter");
		wsd.beginTransaction();
		if (matter != null && matter.getTop() == 1) {
			wsd.execSQL(DatabaseHelper.UPDATE_MATTER_TABLE_TOP);
		} 
		long id = wsd.insertWithOnConflict(MatterTable.TABLE_NAME, null, matter.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
		matter.setId((int)id);
		wsd.setTransactionSuccessful();
		wsd.endTransaction();
	}
	
	public void update(DayMatter matter) {
		SQLiteDatabase wsd = sDatabaseHelper.getWritableDatabase();
		wsd.beginTransaction();
		if (matter != null && matter.getTop() == 1) {
			wsd.execSQL(DatabaseHelper.UPDATE_MATTER_TABLE_TOP);
		} 
//		String sql = matter.getUpdateSql();
//		wsd.execSQL(sql);
		wsd.update(MatterTable.TABLE_NAME, matter.getContentValues(), MatterTable.UID + "=?", new String[] {matter.getUid()});
		wsd.setTransactionSuccessful();
		wsd.endTransaction();
	}
	
	public DayMatter query(String uid) {
		Cursor cursor = null;
		try {
			SQLiteDatabase rsd = sDatabaseHelper.getReadableDatabase();	
			cursor = rsd.query(MatterTable.TABLE_NAME, null, MatterTable.UID + "=?", new String[] {uid}, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				DayMatter matter = new DayMatter(cursor);
				return matter;
			}
		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;	
	}
	
	public DayMatter query(int id) {
		Cursor cursor = null;
		try {
			SQLiteDatabase rsd = sDatabaseHelper.getReadableDatabase();	
			cursor = rsd.query(MatterTable.TABLE_NAME, null, MatterTable.ID + "=?", new String[] {Integer.toString(id)}, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				DayMatter matter = new DayMatter(cursor);
				return matter;
			}
		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;	
	}
	
	public void delete(DayMatter matter) {
		SQLiteDatabase wsd = sDatabaseHelper.getWritableDatabase();
		wsd.delete(MatterTable.TABLE_NAME, MatterTable.UID + "=?", new String[] {matter.getUid()});
	}
	
	public List<DayMatter> queryAll(int category) {
		ArrayList<DayMatter> matterList = new ArrayList<DayMatter>();
		
		Cursor cursor = null;
		try {
			SQLiteDatabase rsd = sDatabaseHelper.getReadableDatabase();
			if (category == 0) {
				cursor = rsd.query(MatterTable.TABLE_NAME, null, null, null, null, null, null);
			} else {
				cursor = rsd.query(MatterTable.TABLE_NAME, null, MatterTable.CATEGORY + "=?", new String[] {Integer.toString(category)}, null, null, null);
			}
			if (cursor != null) {
				while (cursor.moveToNext()) {
					DayMatter matter = new DayMatter(cursor);
					matterList.add(matter);
				}
			}
		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		return matterList;	
	}
	
	public DayMatter queryTop() {
		Cursor cursor = null;
		try {
			SQLiteDatabase rsd = sDatabaseHelper.getReadableDatabase();	
			cursor = rsd.query(MatterTable.TABLE_NAME, null, MatterTable.TOP + "=?", new String[] {"1"}, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				DayMatter matter = new DayMatter(cursor);
				return matter;
			}
		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
	}
	
	public List<CategoryItem> queryCanModifyCategoryList() {
		List<CategoryItem> categoryList = new ArrayList<CategoryItem>();
		
		Cursor cursor = null;
		try {
			SQLiteDatabase rsd = sDatabaseHelper.getReadableDatabase();
			cursor = rsd.query(CategoryTable.TABLE_NAME, null, CategoryTable._ID + ">0", null, null, null, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					CategoryItem category = new CategoryItem(cursor);
					categoryList.add(category);
				}
			}
		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return categoryList;
	}
	
	public List<CategoryItem> queryCategoryList() {
		List<CategoryItem> categoryList = new ArrayList<CategoryItem>();
		LogUtil.Sysout("cursor: queryCategoryList");
		Cursor cursor = null;
		try {
			LogUtil.Sysout("cursor: queryCategoryList1");
			SQLiteDatabase rsd = sDatabaseHelper.getReadableDatabase();
			LogUtil.Sysout("cursor: queryCategoryList2");
			cursor = rsd.query(CategoryTable.TABLE_NAME, null, null, null, null, null, null);
			LogUtil.Sysout("cursor: "+cursor);
			if (cursor != null) {
				LogUtil.Sysout("cursor: "+cursor.getCount());
				while (cursor.moveToNext()) {
					CategoryItem category = new CategoryItem(cursor);
					categoryList.add(category);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return categoryList;
	}
	
	public void addCategory(CategoryItem category) {
		ContentValues initialValues = new ContentValues();
		
		if (category.getId() > 0) {
			initialValues.put(CategoryTable._ID, category.getId());
		}
		initialValues.put(CategoryTable.NAME, category.getName());
		
		SQLiteDatabase wsd = sDatabaseHelper.getWritableDatabase();
		wsd.replace(CategoryTable.TABLE_NAME, null, initialValues);
	}
	
	public void deleteCategory(CategoryItem category) {
		SQLiteDatabase wsd = sDatabaseHelper.getWritableDatabase();
		wsd.delete(CategoryTable.TABLE_NAME, CategoryTable._ID + "=?", new String[] {String.valueOf(category.getId())});
		
		wsd.execSQL(String.format(UPDATE_MATTER_TABLE_CATEGORY, category.getId()));
	}
	
	public String queryCatNameById(int id) {
		Cursor cursor = null;
		
		String catName = "";
		try {
			SQLiteDatabase rsd = sDatabaseHelper.getReadableDatabase();
			cursor = rsd.query(CategoryTable.TABLE_NAME, null, CategoryTable._ID + "=?", new String[] {String.valueOf(id)}, null, null, null);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					catName = cursor.getString(cursor.getColumnIndex(CategoryTable.NAME));
				}
			}
		} catch (Exception e) {
		}
		return catName;
	}
	
	public void updateCatWhenSwithcLang(String[] defaultCategory) {
		if (defaultCategory != null) {
			SQLiteDatabase wsd = sDatabaseHelper.getWritableDatabase();
			for (int index = 0; index < defaultCategory.length; index++) {
				ContentValues initialValues = new ContentValues();
				initialValues.put(CategoryTable._ID, index);
				initialValues.put(CategoryTable.NAME, defaultCategory[index]);
				wsd.replace(CategoryTable.TABLE_NAME, null, initialValues);
			}
		}
	}
	
	public List<WorldTimeZone> queryAllWorldTime(int category) {
		ArrayList<WorldTimeZone> mWorldTimeZones = null;
		SQLiteDatabase rsd = sDatabaseHelper.getReadableDatabase();
		mWorldTimeZones = TimeZonesDb.getMultWorldTimeSelected(rsd);
		return mWorldTimeZones;	
	}
	
	public void updateWorldTimeSelected(WorldTimeZone mWorldTimeZone,int selected) {
		SQLiteDatabase db = sDatabaseHelper.getWritableDatabase();
		TimeZonesDb.update(db, mWorldTimeZone,selected);
	}
	
	public void deleteWorldTimeSelected(WorldTimeZone mWorldTimeZone) {
		SQLiteDatabase db = sDatabaseHelper.getWritableDatabase();
		TimeZonesDb.delete(db, mWorldTimeZone);
	}
}
