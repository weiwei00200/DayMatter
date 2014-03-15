package com.pybeta.daymatter.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.pybeta.daymatter.R;
import com.pybeta.ui.widget.UcDate;
import com.pybeta.ui.widget.UcTitleBar;

public class DateDetailActivity extends Activity {

	/**
	 * ClassName: DateDetailActivity Function: TODO date: 2014-3-14 上午9:48:15
	 * 
	 * @author Devin.Yu
	 */

	private UcDate uc_date;
	private UcTitleBar titlebar;
	private String Yi = "";
	private String Ji = "";
	private String Chong = "";
	private String[] numberlist;
	private String[] chinesenumberlist;
	private ArrayList<Integer> jieqiposition = new ArrayList<Integer>();
	int todayposition;
	private String TodayNumber;
	private String today_nongli;
	private LinearLayout download_btn;
	public static final  String laohuangliUri="http://www.682.com/upload/2014/3/10/11/69c42de94a97c392b084d066774ad011.apk";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_detail);
		titlebar = (UcTitleBar) findViewById(R.id.titlebar);
		uc_date = (UcDate) findViewById(R.id.uc_date);
		download_btn=(LinearLayout)findViewById(R.id.download_btn);
		initgetData();
		initUcTitleBar();
		initinitUcDate();
		download_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				
				
			}
		});

	}

	public void initgetData() {

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		Yi = bundle.getString("Yi");
		Ji = bundle.getString("Ji");
		Chong = bundle.getString("Chong");
		numberlist = bundle.getStringArray("numberlist");
		chinesenumberlist = bundle.getStringArray("chinesenumberlist");
		jieqiposition = bundle.getIntegerArrayList("jieqiposition");
		todayposition = bundle.getInt("todayposition");
		today_nongli = bundle.getString("today_nongli");
		TodayNumber = numberlist[todayposition];

	}

	public void initUcTitleBar() {
		titlebar.setTitleText(getResources().getString(R.string.laohuangli));
		titlebar.setViewVisible(false, true, false, false, false, false, false,
				false);
		titlebar.setListener(new UcTitleBar.ITitleBarListener() {

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
			public void closeClick(Object obj) {
				// TODO Auto-generated method stub

			}

			@Override
			public void backClick(Object obj) {
				// TODO Auto-generated method stub
				finish();

			}

			@Override
			public void addClick(Object obj) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void initinitUcDate() {
		uc_date.setViewVisible(false, false);
		uc_date.setYiJIParam(true);
		uc_date.setYiJiChongText(Yi, Ji, Chong);
		uc_date.setDateText(numberlist, chinesenumberlist);
		uc_date.setChineseNumberTextColor(jieqiposition);
		uc_date.TodayTextColor(todayposition, R.color.date_item_downloadbg);
		uc_date.setTodayNumberText(TodayNumber,
				getResources().getString(R.string.laohuangli_nongli) + " "
						+ today_nongli);
	}

}
