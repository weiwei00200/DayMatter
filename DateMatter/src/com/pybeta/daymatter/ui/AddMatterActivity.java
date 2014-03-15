package com.pybeta.daymatter.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuInflater;
import com.pybeta.daymatter.CategoryItem;
import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.WidgetInfo;
import com.pybeta.daymatter.core.DataManager;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.tool.DateTimeTool;
import com.pybeta.daymatter.utils.DateUtils;
import com.pybeta.daymatter.utils.DealRepeatUtil;
import com.pybeta.daymatter.utils.LogUtil;
import com.pybeta.daymatter.utils.UtilityHelper;
import com.pybeta.ui.widget.DatePickerDialog;
import com.pybeta.util.DMToast;
import com.pybeta.widget.DatePicker;

public class AddMatterActivity extends BaseActivity implements OnClickListener {

	private final static int TYPE_CATEGORY = 1;
	private final static int TYPE_REPEAT = 2;
	private final static int TYPE_REMIND = 3;

	private boolean mModify;

	private EditText mTitleEt;
	private TextView mDateTv;
	private TextView mCategoryTv;
	private TextView mRepeatTv;
	private TextView mRemindTv;
	private ImageView mTopCb;
	private EditText mRemarkEt;

	private String[] mCategoryArray;
	private String[] mRepeatArray;
	private String[] mRemindArray;
	private SparseIntArray mCategoryIndexMap;

	private DayMatter mDayMatter;

