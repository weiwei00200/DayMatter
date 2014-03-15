package com.pybeta.ui.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class DatePickerContainerDialog extends Dialog{
	private Context mContext = null;
	private DatePickerContainer mContentContainer = null;
	
	public DatePickerContainerDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	public DatePickerContainerDialog(Context context,int style) {
		super(context,style);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	private void initView(Context context){
		mContext = context;
		
		this.setCancelable(true);
		mContentContainer = new DatePickerContainer(context);
		mContentContainer.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT,android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.height = android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
		lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.gravity = Gravity.BOTTOM;
		lp.y = 28;
		window.setAttributes(lp);
		this.setContentView(mContentContainer);
	}
	
	public void prepareDate(String id){
		mContentContainer.setDate(id);
	}
	
	public void saveCustomDate(){
		mContentContainer.saveCustomDate();
	}
	
	public void showDialog() {
		this.show();
		
	}
	
	public void closeDialog(){
		this.dismiss();
	}

}
