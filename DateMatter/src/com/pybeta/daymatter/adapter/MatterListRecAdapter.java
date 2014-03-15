package com.pybeta.daymatter.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.ui.MatterView;
import com.pybeta.daymatter.utils.DateUtils;
import com.pybeta.ui.utils.ItemListAdapter;

public class MatterListRecAdapter extends ItemListAdapter<DayMatter, MatterView> {
	private Context mContext;

	public MatterListRecAdapter(Context ctx) {
		super(R.layout.view_list_item_category, LayoutInflater.from(ctx));
		mContext = ctx;
	}

	@Override
	protected void update(int position, MatterView view, DayMatter item) {
//		if(item.getRepeat()!=0){
//			item.setDate(item.getNextRemindTime());
//		}
		boolean future = DateUtils.showDayMatter(mContext, item,view.mMatterTip, view.mMatter, view.mDate, view.mDays, null);
		switch (item.getCategory()) {
		case 0://默认
			view.mTypeLogo.setImageResource(R.drawable.menu_item_default);
			break;
		case 1://生活事件
			view.mTypeLogo.setImageResource(R.drawable.menu_item_life);
			break;
		case 2://工作事件
			view.mTypeLogo.setImageResource(R.drawable.menu_item_work);
			break;
		case 3://纪念日
			view.mTypeLogo.setImageResource(R.drawable.menu_item_memorilday);
			break;
		default://其他
			view.mTypeLogo.setImageResource(R.drawable.menu_item_other);
			break;
		}
		if (future) {
//			view.mItem.setBackgroundColor(Color.BLUE);
		} else {
//			view.mItem.setBackgroundColor(Color.BLUE);
		}
	}

	@Override
	protected MatterView createView(View view) {
		return new MatterView(view);
	}
}
