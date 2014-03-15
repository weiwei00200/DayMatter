// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.pybeta.daymatter.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pybeta.daymatter.R;
import com.pybeta.daymatter.WorldTimeZone;
import com.pybeta.daymatter.db.DatabaseHelper;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.db.TimeZonesDb;
import com.pybeta.daymatter.utils.LogUtil;
import com.pybeta.daymatter.utils.ScreenUtil;

// Referenced classes of package com.pybeta.daymatter.ui:
//            BaseActivity

public class AddWorldTimeActivity extends BaseActivity implements
		android.view.View.OnClickListener {

	private static final String INTENT_LOCALECHANGED = "android.intent.action.LOCALE_CHANGED";
	static final String tag = "AddWorldTimeActivity";
	private EditText addworldtime_et_searchbar;
	private ListView addworldtime_listiview_citylist;
	private View addworldtime_ll;
	private ProgressBar addworldtime_pb_loading;
	Handler mHandler;
	private ArrayList mQueryWorldTimeZones;
	WorldTimeZone mWorldTimeZone2Modify;
	private ArrayList mWorldTimeZones;
	private WorldTimeZonesAdapter mWorldTimeZonesAdapter;
	boolean modify;
	MyReceiver myReceiver;
	private Object object;
	DatabaseHelper sDatabaseHelper;

	class MyReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {
			if (mWorldTimeZonesAdapter != null)
				mWorldTimeZonesAdapter.notifyDataSetChanged();
		}

	}

	private Object obj = new Object();

	class MyThread extends Thread {

		public void run() {

			synchronized (obj) {
				DatabaseHelper databasehelper = new DatabaseHelper(
						getApplicationContext());
				mWorldTimeZones = TimeZonesDb.getMultWorldTime(databasehelper
						.getReadableDatabase());
				mQueryWorldTimeZones = mWorldTimeZones;
				mHandler.sendEmptyMessage(2000);
			}
			return;
		}

	}

	class ViewHolder {

		View cityName;

	}

	class WorldTimeZonesAdapter extends BaseAdapter {

		public int getCount() {
			return mQueryWorldTimeZones.size();
		}

		public Object getItem(int i) {
			return null;
		}

		public long getItemId(int i) {
			return 0L;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			return doGetView(i, view);
		}
	}

	public AddWorldTimeActivity() {
		modify = false;
		object = new Object();
	}

	private View doGetView(int i, View view) {
		if (view == null) {
			view = new TextView(this);
			((TextView) view).setTextSize(2, 18F);
			int j = ScreenUtil.dip2px(getApplicationContext(), 8F);
			int k = ScreenUtil.dip2px(getApplicationContext(), 10F);
			int l = ScreenUtil.dip2px(getApplicationContext(), 10F);
			view.setPadding(j, k, 0, l);
		}
		Locale locale = getResources().getConfiguration().locale;
		locale.getLanguage();
		String s = locale.getCountry();
		locale.getDisplayCountry();
		String s1;
		String s2;
		if (s.equalsIgnoreCase("cn"))
			s1 = ((WorldTimeZone) mQueryWorldTimeZones.get(i)).getCityNameZH();
		else if (s.equalsIgnoreCase("tw"))
			s1 = ((WorldTimeZone) mQueryWorldTimeZones.get(i)).getCityNameTW();
		else
			s1 = ((WorldTimeZone) mQueryWorldTimeZones.get(i)).getCityName();
		s2 = (String) addworldtime_listiview_citylist.getTag();
		if (s2 != null) {
			SpannableStringBuilder spannablestringbuilder = new SpannableStringBuilder(
					s1);
			int i1 = s1.toLowerCase().indexOf(s2.toLowerCase());
			Log.v("AddWorldTimeActivity",
					(new StringBuilder("tmp: ")).append(s2).append(" , len: ")
							.append(s2.length()).append(" ,cityName: ")
							.append(s1).append(" ,start: ").append(i1)
							.toString());
			spannablestringbuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.addworldtime_hightlight)),
					i1, i1 + s2.length(), 33);
			((TextView) view).setText(spannablestringbuilder);
		} else {
			((TextView) view).setText(s1);
		}
		return view;
	}

	private void doItemClick(int i) {
		long time = System.currentTimeMillis();
		if (mWorldTimeZone2Modify != null){
//			DatabaseManager.getInstance(this).deleteWorldTimeSelected(
//					mWorldTimeZone2Modify);
			time = mWorldTimeZone2Modify.getTime2Modify();
			DatabaseManager.getInstance(this).updateWorldTimeSelected(mWorldTimeZone2Modify, 0);
		}
		((WorldTimeZone) mQueryWorldTimeZones.get(i)).setTime2Modify(time);
		DatabaseManager.getInstance(this).updateWorldTimeSelected(
				(WorldTimeZone) mQueryWorldTimeZones.get(i), 1);
		
		finish();
	}

	private void getData() {
		Log.v("AddWorldTimeActivity", "getData");
		showView(false);
		(new MyThread()).start();
	}

	private void initEditText() {
		addworldtime_et_searchbar.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable editable) {
				String s = editable.toString().trim();
				Log.v("AddWorldTimeActivity", (new StringBuilder(
						"afterTextChanged keyWord: ")).append(s).toString());
				queryCityByKeyWord(s);
			}

			public void beforeTextChanged(CharSequence charsequence, int i,
					int j, int k) {
			}

			public void onTextChanged(CharSequence charsequence, int i, int j,
					int k) {
			}

		});
	}

	private void initHandler() {
		mHandler = new Handler() {

			public void handleMessage(Message message) {
				if (message.what == 2000) {
					showView(true);
					if (mQueryWorldTimeZones != null
							&& mQueryWorldTimeZones.size() > 0) {
						LogUtil.Sysout("AddWorldTimeActivity onPostExecute 2");
						if (mWorldTimeZonesAdapter == null) {
							mWorldTimeZonesAdapter = new WorldTimeZonesAdapter();
							addworldtime_listiview_citylist
									.setAdapter(mWorldTimeZonesAdapter);
							LogUtil.Sysout("AddWorldTimeActivity onPostExecute 3");
						}
						mWorldTimeZonesAdapter.notifyDataSetChanged();
					}
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
					"android.intent.action.LOCALE_CHANGED");
			registerReceiver(myReceiver, intentfilter);
		}
	}

	private void initViews() {
		addworldtime_ll = findViewById(R.id.addworldtime_ll);
		addworldtime_et_searchbar = (EditText) findViewById(R.id.addworldtime_et_searchbar);
		addworldtime_listiview_citylist = (ListView) findViewById(R.id.addworldtime_listiview_citylist);
		addworldtime_listiview_citylist
				.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

					public void onItemClick(AdapterView adapterview, View view,
							int i, long l) {
						doItemClick(i);
					}

				});
		addworldtime_pb_loading = (ProgressBar) findViewById(R.id.addworldtime_pb_loading);
		initEditText();
	}