	private DatabaseManager mDatebaseMgr;
	private List<CategoryItem> mCategoryList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_matter);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mDatebaseMgr = DatabaseManager.getInstance(this);

		initControlView();
	}

	protected void initControlView() {
		Intent intent = getIntent();
		mModify = intent.getBooleanExtra(IContants.KEY_MATTER_TYPE, false);
		if (mModify) {
			getSupportActionBar().setTitle(
					R.string.title_activity_modify_matter);
			mDayMatter = (DayMatter) intent
					.getSerializableExtra(IContants.KEY_MATTER_DATA);
		} else {
			getSupportActionBar().setTitle(R.string.title_activity_add_matter);
			mDayMatter = new DayMatter();
		}

		mCategoryIndexMap = new SparseIntArray();
		mCategoryList = mDatebaseMgr.queryCategoryList();
		mCategoryArray = new String[mCategoryList.size()];
		for (int index = 0; index < mCategoryList.size(); index++) {
			CategoryItem item = mCategoryList.get(index);
			mCategoryArray[index] = item.getName();
			mCategoryIndexMap.put(item.getId(), index);
		}

		mRepeatArray = getResources().getStringArray(R.array.matter_repeat);
		mRemindArray = getResources().getStringArray(R.array.matter_remind);

		initView();
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		if (getIntent().getBooleanExtra(IContants.KEY_MATTER_TYPE, false)) {
			inflater.inflate(R.menu.activity_matter_modify, menu);
		} else {
			inflater.inflate(R.menu.activity_matter_add, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			finish();
			break;
		}
		case R.id.menu_item_action_save:
			save();
			break;
		case R.id.menu_item_action_delete: {
			delete();
			break;
		}
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initView() {
		findViewById(R.id.day_date).setOnClickListener(this);
		findViewById(R.id.day_category).setOnClickListener(this);
		findViewById(R.id.day_repeat).setOnClickListener(this);
		findViewById(R.id.day_remind).setOnClickListener(this);
		findViewById(R.id.day_top).setOnClickListener(this);

		mTitleEt = (EditText) findViewById(R.id.day_event_input);
		mTitleEt.setText(mDayMatter.getMatter());
		mDateTv = (TextView) findViewById(R.id.day_date_summary);
		mDateTv.setText(DateUtils.getMatterDateString(this, mDayMatter));

		mCategoryTv = (TextView) findViewById(R.id.day_category_summary);
		String catName = mDatebaseMgr
				.queryCatNameById(mDayMatter.getCategory());
		if (!TextUtils.isEmpty(catName)) {
			mCategoryTv.setText(catName);
		} else {
			mCategoryTv.setText(R.string.category_default);
			mDayMatter.setCategory(0);
		}
		mRepeatTv = (TextView) findViewById(R.id.day_repeat_summary);
		mRepeatTv.setText(mRepeatArray[mDayMatter.getRepeat()]);
		mRemindTv = (TextView) findViewById(R.id.day_remind_summary);
		mRemindTv.setText(mRemindArray[mDayMatter.getRemind()]);
		mTopCb = (ImageView) findViewById(R.id.day_top_check);

		if (mDayMatter.getTop() == 1) {
			mTopCb.setImageResource(R.drawable.checkbox_on);
		} else {
			mTopCb.setImageResource(R.drawable.checkbox_off);
		}

		mRemarkEt = (EditText) findViewById(R.id.day_remark_input);
		mRemarkEt.setText(mDayMatter.getRemark());
		mRemarkEt.setSelection(mDayMatter.getRemark().length());

		mTitleEt.requestFocus();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.day_date: {
			showDatePickerDialog();
			break;
		}
		case R.id.day_category: {
			showPickerDialog(TYPE_CATEGORY);
			break;
		}
		case R.id.day_repeat: {
			showPickerDialog(TYPE_REPEAT);
			break;
		}
		case R.id.day_remind: {
			showPickerDialog(TYPE_REMIND);
			break;
		}
		case R.id.day_top: {
			if (mDayMatter.getTop() == 1) {
				mDayMatter.setTop(0);
				mTopCb.setImageResource(R.drawable.checkbox_off);
			} else {
				mDayMatter.setTop(1);
				mTopCb.setImageResource(R.drawable.checkbox_on);
			}
			break;
		}
		default:
			break;
		}
	}

	private int calendar;
	private int year;
	private int month;
	private int day;

	private void showDatePickerDialog() {
		final Calendar c = Calendar.getInstance();
		c.setTime(new Date(mDayMatter.getDate()));

		calendar = mDayMatter.getCalendar();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dlg = new DatePickerDialog(this, datePickerListener,
				calendar, year, month, day);
		dlg.show();
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int calendar, int year,
				int monthOfYear, int dayOfMonth) {
			Calendar c = Calendar.getInstance();
			c.set(year, monthOfYear, dayOfMonth);
			mDayMatter.setDate(c.getTimeInMillis());
			mDayMatter.setCalendar(calendar);
			mDateTv.setText(DateUtils.getMatterDateStringForAddMatterView(
					AddMatterActivity.this, mDayMatter,"yyyy-MM-dd"));
		}
	};

	private void showPickerDialog(final int type) {
		int titleId = 0, select;
		final String[] array;
		final TextView picker;

		switch (type) {
		case TYPE_CATEGORY: {
			titleId = R.string.mattery_category_title;
			array = mCategoryArray;
			picker = mCategoryTv;
			select = mCategoryIndexMap.get(mDayMatter.getCategory());
			break;
		}
		case TYPE_REPEAT: {
			titleId = R.string.mattery_repeat_title;
			array = mRepeatArray;
			picker = mRepeatTv;
			select = mDayMatter.getRepeat();
			break;
		}
		case TYPE_REMIND: {
			titleId = R.string.mattery_remind_title;
			array = mRemindArray;
			picker = mRemindTv;
			select = mDayMatter.getRemind();
			break;
		}
		default:
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(titleId);

		if (type == TYPE_REMIND) {
			View view = LayoutInflater.from(this).inflate(R.layout.addmatteractivity_remind, null);
			final TimePicker addmatteractivity_remind_timepicker = (TimePicker)view.findViewById(R.id.addmatteractivity_remind_timepicker);
			
//			mDayMatter
			addmatteractivity_remind_timepicker.setIs24HourView(true);
			if(mModify){
				addmatteractivity_remind_timepicker.setCurrentHour(mDayMatter.getHour());
				addmatteractivity_remind_timepicker.setCurrentMinute(mDayMatter.getMin());
			}else{
				Calendar mCalendar = Calendar.getInstance();
				addmatteractivity_remind_timepicker.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
				addmatteractivity_remind_timepicker.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
			}
			builder.setView(view);
			builder.setPositiveButton(getResources().getString(R.string.matter_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							int hour = addmatteractivity_remind_timepicker.getCurrentHour();
							int mins = addmatteractivity_remind_timepicker.getCurrentMinute();
							mDayMatter.setHour(hour);
							mDayMatter.setMin(mins);
							
							
							Calendar c = Calendar.getInstance();
							c.setTimeInMillis(mDayMatter.getDate());
							c.set(Calendar.HOUR_OF_DAY, hour);
							c.set(Calendar.MINUTE, mins);
							mDayMatter.setDate(c.getTimeInMillis());
							mDayMatter.setCalendar(calendar);
							
							dialog.dismiss();
						}
					});
			builder.setNegativeButton(getResources().getString(R.string.matter_cancel), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
		} 
		builder.setSingleChoiceItems(array, select,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						picker.setText(array[item]);
						if (type == TYPE_CATEGORY) {
							mDayMatter.setCategory(mCategoryList.get(item)
									.getId());
						} else if (type == TYPE_REPEAT) {
							mDayMatter.setRepeat(item);
						} else if (type == TYPE_REMIND) {
							mDayMatter.setRemind(item);
						}
						if(type != TYPE_REMIND)
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	public void save() {
		LogUtil.Sysout("AddMatterActivity save");
		String title = mTitleEt.getText().toString().trim();
		if (title == null || title.length() == 0) {
			DMToast.makeText(getApplicationContext(),
					R.string.matter_title_hint, Toast.LENGTH_LONG).show();
			return;
		}
		mDayMatter.setRemark(mRemarkEt.getText().toString().trim());
		
		mDayMatter.setMatter(title);
		
		mDayMatter.setNextRemindTime(DealRepeatUtil.dealRepeatDateTime(mDayMatter,false));
		Log.i("next repeat time ----", DateTimeTool.convertMillisToDateString(mDayMatter.getNextRemindTime(), "yyyy-MM-dd"));
		
		DatabaseManager databaseMgr = DatabaseManager.getInstance(this);
		if (mModify) {
			databaseMgr.update(mDayMatter);
		} else {
			databaseMgr.add(mDayMatter);
		}

		DataManager dataMgr = DataManager.getInstance(this);
		WidgetInfo info = dataMgr.getWidgetInfoByMatterUid(mDayMatter.getUid());
		if (info != null) {
			UtilityHelper.updateWidgetView(AddMatterActivity.this, info);
		}

		UtilityHelper.showStartBarNotification(AddMatterActivity.this);

		DMToast.makeText(getApplicationContext(), R.string.matter_save,
				Toast.LENGTH_LONG).show();
		String[] str = mDayMatter.toRecord();
		for(String t: str){
			LogUtil.Sysout("t: "+t);
		}
		
		DatabaseManager dataBaseMgr = DatabaseManager.getInstance(this);
		DayMatter matter = dataBaseMgr.query(mDayMatter.getUid());
//		DateUtils.setDateChangedListenerForOne(getApplicationContext(), matter);
		LogUtil.Sysout("mDayMatter id: "+mDayMatter.getId());
		if(mDayMatter.getRemind() > IContants.MATTER_REMIND_NONE)
			DateUtils.setDateChangedListenerForOne(getApplicationContext(), mDayMatter);
		finish();
	}

	public void delete() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.app_name);
		builder.setMessage(R.string.matter_delete_confirm);
		builder.setPositiveButton(R.string.matter_delete,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						DataManager dataMgr = DataManager
								.getInstance(AddMatterActivity.this);
						WidgetInfo info = dataMgr
								.getWidgetInfoByMatterUid(mDayMatter.getUid());

						DatabaseManager databaseMgr = DatabaseManager
								.getInstance(AddMatterActivity.this);
						databaseMgr.delete(mDayMatter);

						if (info != null) {
							UtilityHelper.updateWidgetView(
									AddMatterActivity.this, info);
							dataMgr.deleteWidgetInfoByMatterId(info.dayMatterId);
						}

						UtilityHelper
								.showStartBarNotification(AddMatterActivity.this);

						DMToast.makeText(getApplicationContext(),
								R.string.matter_delete_success,
								Toast.LENGTH_LONG).show();
						AddMatterActivity.this.finish();
					}
				});
		builder.setNegativeButton(R.string.matter_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		builder.create().show();
	}
}
