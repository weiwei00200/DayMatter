package com.pybeta.daymatter.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pybeta.daymatter.BuildConfig;
import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.core.AlarmReceiver;
import com.pybeta.daymatter.core.AlarmService;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.tool.DateTimeTool;
import com.pybeta.daymatter.widget.WidgetProviderHuge;
import com.pybeta.util.ChineseCalendarGB;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

public class DateUtils {


	// private static final int MIN_MILLS = 1000 * 60 ;

	public static void setDateChangedListener(Context context) {
		// System.out.println("vk2013 setDateChangedListener ");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		long triggerTime = c.getTimeInMillis();

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent updateTimeIntent = new Intent(WidgetProviderHuge.ACTION_DATE_SET);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				updateTimeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime,
				IContants.DAY_MILLS, pendingIntent);

		DatabaseManager dataBaseMgr = DatabaseManager.getInstance(context);
		List<DayMatter> matterList = dataBaseMgr.queryAll(0);

		if (matterList != null && matterList.size() > 0) {
			// System.out.println("vk2013 setDateChangedListener  matterList.size(): "+
			// matterList.size());
			for (DayMatter mDayMatter : matterList) {
				setAlarms(context, mDayMatter);
			}

		}
	}

	public static void setDateChangedListenerForOne(Context context,
			DayMatter mDayMatter) {
		setAlarms(context, mDayMatter);
	}

	public static void cancelAlarm(Context context,int reqCode){
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent alarmIntent = new Intent(context, AlarmService.class);
		PendingIntent alarmPendingIntent = PendingIntent.getService(context,
				reqCode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(alarmPendingIntent);
	}
	
	private static void setAlarms(Context context, DayMatter mDayMatter) {
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(System.currentTimeMillis());
		int hour = mDayMatter.getHour();
		int min = mDayMatter.getMin();
//		if (c1.get(Calendar.HOUR_OF_DAY) > hour
//				&& c1.get(Calendar.MINUTE) > min) {
//			c1.setTimeInMillis(System.currentTimeMillis() + IContants.DAY_MILLS);
//		}
		if (BuildConfig.DEBUG) {
			System.out.println("alarm: " + c1);
		}
		c1.set(Calendar.HOUR_OF_DAY, hour);
		c1.set(Calendar.MINUTE, min);
		c1.set(Calendar.SECOND, 0);

		long alarmTime = c1.getTimeInMillis()
				- (mDayMatter.getRemind() > IContants.MATTER_REMIND_THEDAYBEFORE ? (IContants.DAY_MILLS) : 0);
		int reqCode = mDayMatter.getId();
		Intent alarmIntent = new Intent(context, AlarmService.class);
		alarmIntent.putExtra("reqCode", reqCode);
		alarmIntent.putExtra("uid", mDayMatter.getUid());
		alarmIntent.putExtra("event", mDayMatter.getMatter());
		alarmIntent.putExtra("time", mDayMatter.getHour() + " h : "
				+ mDayMatter.getMin() + " m"+" reqCode: "+reqCode);
		PendingIntent alarmPendingIntent = PendingIntent.getService(context,
				reqCode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//		if(mDayMatter.getRepeat() == IContants.MATTER_REMIND_NONE)
//			alarmManager.set(AlarmManager.RTC, alarmTime, 
//					alarmPendingIntent);
//		else
			alarmManager.setRepeating(AlarmManager.RTC, alarmTime, IContants.DAY_MILLS,
				alarmPendingIntent);
	}

	public static Map<String,Integer> getDaysBetween(DayMatter dayMatter) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		
		Calendar d1 = Calendar.getInstance();
		Calendar d2 = Calendar.getInstance();

		boolean isFuture = dayMatter.getDate()>=DateTimeTool.getMillisecondByDateTime(DateTimeTool.convertMillisToDateString(System.currentTimeMillis(), "yyyy-MM-dd"));//getDayMatterCalendar(dayMatter);
		d1.setTimeInMillis(dayMatter.getNextRemindTime());

		int days = 0;
		if (dayMatter.getCalendar() == IContants.CALENDAR_GREGORIAN) {
			days = getDaysBetween(d1, d2);
		} else {
			ChineseCalendarGB lunar = new ChineseCalendarGB();
			lunar.setGregorian(d2.get(Calendar.YEAR),
					d2.get(Calendar.MONTH) + 1, d2.get(Calendar.DAY_OF_MONTH));
			lunar.computeChineseFields();

			int year = d1.get(Calendar.YEAR);
			int month = d1.get(Calendar.MONTH) + 1;
			int day = d1.get(Calendar.DAY_OF_MONTH);
			if (lunar.after(year, month, day)) {
				days = 0 - ChineseCalendarGB.computeDistance(year, month, day,
						lunar.getChineseYear(), lunar.getChineseMonth(),
						lunar.getChineseDate());
			} else {
				days = ChineseCalendarGB.computeDistance(
						lunar.getChineseYear(), lunar.getChineseMonth(),
						lunar.getChineseDate(), year, month, day);
			}
		}
		map.put("Days", days);
		map.put("IsFuture", isFuture?0:1);//0代表未来，1代表已经过去
		return map;
	}

	public static int getDaysBetween(Calendar d1, Calendar d2) {
		boolean past = false;

		if (d1.after(d2)) {
			Calendar swap = d1;
			d1 = d2;
			d2 = swap;
			past = true;
		} else {
			past = false;
		}

		int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
		int y2 = d2.get(Calendar.YEAR);
		if (d1.get(Calendar.YEAR) != y2) {
			d1 = (Calendar) d1.clone();
			do {
				days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
				d1.add(Calendar.YEAR, 1);
			} while (d1.get(Calendar.YEAR) != y2);
		}
		return past ? days : 0 - days;
	}

	public static long getRepeatTimeMillis(int type, long dayMatterDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(dayMatterDate));
		switch (type) {
		case 1:
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
			break;
		case 2:
			calendar.add(Calendar.MONTH, 1);
			break;
		case 3:
			calendar.add(Calendar.YEAR, 1);
			break;
		default:
			break;
		}
		return calendar.getTimeInMillis();
	}
	
	public static boolean getDayMatterCalendar(DayMatter dayMatter) {
		boolean isFirstToWhile = true;
		boolean isFuture = true;
		Calendar d1 = Calendar.getInstance();
		d1.setTimeInMillis(dayMatter.getDate());

		if (dayMatter.getCalendar() == IContants.CALENDAR_GREGORIAN) {
			Calendar d2 = Calendar.getInstance();

			long dayMatterDate = dayMatter.getDate();
			int type = dayMatter.getRepeat();

			int dayOfyear1 = d1.get(Calendar.DAY_OF_YEAR);
			int dayOfyear2 = d2.get(Calendar.DAY_OF_YEAR);

			while (type != 0 && dayMatterDate < System.currentTimeMillis()
					&& dayOfyear1 != dayOfyear2) {
				dayMatterDate = getRepeatTimeMillis(type, dayMatterDate);
				d1.setTimeInMillis(dayMatterDate);
				dayOfyear1 = d1.get(Calendar.DAY_OF_YEAR);
				if(isFirstToWhile){
					isFuture = false;
					isFirstToWhile = false;
				}
			}
			dayMatter.setDate(dayMatterDate);
			d1.setTimeInMillis(dayMatterDate);
			
		} else {
			Calendar d2 = Calendar.getInstance();

			long dayMatterDate = dayMatter.getDate();
			int type = dayMatter.getRepeat();

			int dayOfyear1 = ChineseCalendarGB.dayOfChineseYear(
					d1.get(Calendar.YEAR), d1.get(Calendar.MONTH) + 1,
					d1.get(Calendar.DAY_OF_MONTH));

			ChineseCalendarGB lunarToday = new ChineseCalendarGB();
			lunarToday.setGregorian(d2.get(Calendar.YEAR),
					d2.get(Calendar.MONTH) + 1, d2.get(Calendar.DAY_OF_MONTH));
			lunarToday.computeChineseFields();
			int dayOfyear2 = ChineseCalendarGB.dayOfChineseYear(
					lunarToday.getChineseYear(), lunarToday.getChineseMonth(),
					lunarToday.getChineseDate());

			// 目前农历只支持按年循环
			while (type == 3
					&& lunarToday.after(d1.get(Calendar.YEAR),
							d1.get(Calendar.MONTH) + 1,
							d1.get(Calendar.DAY_OF_MONTH))
					&& dayOfyear1 != dayOfyear2) {
				dayMatterDate = getRepeatTimeMillis(type, dayMatterDate);
				d1.setTimeInMillis(dayMatterDate);
				dayOfyear1 = ChineseCalendarGB.dayOfChineseYear(
						d1.get(Calendar.YEAR), d1.get(Calendar.MONTH) + 1,
						d1.get(Calendar.DAY_OF_MONTH));
			}
			dayMatter.setDate(dayMatterDate);
			d1.setTimeInMillis(dayMatterDate);
		}

		return isFuture;
	}

	public static String getDayMatterDesc(Context ctx, DayMatter dayMatter) {
		StringBuilder matterDesc = new StringBuilder("#"
				+ ctx.getString(R.string.app_name) + "#");

		int duration = DateUtils.getDaysBetween(dayMatter).get("Days");
		String dataStr = getMatterDateString(ctx, dayMatter,
				ctx.getString(R.string.matter_calendar_gregorian_date_format_1));
		String format = null;
		if (duration >= 0) {
			format = ctx.getString(R.string.matter_future_format);
		} else {
			format = ctx.getString(R.string.matter_past_format);
		}
		matterDesc.append(String.format(format, dayMatter.getMatter() + "("
				+ dataStr + ")"));
		matterDesc.append(Math.abs(duration));
		matterDesc.append(ctx.getString(R.string.matter_unit));

		return matterDesc.toString();
	}

	public static boolean showDayMatter(Context ctx, DayMatter dayMatter,TextView titleTip,
			TextView titleView, TextView dateView, TextView numView,
			ImageView unitView) {

		int duration = DateUtils.getDaysBetween(dayMatter).get("Days");
		// System.out.println("vk2013 duration: "+duration);
		String format = null;
		if(titleTip != null){
			if (duration >= 0) {
				titleTip.setText(ctx.getString(R.string.distance));
			} else {
				titleTip.setText(ctx.getString(R.string.past));
			}
			if (titleView != null) {
				titleView.setText(dayMatter.getMatter());
			}
		}else{
			if (duration >= 0) {
				format = ctx.getString(R.string.matter_future_format);
			} else {
				format = ctx.getString(R.string.matter_past_format);
			}
			if (titleView != null) {
				titleView.setText(String.format(format, dayMatter.getMatter()));
			}
		}
		

		if (numView != null) {
			numView.setText("" + Math.abs(duration));
		}

		if (dateView != null) {
			String dataStr = getMatterDateString(ctx, dayMatter);
//
//			dateView.setText(String.format(
//					ctx.getResources().getString(
//							R.string.matter_top_date_format), dataStr));
			dateView.setText(dataStr);
		}

		if (unitView != null) {
			if (Math.abs(duration) > 1) {
				unitView.setImageResource(R.drawable.days);
			} else {
				unitView.setImageResource(R.drawable.day);
			}
		}
		return duration >= 0;
	}

	public static String getMatterDateString(Context ctx, DayMatter dayMatter) {
		return getMatterDateString(ctx, dayMatter,
				ctx.getString(R.string.matter_calendar_gregorian_date_format));
	}

	public static String getMatterDateString(Context ctx, DayMatter dayMatter,String format) {
		return getMatterDateStringResult(ctx, dayMatter, format);
	}

	public static String getMatterDateString(Context ctx, long date,DayMatter dayMatter) {
		return getMatterDateStringResult(ctx, dayMatter,ctx.getString(R.string.matter_calendar_gregorian_date_format));
	}

	public static String getMatterDateStringResult(Context ctx,DayMatter dayMatter, String format) {
		long date = dayMatter.getDate();
		int calendar = dayMatter.getCalendar();
		if(dayMatter.getRepeat()!=0){
			boolean isPassed = DateTimeTool.getMillisecondByDateTime(DateTimeTool.getCurrentDateTimeString("yyyy-MM-dd")) > date;
			if(isPassed){
				date = dayMatter.getNextRemindTime();
			}
		}
		StringBuilder dataStr = new StringBuilder("");
		if (calendar == IContants.CALENDAR_GREGORIAN) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			dataStr.append(dateFormat.format(new Date(date)));
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(date);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DAY_OF_MONTH);

			String[] lunarMonths = ctx.getResources().getStringArray(
					R.array.date_picker_calendar_month);
			String[] lunarDays = ctx.getResources().getStringArray(
					R.array.date_picker_calendar_day);

			dataStr.append(String.format(
					ctx.getString(R.string.matter_calendar_lunar_date_format),
					year));
			dataStr.append(lunarMonths[month]).append(lunarDays[day]);
		}

		return dataStr.toString();
	}
	public static String getMatterDateStringForAddMatterView(Context ctx,DayMatter dayMatter, String format) {
		long date = dayMatter.getDate();
		int calendar = dayMatter.getCalendar();
		StringBuilder dataStr = new StringBuilder("");
		if (calendar == IContants.CALENDAR_GREGORIAN) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			dataStr.append(dateFormat.format(new Date(date)));
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(date);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DAY_OF_MONTH);

			String[] lunarMonths = ctx.getResources().getStringArray(
					R.array.date_picker_calendar_month);
			String[] lunarDays = ctx.getResources().getStringArray(
					R.array.date_picker_calendar_day);

			dataStr.append(String.format(
					ctx.getString(R.string.matter_calendar_lunar_date_format),
					year));
			dataStr.append(lunarMonths[month]).append(lunarDays[day]);
		}

		return dataStr.toString();
	}
	/*
	 * 获取农历日期
	 */
	public static String getlunarDays(Context context, String dateStr){
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateTimeTool.convertToDate(dateStr));
		
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String[] lunarDays = context.getResources().getStringArray(
				R.array.date_picker_calendar_day);
		return lunarDays[day];
	}

}
