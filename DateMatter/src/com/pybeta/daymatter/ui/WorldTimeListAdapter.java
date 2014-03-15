// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.pybeta.daymatter.ui;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.pybeta.daymatter.R;
import com.pybeta.daymatter.WorldTimeZone;
import com.pybeta.daymatter.utils.LogUtil;
import com.pybeta.ui.utils.ItemListAdapter;

// Referenced classes of package com.pybeta.daymatter.ui:
//            WorldTimeView

public class WorldTimeListAdapter extends ItemListAdapter<WorldTimeZone, WorldTimeView>
{
    class TimeHolder
    {

        String date;
        String day;
        String dayOfMonth;
        int hour;
        int mins;
        String month;
        String timeInShort;

    }


    public WorldTimeListAdapter(Context context)
    {
        super(R.layout.worldtime_list_item, LayoutInflater.from(context));
        mContext = context;
    }

    private TimeHolder getTimeHolder(Calendar calendar, long l, Locale locale)
    {
        TimeHolder timeholder = new TimeHolder();
        calendar.setTimeInMillis(l);
        int i = calendar.get(5);
        timeholder.dayOfMonth = calendar.getDisplayName(Calendar.DAY_OF_MONTH, Calendar.ALL_STYLES, locale);
        timeholder.month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, locale);
        timeholder.day = calendar.getDisplayName(Calendar.DAY_OF_WEEK,  Calendar.SHORT, locale);
        if(locale.equals(Locale.CHINA) || locale.equals(Locale.TAIWAN) || locale.equals(Locale.CHINESE))
            timeholder.date = (new StringBuilder(String.valueOf(timeholder.month))).append(" ").append(i).append("日 ").append(timeholder.day).toString();
        else
            timeholder.date = (new StringBuilder(String.valueOf(timeholder.month))).append(" ").append(i).append(" ").append(timeholder.day).toString();
        timeholder.hour = calendar.get(Calendar.HOUR_OF_DAY);
        timeholder.mins = calendar.get(Calendar.MINUTE);
        String mins = timeholder.mins+"";
        if(timeholder.mins < 10){
        	mins = "0"+timeholder.mins;
        }
        	
       
        timeholder.timeInShort = (new StringBuilder(String.valueOf(timeholder.hour))).append(":").append(mins).toString();
        return timeholder;
    }

    protected WorldTimeView createView(View view)
    {
        return new WorldTimeView(view);
    }

    protected void update(int i, WorldTimeView worldtimeview, WorldTimeZone worldtimezone)
    {
        int j = TimeZone.getDefault().getRawOffset();
        Calendar calendar = Calendar.getInstance();
        long l = calendar.getTimeInMillis();
        long l1 = l - (long)j;
        long l2 = l1 + (long)worldtimezone.getRawOffset();
        LogUtil.Sysout((new StringBuilder("nowTimeInMillis: ")).append(l).append(" ,gmt0TimeInMillis: ").append(l1).append(" ,itemTimeInMillis: ").append(l2).toString());
        Locale locale = mContext.getResources().getConfiguration().locale;
        locale.getLanguage();
        String s = locale.getCountry();
        locale.getDisplayCountry();
        calendar.setTimeInMillis(l2);
        Locale locale1;
        String s1;
        String s2;
        TimeHolder timeholder;
        if(s.equalsIgnoreCase("cn"))
        {
            s1 = worldtimezone.getCityNameZH();
            s2 = worldtimezone.getCountryNameZH();
            locale1 = Locale.CHINA;
        } else
        if(s.equalsIgnoreCase("tw"))
        {
            locale1 = Locale.TAIWAN;
            s1 = worldtimezone.getCityNameTW();
            s2 = worldtimezone.getCountryNameTW();
        } else
        {
            locale1 = Locale.US;
            s1 = worldtimezone.getCityName();
            s2 = worldtimezone.getCountryName();
        }
        timeholder = getTimeHolder(calendar, l2, locale1);
        worldtimeview.mWorldTime_tv_cityName.setText(s1);
        worldtimeview.mWorldTime_tv_countryName.setText(s2);
        worldtimeview.mWorldTime_tv_time.setText(timeholder.timeInShort);
        worldtimeview.mWorldTime_tv_date.setText(timeholder.date);
        worldtimeview.mWorldTime_tv_gmt.setText(worldtimezone.getGMT());
    }


    private Context mContext;
}
