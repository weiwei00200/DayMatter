package com.pybeta.daymatter.ui;

import com.pybeta.daymatter.IContants;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LockBaseActivity extends BaseActivity {

	private final static int REQUEST_UNLOCK_PATTERN = 10000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences prefs = getSharedPreferences(IContants.LOCK_PREFERENCES, 0);
		String currentPattern = prefs.getString(LockPatternActivity._Pattern, null);
		if (currentPattern != null) {
			Intent intent = new Intent(this, LockPatternActivity.class);
			intent.putExtra(LockPatternActivity._Mode, LockPatternActivity.LPMode.ComparePattern);
			startActivityForResult(intent, REQUEST_UNLOCK_PATTERN);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			if (requestCode == REQUEST_UNLOCK_PATTERN) {
				finish();
			} 
		}
		super.onActivityResult(requestCode, resultCode, data);
	}	
}
