package com.pybeta.daymatter.ui;

import java.io.File;
import java.util.Locale;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.pybeta.daymatter.Config;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.bean.Codes;
import com.pybeta.daymatter.core.DataManager;
import com.pybeta.daymatter.core.MatterApplication;
import com.pybeta.daymatter.tool.BackupRestoreTool;
import com.pybeta.daymatter.utils.UtilityHelper;
import com.pybeta.ui.widget.CustomDialog;
import com.pybeta.ui.widget.UcTitleBar;
import com.pybeta.util.BackupToGoogleDriveTask;
import com.pybeta.util.DMToast;
import com.pybeta.util.RestoreFromDriveTask;
import com.pybeta.util.RestoreFromDriveTask.IRestoreFromDriveListener;

public class SettingRecActivity extends Activity implements OnClickListener{

	private UcTitleBar mTitleBar = null;
	private LinearLayout mLayoutSort,mLayoutLanguage,mLayoutCategory,mLayoutBackup,mLayoutRestore,mLayoutLockByInput,mLayoutLockByPic,mLayoutUnlock,mLayoutAbout = null;
	private RelativeLayout mLayoutRemind = null;
	private TextView mTvUnLock = null;
	private TextView mTvLockByInput = null;
	private TextView mTvLockByPic = null;
	private CheckBox mCbStatusRemind = null;
	
