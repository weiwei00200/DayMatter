package com.pybeta.daymatter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.adapter.HolidayMatterAdapter.HolidayMatterView;
import com.pybeta.daymatter.bean.HolidayConllectionBean;
import com.pybeta.daymatter.bean.HolidayItemBean;
import com.pybeta.daymatter.utils.DateUtils;
import com.pybeta.ui.utils.ItemListAdapter;
import com.pybeta.ui.utils.ItemView;


public class HolidayMatterAdapter extends ItemListAdapter<DayMatter, HolidayMatterView> {

	private Context context;

	public HolidayMatterAdapter(Context context) {
		super(R.layout.holiday_list_item, LayoutInflater.from(context));
		this.context = context;
	}

	@Override
	protected void update(int position, HolidayMatterView view, DayMatter item) {
		HolidayItemBean bean = HolidayConllectionBean.getInstanceStatic().getHolidayById(item.getUid());
		if(item.getCategory()==1 && bean.getOnHolidayList().size()>0){
			view.holidayTag.setVisibility(View.VISIBLE);
		}else{
			view.holidayTag.setVisibility(View.INVISIBLE);
		}
		
		if(item.getCategory() == 0){
			view.categoryImg.setBackgroundColor(this.context.getResources().getColor(R.color.holiday_festival));
		}else if(item.getCategory() == 1){
			view.categoryImg.setBackgroundColor(this.context.getResources().getColor(R.color.holiday_holiday));
		}else if(item.getCategory() == 2){
			view.categoryImg.setBackgroundColor(this.context.getResources().getColor(R.color.holiday_solarterm));
		}
		item.setNextRemindTime(item.getDate());
		DateUtils.showDayMatter(context, item,null, null, view.date, view.nums, null);
		view.title.setText(item.getMatter());
	}

	@Override
	protected HolidayMatterView createView(View view) {
		return new HolidayMatterView(view);
	}
	
	public class HolidayMatterView extends ItemView {
		protected ImageView categoryImg;
		protected TextView title;
		protected TextView date;
		protected TextView nums;
		protected ImageView holidayTag;
			

		public HolidayMatterView(View view) {
			super(view);
			categoryImg = (ImageView) view.findViewById(R.id.img_category);
			title = (TextView) view.findViewById(R.id.tv_matter_title);
			date = (TextView) view.findViewById(R.id.tv_matter_date);
			nums = (TextView) view.findViewById(R.id.tv_matter_days);
			holidayTag = (ImageView)view.findViewById(R.id.img_holiday_tag);
		}
	}
}

