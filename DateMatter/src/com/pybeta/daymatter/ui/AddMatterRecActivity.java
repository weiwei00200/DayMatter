package com.pybeta.daymatter.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pybeta.daymatter.R;
import com.pybeta.daymatter.tool.ExpandAnimation;

public class AddMatterRecActivity extends Activity {

//	private ListView mListView = null;
//	private AddMatterAdapter mAdapter = null;
	private LinearLayout mLayoutItemsDate = null;
	private LinearLayout mLayoutItemsCategory = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_matter_rec);
		mLayoutItemsDate = (LinearLayout)this.findViewById(R.id.layout_items_date);
		mLayoutItemsCategory = (LinearLayout)this.findViewById(R.id.layout_items_category);
//		mListView = (ListView)this.findViewById(R.id.listview_items);
		
		initView();
		initView1();
	}
	private void initView1(){
		ViewGroup vg = (ViewGroup)LayoutInflater.from(this).inflate(R.layout.layout_drop_category_listview_item, null);
		mLayoutItemsCategory.addView(vg);
		
		final LinearLayout toolbar = (LinearLayout)vg.findViewById(R.id.toolbar_category);
		RelativeLayout layoutTitle = (RelativeLayout)vg.findViewById(R.id.layout_title_category);
		layoutTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ExpandAnimation expandAni = new ExpandAnimation(toolbar, 100);
				toolbar.startAnimation(expandAni);
			}
		});
		
	}
	private void initView(){
		ViewGroup vg = (ViewGroup)LayoutInflater.from(this).inflate(R.layout.layout_drop_date_listview_item, null);
		mLayoutItemsDate.addView(vg);
		final LinearLayout toolbar = (LinearLayout)vg.findViewById(R.id.toolbar_date);
		RelativeLayout layoutTitle = (RelativeLayout)vg.findViewById(R.id.layout_title_date);
		layoutTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ExpandAnimation expandAni = new ExpandAnimation(toolbar, 100);
				toolbar.startAnimation(expandAni);
			}
		});
		
		
		
		
		
//		List<Integer> itemSourceIdList = new ArrayList<Integer>();
//		itemSourceIdList.add(R.layout.layout_drop_date_listview_item);
//		ArrayAdapter<String> listAdapter = new AddMatterAdapter(this, R.layout.layout_drop_date_listview_item);
//		for (int i = 0; i < 20; i++)
//			listAdapter.add("udini" + i);
//		
//		mListView.setAdapter(listAdapter);
//
//		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, final View view,
//					int position, long id) {
//
//				
//			}
//		});
	}
	
}
