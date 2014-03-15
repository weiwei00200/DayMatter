package com.pybeta.daymatter.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.core.MatterApplication;
import com.pybeta.daymatter.utils.DateUtils;
import com.pybeta.ui.widget.NumberPickerDialog;
import com.pybeta.ui.widget.NumberPickerDialog.OnNumberSetListener;
import com.pybeta.ui.widget.UcTitleBar;
import com.pybeta.widget.NumberPicker;

public class CountdownRecActivity extends Activity implements OnClickListener{
	//原本继承SherlockFragment
	private UcTitleBar mTitleBar = null;
	//----------Date------------
	private Calendar mStartCalendar;
	private Calendar mEndCalendar;
	private Button mStartBtn;
	private Button mEndBtn;
	private TextView mResultText;
	//----------Dates-----------
	private Calendar mCalendar;
	private Button mCalendarBtn;
	private Button mBeforeResult;
	private Button mBeforeInput;
	
	private Button mAfterResult;
	private Button mAfterInput;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.fragment_countdown);
		
		initTitleBar();
		initDateView();
		initDatesView();
		
		//下方视图
//		ViewGroup daysCountLayout = (ViewGroup) this.findViewById(R.id.days_fragment);
//    	LinearLayout datesCountView = new DaysCountFragment(this);
//    	daysCountLayout.addView(datesCountView);
		
		//上方视图
//		ViewGroup dateCountLayout = (ViewGroup) this.findViewById(R.id.date_fragment);
//		LinearLayout dateCountView = new DateCountFragment(this);
//		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, 0 ,1);
//		lp.gravity = Gravity.CENTER;
//		dateCountLayout.setLayoutParams(lp);
//		dateCountLayout.addView(dateCountView);
    	
		
	}
	private void initTitleBar(){
		mTitleBar = (UcTitleBar)this.findViewById(R.id.uc_titlebar_countdown);
		mTitleBar.setTitleText(getResources().getString(R.string.date_cal));
    	mTitleBar.setViewVisible(false, true, false, false, false, false, false, false);
    	mTitleBar.setListener(new UcTitleBar.ITitleBarListener(){
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
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
			@Override
			public void addClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void closeClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void initDateView(){
		mStartCalendar = Calendar.getInstance();
		mEndCalendar = Calendar.getInstance();
		mStartBtn = (Button) this.findViewById(R.id.count_date_start_btn_date);
		mEndBtn = (Button) this.findViewById(R.id.count_date_end_btn_date);
		mResultText = (TextView) this.findViewById(R.id.count_date_result_num_date);
		mStartBtn.setOnClickListener(this);
		mEndBtn.setOnClickListener(this);
		
		dateCount();
	}
	
	protected void dateCount() {
		String datePattern = getString(R.string.matter_calendar_gregorian_date_format);
		SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
		mStartBtn.setText(dateFormat.format(mStartCalendar.getTime()));
		mEndBtn.setText(dateFormat.format(mEndCalendar.getTime()));
		
		String result = String.valueOf(DateUtils.getDaysBetween(mEndCalendar, mStartCalendar));
		
		SpannableString ss = new SpannableString(result + " " +getResources().getString(R.string.matter_unit));
		ss.setSpan(new ForegroundColorSpan(Color.RED), 0, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		mResultText.setText(ss);
	}
	
	private void initDatesView(){
		mCalendar = Calendar.getInstance();
		mCalendarBtn = (Button) findViewById(R.id.count_date_start_btn_dates);
		mBeforeResult = (Button) findViewById(R.id.count_before_days_result_dates);
		mBeforeInput = (Button) findViewById(R.id.count_before_days_input_dates);
		mBeforeInput.setText("0");
		
		mAfterResult = (Button) findViewById(R.id.count_after_days_result_dates);
		mAfterInput = (Button) findViewById(R.id.count_after_days_input_dates);
		mAfterInput.setText("0");
		
		mCalendarBtn.setOnClickListener(this);
		mBeforeInput.setOnClickListener(this);
		mAfterInput.setOnClickListener(this);
		
		datesCount();
	}
	
	protected void datesCount() {
		String datePattern = getString(R.string.matter_calendar_gregorian_date_format);
		SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
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
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//-----------------DateBtn---------------------
		case R.id.count_date_start_btn_date: {
			new DatePickerDialog(CountdownRecActivity.this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					mStartCalendar.set(year, monthOfYear, dayOfMonth);
					dateCount();
				}
			}, mStartCalendar.get(Calendar.YEAR), mStartCalendar.get(Calendar.MONTH),mStartCalendar.get(Calendar.DAY_OF_MONTH)).show();
			break;
		}
		case R.id.count_date_end_btn_date: {
			new DatePickerDialog(CountdownRecActivity.this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					mEndCalendar.set(year, monthOfYear, dayOfMonth);
					dateCount();
				}
			}, mEndCalendar.get(Calendar.YEAR), mEndCalendar.get(Calendar.MONTH), 
					mEndCalendar.get(Calendar.DAY_OF_MONTH)).show();
			break;
		}
		//-----------------DatesBtn---------------------
		case R.id.count_date_start_btn_dates: {
			new DatePickerDialog(CountdownRecActivity.this,new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
							mCalendar.set(year, monthOfYear, dayOfMonth);
							datesCount();
						}
					}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
			break;
		}
		case R.id.count_before_days_input_dates: {
			int beforeDays = 0;
			try {
				beforeDays = Integer.parseInt(mBeforeInput.getText().toString());
			} catch (Exception e) {
			}
			
			NumberPickerDialog dialog = new NumberPickerDialog(CountdownRecActivity.this, new OnNumberSetListener() {
				@Override
				public void onDateSet(NumberPicker view, int value) {
					mBeforeInput.setText(String.valueOf(value));
					datesCount();
				}
			}, beforeDays);
			dialog.show();
			break;
		}
		case R.id.count_after_days_input_dates: {
			int afterDays = 0;
			try {
				afterDays = Integer.parseInt(mAfterInput.getText().toString());
			} catch (Exception e) {
			}
			
			new NumberPickerDialog(CountdownRecActivity.this, new OnNumberSetListener() {
				@Override
				public void onDateSet(NumberPicker view, int value) {
					mAfterInput.setText(String.valueOf(value));
					datesCount();
				}
			}, afterDays).show();
			break;
		}
		default:
			break;
		}
	}
	
