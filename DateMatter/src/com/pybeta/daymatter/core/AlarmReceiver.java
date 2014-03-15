package com.pybeta.daymatter.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.pybeta.daymatter.BuildConfig;
import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.utils.DateUtils;
import com.pybeta.daymatter.utils.LogUtil;
import com.pybeta.daymatter.utils.ToastUtil;
import com.pybeta.daymatter.utils.UtilityHelper;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
		
//        if (BuildConfig.DEBUG) {
//		ToastUtil.TextToast(ctx, "接到重启广播", Toast.LENGTH_SHORT);
        	System.out.println("AlarmReceiver: " + intent);
//        }
//		DatabaseManager dataBaseMgr = DatabaseManager.getInstance(ctx);
//		int reqCode = intent.getIntExtra("reqCode", -1);
//		DayMatter matter = dataBaseMgr.query(intent.getStringExtra("uid"));
//		Calendar mCalendar2 = Calendar.getInstance();
//		//System.out.println("vk2013 :reqCode:  "+reqCode+" , uid: "+intent.getStringExtra("uid")+" ,DATE: "+mCalendar2.getTime()+" ,time: "+mCalendar2.getTimeInMillis());
//		if(matter != null){
//			//System.out.println("vk2013 0: reqCode: "+reqCode+" ,matter.getId() "+matter.getId());
//			if(reqCode == matter.getId()){
//				//System.out.println("vk2013 1: ");
//				int distance = DateUtils.getDaysBetween(matter);
//				if ((matter.getRemind() == 1 && distance == 0) || (matter.getRemind() == 2 && distance == 1)){
//					boolean flag = true;
//					int hour = matter.getHour();
//					int min = matter.getMin();
//					//System.out.println("vk2013 2: ");
////					
//					Calendar mCalendar = Calendar.getInstance();
//					int nowHour = mCalendar.get(Calendar.HOUR_OF_DAY);
//					int nowMin = mCalendar.get(Calendar.MINUTE);
//					int nowSec = mCalendar.get(Calendar.SECOND);
////					int reqCode = intent.getIntExtra("reqCode", -1);
////					if(matter.getId() == reqCode)
////						notifyMatterList.add(matter);
////					else{
////						if(nowHour != hour || nowMin != min || nowSec != 0)
////							flag = false;
////						if(flag)
////							notifyMatterList.add(matter);
////					}
//					if(nowHour != hour || nowMin != min|| nowSec != 0)
//						flag = false;
//					if(flag){
//						//System.out.println("vk2013 3: ");
//						String contentText = ctx.getString(R.string.notification_content_text);
//						int titleId = matter.getRemind() == 1 ? R.string.notification_matter_today : R.string.notification_matter_tomorrow;
//						String contentTitle = String.format(ctx.getString(titleId), matter.getMatter());
//						UtilityHelper.showNotification(ctx, contentTitle, contentText, 0,reqCode);
//					}
////						else
////						//System.out.println("vk2013 4: ");
//				}
//				//else
//					//System.out.println("vk2013 5: ");
//				
//				
//			}
//			//else
//				//System.out.println("vk2013 6: ");
//		}
		//else
			//System.out.println("vk2013 7: ");
			
//		List<DayMatter> notifyMatterList = new ArrayList<DayMatter>();
//		List<DayMatter> matterList = dataBaseMgr.queryAll(0);
//		for (DayMatter matter : matterList) {
//			int distance = DateUtils.getDaysBetween(matter);
//			if ((matter.getRemind() == 1 && distance == 0) || (matter.getRemind() == 2 && distance == 1)){
//				boolean flag = true;
//				int hour = matter.getHour();
//				int min = matter.getMin();
//				
//				Calendar mCalendar = Calendar.getInstance();
//				int nowHour = mCalendar.get(Calendar.HOUR_OF_DAY);
//				int nowMin = mCalendar.get(Calendar.MINUTE);
//				int nowSec = mCalendar.get(Calendar.SECOND);
//				int reqCode = intent.getIntExtra("reqCode", -1);
//				if(matter.getId() == reqCode)
//					notifyMatterList.add(matter);
//				else{
//					if(nowHour != hour || nowMin != min || nowSec != 0)
//						flag = false;
//					if(flag)
//						notifyMatterList.add(matter);
//				}
//			}
//		}
//		
//		int size = notifyMatterList.size();
//		if (size > 0) {
//			String contentText = ctx.getString(R.string.notification_content_text);
//			if (size == 1) {
//				DayMatter matter = notifyMatterList.get(0);
//				int titleId = matter.getRemind() == 1 ? R.string.notification_matter_today : R.string.notification_matter_tomorrow;
//				String contentTitle = String.format(ctx.getString(titleId), matter.getMatter());
//				UtilityHelper.showNotification(ctx, contentTitle, contentText, 0);
//			} else {
//				UtilityHelper.showNotification(ctx, ctx.getString(R.string.app_name), contentText, size);
//			}
//		}
	}

}
