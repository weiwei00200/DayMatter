package com.pybeta.daymatter.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.utils.DateUtils;
import com.pybeta.ui.widget.NumberPickerDialog;
import com.pybeta.ui.widget.NumberPickerDialog.OnNumberSetListener;
import com.pybeta.widget.NumberPicker;
import com.umeng.fb.UMFeedbackService;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class CountdownActivity extends BaseActivity implements TabListener {

	private ViewPager mViewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	
	private String mDatePattern;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_count_down);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.title_activity_count_down);
        
		mDatePattern = getString(R.string.matter_calendar_gregorian_date_format);
		
		initControlViews();
	}
	
	private void initControlViews() {
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private String[] mTabTitles;

		public SectionsPagerAdapter(Context ctx, FragmentManager fm) {
			super(fm);
			mTabTitles = ctx.getResources().getStringArray(R.array.tab_count_down_title_array);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment;
			if (position == 0) {
				fragment = new DateCountFragment();
			} else {
				fragment = new DaysCountFragment();
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return mTabTitles == null ? 0 : mTabTitles.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTabTitles[position];
		}
	}
	
	public class DateCountFragment extends SherlockFragment implements OnClickListener {
		
		private Calendar mStartCalendar;
		private Calendar mEndCalendar;

		private Button mStartBtn;
		private Button mEndBtn;
		private TextView mResultText;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mStartCalendar = Calendar.getInstance();
			mEndCalendar = Calendar.getInstance();
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			return inflater.inflate(R.layout.count_date_fragement_layout, null);
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			mStartBtn = (Button) findViewById(R.id.count_date_start_btn);
			mEndBtn = (Button) findViewById(R.id.count_date_end_btn);
			mResultText = (TextView) findViewById(R.id.count_date_result_num);

			mStartBtn.setOnClickListener(this);
			mEndBtn.setOnClickListener(this);
			
			count();
		}
		
		protected void count() {
			SimpleDateFormat dateFormat = new SimpleDateFormat(mDatePattern);
			mStartBtn.setText(dateFormat.format(mStartCalendar.getTime()));
			mEndBtn.setText(dateFormat.format(mEndCalendar.getTime()));
			
			long result = DateUtils.getDaysBetween(mEndCalendar, mStartCalendar);
			mResultText.setText(String.valueOf(result));
			
			if (Math.abs(result) > 1) {
				mResultText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.days, 0);
			} else {
				mResultText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.day, 0);
			}
		}
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.count_date_start_btn: {
				new DatePickerDialog(getSherlockActivity(), new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						mStartCalendar.set(year, monthOfYear, dayOfMonth);
						count();
					}
				}, mStartCalendar.get(Calendar.YEAR), mStartCalendar.get(Calendar.MONTH),
						mStartCalendar.get(Calendar.DAY_OF_MONTH)).show();
				break;
			}
			case R.id.count_date_end_btn: {
				new DatePickerDialog(getSherlockActivity(), new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						mEndCalendar.set(year, monthOfYear, dayOfMonth);
						count();
					}
				}, mEndCalendar.get(Calendar.YEAR), mEndCalendar.get(Calendar.MONTH), 
						mEndCalendar.get(Calendar.DAY_OF_MONTH)).show();
				break;
			}
			default:
				break;
			}
		}
	}
    
	public class DaysCountFragment extends SherlockFragment implements OnClickListener {
		
		private Calendar mCalendar;
		private Button mCalendarBtn;
		private Button mBeforeResult;
		private Button mBeforeInput;
		
		private Button mAfterResult;
		private Button mAfterInput;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mCalendar = Calendar.getInstance();
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			return inflater.inflate(R.layout.count_days_fragement_layout, null);
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			
			mCalendarBtn = (Button) view.findViewById(R.id.count_date_start_btn);
			mBeforeResult = (Button) view.findViewById(R.id.count_before_days_result);
			mBeforeInput = (Button) view.findViewById(R.id.count_before_days_input);
			mBeforeInput.setText("0");
			
			mAfterResult = (Button) view.findViewById(R.id.count_after_days_result);
			mAfterInput = (Button) view.findViewById(R.id.count_after_days_input);
			mAfterInput.setText("0");
			
			mCalendarBtn.setOnClickListener(this);
			mBeforeInput.setOnClickListener(this);
			mAfterInput.setOnClickListener(this);
			
			count();
		}

		protected void count() {
			SimpleDateFormat dateFormat = new SimpleDateFormat(mDatePattern);
			mCalendarBtn.setText(dateFormat.format(mCalendar.getTime()));
			
			int beforeDays = 0;
			try {
				beforeDays = Integer.parseInt(mBeforeInput.getText().toString());
			} catch (Exception e) {
			}
			
			Calendar beforeCal = (Calendar) mCalendar.clone();
			beforeCal.add(Calendar.DAY_OF_MONTH, 0 - beforeDays);
			mBeforeResult.setText(dateFormat.format(beforeCal.getTime()));
			
			int afterDays = 0;
			try {
				afterDays = Integer.parseInt(mAfterInput.getText().toString());
			} catch (Exception e) {
			}
			
			Calendar afterCal = (Calendar) mCalendar.clone();
			afterCal.add(Calendar.DAY_OF_MONTH, afterDays);
			mAfterResult.setText(dateFormat.format(afterCal.getTime()));
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.count_date_start_btn: {
				new DatePickerDialog(getSherlockActivity(),
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								mCalendar.set(year, monthOfYear, dayOfMonth);
								count();
							}
						}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
				break;
			}
			case R.id.count_before_days_input: {
				int beforeDays = 0;
				try {
					beforeDays = Integer.parseInt(mBeforeInput.getText().toString());
				} catch (Exception e) {
				}
				
				new NumberPickerDialog(getSherlockActivity(), new OnNumberSetListener() {
					@Override
					public void onDateSet(NumberPicker view, int value) {
						mBeforeInput.setText(String.valueOf(value));
						count();
					}
				}, beforeDays).show();
				break;
			}
			case R.id.count_after_days_input: {
				int afterDays = 0;
				try {
					afterDays = Integer.parseInt(mAfterInput.getText().toString());
				} catch (Exception e) {
				}
				
				new NumberPickerDialog(getSherlockActivity(), new OnNumberSetListener() {
					@Override
					public void onDateSet(NumberPicker view, int value) {
						mAfterInput.setText(String.valueOf(value));
						count();
					}
				}, afterDays).show();
				break;
			}
			}
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_count_down, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			finish();
			break;
		}
		case R.id.menu_item_action_add: {
			Intent intent = new Intent(this, AddMatterActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.menu_item_action_setting: {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.menu_item_action_feedback: {
			UMFeedbackService.openUmengFeedbackSDK(this);
			break;
		}
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

}
