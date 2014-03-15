
/**
 * ϵͳ��Ŀ���
 * com.aohe.chequn.util
 * LogUtil.java
 * 
 * 2011-12-19-����4:25:21
 *  2011AoHe��˾-��Ȩ����
 * 
 */
package com.pybeta.daymatter.utils;

import android.content.Context;
import android.util.Log;

/**
 * 
 * LogUtil
 * 
 * Jameslin Jameslin 2011-12-19 ����4:25:21
 * 
 * @version 1.0.0
 * 
 */
public class LogUtil {

	public static void Sysout(String msg) {
		if (debug) {
			LogUtil.d(tag+"= " + msg);
		}
	}

	public static void Sysout(Context con, String msg) {
		if (debug) {
			LogUtil.d(con.getClass().getSimpleName() + " & msg = "
					+ msg);
		}
	}

	public static void LogD(Context con, String msg) {
		if (debug) {
			Log.d(con.getClass().getSimpleName(), msg);
		}
	}

	public static void LogI(Context con, String msg) {
		if (debug) {
			Log.i(con.getClass().getSimpleName(), msg);
		}
	}

	public static void LogE(Context con, String msg) {
		if (debug) {
			Log.e(con.getClass().getSimpleName(), msg);
		}
	}

	private static String tag = "com.pybeta.daymatter";
	private static boolean debug = true;

	public static boolean isDebug() {
		return debug;
	}

	public static void v(String message) {
		if (debug) {
			Log.v(tag, message);
		}
	}

	public static void d(String message) {
		if (debug) {
			Log.d(tag, message);
		}
	}

	public static void i(String message) {
		if (debug) {
			Log.i(tag, message);
		}
	}

	public static void w(String message) {
		if (debug) {
			Log.w(tag, message);
		}
	}

	public static void e(String message) {
		if (debug) {
			Log.e(tag, message);
		}
	}
}
