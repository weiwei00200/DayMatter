// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.pybeta.daymatter.ui;

import android.view.View;
import android.widget.TextView;

import com.pybeta.daymatter.R;
import com.pybeta.ui.utils.ItemView;

public class WorldTimeView extends ItemView
{

    public WorldTimeView(View view)
    {
        super(view);
        mWorldTime_tv_gmt = (TextView)view.findViewById(R.id.worldtime_tv_gmt);
        mWorldTime_tv_cityName = (TextView)view.findViewById(R.id.worldtime_tv_cityname);
        mWorldTime_tv_time = (TextView)view.findViewById(R.id.worldtime_tv_time);
        mWorldTime_tv_countryName = (TextView)view.findViewById(R.id.worldtime_tv_countryname);
        mWorldTime_tv_date = (TextView)view.findViewById(R.id.worldtime_tv_date);
    }

    public TextView mWorldTime_tv_cityName;
    public TextView mWorldTime_tv_countryName;
    public TextView mWorldTime_tv_date;
    public TextView mWorldTime_tv_gmt;
    public TextView mWorldTime_tv_time;
}
