package com.pybeta.daymatter.calendar;


import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.pybeta.daymatter.R;
import com.pybeta.daymatter.bean.HolidayConllectionBean;
import com.pybeta.daymatter.bean.HolidayItemBean;
import com.pybeta.daymatter.calendar.CalendarView.OnItemClickListener;
import com.pybeta.daymatter.core.MatterApplication;
import com.pybeta.daymatter.tool.DateTimeTool;

public class UcCalendar extends LinearLayout{
	private Context mContext = null;
	private LinearLayout mLayoutUcCalendar = null;
	private CalendarView mCalenderView = null;
	private String mCurrentCalendarDate = "";
	static HolidayItemBean mHolidayItemBean = null;
	private String mMarkRedDay = "";
	
	public UcCalendar(Context context) {
		super(context);
		initView(context);
	}
	public UcCalendar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public void initView(Context context){
		mMarkRedDay = DateTimeTool.getCurrentDateTimeString("yyyy-MM-dd");
		mContext = context;
		LayoutInflater mInflater = LayoutInflater.from(mContext);
		ViewGroup vg = (ViewGroup)mInflater.inflate(R.layout.uc_calendar, null);
		mLayoutUcCalendar = (LinearLayout)vg.findViewById(R.id.layout_uc_calendar);
		LayoutParams lp = new LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		vg.setLayoutParams(lp);
		this.addView(vg);
	}
	
	public void prepareViewDate(String id){
		mHolidayItemBean = HolidayConllectionBean.getInstance().getHolidayById(id);
		if(null != mHolidayItemBean){
			mCurrentCalendarDate = mHolidayItemBean.getItemDate();
			if(DateTimeTool.convertToDate(mCurrentCalendarDate).before(DateTimeTool.getCurrentDateTimeObject())){
				mCurrentCalendarDate = DateTimeTool.getModifyYearString(mCurrentCalendarDate, "yyyy-MM-dd", 1);
			}
			mLayoutUcCalendar.removeAllViews();
			mCalenderView = new CalendarView(mContext);
			mCalenderView.prepareView(mHolidayItemBean, mCurrentCalendarDate, mMarkRedDay);// mHolidayItemBean.getItemDate());
			mCalenderView.setOnItemClickListener(new ItemOnclickEvent(mCalenderView, mLayoutUcCalendar, mHolidayItemBean));
			mLayoutUcCalendar.addView(mCalenderView);
		}
	}
	
	class ItemOnclickEvent implements OnItemClickListener{
		private CalendarView mCalendarView = null;
		private LinearLayout mLayout = null;
		private HolidayItemBean mBean = null;
		public ItemOnclickEvent(CalendarView calendarView,LinearLayout container,HolidayItemBean bean){
			mCalendarView = calendarView;
			mLayout = container;
			mBean=bean;
		}
		@Override
		public void OnItemClick(Date date) {
			// TODO Auto-generated method stub
			String clickedDate = DateTimeTool.toDateString(date, "yyyy-MM-dd");
			List<String> onHolidayList = mBean.getOnHolidayList();
			List<String> onWorkList = mBean.getOnWorkList();
			if(onHolidayList.contains(clickedDate)){//放假->上班->无
				onHolidayList.remove(clickedDate);
				onWorkList.add(clickedDate);
			}else if(onWorkList.contains(clickedDate)){//上班->无
				onWorkList.remove(clickedDate);
			}else{//无->放假->上班
				onHolidayList.add(clickedDate);
			}
			mCurrentCalendarDate = mCalendarView.getShowingViewDate();
			mLayout.removeAllViews();
			mHolidayItemBean = mBean;
			mCalendarView = new CalendarView(mContext);
			
			mCalendarView.prepareView(mBean, mCurrentCalendarDate, mMarkRedDay);//mBean.getItemDate());
			mCalendarView.setOnItemClickListener(new ItemOnclickEvent(mCalendarView, mLayout, mBean));
			mLayout.addView(mCalendarView);
			
		}
	}
	public void saveHoliday(){
		try {
			HolidayConllectionBean.getInstance().modifyHolidayById(mHolidayItemBean.getId(), mHolidayItemBean);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		mHolidayItemBean = null;
		
	}
}
