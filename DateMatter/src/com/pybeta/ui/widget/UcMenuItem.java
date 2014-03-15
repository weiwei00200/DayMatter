package com.pybeta.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.pybeta.daymatter.R;
import com.sammie.common.view.Messager;

public class UcMenuItem extends LinearLayout implements OnClickListener{
	
	private LinearLayout mLayoutMenuItem = null;
	private ImageView mImgView = null;
	private TextView mTvView = null;
	private IItemClickListener mItemClickListener = null;
	public UcMenuItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	public UcMenuItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context){
		ViewGroup vg = (ViewGroup)LayoutInflater.from(context).inflate(R.layout.uc_menu_item, null);
		vg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		this.addView(vg);
		mLayoutMenuItem = (LinearLayout)vg.findViewById(R.id.layout_menu_item);
		mImgView = (ImageView)vg.findViewById(R.id.img_menu_item);
		mTvView = (TextView)vg.findViewById(R.id.tv_menu_item);
		
		mLayoutMenuItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mItemClickListener.itemClick();
			}
		});
		
	}
	
	public void setImage(int imageSource){
		mImgView.setBackgroundResource(imageSource);
	}
	public void setTextView(String text){
		mTvView.setText(text);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void setListener(IItemClickListener listener){
		this.mItemClickListener = listener;
	}
	
	public interface IItemClickListener{
		void itemClick();
	}
}
