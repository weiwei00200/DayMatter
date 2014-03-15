package com.pybeta.daymatter.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pybeta.daymatter.Config;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.core.MatterApplication;
import com.pybeta.daymatter.tool.KeyboardTool;
import com.pybeta.ui.widget.UcTitleBar;
import com.pybeta.ui.widget.UcTitleBar.ITitleBarListener;
import com.sammie.common.util.SerializeManager;
import com.sammie.common.view.Messager;

public class LockPasswordActivity extends Activity implements OnClickListener{
	private RelativeLayout mLayoutKeyboard = null;
	private TextView mTvPasswordOne = null;
	private TextView mTvPasswordTwo = null;
	private TextView mTvPasswordThree = null;
	private TextView mTvPasswordFour = null;
	private View mLineOne = null;
	private View mLineTwo = null;
	private View mLineThree = null;
	private View mLineFour = null;
	private UcTitleBar mTitleBar = null;
	private TextView mTvTitleTip = null;
	private EditText mEtPassword = null;
	
	private String mFirstPassword = "";
	private String mSecondPassword = "";
	private String mOldPassword = "";
	private boolean mIsFromChangePsw = false;
	private boolean mIsPassOldPsw = false;
	
	private boolean mIsFromWelActivity = false;
	private MatterApplication mAppInfo = null;
	private static int mLength = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_password);
		mIsFromWelActivity = getIntent().getExtras().getBoolean("IsFromWelActivity");
		mAppInfo = (MatterApplication)getApplication();
		mOldPassword = mAppInfo.getPassword();
		mIsFromChangePsw = !mOldPassword.equals("") ? true:false;
		
		mLayoutKeyboard = (RelativeLayout)this.findViewById(R.id.layout_jianpan);
		mTvTitleTip = (TextView)this.findViewById(R.id.tv_title_tip);
		mTvPasswordOne = (TextView)this.findViewById(R.id.pass1_dian);
		mTvPasswordTwo = (TextView)this.findViewById(R.id.pass2_dian);
		mTvPasswordThree = (TextView)this.findViewById(R.id.pass3_dian);
		mTvPasswordFour = (TextView)this.findViewById(R.id.pass4_dian);
		mLineOne = (View)this.findViewById(R.id.pass1_xian);
		mLineTwo = (View)this.findViewById(R.id.pass2_xian);
		mLineThree = (View)this.findViewById(R.id.pass3_xian);
		mLineFour = (View)this.findViewById(R.id.pass4_xian);
		mEtPassword = (EditText)this.findViewById(R.id.edit_password);
		mTitleBar = (UcTitleBar)this.findViewById(R.id.uc_titlebar);
		mLayoutKeyboard.setOnClickListener(this);
		
		
		initView();
		
	}
	
	private void initView(){
		clearPasswordView();
		if(mIsFromChangePsw){//修改密码
			mTvTitleTip.setText(getResources().getString(R.string.input_old_psw));
			mIsPassOldPsw = false;
		}
		
		mEtPassword.setFocusable(true);
		
		KeyboardTool.isShowKeyBoard(true, mEtPassword, this);
		
		mEtPassword.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getKeyCode() == 67) {
					if (mLength == 3 ) {
						mLineFour.setVisibility(View.VISIBLE);
						mTvPasswordFour.setVisibility(View.GONE);
					}else if (mLength == 2 ) {
						mLineThree.setVisibility(View.VISIBLE);
						mTvPasswordThree.setVisibility(View.GONE);
					}else if (mLength == 1 ) {
						mLineTwo.setVisibility(View.VISIBLE);
						mTvPasswordTwo.setVisibility(View.GONE);
					}else if (mLength == 0 ) {
						mLineOne.setVisibility(View.VISIBLE);
						mTvPasswordOne.setVisibility(View.GONE);
					}
				}
				return false;
			}
		});
		
		mEtPassword.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mLength = s.length();
				mEtPassword.setSelection(mLength);
