package com.pybeta.daymatter.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.pybeta.daymatter.Config;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.bean.Codes;
import com.pybeta.daymatter.core.MatterApplication;
import com.sammie.common.util.SerializeManager;

public class WelActivity extends Activity {
	
	private MatterApplication mAppInfo = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wel);
		mAppInfo = (MatterApplication)getApplication();
		
        final Handler handler = new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		// TODO Auto-generated method stub
        		super.handleMessage(msg);
        		switch (msg.what) {
				case 0:
					//绘图密码
					Intent intent = new Intent(WelActivity.this, LockPatternActivity.class);
					intent.putExtra(LockPatternActivity._Mode, LockPatternActivity.LPMode.ComparePattern);
					startActivityForResult(intent, Codes.REQUEST_UNLOCK_PATTERN);
					//字符密码
//					Intent intentLock = new Intent(WelActivity.this, LockPasswordActivity.class);
//					Bundle bundle = new Bundle();
//					bundle.putBoolean("IsFromWelActivity", true);
//					intentLock.putExtras(bundle);
//					startActivity(intentLock);
//					overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
//					finish();
					break;
				case 1:
					Intent intentHome = new Intent(WelActivity.this, HomeRecActivity.class);
					startActivity(intentHome);
					overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
					finish();
					break;
				default:
					break;
				}
        	}
        };
        new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();
//				Object obj = SerializeManager.loadFile(Config.SerializePath_UserInfo);
				if(isHasPassword()){
//					mAppInfo.setPassword(obj.toString());
					msg.what = 0;//有密码
				}else{
//					mAppInfo.setPassword("");
					msg.what = 1;//没密码
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	private boolean isHasPassword(){
		SharedPreferences prefs = getSharedPreferences(IContants.LOCK_PREFERENCES, 0);
		String currentPattern = prefs.getString(LockPatternActivity._Pattern, null);
		if(currentPattern!=null){
			if(currentPattern.equals("")){
				return false;
			}
			return true;
		}
		return false;
	}
	
	@Override
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == Codes.REQUEST_UNLOCK_PATTERN) {
				Intent intentHome = new Intent(WelActivity.this, HomeRecActivity.class);
				startActivity(intentHome);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}	
	
}
