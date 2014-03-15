package com.pybeta.daymatter.ui;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pybeta.daymatter.BuildConfig;
import com.pybeta.daymatter.Config;
import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.adapter.HolidayDetailAdapter;
import com.pybeta.daymatter.core.DataManager;
import com.pybeta.daymatter.threadcommon.CustomRunnable;
import com.pybeta.daymatter.threadcommon.IDataAction;
import com.pybeta.daymatter.tool.DateTimeTool;
import com.pybeta.daymatter.utils.DateUtils;
import com.pybeta.daymatter.utils.UtilityHelper;
import com.pybeta.ui.widget.DatePickerContainer;
import com.pybeta.ui.widget.LoadingDialog;
import com.pybeta.ui.widget.UcTitleBar;
import com.pybeta.ui.widget.UcTitleBar.ITitleBarListener;
import com.pybeta.util.DMToast;

public class HolidayDetailActivity extends BaseActivity{
	private List<DayMatter> mDataList = new ArrayList<DayMatter>();
	private ViewPager mViewPager = null;
	private PagerAdapter mAdapter = null;
	private LinearLayout mLayoutZheZhao = null;
	private LinearLayout mLayoutCalendarContainer = null;
	private int currentPage = 0;
//	private DatePickerContainerDialog mDatePickerDialog = null;
	private UcTitleBar mTitleBar = null; 
	private List<View> mViewList = new ArrayList<View>();
	private List<String> mIdList = new ArrayList<String>();
	private MarkHolidayType mMarkHolidayType;
	private DatePickerContainer mContentContainer = null;
	private List<String> mCategoryList = null; 
	private List<String> mOldDateList = null; 
	private String mType = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_holiday_detail);
		
		Bundle bundle = getIntent().getExtras();
		currentPage = bundle.getInt("position");
		mType = bundle.getString("type");
		if(mType.equals(Config.ALL_TYPE)){
			mDataList = HolidayAllPanel.mDayMatterList;
			mMarkHolidayType = MarkHolidayType.All;
		}else if(mType.equals(Config.FESTIVAL_TYPE)){
			mDataList = HolidayFestivalPanel.mDayMatterList;
			mMarkHolidayType = MarkHolidayType.Festival;
		}else if(mType.equals(Config.HOLIDAY_TYPE)){
			mDataList = HolidayHolidayPanel.mDayMatterList;
			mMarkHolidayType = MarkHolidayType.Holiday;
		}else if(mType.equals(Config.SOLARTERM_TYPE)){
			mDataList = HolidaySolartermPanel.mDayMatterList;
			mMarkHolidayType = MarkHolidayType.Solarterm;
		}
		mViewPager = (ViewPager)this.findViewById(R.id.viewpager_holiday_detail);
		mLayoutCalendarContainer = (LinearLayout)this.findViewById(R.id.layout_ucCalendar_container);
		mLayoutZheZhao = (LinearLayout)this.findViewById(R.id.viewpager_holiday_detail_zhezhao);
		mLayoutZheZhao.setOnClickListener(new OnClickListener() {//遮罩层出现的时候将底层事件屏蔽
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			}
		});
		initView(true);
		initTitleBar();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	private void initTitleBar(){
		mTitleBar = (UcTitleBar)this.findViewById(R.id.uc_titlebar);
		mTitleBar.setTitleText(getResources().getString(R.string.title_activity_holiday_festival));
		if(mMarkHolidayType == MarkHolidayType.Holiday){
			mTitleBar.setViewVisible(false, true, false, false, true, false, true, false);
		}else{
			mTitleBar.setViewVisible(false, true, false, false, false, false, true, false);
		}
		
		ITitleBarListener listener = new ITitleBarListener() {
			@Override
			public void shareClick(Object obj) {
				// TODO Auto-generated method stub
				takeScreenshotAndShare();
			}
			@Override
			public void editClick(Object obj) {
				// TODO Auto-generated method stub
	            mLayoutCalendarContainer.removeAllViews();
	            mLayoutCalendarContainer.setVisibility(View.VISIBLE);
	            mContentContainer = new DatePickerContainer(HolidayDetailActivity.this);
	    		mContentContainer.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT,android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
	    		mContentContainer.setDate(mIdList.get(currentPage));
	    		mLayoutCalendarContainer.addView(mContentContainer);
	    		mLayoutZheZhao.setVisibility(View.VISIBLE);
	    		mTitleBar.setViewVisible(false, true, false, false, false, true, true, false);
			}
			@Override
			public void completeClick(Object obj) {
				// TODO Auto-generated method stub
				mLayoutCalendarContainer.setVisibility(View.GONE);
	            mLayoutZheZhao.setVisibility(View.GONE);
	            mTitleBar.setViewVisible(false, true, false, false, true, false, true, false);
	            LoadingDialog.showLoadingDialog(HolidayDetailActivity.this,"修改中..." , false);
	            IDataAction runAction = new IDataAction() {
					@Override
					public Object actionExecute(Object obj) {
						// TODO Auto-generated method stub
						mContentContainer.saveCustomDate();
			            freshDate();
						return null;
					}
				};
				IDataAction completeAction = new IDataAction() {
					@Override
					public Object actionExecute(Object obj) {
						// TODO Auto-generated method stub
						initView(false);
						
						return null;
					}
				};
				CustomRunnable run = new CustomRunnable(runAction, completeAction);
				run.startAction();
			}
			@Override
			public void backClick(Object obj) {
				// TODO Auto-generated method stub
				finish();
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
			public void addClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void closeClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
		};
		mTitleBar.setListener(listener);
		
	}
	private void initView(boolean isFirstInit){
		if(isFirstInit){
			LoadingDialog.showLoadingDialog(HolidayDetailActivity.this,"努力加载中..." , false);
		}
		IDataAction runAction = new IDataAction() {
			@Override
			public Object actionExecute(Object obj) {
				List<HashMap<String,String>> convertedList = new ArrayList<HashMap<String,String>>();
				for(DayMatter dayMatter: mDataList){
					Map<String, Integer> mapBetween = DateUtils.getDaysBetween(dayMatter);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("Uid", dayMatter.getUid()+"");
					map.put("Title",dayMatter.getMatter()+"");
					map.put("BetweenDay", mapBetween.get("Days")+"");
					map.put("TargetDate", DateUtils.getMatterDateString(HolidayDetailActivity.this, dayMatter));
					map.put("Category", dayMatter.getCategory()+"");
					map.put("OldDate", dayMatter.getOldDate()+"");
					convertedList.add(map);
				}
				return convertedList;
			}
		};
		IDataAction completeAction = new IDataAction() {
			@Override
			public Object actionExecute(Object obj) {
				if(null != mViewPager){
					mViewPager.removeAllViews();
					mViewList.clear();
					mIdList.clear();
					mAdapter = null;
				}
				List<HashMap<String,String>> convertedList = (List<HashMap<String,String>>)obj;
				mCategoryList = new ArrayList<String>();
				mOldDateList = new ArrayList<String>();
				for(HashMap<String,String> map : convertedList){
					View view =getLayoutInflater().inflate(R.layout.viewpager_holiday_detail_content, null);
					TextView tv_title = (TextView)view.findViewById(R.id.holiday_detail_title);
					TextView tv_days = (TextView)view.findViewById(R.id.holiday_detail_days);
					TextView tv_date = (TextView)view.findViewById(R.id.holiday_detail_date);
//					if(mMarkHolidayType != MarkHolidayType.Holiday ){
//						View line = (View)view.findViewById(R.id.bottom_line);
//						line.setVisibility(View.GONE);
//					}
					//填充数据
					tv_title.setText(map.get("Title")+"");
					tv_days.setText(map.get("BetweenDay")+"");
					tv_date.setText(map.get("TargetDate")+"");
					mViewList.add(view);
					mIdList.add(map.get("Uid")+"");
					mCategoryList.add(map.get("Category"));
					mOldDateList.add((map.get("OldDate")+""));
				}
				mAdapter = new HolidayDetailAdapter(mViewList,mIdList,HolidayDetailActivity.this,mMarkHolidayType, mCategoryList, mOldDateList);
				mViewPager.setAdapter(mAdapter);
				mViewPager.setCurrentItem(currentPage);
				if(mCategoryList.get(currentPage).equals("1")){
					mTitleBar.setViewVisible(false, true, false, false, true, false, true, false);
				}else{
					mTitleBar.setViewVisible(false, true, false, false, false, false, true, false);
				}
				if(Long.parseLong(mOldDateList.get(currentPage))<System.currentTimeMillis()){
					mTitleBar.setViewVisible(false, true, false, false, false, false, true, false);
				}
				initViewPagerEvent();
				LoadingDialog.closeLoadingDialog();
				return null;
			}
		};
		CustomRunnable run = new CustomRunnable(runAction, completeAction);
		run.startAction();
	}
	
	private void initViewPagerEvent(){
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				currentPage = position;
				if(mCategoryList.get(currentPage).equals("1")){
					mTitleBar.setViewVisible(false, true, false, false, true, false, true, false);
				}else{
					mTitleBar.setViewVisible(false, true, false, false, false, false, true, false);
				}
				if(Long.parseLong(mOldDateList.get(position))<System.currentTimeMillis()){
					mTitleBar.setViewVisible(false, true, false, false, false, false, true, false);
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void takeScreenshotAndShare() {
		new ScreenshotShare().execute();
	}

	class ScreenshotShare extends AsyncTask<String, String, String> {

		private ProgressDialog progressDlg = null;

		@Override
		protected void onPreExecute() {
			Resources res = HolidayDetailActivity.this.getResources();
			progressDlg = ProgressDialog.show(HolidayDetailActivity.this, res.getString(R.string.app_name),
					res.getString(R.string.screen_shot_progress), false, false);

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			String pic = UtilityHelper.takeScreenshot(HolidayDetailActivity.this);
			if (BuildConfig.DEBUG) {
				System.out.println("screen shot: " + pic);
			}
			return pic;
		}

		@Override
		protected void onPostExecute(String result) {
			if (progressDlg != null) {
				progressDlg.dismiss();
			}

			String tips = HolidayDetailActivity.this.getString(R.string.screen_shot_save_path);
			DMToast.makeText(HolidayDetailActivity.this, tips + result, Toast.LENGTH_SHORT).show();

			UtilityHelper.share(HolidayDetailActivity.this, getString(R.string.share_intent_subject), "#"
					+ getString(R.string.app_name) + "#", result);

			super.onPostExecute(result);
		}
	}
	private void freshDate(){
		boolean isAllType = false;
		List<DayMatter> allDayMatterList = DataManager.getInstance(this).getHolidayList();
		List<DayMatter> tempMatterList = null;
		if(mMarkHolidayType == MarkHolidayType.All){
			isAllType = true;
			tempMatterList = HolidayAllPanel.mDayMatterList = allDayMatterList;
		}else if(mMarkHolidayType == MarkHolidayType.Holiday){
			HolidayHolidayPanel.mDayMatterList.clear();
			for(DayMatter dayMatter : allDayMatterList){
				if(dayMatter.getCategory() == 1){
					HolidayHolidayPanel.mDayMatterList.add(dayMatter);
				}
			}
			tempMatterList = HolidayHolidayPanel.mDayMatterList;
		}else if(mMarkHolidayType == MarkHolidayType.Festival){
			HolidayFestivalPanel.mDayMatterList.clear();
			for(DayMatter dayMatter : allDayMatterList){
				if(dayMatter.getCategory() == 0){
					HolidayFestivalPanel.mDayMatterList.add(dayMatter);
				}
			}
			tempMatterList = HolidayFestivalPanel.mDayMatterList;
		}else if(mMarkHolidayType == MarkHolidayType.Solarterm){
			HolidaySolartermPanel.mDayMatterList.clear();
			for(DayMatter dayMatter : allDayMatterList){
				if(dayMatter.getCategory() == 2){
					HolidaySolartermPanel.mDayMatterList.add(dayMatter);
				}
			}
			tempMatterList = HolidaySolartermPanel.mDayMatterList;
		}
		for(DayMatter dayMatter : tempMatterList){
			dayMatter.setNextRemindTime(dayMatter.getDate());
		}
		if(!isAllType){
			mDataList = DataManager.getInstance(this).sortMatters(tempMatterList, IContants.SORT_BY_DATE_AESC);
		}else{
			mDataList = HolidayAllPanel.mDayMatterList = DataManager.getInstance(this).sortMatters(tempMatterList, IContants.SORT_BY_DATE_AESC);
		}
	}
	
	public enum MarkHolidayType{
		All,
		Holiday,
		Festival,
		Solarterm
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mViewPager.removeAllViews();
		mViewPager = null;
		if(null != mListener){
			mListener.closed(mMarkHolidayType);
			mListener = null;
		}
	}
	
	public interface ICloseDetailListener{
		void closed(MarkHolidayType type);
	}
	public static ICloseDetailListener mListener = null;
	public static void setListener(ICloseDetailListener listener){
		mListener = listener;
	}
}
