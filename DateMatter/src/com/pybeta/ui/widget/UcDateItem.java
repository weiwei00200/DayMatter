package com.pybeta.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pybeta.daymatter.R;

public class UcDateItem extends LinearLayout implements OnClickListener {

	private TextView date_border_top;
	private TextView date_border_bottom;
	private TextView date_border_left;
	private TextView date_border_right;
	private TextView date_weekday;
	private TextView date_number;
	private TextView date_chinese_number;
	private TextView date_border_weekday_right;
	private TextView date_border_weekday_left;

	public UcDateItem(Context context) {
		super(context);
		initView(context);

	}

	public UcDateItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	@Override
	public void onClick(View v) {

	}

	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		ViewGroup vg = (ViewGroup) inflater
				.inflate(R.layout.uc_date_item, null);
		date_border_top = (TextView) vg.findViewById(R.id.date_border_top);
		date_border_bottom = (TextView) vg
				.findViewById(R.id.date_border_bottom);
		date_border_left = (TextView) vg.findViewById(R.id.date_border_left);
		date_border_right = (TextView) vg.findViewById(R.id.date_border_right);
		date_weekday = (TextView) vg.findViewById(R.id.date_weekday);
		date_number = (TextView) vg.findViewById(R.id.date_number);
		date_chinese_number = (TextView) vg
				.findViewById(R.id.date_chinese_number);
		date_border_weekday_right = (TextView) vg
				.findViewById(R.id.date_border_weekday_right);
		date_border_weekday_left = (TextView) vg
		.findViewById(R.id.date_border_weekday_left);
		
		vg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		this.addView(vg);
	}

	/**
	 * 设置视图文本
	 * 
	 * @param Weekday 星期几
	 *            
	 * 
	 * 
	 */
	public void setTopText(String Weekday) {
		date_weekday.setText(Weekday);

	}

	/**
	 * 设置视图文本
	 * 
	 * 
	 * @param Number 几号
	 * @param Chinese Number 农历几号
	 */
	public void setBottomText(String Number, String ChineseNumber) {

		date_number.setText(Number);
		date_chinese_number.setText(ChineseNumber);
	}

	/**
	 * 设置视图是否可视
	 * 
	 * @param isTopVisible 上边框是否可见
	 *            
	 *  
	 * @param isBottomVisible  下边框是否可见
	 *           
	 * 
	 * @param isLeftVisible  左边框是否可见
	 *           
	 * 
	 * @param isRightVisible  右边框是否可见
	 *           
	 * 
	 */

	public void setViewVisible(boolean isTopVisible, boolean isBottomVisible,
			boolean isLeftVisible, boolean isRightVisible,
			boolean isWeekdayRightVisible,boolean isWeekdayLeftVisible) {
		date_border_top.setVisibility(isTopVisible ? View.VISIBLE : View.GONE);
		date_border_bottom.setVisibility(isBottomVisible ? View.VISIBLE
				: View.GONE);
		date_border_left
				.setVisibility(isLeftVisible ? View.VISIBLE : View.GONE);
		date_border_right.setVisibility(isRightVisible ? View.VISIBLE
				: View.GONE);
		date_border_weekday_right.setVisibility(isWeekdayRightVisible ? View.VISIBLE
				: View.GONE);
		date_border_weekday_left.setVisibility(isWeekdayLeftVisible ? View.VISIBLE
				: View.GONE);
	}

	/**
	 * 设置边框颜色
	 * 
	 * @param borderColor
	 *            边框背景色
	 * 
	 */

	public void setBorderColor(int borderColorId) {
		
		
		date_border_top.setVisibility(View.VISIBLE);
		date_border_bottom.setVisibility(View.VISIBLE);
		date_border_left.setVisibility(View.VISIBLE);
		date_border_right.setVisibility(View.VISIBLE);
		date_border_top.setBackgroundColor(getResources().getColor(
				borderColorId));
		date_border_bottom.setBackgroundColor(getResources().getColor(
				borderColorId));
		date_border_left.setBackgroundColor(getResources().getColor(
				borderColorId));
		date_border_right.setBackgroundColor(getResources().getColor(
				borderColorId));
		setTextColor(borderColorId,borderColorId);
	}

	/**
	 * 设置文本颜色
	 * 
	 * @param NumberColor
	 *            几号颜色
	 * @param ChineseNumberColor
	 *            农历几号颜色
	 */

	public void setTextColor(int NumberColorId, int ChineseNumberColorId) {
		date_number.setTextColor(getResources().getColor(NumberColorId));
		date_chinese_number.setTextColor(getResources().getColor(
				ChineseNumberColorId));
	}
	
	
	public void setChineseTextColor( int ChineseNumberColorId)
	{
		date_chinese_number.setTextColor(getResources().getColor(
				ChineseNumberColorId));
	}
	
	public void setDateNumberText(String dateNumber)
	{
		date_number.setText(dateNumber);
	}

}
