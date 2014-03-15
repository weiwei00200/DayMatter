package com.pybeta.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.pybeta.daymatter.R;

public class SyncDialog extends Dialog implements android.view.View.OnClickListener{
	private LinearLayout mBackupSdcard = null;
	private LinearLayout mBackupGoogle = null;
	private LinearLayout mRestoreSdcard = null;
	private LinearLayout mRestoreGoogle = null;
	private ISyncDialogListener mListener = null;
	
	public SyncDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sync_dialog);
		mBackupSdcard = (LinearLayout)this.findViewById(R.id.backup_sdcard);
		mBackupGoogle = (LinearLayout)this.findViewById(R.id.backup_googledrive);
		mRestoreSdcard = (LinearLayout)this.findViewById(R.id.restore_sdcard);
		mRestoreGoogle = (LinearLayout)this.findViewById(R.id.restore_googledrive);
		mBackupSdcard.setOnClickListener(this);
		mBackupGoogle.setOnClickListener(this);
		mRestoreSdcard.setOnClickListener(this);
		mRestoreGoogle.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == mBackupSdcard){
			mListener.backupSdcard();
		}else if(v == mBackupGoogle){
			mListener.backupGoogle();
		}else if(v == mRestoreSdcard){
			mListener.restoreSdcard();
		}else if(v == mRestoreGoogle){
			mListener.restoreGoogle();
		}
	
	}
	public void setListener(ISyncDialogListener listener){
		this.mListener = listener;
	}
	public interface ISyncDialogListener{
		void backupSdcard();
		void backupGoogle();
		void restoreSdcard();
		void restoreGoogle();
	} 
	
	
}
