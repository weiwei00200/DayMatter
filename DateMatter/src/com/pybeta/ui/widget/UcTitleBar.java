package com.pybeta.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.pybeta.daymatter.R;

public class UcTitleBar extends LinearLayout implements OnClickListener {
	private Context mContext = null;
	private TextView mTvTitle = null;
	private ImageView mImgMenu = null;
	private ImageView mImgClose = null;
	private ImageView mImgBack = null;
	private ImageButton mBtnAdd = null;
	private ImageButton mBtnEdit = null;
	private ImageButton mBtnComplete = null;
	private ImageButton mBtnShare = null;
	private ImageButton mBtnFeedback = null;
	private ITitleBarListener mListener = null;

	public UcTitleBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public UcTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		this.mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.uc_title_bar, null);
		mImgClose = (ImageView) vg.findViewById(R.id.img_close_titlebar);
		mImgBack = (ImageView) vg.findViewById(R.id.img_back_titlebar);
		mImgMenu = (ImageView) vg.findViewById(R.id.img_menu_titlebar);
		mTvTitle = (TextView) vg.findViewById(R.id.tv_title);
		mBtnAdd = (ImageButton) vg.findViewById(R.id.btn_add);
		mBtnEdit = (ImageButton) vg.findViewById(R.id.btn_edit);
		mBtnComplete = (ImageButton) vg.findViewById(R.id.btn_complete);
		mBtnShare = (ImageButton) vg.findViewById(R.id.btn_share);
		mBtnFeedback = (ImageButton) vg.findViewById(R.id.btn_feedback);
		
		mImgClose.setOnClickListener(this);
		mBtnAdd.setOnClickListener(this);
		mImgMenu.setOnClickListener(this);
		mImgBack.setOnClickListener(this);
		mBtnEdit.setOnClickListener(this);
		mBtnComplete.setOnClickListener(this);
		mBtnShare.setOnClickListener(this);
		mBtnFeedback.setOnClickListener(this);

		this.addView(vg);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (null == mListener) {
			mListener = tempListener;
		}
		if(v == mImgClose){
			mListener.closeClick(null);
		}else if (v == mImgMenu){
			mListener.menuClick(null);
		}else if (v == mImgBack) {
			mListener.backClick(null);
		}else if(v == mBtnAdd){
			mListener.addClick(null);
		} else if (v == mBtnEdit) {
			mListener.editClick(null);
		} else if (v == mBtnComplete) {
			mListener.completeClick(null);
		} else if (v == mBtnShare) {
			mListener.shareClick(null);
		}else if(v == mBtnFeedback){
			mListener.feedBackClick(null);
		}
	}

	public interface ITitleBarListener {
		void closeClick(Object obj);
		
		void menuClick(Object obj);
		
		void backClick(Object obj);

		void addClick(Object obj);
		
		void editClick(Object obj);

		void completeClick(Object obj);

		void shareClick(Object obj);
		
		void feedBackClick(Object obj);
	}

	private ITitleBarListener tempListener = new ITitleBarListener() {

		@Override
		public void shareClick(Object obj) {
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
		public void addClick(Object obj) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void closeClick(Object obj) {
			// TODO Auto-generated method stub
			
		}
	};
	
	//--------------------公开方法----------------------
	/**
	 * 初始化监听器
	 * @param listener
	 */
	public void setListener(ITitleBarListener listener) {
		mListener = listener;
	}
	/**
	 * 设置bar中间的文字
	 * @param text
	 */
	public void setTitleText(String text){
		this.mTvTitle.setText(text+"");
	}
	
	/**
	 * 设置视图是否可视
	 * @param isCloseVisible 关闭
	 * @param isBackVisible 返回
	 * @param isMenuVisible 菜单
	 * @param isAddVisible 添加
	 * @param isEidtVisible 编辑
	 * @param isCompleteVisible 完成
	 * @param isShareVisible 分享
	 * @param isFeedbackVisible 反馈
	 * 
	 */
	public void setViewVisible(boolean isCloseVisible,boolean isBackVisible ,boolean isMenuVisible , boolean isAddVisible, boolean isEidtVisible, boolean isCompleteVisible, boolean isShareVisible, boolean isFeedbackVisible){
		this.mBtnAdd.setVisibility(isAddVisible ? View.VISIBLE :View.GONE);
		this.mBtnEdit.setVisibility(isEidtVisible ? View.VISIBLE :View.GONE);
		this.mBtnComplete.setVisibility(isCompleteVisible ? View.VISIBLE :View.GONE);
		this.mBtnShare.setVisibility(isShareVisible ? View.VISIBLE :View.GONE);
		this.mBtnFeedback.setVisibility(isFeedbackVisible ? View.VISIBLE :View.GONE);
		
		this.mImgBack.setVisibility(isBackVisible ? View.VISIBLE :View.GONE);
		this.mImgMenu.setVisibility(isMenuVisible ? View.VISIBLE :View.GONE);
		this.mImgClose.setVisibility(isCloseVisible ? View.VISIBLE :View.GONE);
	}
}