//				String passWordStr = mEtPassword.getText().toString().trim();
				if (mLength == 1) {
					mLineOne.setVisibility(View.GONE);
					mTvPasswordOne.setVisibility(View.VISIBLE);
				}else if (mLength == 2 ) {
					mLineTwo.setVisibility(View.GONE);
					mTvPasswordTwo.setVisibility(View.VISIBLE);
				}else if (mLength == 3 ) {
					mLineThree.setVisibility(View.GONE);
					mTvPasswordThree.setVisibility(View.VISIBLE);
				}else if (mLength == 4 ) {
					mLineFour.setVisibility(View.GONE);
					mTvPasswordFour.setVisibility(View.VISIBLE);
				}else if(mLength > 4){
					mEtPassword.setText(mEtPassword.getText().toString().substring(0, 4));
				}
				
			}
			
		});
		//-----------------------------TitleBar---------------------------------
		mTitleBar.setTitleText(getResources().getString(R.string.setting_password));
		mTitleBar.setViewVisible(false, true, false, false, false, true, false, false);
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
				if(mLength!=4){
					Messager.showToast(LockPasswordActivity.this, getResources().getString(R.string.please_set_password_by_four), 1000, Gravity.CENTER);
					return;
				}
				String password = mEtPassword.getText().toString();
				if(!mIsFromWelActivity){//从设置界面来的
					if(mIsFromChangePsw){
						if(!mIsPassOldPsw){
							if(mOldPassword.equals(password)){
								mIsPassOldPsw = true;
								mIsFromChangePsw = false;
								mTvTitleTip.setText(getResources().getString(R.string.please_set_password));
							}else{
								Messager.showToast(LockPasswordActivity.this, getResources().getString(R.string.password_error), 1000, Gravity.CENTER);
							}
						}
					}else{
						if(mFirstPassword.equals("")){
							mFirstPassword = password;
							mTvTitleTip.setText(getResources().getString(R.string.please_set_password_again));
						}else if(mSecondPassword.equals("")){
							mSecondPassword = password;
							if(mFirstPassword.equals(mSecondPassword)){
								//设置密码成功
								SerializeManager.saveFile(mSecondPassword, Config.SerializePath_UserInfo);
								mAppInfo.setPassword(mSecondPassword);
								Messager.showToast(LockPasswordActivity.this, getResources().getString(R.string.setting_password_sucessful), 1000, Gravity.CENTER);
								KeyboardTool.isShowKeyBoard(false, mEtPassword, LockPasswordActivity.this);
								finish();
								overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
							}else{
								//两次密码不一致
								Messager.showToast(LockPasswordActivity.this, getResources().getString(R.string.password_different), 1000, Gravity.CENTER);
								mFirstPassword = "";
								mSecondPassword = "";
								mTvTitleTip.setText(getResources().getString(R.string.please_set_password));
							}
						} 
					}
					clearPasswordView();
				}else{//从WelActivity来的
					if(mAppInfo.getPassword().equals(password)){
						Intent intent = new Intent(LockPasswordActivity.this, HomeRecActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						KeyboardTool.isShowKeyBoard(false, mEtPassword, LockPasswordActivity.this);
						finish();
					}else{
						Messager.showToast(LockPasswordActivity.this, getResources().getString(R.string.password_error), 1000, Gravity.CENTER);
						clearPasswordView();
					}
				}
				
			}
			@Override
			public void closeClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void backClick(Object obj) {
				// TODO Auto-generated method stub
				KeyboardTool.isShowKeyBoard(false, mEtPassword, LockPasswordActivity.this);
				onBackPressed();
				finish();
				if(!mIsFromWelActivity){
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			}
			@Override
			public void addClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void clearPasswordView(){
		mLineOne.setVisibility(View.VISIBLE);
		mTvPasswordOne.setVisibility(View.GONE);
		mLineTwo.setVisibility(View.VISIBLE);
		mTvPasswordTwo.setVisibility(View.GONE);
		mLineThree.setVisibility(View.VISIBLE);
		mTvPasswordThree.setVisibility(View.GONE);
		mLineFour.setVisibility(View.VISIBLE);
		mTvPasswordFour.setVisibility(View.GONE);
		mEtPassword.setText("");
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == mLayoutKeyboard){
			mEtPassword.setFocusable(true);
			KeyboardTool.isShowKeyBoard(true, mEtPassword, LockPasswordActivity.this);
		}
	}
}
