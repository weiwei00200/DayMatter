package com.pybeta.daymatter.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pybeta.daymatter.R;
import com.pybeta.daymatter.R.anim;
import com.pybeta.daymatter.R.id;
import com.pybeta.daymatter.R.layout;
import com.pybeta.daymatter.R.string;
import com.pybeta.ui.widget.UcTitleBar;
import com.pybeta.ui.widget.UcTitleBar.ITitleBarListener;
import com.umeng.fb.UMFeedbackService;

public class AboutRecActivity extends Activity {
	private UcTitleBar mTitleBar = null;
	private TextView mTvVersion = null;
	private RelativeLayout mLayoutFeedBack = null;
	private RelativeLayout mLayoutWebsite = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_rec);
		mTitleBar = (UcTitleBar)this.findViewById(R.id.uc_titlebar);
		mTvVersion = (TextView)this.findViewById(R.id.tv_version_about);
		mLayoutFeedBack = (RelativeLayout)this.findViewById(R.id.layout_feedback_about);
		mLayoutWebsite = (RelativeLayout)this.findViewById(R.id.layout_appweb_about);
		
		initTitleBar();
		initContentView();
	}
	
	private void initTitleBar(){
		mTitleBar.setTitleText(getResources().getString(R.string.title_activity_about));
		mTitleBar.setViewVisible(false, true, false, false, false, false, false, false);
		mTitleBar.setListener(new ITitleBarListener() {
			@Override
			public void shareClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void menuClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void feedBackClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void editClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void completeClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void closeClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void backClick(Object obj) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
			@Override
			public void addClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void initContentView(){
		mTvVersion.setText(getVersionCode()+"");
		mLayoutFeedBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UMFeedbackService.openUmengFeedbackSDK(AboutRecActivity.this);
				finish();
			}
		});
		mLayoutWebsite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent= new Intent();        
			    intent.setAction("android.intent.action.VIEW");
			    Uri content_url = Uri.parse("http://"+getResources().getString(R.string.website068));   
			    intent.setData(content_url);  
			    startActivity(intent);
			}
		});
	}
	private String getVersionCode(){
		String versionCode = "";
        try {
			PackageInfo pi = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			versionCode = pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return versionCode;
	}
}
