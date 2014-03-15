package com.pybeta.daymatter.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pybeta.daymatter.R;
import com.pybeta.daymatter.bean.HardCodeHolidayDate;
import com.pybeta.daymatter.bean.HolidayConllectionBean;
import com.pybeta.daymatter.bean.HolidayItemBean;
import com.pybeta.daymatter.calendar.UcCalendar;
import com.pybeta.daymatter.ui.HolidayDetailActivity.MarkHolidayType;

public class HolidayDetailAdapter extends PagerAdapter {
	private List<View> mViewList = null;
	private Context mContext = null;
	private List<String> mIdList = null;
	private MarkHolidayType mMarkHolidayType = null;
	private List<String> mCategoryList = null;
	private List<String> mOldDateList = null;
	public HolidayDetailAdapter(List<View> viewList, List<String> idList, Context context, MarkHolidayType type,List<String> categoryList,List<String> oldDateList){
		mViewList = viewList;
		mIdList = idList;
		mContext = context;
		mMarkHolidayType = type;
		mCategoryList = categoryList;
		mOldDateList = oldDateList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mViewList.size();
	}
	@Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }
	
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        container.removeView(mViewList.get(position));
    }
    
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        View view = mViewList.get(position);//当前视图
        final LinearLayout layout_mask = (LinearLayout)view.findViewById(R.id.top_layout);
        layout_mask.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
        LinearLayout layout_tag = (LinearLayout)view.findViewById(R.id.layout_tag);
        if(mMarkHolidayType == MarkHolidayType.Holiday || (mMarkHolidayType == MarkHolidayType.All && mCategoryList.get(position).equals("1"))){
        	final LinearLayout layout_datePicker = (LinearLayout)view.findViewById(R.id.layout_detail_datepicker);
            UcCalendar mUcCalendar = new UcCalendar(mContext);
            mUcCalendar.prepareViewDate(mIdList.get(position));
            layout_datePicker.addView(mUcCalendar);
        }else{
        	//line.setVisibility(View.GONE);
        	layout_tag.removeAllViews();
        }
        container.addView(view);
        return view;
    }
}
