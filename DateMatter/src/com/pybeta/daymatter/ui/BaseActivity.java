package com.pybeta.daymatter.ui;

import com.actionbarsherlock.app.SherlockFragmentActivity;
//import com.google.ads.AdRequest;
//import com.google.ads.AdSize;
//import com.google.ads.AdView;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.core.MatterApplication;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.widget.LinearLayout;

public abstract class BaseActivity extends SherlockFragmentActivity {

//	protected AdView mAdView = null;
//	protected LinearLayout mAdLayout = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		long startTic = System.currentTimeMillis();
		while (!MatterApplication.isInitialized()
				&& !MatterApplication.waitInitTimeOut(System.currentTimeMillis(), startTic)) {
		}
	}
	
	@Override
	public void setContentView(int layoutResId) {
		super.setContentView(layoutResId);
//		loadAd();
	}

	/**
	 * ¹ã¸æ
	 */
//	protected void loadAd() {
//		mAdLayout = (LinearLayout)this.findViewById(R.id.adView);
//        if (mAdLayout != null) {
//        	mAdView = new AdView(this, AdSize.BANNER, "a14fd8f6f7b69dc");
////        	mAdView.loadAd(new AdRequest().addTestDevice("328963CEE2031CB173F15FCE72420FD4"));
//        	mAdView.loadAd(new AdRequest());
//        	mAdLayout.addView(mAdView);
//        }
//	}
	
	@Override
	protected void onDestroy() {
//        if (mAdLayout != null) {
//        	mAdLayout.removeAllViews();
//        	if (mAdView != null) {
//        		mAdView.destroy();
//        	}
//        }
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
}
