// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.pybeta.daymatter.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.WorldTimeZone;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.utils.LogUtil;

// Referenced classes of package com.pybeta.daymatter.ui:
//            WorldTimeListAdapter, AddWorldTimeActivity, WorldTimeListLoader

public class WorldTimeFragment extends SherlockFragment implements
		LoaderCallbacks<List<WorldTimeZone>> {

	private static final String ACTION_DATE_CHANGED = "android.intent.action.DATE_CHANGED";
	private static final String ACTION_TIMEZONE_CHANGED = "android.intent.action.TIMEZONE_CHANGED";
	private static final String ACTION_TIME_CHANGED = "android.intent.action.TIME_SET";
	private static final String ACTION_TIME_TICK = "android.intent.action.TIME_TICK";
	public static final String ARG_SECTION_NUMBER = "section_number";
	public static final String INTENT_WORLDTIME_UPDATE = "daymatter.intent.action.worldtime_update";
	private WorldTimeListAdapter mAdapter;
	protected TextView mEmptyView;
	private Handler mHandler;
	protected ProgressBar mProgressBar;
	private int mSectionNumber;
	protected View mTimeZoneNoData;
	protected List<WorldTimeZone> mWorldTimeList;
	private MyComparator myComparator = new MyComparator();
	private MyReceiver myReceiver;
	protected ListView worldtime_listiview_citylist;

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
					|| action.equals(ACTION_TIME_TICK) || action.equals(INTENT_WORLDTIME_UPDATE))
				mAdapter.setItems(mWorldTimeList.toArray());
			return;
		}

	}

//	public WorldTimeFragment() {
//		mSectionNumber = 0;
//		mWorldTimeList = Collections.emptyList();
//	}

	private void delete(WorldTimeZone worldtimezone) {
		DatabaseManager.getInstance(getSherlockActivity())
				.updateWorldTimeSelected(worldtimezone, 0);
		mWorldTimeList.remove(worldtimezone);
		mAdapter.setItems(mWorldTimeList.toArray());
	}

	private void initHandler() {
		mHandler = new Handler() {

			public void handleMessage(Message message) {
				if (message.what == 1000) {
					Bundle bundle = new Bundle();
					bundle.putInt("section_number", mSectionNumber);
					getLoaderManager().restartLoader(0, bundle,
							WorldTimeFragment.this);
				} else {
					super.handleMessage(message);
				}
			}
		};
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
			getActivity().registerReceiver(myReceiver, intentfilter);
		}
	}

	private void modify(WorldTimeZone worldtimezone) {
		Intent intent = new Intent(getSherlockActivity(),
				AddWorldTimeActivity.class);
		intent.putExtra("modify", true);
		intent.putExtra("WorldTimeZone2Modify", worldtimezone);
		startActivity(intent);
	}

	private void refresh(Bundle bundle) {
		if (bundle == null) {
			bundle = new Bundle();
			bundle.putInt("section_number", mSectionNumber);
		} else {
			bundle.putInt("section_number", mSectionNumber);
		}
		getLoaderManager().restartLoader(0, bundle, this);
	}

	private void sortWorldTimeList() {
		Collections.sort(mWorldTimeList, myComparator);
	}

	private void top(WorldTimeZone worldtimezone) {
		worldtimezone.setTime2Modify(System.currentTimeMillis());
		DatabaseManager.getInstance(getSherlockActivity())
				.updateWorldTimeSelected(worldtimezone, 1);
		sortWorldTimeList();
		mAdapter.setItems(mWorldTimeList.toArray());
	}

	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);
		if (!mWorldTimeList.isEmpty())
			setListShown(true);
	}

	public Loader onCreateLoader(int i, Bundle bundle) {
		LogUtil.Sysout("WorldTimeFragment onCreateLoader");
		return new WorldTimeListLoader(getSherlockActivity(), bundle.getInt(
				"section_number", 0));
	}

	public View onCreateView(LayoutInflater layoutinflater,
			ViewGroup viewgroup, Bundle bundle) {
		getSherlockActivity().getSupportActionBar().setTitle(R.string.title_activity_worldtime);
		mWorldTimeList = new ArrayList<WorldTimeZone>();
		TimeZone timezone = TimeZone.getDefault();
		LogUtil.Sysout((new StringBuilder("mTimeZone: ")).append(timezone)
				.toString());
		initReceiver();
		initHandler();
		return layoutinflater.inflate(R.layout.fragment_worldtime, null);
	}

	public void onDestroyView() {
		mEmptyView = null;
		mProgressBar = null;
		mTimeZoneNoData = null;
		worldtime_listiview_citylist = null;
		if (myReceiver != null) {
			getActivity().unregisterReceiver(myReceiver);
			myReceiver = null;
		}
		super.onDestroyView();
	}


	public void onLoadFinished(Loader<List<WorldTimeZone>> loader, List<WorldTimeZone> list) {
		LogUtil.Sysout("WorldTimeFragment onLoadFinished");
		if (list == null) {
			mWorldTimeList = Collections.emptyList();
		} else {
			mWorldTimeList = list;
			sortWorldTimeList();
		}
		mAdapter.setItems(mWorldTimeList.toArray());
		setListShown(true);
	}

	public void onLoaderReset(Loader<List<WorldTimeZone>> loader) {
	}

	public void onResume() {
		super.onResume();
		if (mHandler != null)
			mHandler.sendEmptyMessageDelayed(1000, 700L);
	}

	public void onViewCreated(View view, Bundle bundle) {
		super.onViewCreated(view, bundle);
		mTimeZoneNoData = view.findViewById(R.id.worldtime_timezone_nodata);
		worldtime_listiview_citylist = (ListView) view.findViewById(R.id.worldtime_listiview_citylist);
		worldtime_listiview_citylist
				.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView adapterview,
							View view1, int i, long l) {
						showActionDialog((WorldTimeZone) mWorldTimeList.get(i),
								i);
						return false;
					}

				});
		mProgressBar = (ProgressBar) view.findViewById(R.id.worldtime_pb_loading);
		mAdapter = new WorldTimeListAdapter(getSherlockActivity());
		worldtime_listiview_citylist.setAdapter(mAdapter);
	}

	public void refresh() {
		refresh(null);
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

	protected void showActionDialog(final WorldTimeZone mWorldTimeZone, int i) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				getSherlockActivity());
		builder.setTitle(R.string.worldtime_list_action_title);
		builder.setItems(R.array.worldtime_list_action_array,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialoginterface, int j) {
						LogUtil.Sysout((new StringBuilder(
								"showActionDialog which: ")).append(j)
								.toString());
						if (j == 1)
							modify(mWorldTimeZone);
						else if (j == 2)
							delete(mWorldTimeZone);
						else
							top(mWorldTimeZone);
					}

				});
		builder.create().show();
	}

}
