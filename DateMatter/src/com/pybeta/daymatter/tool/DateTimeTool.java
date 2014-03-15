package com.pybeta.daymatter.tool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimeTool {
	/**   
	 * @Title: DateTimeTool.java 
	 * @Package com.gw.tools 
	 * @Description:  
	 * @author Sammie.Zhang
	 * @date 2013-9-26 下午5:49:49 
	 * @version V1.0   
	 */
	/**
	 * string转date
	 * @param dateStr
	 * @return
	 */
	public static Date convertToDate(String dateStr){
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 计算两日期相隔天数
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int getIntervalDays(Date fDate, Date oDate) {
       if (null == fDate || null == oDate) {
           return -1;
       }
       long intervalMilli = oDate.getTime() - fDate.getTime();
       return (int) (intervalMilli / (24 * 60 * 60 * 1000));
	}
	
	
	public static String toDateString(Date date, String formatString)
	{
		String retStr = "";
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat(formatString);
			retStr = sdf.format(date);
		} catch (Exception e){
			// TODO: handle exception
			e.printStackTrace();
		}
		return retStr;
	}
	public static String getCurrentDateTimeString()
	{
		SimpleDateFormat timeFormater = new SimpleDateFormat("yyyy年MM月dd日  E");// 日志内容的时间格式
		return timeFormater.format(new java.util.Date().getTime());
	}
	public static String getCurrentDateTimeString(String format){
		SimpleDateFormat timeFormater = new SimpleDateFormat(format);// 日志内容的时间格式
		return timeFormater.format(new java.util.Date().getTime());
	}
	public static Date getCurrentDateTimeObject(){
		return new Date(System.currentTimeMillis());
	}
	
	public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK);
        if ((w-1) < 0)
            w = 0;
        return weekDays[w-1];
    }
	
	public static String getModifyDateTimeString(Date currentDateTime,int plusDate,String format){
	     Calendar calendar = new GregorianCalendar(); 
	     calendar.setTime(currentDateTime); 
	     calendar.add(calendar.DATE,plusDate);//把日期往后增加或减少
	     currentDateTime = calendar.getTime(); 
	     return toDateString(currentDateTime,format);
	}
	
	public static String getDateByString(String dateString,String formatStr){
		String dateTime = "";
		try {
			DateFormat format = new SimpleDateFormat(formatStr); 
			Date date = format.parse(dateString);
			dateTime = toDateString(date,formatStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateTime;
	}
	
	public static String getDateTimeByString(String sourceDatetime ,int plusDate,String format){
		Calendar c=Calendar.getInstance();  
		c.setTime(convertToDate(sourceDatetime));
		c.add(Calendar.DAY_OF_MONTH, plusDate);//当前日期加一天
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		SimpleDateFormat df2 = new SimpleDateFormat(format);//设置日期格式
		String strNow=df.format(c.getTime());
//		strNow=strNow+" "+sourceDatetime;
		Date date1 =null;
		String strRet="";
		try {
			 date1 = df1.parse(strNow);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		strRet=df2.format(date1);
		return strRet;
	}
	/**
	 * 毫秒转换
	 * @param millis 毫秒
	 * @param format 类型
	 * @return
	 */
	public static String convertMillisToDateString(long millis,String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(new Date(millis));
	}
	public static String getModifyYearString(String dateStr,String format ,int plusYearAmount){
		Date date = convertToDate(dateStr);
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, plusYearAmount);
		Date resultDate = cal.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		String resultDateStr = dateFormat.format(resultDate);
		return resultDateStr;
	}

	public static int getDayCountOfWeek(long millis){
		Date date = new Date(millis);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK); 
	}

	public static long getModifyYearString(long dateStr,String format ,int plusYearAmount){
		Date date = convertToDate(convertMillisToDateString(dateStr,"yyyy-MM-dd HH:mm:ss:sss"));
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, plusYearAmount);
		Date resultDate = cal.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		String resultDateStr = dateFormat.format(resultDate);
		return getMillisecondByDateTime(resultDateStr);
	}
	public static long getModifyMonthString(long dateStr,String format ,int plusMonthAmount){
		Date date = convertToDate(convertMillisToDateString(dateStr,"yyyy-MM-dd HH:mm:ss:sss"));
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, plusMonthAmount);
		Date resultDate = cal.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		String resultDateStr = dateFormat.format(resultDate);
		return getMillisecondByDateTime(resultDateStr);
	}
	public static long getModifyWeekString(long dateStr,String format ,int plusWeekAmount){
		Date date = convertToDate(convertMillisToDateString(dateStr,"yyyy-MM-dd HH:mm:ss:sss"));
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, plusWeekAmount*7);
		Date resultDate = cal.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		String resultDateStr = dateFormat.format(resultDate);
		return getMillisecondByDateTime(resultDateStr);
	}
	public static long getMillisecondByDateTime(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long millionSeconds = 0;
		try {
			millionSeconds = sdf.parse(str).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return millionSeconds;
	}
	
	/**
     * 得到两日期相差几个月
     * 
     * @param String
     * @return
     */
    public static long getMonthSpace(String startDate, String endDate) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        long monthday;
        try {
            Date startDate1 = f.parse(startDate);
            //开始时间与今天相比较
            Date endDate1 = new Date();

            Calendar starCal = Calendar.getInstance();
            starCal.setTime(startDate1);

            int sYear = starCal.get(Calendar.YEAR);
            int sMonth = starCal.get(Calendar.MONTH);
            int sDay = starCal.get(Calendar.DATE);

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(endDate1);
            int eYear = endCal.get(Calendar.YEAR);
            int eMonth = endCal.get(Calendar.MONTH);
            int eDay = endCal.get(Calendar.DATE);

            monthday = ((eYear - sYear) * 12 + (eMonth - sMonth));

            if (sDay < eDay) {
                monthday = monthday + 1;
            }
            return monthday;
        } catch (ParseException e) {
        	e.printStackTrace();
            monthday = 0;
        }
        return monthday;
    }
    
    public static int getWeekSpace(String startDate, String endDate){
    	int interval = 0;
    	try {
    		interval = getIntervalDays(convertToDate(startDate),convertToDate(endDate))/7;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return interval;
    }
}
