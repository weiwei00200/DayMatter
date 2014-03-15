package com.pybeta.ui.widget;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pybeta.daymatter.R;

public class UcDate extends LinearLayout implements OnClickListener {

	public String[] weekday = { "日", "一", "二", "三", "四", "五", "六" };
	private LinearLayout linear_yi_ji_btn;
	private LinearLayout linear_date_btn;
	private RelativeLayout layout_yi;
	private RelativeLayout layout_ji;
	private RelativeLayout layout_chong;
	private RelativeLayout UcDateItem_Second;

	private RelativeLayout relative_year;
	private TextView tv_should_context;
	private TextView tv_avoid_context;
	private TextView tv_chong_context;
	private TextView UcDateItem_Second_Number;
	private TextView UcDateItem_Second_Chinese_Number;
	private TextView year;
	private View ucdate_yiji_line;

	private UcDateItem SundayItem;
	private UcDateItem MondayItem;
	private UcDateItem TuesdayItem;
	private UcDateItem WednesdayItem;
	private UcDateItem ThursdayItem;
	private UcDateItem FridayItem;
	private UcDateItem SaturdayItem;
	private UcDateItem[] UcDateList = { SundayItem, MondayItem, TuesdayItem,
			WednesdayItem, ThursdayItem, FridayItem, SaturdayItem };
	private int[] UcDateItemIDList = { R.id.UcDateItem_Sunday,
			R.id.UcDateItem_Monday, R.id.UcDateItem_Tuesday,
			R.id.UcDateItem_Wednesday, R.id.UcDateItem_Thursday,
			R.id.UcDateItem_Friday, R.id.UcDateItem_Saturday };
	private IUcDateListener mListener;

	public UcDate(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public UcDate(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.uc_date, null);
		linear_yi_ji_btn = (LinearLayout) vg
				.findViewById(R.id.linear_yi_ji_btn);
		linear_date_btn = (LinearLayout) vg.findViewById(R.id.linear_date_btn);

		layout_yi = (RelativeLayout) vg.findViewById(R.id.yi);
		layout_ji = (RelativeLayout) vg.findViewById(R.id.ji);
		layout_chong = (RelativeLayout) vg.findViewById(R.id.chong);
		UcDateItem_Second = (RelativeLayout) vg
				.findViewById(R.id.UcDateItem_Second);

		relative_year = (RelativeLayout) vg.findViewById(R.id.relative_year);
		ucdate_yiji_line = (View) vg.findViewById(R.id.ucdate_yiji_line);

		tv_should_context = (TextView) vg.findViewById(R.id.tv_should_context);
		tv_avoid_context = (TextView) vg.findViewById(R.id.tv_avoid_context);
		tv_chong_context = (TextView) vg.findViewById(R.id.tv_chong_context);
		
		
		
		
		UcDateItem_Second_Number=(TextView) vg.findViewById(R.id.UcDateItem_Second_Number);
		UcDateItem_Second_Chinese_Number=(TextView) vg.findViewById(R.id.UcDateItem_Second_Chinese_Number);
		
		year = (TextView) vg.findViewById(R.id.year);

		linear_date_btn.setOnClickListener(this);
		linear_yi_ji_btn.setOnClickListener(this);

		for (int i = 0; i < UcDateList.length; i++) {
			UcDateList[i] = (UcDateItem) vg.findViewById(UcDateItemIDList[i]);
			UcDateList[i].setTopText(weekday[i]);
			UcDateList[i].setViewVisible(true, true, true, false, false, true);

		}
		vg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		this.addView(vg);

	}

	@Override
	public void onClick(View v) {
		if (mListener == null) {
			mListener = tempListener;
		}
		if (v == linear_date_btn) {
			mListener.linear_date(null);
		}
		if (v == linear_yi_ji_btn) {
			mListener.linear_yi_ji(null);
		}

	}

	public interface IUcDateListener {

		void linear_date(Object obj);

		void linear_yi_ji(Object obj);
	}

	IUcDateListener tempListener = new IUcDateListener() {

		@Override
		public void linear_date(Object obj) {
			// TODO Auto-generated method stub

		}

		@Override
		public void linear_yi_ji(Object obj) {
			// TODO Auto-generated method stub

		}
	};

	// --------------------公开方法----------------------
	/**
	 * 初始化监听器
	 * 
	 * @param listener
	 */
	public void setListener(IUcDateListener listener) {
		mListener = listener;
	}

	/**
	 * 设置视图是否显示
	 * 
	 * 
	 */

	public void setViewVisible(boolean isYear, boolean isYiJILine) {
		relative_year.setVisibility(isYear ? View.VISIBLE : View.GONE);
		UcDateList[0].setViewVisible(true, true, false, false, false, false);
	}

	public void setYiJIParam(boolean isVERTICAL) {
		UcDateItem_Second.setVisibility(View.VISIBLE);
		layout_yi.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				0, 1));
		layout_ji.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				0, 1));
		ucdate_yiji_line.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, 1));
		linear_yi_ji_btn.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
		linear_yi_ji_btn.setOrientation(isVERTICAL ? LinearLayout.VERTICAL
				: LinearLayout.HORIZONTAL);
		layout_chong.setVisibility(View.VISIBLE);
		/*tv_should_context.setGravity(Gravity.CENTER_VERTICAL);
		tv_avoid_context.setGravity(Gravity.CENTER_VERTICAL);
		tv_chong_context.setGravity(Gravity.CENTER_VERTICAL);
		layout_yi.setGravity(Gravity.CENTER_VERTICAL);
		layout_ji.setGravity(Gravity.CENTER_VERTICAL);
		layout_chong.setGravity(Gravity.CENTER_VERTICAL);*/
		

	}

	/**
	 * 设置视图文本
	 * 
	 * @param YI
	 *            宜 的文本
	 * @param JI
	 *            忌 的文本
	 * 
	 */
	public void setYiJiText(String YiText, String JiText, String YearText) {
		tv_should_context.setText(YiText);
		tv_avoid_context.setText(JiText);
		year.setText(YearText);
	}

	public void setYiJiChongText(String YiText, String JiText, String ChongText) {
		tv_should_context.setText(YiText);
		tv_avoid_context.setText(JiText);
		tv_chong_context.setText(ChongText);
	}
	
	public void setTodayNumberText(String TodayText,String Chinese_date)
	{
		UcDateItem_Second_Number.setText(TodayText);
		UcDateItem_Second_Chinese_Number.setText(Chinese_date);
	}
	
	
	/**
	 * 设置一周日期的视图
	 * 
	 */
	public void setDateText(String[] numbertext, String[] chineseNumberText) {
		for (int i = 0; i < numbertext.length; i++) {
			UcDateList[i].setBottomText(numbertext[i], chineseNumberText[i]);
		}
	}

	public void TodayTextColor(int position, int borderColorId) {
		UcDateList[position].setBorderColor(borderColorId);
	}

	public void setChineseNumberTextColor(ArrayList<Integer> position) {
		for (int i = 0; i < position.size(); i++) {
			UcDateList[position.get(i)]
					.setChineseTextColor(R.color.date_item_chinese_number_change);
		}
	}

}
