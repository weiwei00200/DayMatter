package com.pybeta.daymatter;

public class Config {
	
	
	public final static String ROOT_PATH = "/mnt/sdcard/QiuYou/";
	public final static String APP_PATH = "/data/data/com.pybeta.daymatter/files/";
	/**
	 * 版本更新文件存放的目录
	 * @return
	 */
	public static String getAppUpdate(){
		return ROOT_PATH + "backup/";
	}
	/**
	 * 配置文件存放目录
	 * @return
	 */
	public static String getConfigPath(){
		return ROOT_PATH + "config/";
	}
	/**
	 * 日志文件存放目录
	 * @return
	 */
	public static String getLogPath(){
		return ROOT_PATH + "log/";
	}
	
	public static final String  ALL_TYPE="ALL_TYPE";
	public static final String  FESTIVAL_TYPE="FESTIVAL_TYPE";
	public static final String  HOLIDAY_TYPE="HOLIDAY_TYPE";
	public static final String  SOLARTERM_TYPE="SOLARTERM_TYPE";
	//序列化文件地址
	public final static String SerializePath_UserInfo= APP_PATH+"Serialize/Psw.ini";
}
