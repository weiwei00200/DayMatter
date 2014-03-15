
package com.pybeta.daymatter.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pybeta.daymatter.R;
import com.pybeta.ui.utils.ItemView;

public class MatterView extends ItemView {

    public View mItem;
    public TextView mMatterTip;
    public TextView mMatter;
    public TextView mDays;
    public TextView mDate;
    public ImageView mTypeLogo;
    
	public MatterView(View view) {
		super(view);
        mItem = view.findViewById(R.id.list_item);
        mMatter = (TextView) view.findViewById(R.id.day_matter_title);
        mDays = (TextView) view.findViewById(R.id.day_matter_days);
        mMatterTip = (TextView)view.findViewById(R.id.day_title_tip);
        mDate = (TextView)view.findViewById(R.id.tv_date_list_item);
        mTypeLogo = (ImageView)view.findViewById(R.id.img_list_item_category);
	}

}
