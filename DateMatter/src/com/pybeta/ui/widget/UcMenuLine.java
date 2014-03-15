package com.pybeta.ui.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pybeta.daymatter.CategoryItem;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.core.MatterApplication;
import com.pybeta.daymatter.ui.AboutRecActivity;
import com.pybeta.daymatter.ui.CategoryActivity;
import com.pybeta.daymatter.ui.CountdownRecActivity;
import com.pybeta.daymatter.ui.HistoryTodayRecActivity;
import com.pybeta.daymatter.ui.HolidayRecActivity;
import com.pybeta.daymatter.ui.SettingRecActivity;
import com.pybeta.daymatter.ui.SettingsActivity;
import com.pybeta.daymatter.ui.WorldTimeRecActivity;
import com.pybeta.ui.widget.UcMenuItem.IItemClickListener;
import com.sammie.common.view.Messager;

public class UcMenuLine extends LinearLayout implements OnClickListener{
	private Context mContext = null;
	private LinearLayout mLayoutContainerCategory = null;
	private LinearLayout mLayoutTool = null;
	private LinearLayout mLayoutMore = null;
	private MatterApplication mAppInfo = null;
	private int mItemSize = 0;
	private ICategoryClickListener mCategoryClickListener = null;
	private ISyncClickListener mSyncClickListener = null;
	public UcMenuLine(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	public UcMenuLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	private void initView(Context context){
		mContext = context;
		mAppInfo = (MatterApplication)(((Activity)context).getApplication());
		mItemSize = mAppInfo.getScreenWidth()/5;
		
		ViewGroup vg = (ViewGroup)LayoutInflater.from(context).inflate(R.layout.uc_menu_line, null);
		vg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		this.addView(vg);
		
		mLayoutContainerCategory = (LinearLayout) vg.findViewById(R.id.layout_category_container_menu);
		mLayoutTool = (LinearLayout) vg.findViewById(R.id.layout_tool_menu);
		mLayoutMore = (LinearLayout) vg.findViewById(R.id.layout_more_menu);
		
		initTool();
		initMore();
	}
	
	
	public View getLine(){
		View line = new View(mContext);
		line.setLayoutParams(new LayoutParams(2, LayoutParams.MATCH_PARENT));
		line.setBackgroundColor(getResources().getColor(R.color.menu_line));
		return line;
	}
	public void setCategory(List<CategoryItem> categoryList){
		if(null != mLayoutContainerCategory){
			mLayoutContainerCategory.removeAllViewsInLayout();
		}
		int itemCount = categoryList.size()+1;
		int lineCount = itemCount <=5 ? 1 : (itemCount/5+(itemCount%5 == 0 ? 0 : 1));
		List<LinearLayout> lineLayoutList = new ArrayList<LinearLayout>();
		for(int i=0; i<lineCount; i++){
			LinearLayout lineLayout = new LinearLayout(mContext);
			lineLayoutList.add(lineLayout);
		}
		for(int i=0; i<categoryList.size()+1;i++){
			UcMenuItem itemCategory = new UcMenuItem(mContext);
			if(i != categoryList.size()){//不是最后一个
				final CategoryItem item = categoryList.get(i);
				if(item.getId()==0){
					itemCategory.setImage(R.drawable.menu_item_all);
				}else if(item.getId()==1){
					itemCategory.setImage(R.drawable.menu_item_life);
				}else if(item.getId()==2){
					itemCategory.setImage(R.drawable.menu_item_work);
				}else if(item.getId()==3){
					itemCategory.setImage(R.drawable.menu_item_memorilday);
				}else{
					itemCategory.setImage(R.drawable.menu_item_other);
				}
				itemCategory.setTextView(item.getName());
				itemCategory.setLayoutParams(new LayoutParams(mItemSize-1, LayoutParams.WRAP_CONTENT));
				itemCategory.setTag(item.getId());
				itemCategory.setListener(new IItemClickListener() {
					@Override
					public void itemClick() {
						// TODO Auto-generated method stub
						mCategoryClickListener.itemClick(item.getId());
					}
				});
			}else{//最后一个
				itemCategory.setImage(R.drawable.menu_item_add);
				itemCategory.setTextView(getResources().getString(R.string.menu_new_daysmatter));
				itemCategory.setLayoutParams(new LayoutParams(mItemSize-1, LayoutParams.WRAP_CONTENT));
				itemCategory.setTag("Add");
				itemCategory.setListener(new IItemClickListener() {
					@Override
					public void itemClick() {
						// TODO Auto-generated method stub
						Intent intent = new Intent(mContext, CategoryActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						mContext.startActivity(intent);
					}
				});
			}
			int position = i+1;
			int currentLine = position <=5 ? 0 : (position/5-1+(position%5 == 0 ? 0 : 1));
			
			lineLayoutList.get(currentLine).addView(itemCategory);
			lineLayoutList.get(currentLine).addView(getLine());
		}
		for(LinearLayout lineLayout : lineLayoutList){
			View line = new View(mContext);
			line.setBackgroundColor(getResources().getColor(R.color.menu_line));
			line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
			mLayoutContainerCategory.addView(lineLayout);
			mLayoutContainerCategory.addView(line);
		}
	}
	
	private void initTool(){
		UcMenuItem itemSync = new UcMenuItem(mContext);
		itemSync.setImage(R.drawable.menu_item_sync);
		itemSync.setTextView(getResources().getString(R.string.sync));
		itemSync.setLayoutParams(new LayoutParams(mItemSize-1, LayoutParams.WRAP_CONTENT));
		itemSync.setListener(new IItemClickListener() {
			@Override
			public void itemClick() {
				// 同步
				mSyncClickListener.syncClick();
			}
		});
		
		UcMenuItem itemHoliday = new UcMenuItem(mContext);
		itemHoliday.setImage(R.drawable.menu_item_holiday);
		itemHoliday.setTextView(getResources().getString(R.string.festival_history));
		itemHoliday.setLayoutParams(new LayoutParams(mItemSize-1, LayoutParams.WRAP_CONTENT));
		itemHoliday.setListener(new IItemClickListener() {
			@Override
			public void itemClick() {
				// 节日假日
				Intent intent = new Intent(mContext, HolidayRecActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				mContext.startActivity(intent);
			}
		});
		
		UcMenuItem itemCalc = new UcMenuItem(mContext);
		itemCalc.setImage(R.drawable.menu_item_cal);
		itemCalc.setTextView(getResources().getString(R.string.date_cal));
		itemCalc.setLayoutParams(new LayoutParams(mItemSize-1, LayoutParams.WRAP_CONTENT));
		itemCalc.setListener(new IItemClickListener() {
			@Override
			public void itemClick() {
				// 时间计算器
				Intent intent = new Intent(mContext, CountdownRecActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				mContext.startActivity(intent);
			}
		});
		
		UcMenuItem itemWorldTime = new UcMenuItem(mContext);
		itemWorldTime.setImage(R.drawable.menu_item_worldtime);
		itemWorldTime.setTextView(getResources().getString(R.string.world_time));
		itemWorldTime.setLayoutParams(new LayoutParams(mItemSize-1, LayoutParams.WRAP_CONTENT));
		itemWorldTime.setListener(new IItemClickListener() {
			@Override
			public void itemClick() {
				// 世界时间
				Intent intent = new Intent(mContext, WorldTimeRecActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				mContext.startActivity(intent);
			}
		});
		
		UcMenuItem itemHistory = new UcMenuItem(mContext);
		itemHistory.setImage(R.drawable.menu_item_history);
		itemHistory.setTextView(getResources().getString(R.string.today_history));
		itemHistory.setLayoutParams(new LayoutParams(mItemSize-1, LayoutParams.WRAP_CONTENT));
		itemHistory.setListener(new IItemClickListener() {
			@Override
			public void itemClick() {
				// 历史今天
				Intent intent = new Intent(mContext, HistoryTodayRecActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				mContext.startActivity(intent);
			}
		});
		
		mLayoutTool.addView(itemSync);
		mLayoutTool.addView(getLine());
		mLayoutTool.addView(itemHoliday);
		mLayoutTool.addView(getLine());
		mLayoutTool.addView(itemCalc);
		mLayoutTool.addView(getLine());
		mLayoutTool.addView(itemWorldTime);
		mLayoutTool.addView(getLine());
		mLayoutTool.addView(itemHistory);
		mLayoutTool.addView(getLine());
	}
	
	private void initMore(){
		UcMenuItem itemMore = new UcMenuItem(mContext);
		itemMore.setImage(R.drawable.menu_item_more);
		itemMore.setTextView(getResources().getString(R.string.more_function));
		itemMore.setLayoutParams(new LayoutParams(mItemSize-1, LayoutParams.WRAP_CONTENT));
		itemMore.setListener(new IItemClickListener() {
			@Override
			public void itemClick() {
				// 更多功能
				Messager.showToast(mContext, "更多功能", 1000, Gravity.CENTER);
			}
		});
		
		UcMenuItem itemSetting = new UcMenuItem(mContext);
		itemSetting.setImage(R.drawable.menu_item_setting);
		itemSetting.setTextView(getResources().getString(R.string.menu_settings));
		itemSetting.setLayoutParams(new LayoutParams(mItemSize-1, LayoutParams.WRAP_CONTENT));
		itemSetting.setListener(new IItemClickListener() {
			@Override
			public void itemClick() {
				// 设置
				Intent intent = new Intent(mContext,SettingRecActivity.class);//SettingRecActivity SettingsActivity
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				mContext.startActivity(intent);
			}
		});
		
		UcMenuItem itemAbout = new UcMenuItem(mContext);
		itemAbout.setImage(R.drawable.menu_item_about);
		itemAbout.setTextView(getResources().getString(R.string.menu_about));
		itemAbout.setLayoutParams(new LayoutParams(mItemSize-1, LayoutParams.WRAP_CONTENT));
		itemAbout.setListener(new IItemClickListener() {
			@Override
			public void itemClick() {
				// 关于
				Intent intent = new Intent(mContext,AboutRecActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				mContext.startActivity(intent);
			}
		});
		
		mLayoutMore.addView(itemMore);
		mLayoutMore.addView(getLine());
		mLayoutMore.addView(itemSetting);
		mLayoutMore.addView(getLine());
		mLayoutMore.addView(itemAbout);
		mLayoutMore.addView(getLine());
		
	}
	public void setCategoryClickListener(ICategoryClickListener listener){
		mCategoryClickListener = listener;
	}
	public interface ICategoryClickListener{
		void itemClick(int id);
	}
	public void setSyncListener(ISyncClickListener listener){
		this.mSyncClickListener = listener;
	}
	public interface ISyncClickListener{
		void syncClick();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}
