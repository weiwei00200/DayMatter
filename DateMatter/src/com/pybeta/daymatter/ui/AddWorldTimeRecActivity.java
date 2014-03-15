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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pybeta.daymatter.R;
import com.pybeta.daymatter.WorldTimeZone;
import com.pybeta.daymatter.core.MatterApplication;
import com.pybeta.daymatter.db.DatabaseHelper;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.db.TimeZonesDb;
import com.pybeta.daymatter.tool.AnimationTool;
import com.pybeta.daymatter.tool.AnimationTool.AnimationType;
import com.pybeta.daymatter.tool.AnimationTool.AnimationTypeWorldTime;
import com.pybeta.daymatter.tool.AnimationTool.IAnimationListener;
import com.pybeta.daymatter.tool.KeyboardTool;
import com.pybeta.daymatter.utils.LogUtil;
import com.pybeta.daymatter.utils.ScreenUtil;
import com.pybeta.ui.widget.UcTitleBar;

// Referenced classes of package com.pybeta.daymatter.ui:
//            BaseActivity

public class AddWorldTimeRecActivity extends BaseActivity implements
		android.view.View.OnClickListener {

	private MatterApplication mAppInfo = null;
	private static final String INTENT_LOCALECHANGED = "android.intent.action.LOCALE_CHANGED";
	public static final String action = "com.pybeta.daymatter.worldtime.update";
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
	private UcTitleBar titlebar;
	private LinearLayout seach_before_view, seach_after_view;
	private RelativeLayout add_worldtime_content;
	private boolean isShowKeyBoard;

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

	public AddWorldTimeRecActivity() {
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
			spannablestringbuilder.setSpan(new ForegroundColorSpan(
					getResources().getColor(R.color.addworldtime_hightlight)),
					i1, i1 + s2.length(), 33);
			((TextView) view).setText(spannablestringbuilder);
		} else {
			((TextView) view).setText(s1);
		}
		return view;
	}

	private void doItemClick(int i) {
		long time = System.currentTimeMillis();
		if (mWorldTimeZone2Modify != null) {
			// DatabaseManager.getInstance(this).deleteWorldTimeSelected(
			// mWorldTimeZone2Modify);
			time = mWorldTimeZone2Modify.getTime2Modify();
			DatabaseManager.getInstance(this).updateWorldTimeSelected(
					mWorldTimeZone2Modify, 0);
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
				findViewById(R.id.add_city_Transparent)
						.setVisibility(View.GONE);
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

	private void initBar() {
		titlebar = (UcTitleBar) findViewById(R.id.uc_titlebar);
		titlebar.setTitleText(getResources().getString(
				R.string.worldtime_add_city));
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
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}

			@Override
			public void addClick(Object obj) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void initViews() {
		addworldtime_et_searchbar = (EditText) findViewById(R.id.addworldtime_et_searchbar);
		addworldtime_listiview_citylist = (ListView) findViewById(R.id.addworldtime_listiview_citylist);
		mAppInfo = (MatterApplication) this.getApplication();
		addworldtime_ll = findViewById(R.id.addworldtime_ll);
		findViewById(R.id.addworldtime_cannel_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						titlebar.setVisibility(View.VISIBLE);
						findViewById(R.id.add_city_Transparent).setVisibility(
								View.GONE);
						seach_before_view.setVisibility(View.VISIBLE);
						seach_after_view.setVisibility(View.GONE);
						add_worldtime_content.startAnimation(AnimationTool
								.getAnimationInWorldTime(add_worldtime_content,
										0, 0, titlebar.getHeight(), 0,
										AnimationType.Vertical,
										mAppInfo.screenHeight,
										AnimationTypeWorldTime.CLOSE));
						isShowKeyBoard = false;

					}
				});

		addworldtime_listiview_citylist
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int i, long arg3) {
						// TODO Auto-generated method stub
						doItemClick(i);
						Intent intent = new Intent(
								AddWorldTimeRecActivity.this,
								WorldTimeRecActivity.class);
						setResult(RESULT_OK, intent);
						overridePendingTransition(R.anim.push_right_out,
								R.anim.push_right_in);

					}
				});
		addworldtime_pb_loading = (ProgressBar) findViewById(R.id.addworldtime_pb_loading);
		add_worldtime_content = (RelativeLayout) findViewById(R.id.add_worldtime_content);
		seach_before_view = (LinearLayout) findViewById(R.id.seach_before_view);
		seach_after_view = (LinearLayout) findViewById(R.id.seach_after_view);
		seach_before_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				titlebar.setVisibility(View.GONE);
				findViewById(R.id.add_city_Transparent).setVisibility(
						View.VISIBLE);
				seach_before_view.setVisibility(View.GONE);
				seach_after_view.setVisibility(View.VISIBLE);

				add_worldtime_content.startAnimation(AnimationTool
						.getAnimationInWorldTime(add_worldtime_content, 0, 0,
								titlebar.getHeight(), 0,
								AnimationType.Vertical, mAppInfo.screenHeight,
								AnimationTypeWorldTime.OPEN));
				isShowKeyBoard = true;


			}
		});
		initEditText();

		IAnimationListener listener = new IAnimationListener() {

			@Override
			public void startAnim() {

			}

			@Override
			public void endAnim() {
              KeyboardTool.isShowKeyBoard(isShowKeyBoard, addworldtime_et_searchbar , AddWorldTimeRecActivity.this);

			}

			@Override
			public void repeatAnim() {
				// TODO Auto-generated method stub

			}

		};
		AnimationTool.setAnimListener(listener);

	}

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
						addworldtime_listiview_citylist
								.setAdapter(mWorldTimeZonesAdapter);
					}
					addworldtime_listiview_citylist.setTag(keyWord);
					mWorldTimeZonesAdapter.notifyDataSetChanged();
				}
			} else {
				addworldtime_listiview_citylist.setTag(null);
				mQueryWorldTimeZones = mWorldTimeZones;
				if (mWorldTimeZonesAdapter == null) {
					mWorldTimeZonesAdapter = new WorldTimeZonesAdapter();
					addworldtime_listiview_citylist
							.setAdapter(mWorldTimeZonesAdapter);
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
		setContentView(R.layout.activity_add_worldtime_rec);
		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		TimeZone timezone = TimeZone.getDefault();
		System.out.println((new StringBuilder("mTimeZone: ")).append(timezone)
				.toString());
		modify = getIntent().getBooleanExtra("modify", false);
		if (modify)
			mWorldTimeZone2Modify = (WorldTimeZone) getIntent()
					.getSerializableExtra("WorldTimeZone2Modify");
		else
			mWorldTimeZone2Modify = null;
		initBar();
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
