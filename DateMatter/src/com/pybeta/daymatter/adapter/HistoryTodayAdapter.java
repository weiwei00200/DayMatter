package com.pybeta.daymatter.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.pybeta.daymatter.HistoryToday;
import com.pybeta.ui.utils.ItemListAdapter;
import com.pybeta.widget.HistoryTodayView;

public class HistoryTodayAdapter extends ItemListAdapter<HistoryToday, HistoryTodayView> {

	public HistoryTodayAdapter(int viewId, LayoutInflater inflater) {
		super(viewId, inflater);
	}

	@Override
	protected void update(int position, HistoryTodayView view, HistoryToday item) {
		view.mContent.setText(item.getContent());
	}

	@Override
	protected HistoryTodayView createView(View view) {
		return new HistoryTodayView(view);
	}

}