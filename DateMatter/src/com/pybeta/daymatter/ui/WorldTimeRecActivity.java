package com.pybeta.daymatter.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pybeta.daymatter.R;
import com.pybeta.daymatter.WorldTimeZone;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.utils.LogUtil;
import com.pybeta.ui.widget.CustomDialog;
import com.pybeta.ui.widget.UcTitleBar;
import com.sammie.common.thread.CustomRunnable;
import com.sammie.common.thread.IDataAction;

public class WorldTimeRecActivity extends Activity {

	private UcTitleBar titlebar;
	private static final String ACTION_DATE_CHANGED = "android.intent.action.DATE_CHANGED";
	private static final String ACTION_TIMEZONE_CHANGED = "android.intent.action.TIMEZONE_CHANGED";
	private static final String ACTION_TIME_CHANGED = "android.intent.action.TIME_SET";
	private static final String ACTION_TIME_TICK = "android.intent.action.TIME_TICK";
	public static final String ARG_SECTION_NUMBER = "section_number";
	public static final String INTENT_WORLDTIME_UPDATE = "daymatter.intent.action.worldtime_update";
	private WorldTimeListAdapter mAdapter;
	protected TextView mEmptyView;
	protected ProgressBar mProgressBar;
	private int mSectionNumber;
	protected View mTimeZoneNoData;
	protected List<WorldTimeZone> mWorldTimeList;
	private MyComparator myComparator = new MyComparator();
	private MyReceiver myReceiver;
	protected ListView worldtime_listiview_citylist;
	public static final int REQCODE = 3000;

	private class MyComparator implements Comparator<WorldTimeZone> {

		public int compare(WorldTimeZone worldtimezone,
				WorldTimeZone worldtimezone1) {
			long l = worldtimezone.getTime2Modify()
					- worldtimezone1.getTime2Modify();
			int ret;
			if (l > 0L)
				ret = -1;
			else if (l == 0L)
				ret = 0;
			else
				ret = 1;
			return ret;
		}

	}

