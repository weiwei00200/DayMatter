// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.pybeta.daymatter.ui;

import android.content.Context;

import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.WorldTimeZone;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.util.AsyncLoader;
import java.util.List;

public class WorldTimeListLoader extends AsyncLoader<List<WorldTimeZone>>
{

    public WorldTimeListLoader(Context context, int i)
    {
        super(context);
        mCategory = 0;
        mCategory = i;
        mContext = context;
    }


    public List<WorldTimeZone> loadInBackground()
    {
        return DatabaseManager.getInstance(getContext()).queryAllWorldTime(mCategory);
    }

    private int mCategory;
    private Context mContext;
}
