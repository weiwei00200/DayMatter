package com.pybeta.daymatter.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.adapter.MatterViewPagerAdapter;
import com.pybeta.daymatter.bean.YiJiChongBean;
import com.pybeta.daymatter.core.DataManager;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.tool.DateTimeTool;
import com.pybeta.daymatter.tool.MyDateHelper;
import com.pybeta.daymatter.tool.ChineseCalendar;
import com.pybeta.daymatter.utils.DateUtils;
import com.pybeta.daymatter.yiji.db.DBManager;
import com.pybeta.daymatter.yiji.db.LaoHuangLiDBHelper;
import com.pybeta.daymatter.yiji.db.YiJiDBHelper;
import com.pybeta.ui.widget.LoadingDialog;
import com.pybeta.ui.widget.UcDate;
import com.pybeta.ui.widget.UcTitleBar;
import com.sammie.common.thread.CustomRunnable;
import com.sammie.common.thread.IDataAction;

public class ViewMatterRecActivity extends Activity implements
		OnPageChangeListener {
	private UcTitleBar mTitleBar = null;
	private ViewPager mViewPager = null;
	private MatterViewPagerAdapter mAdapter = null;
	private List<DayMatter> mMatterList = null;
	private int mCurrentPosition = 0;
	public UcDate datewidget;

	private String[] mRemindArray;
	
	
	public ArrayList<YiJiChongBean> yijichonglist;
	ArrayList<HashMap<String,Object>> weeklist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_matter_rec);
		mRemindArray = getResources().getStringArray(R.array.matter_remind);

		mTitleBar = (UcTitleBar) this.findViewById(R.id.uc_titlebar);
		mViewPager = (ViewPager) this
				.findViewById(R.id.viewpager_viewmatter_detail);
		initTitleBar();
		initViewPager();
	}

	private void initViewPager() {
		try {
			final DayMatter matter = (DayMatter) getIntent()
					.getSerializableExtra(IContants.KEY_MATTER_DATA);
			final int position = getIntent().getIntExtra(
					IContants.kEY_MATTER_INDEX, 0);
			mCurrentPosition = position;
			final int category = matter.getCategory();
			IDataAction runAction = new IDataAction() {
				@Override
				public Object actionExecute(Object arg0) {
					// TODO Auto-generated method stub
					List<DayMatter> dayMatterList = getDayMatterListById(0);// 默认所有都查出来
																			// 可以用category只拿出对应类型的列表
					// 复制本地DB
					DBManager.getDBManager(ViewMatterRecActivity.this)
							.WriteDBFile(ViewMatterRecActivity.this,
									R.raw.yiji, "yiji.db");
					DBManager.getDBManager(ViewMatterRecActivity.this)
							.WriteDBFile(ViewMatterRecActivity.this,
									R.raw.laohuangli, "laohuangli.db");
					// 查询宜忌,节气

					List<YiJiChongBean> yiJiList = getYiJiList(dayMatterList);

					List<HashMap<String,Object>> WeekforDateList = getWeekforDate(dayMatterList);

					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put("DayMatterList", dayMatterList);
					resultMap.put("YiJiChongBeanList", yiJiList);
					resultMap.put("WeekforDateList", WeekforDateList);
					return resultMap;
				}
			};
			IDataAction completeAction = new IDataAction() {
				@Override
				public Object actionExecute(Object arg0) {
					// TODO Auto-generated method stub
					if (arg0 != null) {
						Map<String, Object> resultMap = (Map<String, Object>) arg0;
						List<DayMatter> dayMatterList = (List<DayMatter>) resultMap
								.get("DayMatterList");
						List<YiJiChongBean> yiJiList = (List<YiJiChongBean>) resultMap
								.get("YiJiChongBeanList");

						List<HashMap<String,Object>> WeekforDateList = (List<HashMap<String,Object>>) resultMap
								.get("WeekforDateList");
						
						yijichonglist=(ArrayList<YiJiChongBean>) yiJiList;
						weeklist=(ArrayList<HashMap<String, Object>>) WeekforDateList;

						List<View> viewList = new ArrayList<View>();
						for (int i = 0; i < dayMatterList.size(); i++) {
							DayMatter dayMatter = dayMatterList.get(i);
							View perPage = LayoutInflater.from(
									ViewMatterRecActivity.this).inflate(
									R.layout.view_matter_per_page, null);
							TextView tvTitleTip = (TextView) perPage
									.findViewById(R.id.tv_title_tip_detail);
							TextView tvTitle = (TextView) perPage
									.findViewById(R.id.tv_title_detail);
							TextView tvDays = (TextView) perPage
									.findViewById(R.id.tv_day_detail);
							TextView tvDate = (TextView) perPage
									.findViewById(R.id.tv_date_detail);
							TextView tvClock = (TextView) perPage
									.findViewById(R.id.tv_clock_detail);
							TextView tvMark = (TextView) perPage
									.findViewById(R.id.tv_mark_detail);
							datewidget = (UcDate) perPage
									.findViewById(R.id.datewidget);

							String[] positionlist=(String[])(WeekforDateList
									.get(i).get("position"));
							int position = Integer.parseInt(positionlist[0]);
							datewidget.TodayTextColor(position,
									R.color.almanac_red_date_msg);

							// datewidget.setChineseNumberTextColor();
							int duration = DateUtils.getDaysBetween(dayMatter)
									.get("Days");
							if (duration >= 0) {
								tvTitleTip.setText(getResources().getString(
										R.string.distance));
							} else {
								tvTitleTip.setText(getResources().getString(
										R.string.past));
							}
							tvTitle.setText(dayMatter.getMatter() + "");
							tvDays.setText(Math.abs(duration) + "");
							tvDate.setText(DateUtils.getMatterDateString(
									ViewMatterRecActivity.this, dayMatter) + "");
							int remindTypePosition = dayMatter.getRemind();
							tvClock.setText(mRemindArray[remindTypePosition]
									+ (remindTypePosition != 0 ? "：" : "")
									+ (remindTypePosition != 0 ? DateTimeTool.convertMillisToDateString(
											dayMatter.getNextRemindTime(),
											"HH:mm")
											: ""));
							tvMark.setText(dayMatter.getRemark() + "");

							String yearText = DateTimeTool
									.convertMillisToDateString(
											dayMatter.getNextRemindTime(),
											"yyyy-MM-dd").substring(0, 4);

							String yi = yiJiList.get(i).getYi();
							String ji = yiJiList.get(i).getJi();
							datewidget.setYiJiText(yi.equals("") ? "" : yi,
									ji.equals("") ? "" : ji, yearText);
							
							String[] numberlist=(String[])(WeekforDateList
									.get(i).get("number"));
							String[] chinesenumberlist=(String[])(WeekforDateList
									.get(i).get("chinesenumber"));
							
							
							ArrayList<Integer> jieqiposition=(ArrayList<Integer>)(WeekforDateList
									.get(i).get("jieqiposition"));
							
							datewidget.setChineseNumberTextColor(jieqiposition);
							
							datewidget.setDateText(numberlist,chinesenumberlist);
							
							datewidget.setListener(new UcDate.IUcDateListener() {
								
								@Override
								public void linear_date(Object obj) {
									// TODO Auto-generated method stub
									startDateDetailActivity();
								}
								@Override
								public void linear_yi_ji(Object obj) {
									// TODO Auto-generated method stub
									startDateDetailActivity();
								}
								
							});
							
							
							

							viewList.add(perPage);
						}
						mAdapter = new MatterViewPagerAdapter(viewList);
						mViewPager.setAdapter(mAdapter);
						mViewPager
								.setOnPageChangeListener(ViewMatterRecActivity.this);
						mViewPager.setCurrentItem(position);
					}
					LoadingDialog.closeLoadingDialog();
					return null;
				}
			};
			CustomRunnable run = new CustomRunnable(runAction, completeAction);
			run.startAction();
			LoadingDialog.showLoadingDialog(this, "加载中...", true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LoadingDialog.closeLoadingDialog();
		}

	}
	public void startDateDetailActivity()
	{
		Bundle bundle=new Bundle();
		bundle.putInt("todayposition",(Integer)(weeklist.get(mCurrentPosition).get("todayposition")));
		bundle.putString("today_nongli",(String)(weeklist.get(mCurrentPosition).get("today_nongli")));
		bundle.putString("Yi",yijichonglist.get(mCurrentPosition).getYi());
		bundle.putString("Ji",yijichonglist.get(mCurrentPosition).getJi());
		bundle.putString("Chong",yijichonglist.get(mCurrentPosition).getChong());
		bundle.putStringArray("numberlist",(String[])(weeklist.get(mCurrentPosition).get("number")));
		bundle.putStringArray("chinesenumberlist",(String[])(weeklist.get(mCurrentPosition).get("chinesenumber")));
		bundle.putIntegerArrayList("jieqiposition",(ArrayList<Integer>)(weeklist
				.get(mCurrentPosition).get("jieqiposition")));
		Intent intent=new Intent(ViewMatterRecActivity.this,DateDetailActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private List<YiJiChongBean> getYiJiList(List<DayMatter> dayMatterList) {
		List<YiJiChongBean> yiJiList = new ArrayList<YiJiChongBean>();
		if (dayMatterList != null) {
			for (DayMatter dayMatter : dayMatterList) {
				String dateStr = "";
				if (dayMatter.getCalendar() == IContants.CALENDAR_GREGORIAN) {
					dateStr = DateTimeTool.convertMillisToDateString(
							dayMatter.getNextRemindTime(), "yyyy-MM-dd");
				} else {
					int[] nonglidate = StrToInt(DateTimeTool
							.convertMillisToDateString(
									dayMatter.getNextRemindTime(), "yyyy-MM-dd"));
					dateStr = DateFormat(nonglidate[0], nonglidate[1],
							nonglidate[2], false);
					dateStr = IntToStr(dateStr);
					long yangli = DateTimeTool
							.getMillisecondByDateTime(dateStr);
					dateStr = DateTimeTool.convertMillisToDateString(yangli,
							"yyyy-MM-dd");
				}

				YiJiChongBean yijiBean = YiJiDBHelper.getHelper(
						ViewMatterRecActivity.this).QueryDB(dateStr);
				yiJiList.add(yijiBean);
			}
		}
		return yiJiList;
	}

	private List<HashMap<String,Object>> getWeekforDate(List<DayMatter> dayMatterList) {
		List<HashMap<String,Object>> weekLists = new ArrayList<HashMap<String,Object>>();

		if (dayMatterList != null) {
			for (DayMatter dayMatter : dayMatterList) {

				String dateStr = "";
				int weekday = 0;
				if (dayMatter.getCalendar() == IContants.CALENDAR_GREGORIAN) {
					dateStr = DateTimeTool.convertMillisToDateString(
							dayMatter.getNextRemindTime(), "yyyy-MM-dd");
					weekday = DateTimeTool.getDayCountOfWeek(dayMatter
							.getNextRemindTime());
				} else {
					int[] nonglidate = StrToInt(DateTimeTool
							.convertMillisToDateString(
									dayMatter.getNextRemindTime(), "yyyy-MM-dd"));
					dateStr = DateFormat(nonglidate[0], nonglidate[1],
							nonglidate[2], false);
					dateStr = IntToStr(dateStr);
					long yangli = DateTimeTool
							.getMillisecondByDateTime(dateStr);
					dateStr = DateTimeTool.convertMillisToDateString(yangli,
							"yyyy-MM-dd");
					weekday = DateTimeTool.getDayCountOfWeek(yangli);

				}

				HashMap<String,Object> weekdayList = new HashMap<String,Object>();
				weekdayList = getDateNumber(weekday, dateStr);
				weekLists.add(weekdayList);
			}
		}

		return weekLists;
	}

	private HashMap<String, Object> getDateNumber(int weeekday, String dateStr) {

		HashMap<String, Object> strlists = new HashMap<String, Object>();
		
		ArrayList<Integer> jieqiposition=new ArrayList<Integer>();
		int todayposition=weeekday - 1;
		
		
		

		int k = 1;
		int y = 1;
		String[] number = new String[7];
		String[] chinesenumber = new String[7];
		String[] position = new String[1];

		position[0] = (weeekday - 1) + "";

		String today = dateStr;

		String jieqi = "";

		number[weeekday - 1] = Integer.parseInt(dateStr.substring(
				dateStr.length() - 2, dateStr.length()))
				+ "";
		jieqi = LaoHuangLiDBHelper.getHelper(ViewMatterRecActivity.this)
				.QueryDB(dateStr);
		
		String today_nonglistr=nonglistr(StrToInt(dateStr)[0],
				StrToInt(dateStr)[1], StrToInt(dateStr)[2]);
		if (jieqi == "") {

			chinesenumber[weeekday - 1] = DateFormat(StrToInt(dateStr)[0],
					StrToInt(dateStr)[1], StrToInt(dateStr)[2], true);
		} else {

			chinesenumber[weeekday - 1] = jieqi;

		}

		int beforedayBetween = weeekday - 1;

		if (beforedayBetween > 0) {

			for (int i = (weeekday - 1 - 1); i >= 0; i--) {

				String date = DateTimeTool.getDateTimeByString(today, -k,
						"yyyy-MM-dd");

				number[i] = Integer.parseInt(date.substring(date.length() - 2,
						date.length())) + "";
				jieqi = LaoHuangLiDBHelper
						.getHelper(ViewMatterRecActivity.this).QueryDB(date);
				if (jieqi == "") {
					chinesenumber[i] = DateFormat(StrToInt(date)[0],
							StrToInt(date)[1], StrToInt(date)[2], true);

				} else {
					chinesenumber[i] = jieqi;
					jieqiposition.add(i);

				}

				k++;
			}
			if ((beforedayBetween) < 6) {
				for (int j = weeekday; j <= 6; j++) {
					String date = DateTimeTool.getDateTimeByString(today, y,
							"yyyy-MM-dd");

					number[j] = Integer.parseInt(date.substring(
							date.length() - 2, date.length())) + "";
					jieqi = LaoHuangLiDBHelper.getHelper(
							ViewMatterRecActivity.this).QueryDB(date);
					if (jieqi == "") {
						chinesenumber[j] = DateFormat(StrToInt(date)[0],
								StrToInt(date)[1], StrToInt(date)[2], true);
					} else {
						chinesenumber[j] = jieqi;
						jieqiposition.add(j);

					}

					y++;
				}
			}

		} else {
			for (int i = 1; i <= 6; i++) {
				String date = DateTimeTool.getDateTimeByString(today, i,
						"yyyy-MM-dd");
				number[i] = Integer.parseInt(date.substring(date.length() - 2,
						date.length())) + "";
				jieqi = LaoHuangLiDBHelper
						.getHelper(ViewMatterRecActivity.this).QueryDB(date);
				if (jieqi == "") {
					chinesenumber[i] = DateFormat(StrToInt(date)[0],
							StrToInt(date)[1], StrToInt(date)[2], true);
				} else {
					chinesenumber[i] = jieqi;
					jieqiposition.add(i);

				}

			}
		}
		strlists.put("today_nongli", today_nonglistr);
		strlists.put("todayposition", todayposition);
		strlists.put("number", number);
		strlists.put("chinesenumber",chinesenumber);
		strlists.put("position",position);
		strlists.put("jieqiposition",jieqiposition);
		return strlists;

	}

	public String DateFormat(int year, int month, int day, boolean isLunar) {

		String str = "";
		if (isLunar) {
			// 公历转农历
			// 参数 day month year
			int[] nongli = MyDateHelper.convertSolar2Lunar(day, month, year);
			String[] nongliList = getResources().getStringArray(
					R.array.date_picker_calendar_day);
			str = nongliList[nongli[0]];

		} else {
			// 农历转公历
			// 参数 day month year
			str = ChineseCalendar.sCalendarLundarToSolar(year, month, day);

		}

		return str;
	}

	public String nonglistr(int year, int month, int day)
	{
		String str = "";
		int[] nongli = MyDateHelper.convertSolar2Lunar(day, month, year);
		String[] nonglidayList = getResources().getStringArray(
				R.array.date_picker_calendar_day);
		String[] nonglimonthList = getResources().getStringArray(
				R.array.date_picker_calendar_month);
		str = nonglimonthList[nongli[1]-1]+nonglidayList[nongli[0]];
		
		return str;
	}
	public int[] StrToInt(String datestr) {
		String tt = "-";
		datestr = datestr.replaceAll(tt, "");
		int[] date = new int[3];
		date[0] = Integer.parseInt(datestr.substring(0, 4));
		date[1] = Integer.parseInt(datestr.substring(4, 6));
		date[2] = Integer.parseInt(datestr.substring(6, 8));
		return date;
	}

	public String IntToStr(String datestr) {
		String t = "-";
		String[] date = new String[3];
		date[0] = datestr.substring(0, 4);
		date[1] = datestr.substring(4, 6);
		date[2] = datestr.substring(6, 8);
		datestr = date[0] + t + date[1] + t + date[2];
		return datestr;
	}

	private List<DayMatter> getDayMatterListById(int categoryId) {
		mMatterList = DatabaseManager.getInstance(this).queryAll(categoryId);
		return DataManager.getInstance(this).sortMatters(mMatterList);
	}

	private void initTitleBar() {
		mTitleBar.setTitleText(getResources().getString(R.string.detail));
		mTitleBar.setViewVisible(false, true, false, false, true, false, false,
				false);
		mTitleBar.setListener(new UcTitleBar.ITitleBarListener() {
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
				gotoModifyActivity();
			}

			@Override
			public void completeClick(Object obj) {
				// TODO Auto-generated method stub

			}

			@Override
			public void backClick(Object obj) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
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

	private void gotoModifyActivity() {
		DayMatter dayMatter = mMatterList.get(mCurrentPosition);

		Intent intent = new Intent(this, AddMatterActivity.class);
		intent.putExtra(IContants.KEY_MATTER_TYPE, true);
		intent.putExtra(IContants.KEY_MATTER_DATA, dayMatter);
		startActivity(intent);

		finish();
	}

	@Override
	public void onPageScrollStateChanged(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int position, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		mCurrentPosition = position;
	}
}
