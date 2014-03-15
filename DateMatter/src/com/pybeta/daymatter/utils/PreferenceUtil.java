/**
 * 
 */
package com.pybeta.daymatter.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * @author james.lin
 *
 */
public class PreferenceUtil {

	public static Editor getEditor(String prefer,Context context){
		SharedPreferences sharedPreferences=context.getSharedPreferences(prefer, context.MODE_WORLD_WRITEABLE|context.MODE_WORLD_READABLE);
		Editor editor=sharedPreferences.edit();
		return editor;
	}
	
	public static SharedPreferences getSharedPreferences(String prefer,Context context){
		SharedPreferences sharedPreferences=context.getSharedPreferences(prefer, context.MODE_WORLD_WRITEABLE|context.MODE_WORLD_READABLE);
		return sharedPreferences;
	}
	
	public static Editor getDefaultEditor(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit = preferences.edit();
		return edit;
	}
	
	public static SharedPreferences getDefaultSharedPreferences(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences;
	}
}