//	private void queryCityByKeyWord(String keyWord)
//    {
//        if(mWorldTimeZones == null || mWorldTimeZones.size() <= 0) {
//        	if (keyWord != null && !keyWord.equals("")) {
//				ArrayList<WorldTimeZone> mTmpWorldTimes = new ArrayList<WorldTimeZone>();
//				Locale mLocale = getResources().getConfiguration().locale;
//				String country = mLocale.getCountry();
//        Iterator<WorldTimeZone> mIterator = mWorldTimeZones.iterator();
//        if(mIterator.hasNext()) 
//        if(arraylist != null && arraylist.size() > 0)
//        {
//            mQueryWorldTimeZones = arraylist;
//            if(mWorldTimeZonesAdapter == null)
//            {
//                mWorldTimeZonesAdapter = new WorldTimeZonesAdapter();
//                addworldtime_listiview_citylist.setAdapter(mWorldTimeZonesAdapter);
//            }
//            addworldtime_listiview_citylist.setTag(s);
//            mWorldTimeZonesAdapter.notifyDataSetChanged();
//        }
//        WorldTimeZone worldtimezone = (WorldTimeZone)mIterator.next();
//        String cityName = null;
//        if(s1.equalsIgnoreCase("cn"))
//            s2 = worldtimezone.getCityNameZH();
//        else
//        if(s1.equalsIgnoreCase("tw"))
//            s2 = worldtimezone.getCityNameTW();
//        else
//            s2 = worldtimezone.getCityName();
//        if(s2 != null && s2.toLowerCase().contains(s.toLowerCase()))
//        	mTmpWorldTimes.add(worldtimezone);
//        addworldtime_listiview_citylist.setTag(null);
//        mQueryWorldTimeZones = mWorldTimeZones;
//        if(mWorldTimeZonesAdapter == null)
//        {
//            mWorldTimeZonesAdapter = new WorldTimeZonesAdapter();
//            addworldtime_listiview_citylist.setAdapter(mWorldTimeZonesAdapter);
//        }
//        mWorldTimeZonesAdapter.notifyDataSetChanged();
//        	}
//    }
        
        private void queryCityByKeyWord(String keyWord) {
    		if (mWorldTimeZones != null && mWorldTimeZones.size() > 0) {
    			if (keyWord != null && !keyWord.equals("")) {
    				ArrayList<WorldTimeZone> mTmpWorldTimes = new ArrayList<WorldTimeZone>();
    				Locale mLocale = getResources().getConfiguration().locale;
    				String country = mLocale.getCountry();
    				Iterator<WorldTimeZone> mIterator = mWorldTimeZones.iterator();
    				while (mIterator.hasNext()) {
    					WorldTimeZone mWorldTime = mIterator.next();
    					String cityName = null;
    					if (country.equalsIgnoreCase("cn"))
    						cityName = mWorldTime.getCityNameZH();
    					else if (country.equalsIgnoreCase("tw"))
    						cityName = mWorldTime.getCityNameTW();
    					else
    						cityName = mWorldTime.getCityName();
    					if (cityName == null)
    						continue;
    					if (cityName.toLowerCase().contains(keyWord.toLowerCase())) {
    						mTmpWorldTimes.add(mWorldTime);
    					}
    				}

    				if (mTmpWorldTimes != null && mTmpWorldTimes.size() > 0) {
    					mQueryWorldTimeZones = mTmpWorldTimes;
    					if (mWorldTimeZonesAdapter == null) {
    						mWorldTimeZonesAdapter = new WorldTimeZonesAdapter();
    						addworldtime_listiview_citylist.setAdapter(mWorldTimeZonesAdapter);
    					}
    					addworldtime_listiview_citylist.setTag(keyWord);
    					mWorldTimeZonesAdapter.notifyDataSetChanged();
    				}
    			} else {
    				addworldtime_listiview_citylist.setTag(null);
    				mQueryWorldTimeZones = mWorldTimeZones;
    				if (mWorldTimeZonesAdapter == null) {
    					mWorldTimeZonesAdapter = new WorldTimeZonesAdapter();
    					addworldtime_listiview_citylist.setAdapter(mWorldTimeZonesAdapter);
    				}
    				mWorldTimeZonesAdapter.notifyDataSetChanged();
    			}
    		}
    	}


	private void showView(boolean flag) {
		if (flag) {
			addworldtime_ll.setVisibility(0);
			addworldtime_pb_loading.setVisibility(8);
		} else {
			addworldtime_ll.setVisibility(8);
			addworldtime_pb_loading.setVisibility(0);
		}
	}

	public void onClick(View view) {
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_add_worldtime);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		TimeZone timezone = TimeZone.getDefault();
		System.out.println((new StringBuilder("mTimeZone: ")).append(timezone)
				.toString());
		modify = getIntent().getBooleanExtra("modify", false);
		if (modify)
			mWorldTimeZone2Modify = (WorldTimeZone) getIntent()
					.getSerializableExtra("WorldTimeZone2Modify");
		else
			mWorldTimeZone2Modify = null;
		initViews();
		initHandler();
		initReceiver();
		sDatabaseHelper = new DatabaseHelper(this);
		getData();
	}

	protected void onDestroy() {
		super.onDestroy();
		setResult(0);
		if (myReceiver != null) {
			unregisterReceiver(myReceiver);
			myReceiver = null;
		}
		mHandler = null;
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			finish();
			break;
		}
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void onPause() {
		super.onPause();
		LogUtil.Sysout("onPause");
	}

	protected void onResume() {
		super.onResume();
		LogUtil.Sysout("onResume");
	}

}
