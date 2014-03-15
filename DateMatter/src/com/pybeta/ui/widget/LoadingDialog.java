package com.pybeta.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pybeta.daymatter.R;

public class LoadingDialog extends Dialog {
	private static LoadingDialog mLoadingDialog = null;
	private static TextView mTvText = null;
	public LoadingDialog(Context context){
		super(context);
		initView(context);
	}
	public LoadingDialog(Context context, int theme){
		super(context, theme);
		initView(context);
	}
	
	public static void showLoadingDialog(Context context,String text,boolean isCanCancel) {
		try {
			mLoadingDialog = new LoadingDialog(context,R.style.LoadingDialog);
			mLoadingDialog.setCancelable(isCanCancel);
			mTvText.setText(""+text);
			if(!mLoadingDialog.isShowing())
				mLoadingDialog.show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	public static void closeLoadingDialog(){
		try {
			if(mLoadingDialog.isShowing())
				mLoadingDialog.dismiss();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	private void initView(Context context){
		try {
			LayoutInflater inflater = LayoutInflater.from(context);
			ViewGroup vg = (ViewGroup)inflater.inflate(R.layout.uc_loading_dialog, null);
			mTvText = (TextView)vg.findViewById(R.id.tv_loadingdialog_text);
			this.setContentView(vg);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	} 
	

}
