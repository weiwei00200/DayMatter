package com.pybeta.widget;

import android.view.View;
import android.widget.TextView;

import com.pybeta.daymatter.R;
import com.pybeta.ui.utils.ItemView;

public class HistoryTodayView extends ItemView{
	public TextView mContent;

	public HistoryTodayView(View view) {
		super(view);
		mContent = textView(view, R.id.tv_history_content);
	}
}
