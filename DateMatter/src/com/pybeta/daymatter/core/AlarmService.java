package com.pybeta.daymatter.core;

import java.util.Calendar;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.utils.DateUtils;
import com.pybeta.daymatter.utils.DealRepeatUtil;
import com.pybeta.daymatter.utils.LogUtil;
import com.pybeta.daymatter.utils.UtilityHelper;

public class AlarmService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(intent != null){
		String matter = intent.getStringExtra("event");
		String time = intent.getStringExtra("time");
		int reqCode = -1;
		reqCode = intent.getIntExtra("reqCode", -1);
		String uid = intent.getStringExtra("uid");
		LogUtil.Sysout("AlarmService onStartCommand matter: "+matter+" , time: "+time+" ,reqCode: "+reqCode);
//		if(reqCode != -1)
//			showAlarm(reqCode);
		if(reqCode != -1 && uid != null)
			if(showAlarm(reqCode)){
				DateUtils.cancelAlarm(getApplicationContext(), reqCode);
			}
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		LogUtil.Sysout("AlarmService onCreate");
		super.onCreate();
	}
	

	private boolean showAlarm(int reqCode){
		DatabaseManager dataBaseMgr = DatabaseManager.getInstance(getApplicationContext());
		DayMatter matter = dataBaseMgr.query(reqCode);
		boolean showAlarmFlag = showAlarm(matter);
		if(matter.getRepeat()!=0){//如果有重复，返回false，不结束服务，继续等待下一个时间到来
			long nextRepeatTime = DealRepeatUtil.dealRepeatDateTime(matter,true);
			matter.setDate(nextRepeatTime);
			matter.setNextRemindTime(nextRepeatTime);
			DatabaseManager databaseMgr = DatabaseManager.getInstance(this);
			databaseMgr.update(matter);
			return false;
		}else{
			return showAlarmFlag;
		}
	}
	
	private boolean showAlarm(DayMatter matter){
		boolean ret = true;
		LogUtil.Sysout("AlarmService showAlarm matter: "+matter);
		if(matter == null)
			return false;
		int hour = matter.getHour();
		int min = matter.getMin();
		
		Calendar mCalendar = Calendar.getInstance();
		int curHour = mCalendar.get(Calendar.HOUR_OF_DAY);
		int curMin = mCalendar.get(Calendar.MINUTE);
		if(hour != curHour || min != curMin){
			return ret;
		}
		
		int distance = DateUtils.getDaysBetween(matter).get("Days");
		if ((matter.getRemind() == 1 && distance == 0) || (matter.getRemind() == 2 && distance == 1)){
			String contentText = getResources().getString(R.string.notification_content_text);
	//		if (size == 1) {
	//			DayMatter matter = notifyMatterList.get(0);
			int titleId = matter.getRemind() == 1 ? R.string.notification_matter_today : R.string.notification_matter_tomorrow;
			String contentTitle = String.format(getResources().getString(titleId), matter.getMatter());
			UtilityHelper.showNotification(getApplicationContext(), contentTitle, contentText, 0,matter.getId());
	//		} else {
	//			UtilityHelper.showNotification(ctx, ctx.getString(R.string.app_name), contentText, size);
	//		}
		}
		return ret;
	}
	
}