	private MatterApplication mAppInfo = null;
	private GoogleAccountCredential mDriveCredential = null;
	private Drive mDriveService = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_rec);
		mAppInfo = (MatterApplication)getApplication();
		mTitleBar = (UcTitleBar)this.findViewById(R.id.uc_titlebar);
		mLayoutSort = (LinearLayout)this.findViewById(R.id.layout_sort);
		mLayoutLanguage = (LinearLayout)this.findViewById(R.id.layout_language);
		mLayoutCategory = (LinearLayout)this.findViewById(R.id.layout_category);
		mLayoutBackup = (LinearLayout)this.findViewById(R.id.layout_backup);
		mLayoutRestore = (LinearLayout)this.findViewById(R.id.layout_restore);
		mLayoutLockByInput = (LinearLayout)this.findViewById(R.id.layout_lock_by_input);
		mLayoutLockByPic = (LinearLayout)this.findViewById(R.id.layout_lock_by_pic);
		mLayoutUnlock = (LinearLayout)this.findViewById(R.id.layout_unlock);
		mLayoutAbout = (LinearLayout)this.findViewById(R.id.layout_about);
		mLayoutRemind = (RelativeLayout)this.findViewById(R.id.layout_statusbar_remind);
		mCbStatusRemind = (CheckBox)this.findViewById(R.id.checkbox_remind);
		mTvUnLock = (TextView)this.findViewById(R.id.tv_unlock);
		mTvLockByInput = (TextView)this.findViewById(R.id.tv_lock_by_input);
		mTvLockByPic = (TextView)this.findViewById(R.id.tv_lock_by_pic);
		mLayoutSort.setOnClickListener(this);
		mLayoutLanguage.setOnClickListener(this);
		mLayoutCategory.setOnClickListener(this);
		mLayoutBackup.setOnClickListener(this);
		mLayoutRestore.setOnClickListener(this);
		mLayoutLockByInput.setOnClickListener(this);
		mLayoutAbout.setOnClickListener(this);
		mLayoutRemind.setOnClickListener(this);
		mLayoutLockByPic.setOnClickListener(this);
		
		initTitleBar();
	}

	private void initTitleBar(){
    	mTitleBar.setTitleText(getResources().getString(R.string.menu_settings));
    	mTitleBar.setViewVisible(false, true, false, false, false, false, false, false);
    	mTitleBar.setListener(new UcTitleBar.ITitleBarListener() {
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
			public void backClick(Object obj) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
			@Override
			public void addClick(Object obj) {
				// TODO Auto-generated method stub
			}
			@Override
			public void closeClick(Object obj) {
				// TODO Auto-generated method stub
			}
		});
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initViewStatus();
	}
	//拿出配置，设置界面元素
	private void initViewStatus(){
		//状态栏提醒项
		mCbStatusRemind.setClickable(false);
		if(UtilityHelper.getBarNotifySettings(this)){
			mCbStatusRemind.setChecked(true);
		}else{
			mCbStatusRemind.setChecked(false);
		}
		//锁屏项
		//字符密码
//		if(!mAppInfo.getPassword().equals("")){
//			mTvLockByInput.setText(getResources().getString(R.string.modify_password_input));
//			mTvUnLock.setTextColor(getResources().getColor(R.color.black));
//			mLayoutUnlock.setOnClickListener(this);
//		}else{
//			mTvLockByInput.setText(getResources().getString(R.string.create_password_input));
//			mTvUnLock.setTextColor(getResources().getColor(R.color.list_line));
//			mLayoutUnlock.setOnClickListener(null);
//		}
		//绘图密码
		if(isHasPassword()){
			mTvLockByPic.setText(getResources().getString(R.string.change_lock));
			mTvUnLock.setTextColor(getResources().getColor(R.color.black));
			mLayoutUnlock.setOnClickListener(this);
		}else{
			mTvLockByPic.setText(getResources().getString(R.string.set_lock));
			mTvUnLock.setTextColor(getResources().getColor(R.color.list_line));
			mLayoutUnlock.setOnClickListener(null);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == mLayoutSort){
			showSortDialog();
		}else if(v == mLayoutLanguage){
			showLanguageDialog();
		}else if(v == mLayoutCategory){
			turnToSetCategory();
		}else if(v == mLayoutBackup){
			showBackupDialog();
		}else if(v == mLayoutRestore){
			showRestoreDialog();
		}else if(v == mLayoutLockByInput){
			Intent intent = new Intent(SettingRecActivity.this,LockPasswordActivity.class);
			Bundle bundle = new Bundle();
			bundle.putBoolean("IsFromWelActivity", false);
			intent.putExtras(bundle);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(v == mLayoutLockByPic){
			
			Intent intent = new Intent(this, LockPatternActivity.class);
			if (isHasPassword()) {
				intent.putExtra(LockPatternActivity._Mode, LockPatternActivity.LPMode.ComparePattern);
				startActivityForResult(intent, Codes.REQUEST_LOCK_CHANGE);
			} else {
				intent.putExtra(LockPatternActivity._Mode, LockPatternActivity.LPMode.CreatePattern);
				startActivity(intent);
			}
		}else if(v == mLayoutUnlock){
//			File file = new File(Config.SerializePath_UserInfo);
//			if(file.exists()){
//				file.delete();
//			}
//			mAppInfo.setPassword("");
			Intent intent = new Intent(this, LockPatternActivity.class);
			intent.putExtra(LockPatternActivity._Mode, LockPatternActivity.LPMode.ComparePattern);
			startActivityForResult(intent, Codes.REQUEST_LOCK_DISABLE);
		}else if(v == mLayoutAbout){
			Intent intent = new Intent(SettingRecActivity.this,AboutRecActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(v == mLayoutRemind){
			if(mCbStatusRemind.isChecked()){
				mCbStatusRemind.setChecked(false);
				UtilityHelper.setBarNotifySettings(SettingRecActivity.this,false);
			}else{
				mCbStatusRemind.setChecked(true);
				UtilityHelper.setBarNotifySettings(SettingRecActivity.this,true);
			}
			UtilityHelper.showStartBarNotification(this);
		}
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
	private void showSortDialog(){
		final CustomDialog dialog = new CustomDialog(SettingRecActivity.this, R.layout.layout_sort_dialog, R.style.SyncDialog);
		dialog.show();
		((Button)dialog.findViewById(R.id.btn_cancel)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		RadioGroup rg = (RadioGroup)dialog.findViewById(R.id.radio_group);
		int sortIndex = UtilityHelper.getSortTypeIndex(SettingRecActivity.this);
		rg.check(sortIndex==0 ? R.id.rb_default : (sortIndex==1? R.id.rb_time:R.id.rb_days));
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_default:
					UtilityHelper.setSortTypeIndex(SettingRecActivity.this, 0);
					break;
				case R.id.rb_time:
					UtilityHelper.setSortTypeIndex(SettingRecActivity.this, 1);
					break;
				case R.id.rb_days:
					UtilityHelper.setSortTypeIndex(SettingRecActivity.this, 2);
					break;
				default:
					break;
				}
				dialog.dismiss();
			}
		});
	}
	private void showLanguageDialog(){
		final CustomDialog dialog = new CustomDialog(SettingRecActivity.this, R.layout.layout_language_dialog, R.style.SyncDialog);
		dialog.show();
		((Button)dialog.findViewById(R.id.btn_cancel)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		RadioGroup rg = (RadioGroup)dialog.findViewById(R.id.radio_group);
		Locale locale = UtilityHelper.getCurrentLocale(SettingRecActivity.this);
		rg.check(locale == Locale.getDefault() ? R.id.rb_default:(locale == Locale.SIMPLIFIED_CHINESE ? R.id.rb_simplified : (locale == Locale.TRADITIONAL_CHINESE ? R.id.rb_traditional : R.id.rb_english)));
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_default:
					UtilityHelper.setLocale(SettingRecActivity.this, null);
					break;
				case R.id.rb_simplified:
				UtilityHelper.setLocale(SettingRecActivity.this, Locale.SIMPLIFIED_CHINESE);
					break;
				case R.id.rb_traditional:
					UtilityHelper.setLocale(SettingRecActivity.this, Locale.TRADITIONAL_CHINESE);
					break;
				case R.id.rb_english:
					UtilityHelper.setLocale(SettingRecActivity.this, Locale.ENGLISH);
					break;
				default:
					break;
				}
				UtilityHelper.switchLanguage(SettingRecActivity.this);
				dialog.dismiss();
				
			}
		});
	}
	private void turnToSetCategory(){
		Intent intent = new Intent(this, CategoryActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		this.startActivity(intent);
	}
	private void showBackupDialog(){
		final CustomDialog dialog = new CustomDialog(SettingRecActivity.this, R.layout.layout_backup_dialog, R.style.SyncDialog);
		dialog.show();
		((LinearLayout)dialog.findViewById(R.id.backup_sdcard)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				BackupRestoreTool.backupToSdcard(SettingRecActivity.this);
				dialog.dismiss();
			}
		});
		((LinearLayout)dialog.findViewById(R.id.backup_googledrive)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDriveCredential = BackupRestoreTool.backupToGoogleDrive(SettingRecActivity.this, mDriveCredential);
				dialog.dismiss();
			}
		});
	}
	private void showRestoreDialog(){
		final CustomDialog dialog = new CustomDialog(SettingRecActivity.this, R.layout.layout_restore_dialog, R.style.SyncDialog);
		dialog.show();
		((LinearLayout)dialog.findViewById(R.id.restore_sdcard)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				BackupRestoreTool.restoreFromSdcard(SettingRecActivity.this);
				dialog.dismiss();
			}
		});
		((LinearLayout)dialog.findViewById(R.id.restore_googledrive)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mDriveCredential = BackupRestoreTool.restoreFromGoogleDrive(SettingRecActivity.this, mDriveCredential);
				dialog.dismiss();
			}
		});
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	if(resultCode == RESULT_OK){
    		if (requestCode == Codes.REQUEST_SDCARD_RESTORE) {
				String filePath = data.getStringExtra(FileManagerActivity.RESULT_PATH);
				boolean success = DataManager.getInstance(this).restoreData(filePath);
				if (!success) {
					success = DataManager.getInstance(this).importDataCSV(filePath);
				}
				if (success) {
					DMToast.makeText(this, R.string.settings_restore_success, Toast.LENGTH_LONG).show();
				} else {
					DMToast.makeText(this, R.string.settings_restore_failed, Toast.LENGTH_LONG).show();
				}
			}else if (requestCode == Codes.REQUEST_GOOGLE_BACKUP) {
				if (data != null && data.getExtras() != null) {
					String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
					if (accountName != null) {
						mDriveCredential.setSelectedAccountName(accountName);
						mDriveService = new Drive.Builder(AndroidHttp.newCompatibleTransport(),new GsonFactory(), mDriveCredential).build();
						new BackupToGoogleDriveTask(SettingRecActivity.this,mDriveService).execute();
					}
				}
			}else if(requestCode == Codes.REQUEST_GOOGLE_RESTORE){
				if (data != null && data.getExtras() != null) {
					String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
					if (accountName != null) {
						mDriveCredential.setSelectedAccountName(accountName);
						mDriveService = new Drive.Builder(AndroidHttp.newCompatibleTransport(),new GsonFactory(), mDriveCredential).build();
						new RestoreFromDriveTask(SettingRecActivity.this,mDriveService,null).execute();
					}
				}
			}else if (requestCode == Codes.REQUEST_LOCK_CHANGE) {
				SharedPreferences prefs = getSharedPreferences(IContants.LOCK_PREFERENCES, 0);
				prefs.edit().clear().commit();
				
				Intent intent = new Intent(this, LockPatternActivity.class);
				intent.putExtra(LockPatternActivity._Mode, LockPatternActivity.LPMode.CreatePattern);
				startActivity(intent);
			}else if(requestCode == Codes.REQUEST_LOCK_DISABLE){
				SharedPreferences prefs = getSharedPreferences(IContants.LOCK_PREFERENCES, 0);
				prefs.edit().clear().commit();
				
			}
    		initViewStatus();
    	}
    }
}
