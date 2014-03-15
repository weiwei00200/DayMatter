package com.pybeta.daymatter.yiji.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.pybeta.daymatter.Config;

import android.content.Context;

public class DBManager {

	/*
	 *
	 * ClassName: FileTool Function: TODO date: 2014-3-11 下午2:10:50
	 * @author Devin.Yu
	 *
	 */
	
	private Context mContext;
	
	public static  String DB_PATH=Config.APP_PATH;
	public static final String DB_Name="yiji.db";
	
	
	
	public static DBManager dbmanager;
	public static DBManager getDBManager(Context context)
	{
		if(dbmanager==null)
		{
			dbmanager=new DBManager(context);
		}
		return dbmanager;
	}
	public DBManager(Context context)
	{
		this.mContext=context;
		
	}
	
	public static void WriteDBFile(Context context,int rawID,String DB_Name) {
		try {
			File file=new File(DB_PATH);
			
			if(!file.exists())
			{
				file.mkdir();
			}
			File dbfile=new File(DB_PATH+DB_Name);
			if(!dbfile.exists())
				{
					InputStream input =context.getResources().openRawResource(rawID);
					byte[] buf = new byte[1024];
					int Sum = 0;
					FileOutputStream fout = null;
					
						fout = new FileOutputStream(DB_PATH+DB_Name);
						while ((Sum = input.read(buf)) != -1) {
							fout.write(buf, 0, Sum);
						}
						fout.flush();
						fout.close();
						input.close();
				
			     }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
