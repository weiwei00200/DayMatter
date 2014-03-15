package com.pybeta.daymatter.utils;

import android.util.Log;

import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.tool.DateTimeTool;

/**
 * ClassName: DealRepeatUtil
 * Function: TODO
 * date: 2014-3-13 下午8:01:08
 * @author Sammie.Zhang
 */
public class DealRepeatUtil {
	/**
	 * 处理重复周期时间
	 * @param dayMatter
	 * @return
	 */
	public static long dealRepeatDateTime(DayMatter dayMatter,boolean toNextRemind){
		long nextRemindTime =0;
		//周期提醒时间
		String nowDate = DateTimeTool.getCurrentDateTimeString("yyyy-MM-dd");
		String targetDate = DateTimeTool.convertMillisToDateString(dayMatter.getDate(), "yyyy-MM-dd");
		String []nowDates = nowDate.split("-");
		String []targetDates = targetDate.split("-");
		
		boolean isPassed = DateTimeTool.getMillisecondByDateTime(nowDate)>dayMatter.getDate();
		switch (dayMatter.getRepeat()) {
		case 0:
			nextRemindTime = dayMatter.getDate();
			break;
		case 1://周
			int plusWeek = 0;
			if(isPassed){
				plusWeek = DateTimeTool.getWeekSpace(targetDate, nowDate);
				if(DateTimeTool.getDayCountOfWeek(DateTimeTool.getMillisecondByDateTime(nowDate))!=DateTimeTool.getDayCountOfWeek(dayMatter.getDate())){
					plusWeek++;
				}
			}
			nextRemindTime = isPassed ? DateTimeTool.getModifyWeekString(dayMatter.getDate(), "yyyy-MM-dd", plusWeek+(toNextRemind?1:0)):dayMatter.getDate();
			break;
		case 2://月
			int plusMonth=0;
			if(isPassed){
				plusMonth = (int)DateTimeTool.getMonthSpace(targetDate,nowDate);
			}
			nextRemindTime = isPassed ? DateTimeTool.getModifyMonthString(dayMatter.getDate(), "yyyy-MM-dd", plusMonth+(toNextRemind?1:0)):dayMatter.getDate();
			break;
		case 3://年
			int plusYear = 0;
			if(isPassed){
				if(Integer.parseInt(nowDates[1]) < Integer.parseInt(targetDates[1])){
					plusYear = (Integer.parseInt(targetDates[0])-Integer.parseInt(nowDates[0]));
				}else if(Integer.parseInt(nowDates[1]) == Integer.parseInt(targetDates[1])){
					if(Integer.parseInt(nowDates[2]) <= Integer.parseInt(targetDates[2])){
						plusYear = Integer.parseInt(nowDates[0])-(Integer.parseInt(targetDates[0]));
					}else{
						plusYear = Integer.parseInt(nowDates[0])-(Integer.parseInt(targetDates[0]))+1;
					}
				}else{
					plusYear = Integer.parseInt(nowDates[0])-(Integer.parseInt(targetDates[0]))+1;
				}
			}
			nextRemindTime = isPassed ? DateTimeTool.getModifyYearString(dayMatter.getDate(), "yyyy-MM-dd", Math.abs(plusYear+(toNextRemind?1:0))):dayMatter.getDate();
			break;
		default:
			break;
		}
		return nextRemindTime;
	}
}
