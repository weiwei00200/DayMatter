package com.pybeta.ui.widget;

import com.pybeta.daymatter.R;
import com.pybeta.daymatter.calendar.UcCalendar;
import com.pybeta.daymatter.tool.DateTimeTool;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DatePickerContainer extends LinearLayout{
	private Context mContext= null;
	private UcCalendar mUcDatePicker = null;
	private TextView mTvToday = null;
	
	public DatePickerContainer(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	public DatePickerContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public void initView(Context context){
		mContext = context;
		ViewGroup vg = (ViewGroup)LayoutInflater.from(context).inflate(R.layout.uc_datepicker_container, null);
		mUcDatePicker = (UcCalendar)vg.findViewById(R.id.dialog_container_datepicker);
		mUcDatePicker.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//		mTvToday = (TextView)vg.findViewById(R.id.tv_dialog_container_today);
		vg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		this.addView(vg);
	}
	public void setDate(String id){
		mUcDatePicker.prepareViewDate(id);
//		mTvToday.setText(String.format(getResources().getString(R.string.matter_today_format),DateTimeTool.getCurrentDateTimeString()));
	}
	
	public void saveCustomDate(){
		mUcDatePicker.saveHoliday();
	}
}
