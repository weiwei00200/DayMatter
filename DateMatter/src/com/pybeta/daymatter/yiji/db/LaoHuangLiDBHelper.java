package com.pybeta.daymatter.yiji.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pybeta.daymatter.Config;
import com.pybeta.daymatter.bean.YiJiChongBean;
import com.pybeta.daymatter.tool.DateTimeTool;

public class LaoHuangLiDBHelper extends SQLiteOpenHelper {

	/**
	 * ClassName: YiJiDBHelper Function: TODO date: 2014-3-11 上午11:58:55
	 * 
	 * @author Devin.Yu
	 */
	public Context mContext;
	public static final String DB_NAME = Config.APP_PATH + "laohuangli.db";
	public static final int DB_VERSION = 1;
	public SQLiteDatabase db;

	public static LaoHuangLiDBHelper dbHelper;

	public static LaoHuangLiDBHelper getHelper(Context context) {
		if (dbHelper == null) {
			synchronized (LaoHuangLiDBHelper.class) {
				dbHelper = new LaoHuangLiDBHelper(context);
			}
		}
		return dbHelper;
	}

	public LaoHuangLiDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.mContext = context;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public String QueryDB(String time) {

		String JieQi = "";
		time = DateTimeTool.getDateByString(time, "yyyy-MM-dd");
		SQLiteDatabase db = getReadableDatabase();
	    String sql = "select count(*) from hl_jieqi WHERE time like '" +time +"%"+ "'";
	    String sumsql = "select * from hl_jieqi WHERE time like '" +time +"%"+ "'";
		Cursor c = db.rawQuery(sql, null);
		c.moveToLast();
		long sum = c.getLong(0);
		c.close();
		if (sum > 0) {
			Cursor cursor=db.rawQuery(sumsql, null);
			while (cursor.moveToNext()) {
				JieQi = cursor.getString(cursor.getColumnIndex("type"));
                
			}
			cursor.close();
		}
		db.close();
		return JieQi;

	}

}
