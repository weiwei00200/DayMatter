package com.pybeta.daymatter.ui;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.R;
import com.pybeta.ui.widget.UcTitleBar;

public class HolidayRecActivity extends Activity implements OnTabChangeListener{
	//本来继承SherlockFragment
	protected List<DayMatter> mHolidayList = Collections.emptyList();
	private TabHost mTabHost = null;
	private TabWidget mTabWidget = null;
	
	private UcTitleBar mTitleBar = null;
	private HolidayAllPanel mHolidayAllPanel = null;
	private HolidayFestivalPanel mHolidayFestivalPanel = null;
	private HolidayHolidayPanel mHolidayHolidayPanel = null;
	private HolidaySolartermPanel mHolidaySolartermPanel = null;
	private boolean isAllDirty,isFestivalDirty,isHolidayDirty,isSolartermDirty = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_holiday_rec);
		mHolidayAllPanel = (HolidayAllPanel)this.findViewById(R.id.holiday_all_panel);
		mHolidayFestivalPanel = (HolidayFestivalPanel)this.findViewById(R.id.holiday_festival_panel);
		mHolidayHolidayPanel = (HolidayHolidayPanel)this.findViewById(R.id.holiday_holiday_panel);
		mHolidaySolartermPanel = (HolidaySolartermPanel)this.findViewById(R.id.holiday_solarterm_panel);
		mTitleBar = (UcTitleBar)this.findViewById(R.id.uc_titlebar_holidayrec);
		
		mTabHost = (TabHost) this.findViewById(R.id.tabhost);
		mTabWidget = (TabWidget)this.findViewById(android.R.id.tabs);
		mTabHost.setup();
		setIndicator(getResources().getString(R.string.holiday_tab_all),"all",R.id.holiday_all_panel,true);
		setIndicator(getResources().getString(R.string.holiday_tab_festival),"festival",R.id.holiday_festival_panel,false);
		setIndicator(getResources().getString(R.string.holiday_tab_holiday),"holiday",R.id.holiday_holiday_panel,false);
		setIndicator(getResources().getString(R.string.holiday_tab_solarterm),"solarterm",R.id.holiday_solarterm_panel,false);
		mTabHost.setOnTabChangedListener(this);
		mTabHost.setCurrentTab(0);
		//将第一个tab的字体变蓝色
		View view = (View)mTabHost.getTabWidget().getChildTabViewAt(0);
		TextView tv = (TextView)view.findViewById(R.id.tv_holiday_tabtext);
		tv.setTextColor(getResources().getColor(R.color.title_bar));
		
		loadDefaultPanel(this);
		
		initTitleBar();
	}
	private void initTitleBar(){
		
		mTitleBar.setTitleText(getResources().getString(R.string.title_activity_holiday_festival));
    	mTitleBar.setViewVisible(false, true, false, true, false, false, false, false);
    	mTitleBar.setListener(new UcTitleBar.ITitleBarListener() {
			@Override
			public void shareClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void menuClick(Object obj) {
				// TODO Auto-generated method stub
			}
			@Override
			public void feedBackClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void editClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void completeClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void backClick(Object obj) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
			@Override
			public void addClick(Object obj) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HolidayRecActivity.this, AddMatterActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			@Override
			public void closeClick(Object obj) {
				// TODO Auto-generated method stub
			}
		});
	} 
	
	private void setIndicator(String tabText, String tabFlag, int layoutId, boolean isAllTab) {
		View view = (View)LayoutInflater.from(this.mTabHost.getContext()).inflate(R.layout.uc_holiday_tabitem, null);
		TextView tv = (TextView)view.findViewById(R.id.tv_holiday_tabtext);
		tv.setText(tabText);
		ImageView img = (ImageView)view.findViewById(R.id.img_holiday_tab_triangle);
		view.setTag(tabFlag);
		if(isAllTab){
			tv.setTextColor(Color.BLACK);
		}else{
			img.setVisibility(View.INVISIBLE);
			tv.setTextColor(Color.GRAY);
		}
		TabHost.TabSpec spec = this.mTabHost.newTabSpec(tabFlag).setIndicator(view).setContent(layoutId);
		this.mTabHost.addTab(spec);
	}
	
	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		for(int i=0; i<mTabHost.getTabWidget().getChildCount(); i++){
			View view = (View)mTabHost.getTabWidget().getChildTabViewAt(i);
			TextView tv = (TextView)view.findViewById(R.id.tv_holiday_tabtext);
			tv.setTextColor(Color.BLACK);
			ImageView img = (ImageView)view.findViewById(R.id.img_holiday_tab_triangle);
			img.setVisibility(View.INVISIBLE);
			if(mTabHost.getTabWidget().getChildTabViewAt(i).getTag().toString().equals(tabId)){
				tv.setTextColor(getResources().getColor(R.color.title_bar));
				img.setVisibility(View.VISIBLE);
				if("all".equals(tabId) && !isAllDirty){
					loadDefaultPanel(HolidayRecActivity.this);
				}else if("festival".equals(tabId) && !isFestivalDirty){
					loadFestivalPanel(HolidayRecActivity.this);
				}else if("holiday".equals(tabId) && !isHolidayDirty){
					loadHolidayPanel(HolidayRecActivity.this);
				}else if("solarterm".equals(tabId) && !isSolartermDirty){
					loadSolartermPanel(HolidayRecActivity.this);
				}
			}
		}
	}
	
	private void loadDefaultPanel(Context context){
		mHolidayAllPanel.loadListViewData(context);
		isAllDirty = false;
	}
	private void loadFestivalPanel(Context context){
		mHolidayFestivalPanel.loadListViewData(context);
		isFestivalDirty = false;
	}
	private void loadHolidayPanel(Context context){
		mHolidayHolidayPanel.loadListViewData(context);
		isHolidayDirty = false;
	}
	private void loadSolartermPanel(Context context){
		mHolidaySolartermPanel.loadListViewData(context);
		isSolartermDirty = false;
	}
}
