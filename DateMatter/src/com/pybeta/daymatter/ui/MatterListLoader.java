package com.pybeta.daymatter.ui;

import java.util.List;

import android.content.Context;

import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.core.DataManager;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.util.AsyncLoader;

public class MatterListLoader extends AsyncLoader<List<DayMatter>> {

	private Context mContext;
    private int mCategory = 0;
    
    public MatterListLoader(Context context, int category) {
        super(context);
        mCategory = category;
        mContext = context;
    }

    @Override
    public List<DayMatter> loadInBackground() {
    	List<DayMatter> matterList = DatabaseManager.getInstance(getContext()).queryAll(mCategory);
    	return DataManager.getInstance(mContext).sortMatters(matterList);
    }
    
}
