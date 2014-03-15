package com.pybeta.daymatter.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pybeta.daymatter.R;


/**
 * �Զ��嶯��
 * 
 * @author xiaobenben
 * 
 */
public class CustomLoading extends LinearLayout {
	private AnimationDrawable mAnimationLoading;
	private View m_vLoading;
	private TextView m_tvError;

	public CustomLoading(Context context) {
		super(context);
		init(context);
	}

	public CustomLoading(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@SuppressLint("NewApi")
	public CustomLoading(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.layout_loading, this);
		m_vLoading = findViewById(R.id.layout_loading_view);
		m_tvError = (TextView) findViewById(R.id.layout_loading_error);
		if (m_vLoading.getBackground() instanceof AnimationDrawable) {
			mAnimationLoading = (AnimationDrawable) m_vLoading.getBackground();
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
//		if (mAnimationLoading != null) {
//			if (hasWindowFocus && !mAnimationLoading.isRunning()) {
//				mAnimationLoading.start();
//			} else {
//				mAnimationLoading.stop();
//			}
//		}
	}

	private void start(){
		if (mAnimationLoading != null) {
			if ( !mAnimationLoading.isRunning()) {
				mAnimationLoading.start();
			}
		}
	}
	
	private void stop(){
		if (mAnimationLoading != null) {
			if ( mAnimationLoading.isRunning()) {
				mAnimationLoading.stop();
			}
		}
	}
	
	/**
	 * ��ʾloading
	 */
	public void showLoading() {
		start();
		setVisibility(VISIBLE);
		m_vLoading.setVisibility(VISIBLE);
		m_tvError.setVisibility(GONE);
	}

	/**
	 * ��ʾ��Ϣ
	 * 
	 * @param msg
	 */
	public void showMsg(String msg) {
		setVisibility(VISIBLE);
		m_vLoading.setVisibility(GONE);
		m_tvError.setVisibility(VISIBLE);
		m_tvError.setText(msg);
	}

	/**
	 * ��ʾ��Ϣ
	 * 
	 * @param resId
	 */
	public void showMsg(int resId) {
		setVisibility(VISIBLE);
		m_vLoading.setVisibility(GONE);
		m_tvError.setVisibility(VISIBLE);
		m_tvError.setText(resId);
	}

	/**
	 * ����
	 */
	public void hide() {
		stop();
		setVisibility(VISIBLE);
		m_vLoading.setVisibility(GONE);
		m_tvError.setVisibility(GONE);
	}

}
