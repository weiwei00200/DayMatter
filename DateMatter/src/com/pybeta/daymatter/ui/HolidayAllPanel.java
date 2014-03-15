package com.pybeta.daymatter.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pybeta.daymatter.Config;
import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.adapter.HolidayMatterAdapter;
import com.pybeta.daymatter.core.DataManager;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.ui.HolidayDetailActivity.ICloseDetailListener;
import com.pybeta.daymatter.ui.HolidayDetailActivity.MarkHolidayType;
import com.pybeta.daymatter.utils.DealRepeatUtil;
import com.pybeta.ui.widget.LoadingDialog;
import com.pybeta.util.DMToast;

public class HolidayAllPanel extends LinearLayout{
	
	private ListView mListView;
	private HolidayMatterAdapter mAdapter = null;
	public static Context mContext = null;
	public static List<DayMatter> mDayMatterList = null;
	private ProgressBar mProgressBar = null;
	public HolidayAllPanel(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	public HolidayAllPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context){
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		ViewGroup vg = (ViewGroup)inflater.inflate(R.layout.layout_holiday_all, null);
		mListView = (ListView)vg.findViewById(R.id.holiday_all_list);
		mProgressBar = (ProgressBar)vg.findViewById(R.id.all_loading);
		vg.setLayoutParams(new LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)));
		this.addView(vg);
	}
	
	private void initListener(){
		ICloseDetailListener listener = new ICloseDetailListener() {
			@Override
			public void closed(MarkHolidayType type) {
				// TODO Auto-generated method stub
				if(type == MarkHolidayType.All){
					loadListViewData(mContext);
				}
			}
		};
		HolidayDetailActivity.setListener(listener);
	}
	
	public void loadListViewData(final Context context){
		LoadingDialog.showLoadingDialog(mContext, "加载中...", false);
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(null != mDayMatterList){
					mAdapter = new HolidayMatterAdapter(context);
					mAdapter.setItems(mDayMatterList.toArray());
					mListView.setAdapter(mAdapter);
					mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
						@Override
						public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
							DayMatter matter = mAdapter.getItem(position);
							addToLocal(matter);
							return false;
						}
					});
					mListView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View view,int position, long arg3) {
							// TODO Auto-generated method stub
							initListener();
							Intent intent = new Intent(mContext,HolidayDetailActivity.class);
							Bundle bundle = new Bundle();
							bundle.putInt("position", position);
							bundle.putString("type",Config.ALL_TYPE);
							intent.putExtras(bundle);
							mContext.startActivity(intent);
							((Activity)mContext).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
						}
					});
				}else{
					DMToast.makeText(context,R.string.matter_loaddata_faild , Toast.LENGTH_LONG).show();
				}
				LoadingDialog.closeLoadingDialog();
			}
		};
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArrayList<DayMatter> matterList = DataManager.getInstance(context).getHolidayList();
				for(DayMatter dayMatter : matterList){
					dayMatter.setDate(DealRepeatUtil.dealRepeatDateTime(dayMatter, false));
					dayMatter.setNextRemindTime(dayMatter.getDate());
				} 
				mDayMatterList = DataManager.getInstance(context).sortMatters(matterList, IContants.SORT_BY_DATE_AESC);
				
				Message msg = new Message();
				handler.sendMessage(msg);
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	public void addToLocal(final DayMatter matter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.holiday_add_local);
        builder.setPositiveButton(R.string.menu_new_daysmatter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	DatabaseManager databaseMgr = DatabaseManager.getInstance(mContext);
            	databaseMgr.add(matter);
            	DMToast.makeText(mContext, R.string.matter_save, Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.matter_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create().show();
    }
}