	class MyReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {
			String action;
			action = intent.getAction();
			if (action == null)
				return;
			LogUtil.Sysout("action: " + action);
			if (action.equals(ACTION_DATE_CHANGED)
					|| action.equals(ACTION_TIMEZONE_CHANGED)
					|| action.equals(ACTION_TIME_CHANGED)
					|| action.equals(ACTION_TIME_TICK)
					|| action.equals(INTENT_WORLDTIME_UPDATE))
				mAdapter.setItems(mWorldTimeList.toArray());
			return;
		}

	}

	private void initReceiver() {
		if (myReceiver == null) {
			myReceiver = new MyReceiver();
			IntentFilter intentfilter = new IntentFilter(
					"daymatter.intent.action.worldtime_update");
			intentfilter.addAction("android.intent.action.TIMEZONE_CHANGED");
			intentfilter.addAction("android.intent.action.DATE_CHANGED");
			intentfilter.addAction("android.intent.action.TIME_SET");
			intentfilter.addAction("android.intent.action.TIME_TICK");
			WorldTimeRecActivity.this
					.registerReceiver(myReceiver, intentfilter);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.worldtime_rec);

		mWorldTimeList = new ArrayList<WorldTimeZone>();

		titlebar = (UcTitleBar) findViewById(R.id.uc_titlebar);
		mTimeZoneNoData = findViewById(R.id.worldtime_timezone_nodata);
		worldtime_listiview_citylist = (ListView) findViewById(R.id.worldtime_listiview_citylist);
		mProgressBar = (ProgressBar) findViewById(R.id.worldtime_pb_loading);
		initTitlebar();
		initView();
		initReceiver();
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	private void initTitlebar() {

		titlebar.setTitleText(getResources().getString(R.string.world_time));
		titlebar.setViewVisible(false, true, false, true, false, false, false,
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
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);

			}

			@Override
			public void addClick(Object obj) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WorldTimeRecActivity.this,
						AddWorldTimeRecActivity.class);
				startActivityForResult(intent, REQCODE);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

			}
		});

	}

	private void initView() {

		IDataAction runAction = new IDataAction() {

			@Override
			public Object actionExecute(Object obj) {

				return DatabaseManager.getInstance(WorldTimeRecActivity.this)
						.queryAllWorldTime(0);
			}
		};
		IDataAction completeAction = new IDataAction() {

			@Override
			public Object actionExecute(Object obj) {
				if (obj != null) {
					mWorldTimeList = (List<WorldTimeZone>) obj;
					sortWorldTimeList();
					mAdapter = new WorldTimeListAdapter(
							WorldTimeRecActivity.this);
					mAdapter.setItems(mWorldTimeList.toArray());
					setListShown(true);
					worldtime_listiview_citylist.setAdapter(mAdapter);
					worldtime_listiview_citylist
							.setOnItemLongClickListener(new OnItemLongClickListener() {

								@Override
								public boolean onItemLongClick(
										AdapterView<?> arg0, View arg1,
										final int i, long arg3) {
									// showActionDialog((WorldTimeZone)
									// mWorldTimeList.get(i),i);
									CustomDialog dialog = new CustomDialog(
											WorldTimeRecActivity.this,
											R.layout.worldtime_dialog,
											R.style.SyncDialog);
									
									dialog.show();
									showActionDialog(dialog,i);
									
									
									
									return false;
								}
							});
				} else {
					mWorldTimeList = Collections.emptyList();
				}

				return null;
			}
		};
		CustomRunnable run = new CustomRunnable(runAction, completeAction);
		run.startAction();

	}

	private void sortWorldTimeList() {
		Collections.sort(mWorldTimeList, myComparator);
	}

	public void setListShown(boolean flag) {
		if (flag) {
			mProgressBar.setVisibility(View.GONE);
			worldtime_listiview_citylist.setVisibility(View.VISIBLE);
			if (!mWorldTimeList.isEmpty())
				mTimeZoneNoData.setVisibility(View.GONE);
			else
				mTimeZoneNoData.setVisibility(View.VISIBLE);
		} else {
			mProgressBar.setVisibility(View.VISIBLE);
			worldtime_listiview_citylist.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		mEmptyView = null;
		mProgressBar = null;
		mTimeZoneNoData = null;
		worldtime_listiview_citylist = null;
		if (myReceiver != null) {
			WorldTimeRecActivity.this.unregisterReceiver(myReceiver);
			myReceiver = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQCODE) {
			{
				initView();
				Log.i("REQCODE", "REQCODE");
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showActionDialog(final Dialog dialog,final int i) {
		TextView worldtime_top_btn = (TextView) dialog
				.findViewById(R.id.worldtime_top_btn);
		worldtime_top_btn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						top((WorldTimeZone) mWorldTimeList
								.get(i));
						dialog.dismiss();
					}
				});
		TextView worldtime_modfiy_btn = (TextView) dialog
				.findViewById(R.id.worldtime_modfiy_btn);
		worldtime_modfiy_btn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						modify((WorldTimeZone) mWorldTimeList
								.get(i));
						dialog.dismiss();

					}
				});
		TextView worldtime_delete_btn = (TextView) dialog
				.findViewById(R.id.worldtime_delete_btn);
		worldtime_delete_btn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						delete((WorldTimeZone) mWorldTimeList
								.get(i));
						dialog.dismiss();
					}
				});

	}

	private void modify(WorldTimeZone worldtimezone) {
		Intent intent = new Intent(WorldTimeRecActivity.this,
				AddWorldTimeActivity.class);
		intent.putExtra("modify", true);
		intent.putExtra("WorldTimeZone2Modify", worldtimezone);
		startActivityForResult(intent, REQCODE);
	}

	private void top(WorldTimeZone worldtimezone) {
		worldtimezone.setTime2Modify(System.currentTimeMillis());
		DatabaseManager.getInstance(WorldTimeRecActivity.this)
				.updateWorldTimeSelected(worldtimezone, 1);
		sortWorldTimeList();
		mAdapter.setItems(mWorldTimeList.toArray());
	}

	private void delete(WorldTimeZone worldtimezone) {
		DatabaseManager.getInstance(WorldTimeRecActivity.this)
				.updateWorldTimeSelected(worldtimezone, 0);
		mWorldTimeList.remove(worldtimezone);
		mAdapter.setItems(mWorldTimeList.toArray());
	}

}
