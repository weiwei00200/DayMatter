package com.pybeta.daymatter.core;

import java.lang.Thread.UncaughtExceptionHandler;

import com.pybeta.daymatter.utils.DateUtils;
import com.sammie.common.log.LogManager;

import android.app.Application;
import android.util.DisplayMetrics;
import android.util.Log;

public class MatterApplication extends Application {

	public int screenHeight = 0;
	public int screenWidth = 0;
	
	public String password = "";
	
	
	
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private static boolean mInitialized = false;

    public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	@Override
    public void onCreate() {
        super.onCreate();
        
        initScreenInfo();
        
        LogManager.setRootPath("/mnt/sdcard/DayMatter/Log/");
        if (!mInitialized) {
        	
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DataManager dataMgr = DataManager.getInstance(getApplicationContext());
                    dataMgr.startup();

                    DateUtils.setDateChangedListener(getApplicationContext());
                    mInitialized = true;
                }
            }).start();
        }
    }
    private void initScreenInfo(){
    	DisplayMetrics displayMetrics = getResources().getDisplayMetrics();  
    	this.setScreenWidth(displayMetrics.widthPixels);
    	this.setScreenHeight(displayMetrics.heightPixels);
    }
	
    public static boolean isInitialized() {
        return mInitialized;
    }
    
    public static boolean waitInitTimeOut(long startTime, long endTime){
        return (Math.abs(endTime - startTime) > 2000) ? true : false;
    }

	
}