//    public class DateCountFragment extends LinearLayout implements OnClickListener {
//
//		private Calendar mStartCalendar;
//		private Calendar mEndCalendar;
//
//		private Button mStartBtn;
//		private Button mEndBtn;
//		private TextView mResultText;
//		
//		public DateCountFragment(Context context) {
//			super(context);
//			
//			mStartCalendar = Calendar.getInstance();
//			mEndCalendar = Calendar.getInstance();
//			
//			ViewGroup container = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.count_date_fragement_layout, null);
//			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//			container.setLayoutParams(lp);
//			
//			addView(container);
//			
//			mStartBtn = (Button) container.findViewById(R.id.count_date_start_btn);
//			mEndBtn = (Button) container.findViewById(R.id.count_date_end_btn);
//			mResultText = (TextView) container.findViewById(R.id.count_date_result_num);
//
//			mStartBtn.setOnClickListener(this);
//			mEndBtn.setOnClickListener(this);
//			
//			count();
//		}
//
//		protected void count() {
//			String datePattern = getString(R.string.matter_calendar_gregorian_date_format);
//			SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
//			mStartBtn.setText(dateFormat.format(mStartCalendar.getTime()));
//			mEndBtn.setText(dateFormat.format(mEndCalendar.getTime()));
//			
//			String result = String.valueOf(DateUtils.getDaysBetween(mEndCalendar, mStartCalendar));
//			
//			SpannableString ss = new SpannableString(result + " " +getResources().getString(R.string.matter_unit));
//			ss.setSpan(new ForegroundColorSpan(Color.RED), 0, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//			
//			mResultText.setText(ss);
//			
////			if (Math.abs(result) > 1) {
////				mResultText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.string.matter_unit, 0);
////			} else {
////				mResultText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.string.matter_unit, 0);
////			}
//		}
//		
//		@Override
//		public void onClick(View v) {
//			switch (v.getId()) {
//			case R.id.count_date_start_btn: {
//				new DatePickerDialog(CountdownRecActivity.this, new DatePickerDialog.OnDateSetListener() {
//					@Override
//					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//						mStartCalendar.set(year, monthOfYear, dayOfMonth);
//						count();
//					}
//				}, mStartCalendar.get(Calendar.YEAR), mStartCalendar.get(Calendar.MONTH),
//						mStartCalendar.get(Calendar.DAY_OF_MONTH)).show();
//				break;
//			}
//			case R.id.count_date_end_btn: {
//				new DatePickerDialog(CountdownRecActivity.this, new DatePickerDialog.OnDateSetListener() {
//					@Override
//					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//						mEndCalendar.set(year, monthOfYear, dayOfMonth);
//						count();
//					}
//				}, mEndCalendar.get(Calendar.YEAR), mEndCalendar.get(Calendar.MONTH), 
//						mEndCalendar.get(Calendar.DAY_OF_MONTH)).show();
//				break;
//			}
//			default:
//				break;
//			}
//		}
//    }
//
//	public class DaysCountFragment extends LinearLayout implements OnClickListener {
//
//		private Calendar mCalendar;
//		private Button mCalendarBtn;
//		private Button mBeforeResult;
//		private Button mBeforeInput;
//		
//		private Button mAfterResult;
//		private Button mAfterInput;
//		
//		public DaysCountFragment(Context context) {
//			super(context);
//			mCalendar = Calendar.getInstance();
//			
//			LayoutInflater.from(context).inflate(R.layout.count_days_fragement_layout, this, true);
//			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//			this.setLayoutParams(lp);
//			
//			
//			mCalendarBtn = (Button) findViewById(R.id.count_date_start_btn);
//			mBeforeResult = (Button) findViewById(R.id.count_before_days_result);
//			mBeforeInput = (Button) findViewById(R.id.count_before_days_input);
//			mBeforeInput.setText("0");
//			
//			mAfterResult = (Button) findViewById(R.id.count_after_days_result);
//			mAfterInput = (Button) findViewById(R.id.count_after_days_input);
//			mAfterInput.setText("0");
//			
//			mCalendarBtn.setOnClickListener(this);
//			mBeforeInput.setOnClickListener(this);
//			mAfterInput.setOnClickListener(this);
//			
//			count();
//			
//		}
//
//		protected void count() {
//			String datePattern = getString(R.string.matter_calendar_gregorian_date_format);
//			SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
//			mCalendarBtn.setText(dateFormat.format(mCalendar.getTime()));
//			
//			int beforeDays = 0;
//			try {
//				beforeDays = Integer.parseInt(mBeforeInput.getText().toString());
//			} catch (Exception e) {
//			}
//			
//			Calendar beforeCal = (Calendar) mCalendar.clone();
//			beforeCal.add(Calendar.DAY_OF_MONTH, 0 - beforeDays);
//			mBeforeResult.setText(dateFormat.format(beforeCal.getTime()));
//			
//			int afterDays = 0;
//			try {
//				afterDays = Integer.parseInt(mAfterInput.getText().toString());
//			} catch (Exception e) {
//			}
//			
//			Calendar afterCal = (Calendar) mCalendar.clone();
//			afterCal.add(Calendar.DAY_OF_MONTH, afterDays);
//			mAfterResult.setText(dateFormat.format(afterCal.getTime()));
//		}
//
//		@Override
//		public void onClick(View v) {
//			switch (v.getId()) {
//			case R.id.count_date_start_btn: {
//				new DatePickerDialog(CountdownRecActivity.this,
//						new DatePickerDialog.OnDateSetListener() {
//							@Override
//							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//								mCalendar.set(year, monthOfYear, dayOfMonth);
//								count();
//							}
//						}, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
//				break;
//			}
//			case R.id.count_before_days_input: {
//				int beforeDays = 0;
//				try {
//					beforeDays = Integer.parseInt(mBeforeInput.getText().toString());
//				} catch (Exception e) {
//				}
//				
//				new NumberPickerDialog(CountdownRecActivity.this, new OnNumberSetListener() {
//					@Override
//					public void onDateSet(NumberPicker view, int value) {
//						mBeforeInput.setText(String.valueOf(value));
//						count();
//					}
//				}, beforeDays).show();
//				break;
//			}
//			case R.id.count_after_days_input: {
//				int afterDays = 0;
//				try {
//					afterDays = Integer.parseInt(mAfterInput.getText().toString());
//				} catch (Exception e) {
//				}
//				
//				new NumberPickerDialog(CountdownRecActivity.this, new OnNumberSetListener() {
//					@Override
//					public void onDateSet(NumberPicker view, int value) {
//						mAfterInput.setText(String.valueOf(value));
//						count();
//					}
//				}, afterDays).show();
//				break;
//			}
//			}
//		}
//	}
//	
//  @Override
//  public View onCreateView(LayoutInflater inflater, ViewGroup container,
//  		Bundle savedInstanceState) {
//  	getSherlockActivity().getSupportActionBar().setTitle(R.string.title_activity_count_down);
//  	return inflater.inflate(R.layout.fragment_countdown, null);
//  }

//  @Override
//  public void onViewCreated(View view, Bundle savedInstanceState) {
//  	ViewGroup dateCountView = (ViewGroup) view.findViewById(R.id.date_fragment);
//  	dateCountView.addView(new DateCountFragment(getSherlockActivity()));
//  	
//  	ViewGroup daysCountView = (ViewGroup) view.findViewById(R.id.days_fragment);
//  	daysCountView.addView(new DaysCountFragment(getSherlockActivity()));
//  }
  
}
