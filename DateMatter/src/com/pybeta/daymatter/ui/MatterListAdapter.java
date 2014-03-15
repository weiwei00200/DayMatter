
package com.pybeta.daymatter.ui;

import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.utils.DateUtils;
import com.pybeta.ui.utils.ItemListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class MatterListAdapter extends ItemListAdapter<DayMatter, MatterView> {

	private Context mContext;
	
	public MatterListAdapter(Context ctx) {
		super(R.layout.view_list_item, LayoutInflater.from(ctx));
		mContext = ctx;
	}

	@Override
	protected void update(int position, MatterView view, DayMatter item) {
        boolean future = DateUtils.showDayMatter(mContext, item, null, view.mMatter, null, view.mDays, null);
        if (future) {
        	view.mItem.setBackgroundResource(R.drawable.item_future_bg_selector);
        } else {
        	view.mItem.setBackgroundResource(R.drawable.item_past_bg_selector);
        }
	}

	@Override
	protected MatterView createView(View view) {
		return new MatterView(view);
	}

}
