package com.pybeta.daymatter.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.pybeta.daymatter.R;

public class SettingsFragement extends SherlockFragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getSherlockActivity().getSupportActionBar().setTitle(R.string.title_activity_settings);
		return inflater.inflate(R.layout.activity_holiday, null);
	}
}
