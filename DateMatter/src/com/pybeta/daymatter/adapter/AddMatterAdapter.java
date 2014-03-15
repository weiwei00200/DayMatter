package com.pybeta.daymatter.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pybeta.daymatter.R;

/**
 * ClassName: AddMatterAdapter
 * Function: TODO
 * date: 2014-3-14 上午11:45:18
 * @author Sammie.Zhang
 */
public class AddMatterAdapter extends ArrayAdapter<String>{
	private Context mContext;
	public AddMatterAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_drop_date_listview_item,
					null);
		}
		//((TextView) convertView.findViewById(R.id.tv_detail_title)).setText(getItem(position));

		// Resets the toolbar to be closed
		View toolbar = convertView.findViewById(R.id.toolbar_category);
		((LinearLayout.LayoutParams) toolbar.getLayoutParams()).bottomMargin = -50;
		toolbar.setVisibility(View.GONE);

		return convertView;
	}
//	private Context mContext = null;
//	private List<Integer> mLayoutList = null;
//	private LayoutInflater mInflater = null;
//	
////	public AddMatterAdapter(List<Integer> layoutList, Context context){
////		mLayoutList = layoutList;
////		mContext = context;
////		mInflater = LayoutInflater.from(mContext);
////	} 
//	public AddMatterAdapter(Context context, int textViewResourceId) {
//		super(context, textViewResourceId);
//		mContext = context;
//		mInflater = LayoutInflater.from(mContext);
//	}
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
////		switch (mLayoutList.get(position)) {
////		case R.layout.layout_drop_date_listview_item:
////			convertView = getDateView();
////			break;
////
////		default:
////			break;
////		}
//		convertView = getDateView();
//		return convertView;
//	}
//	
//	private View getDateView(){
//		View layout = mInflater.inflate(R.layout.layout_drop_date_listview_item, null);
//		LinearLayout toolBar = (LinearLayout)layout.findViewById(R.id.toolbar);
//		((LinearLayout.LayoutParams) toolBar.getLayoutParams()).bottomMargin = -50;
//		toolBar.setVisibility(View.GONE);
//		return layout;
//	}
	
}
