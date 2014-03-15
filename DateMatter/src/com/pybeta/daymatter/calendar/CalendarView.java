package com.pybeta.daymatter.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.pybeta.daymatter.R;
import com.pybeta.daymatter.bean.HolidayItemBean;
import com.pybeta.daymatter.core.MatterApplication;
import com.pybeta.daymatter.tool.DateTimeTool;
import com.pybeta.daymatter.tool.ImageTool;

/**
 * 日历控件
 * 
 */
public class CalendarView extends View implements View.OnTouchListener {
	private final static String TAG = "anCalendar";
	private Date selectedStartDate;
	private Date selectedEndDate;
	private Date curDate; // 当前日历显示的月

	private HolidayItemBean mHolidayItemBean = null;
	private List<Calendar> onWorkList = new ArrayList<Calendar>();
	private List<Calendar> onHolidayList = new ArrayList<Calendar>();
	
	private Date today; // 今天的日期文字显示红色
	private Date downDate; // 手指按下状态时临时日期
	private Date showFirstDate, showLastDate; // 日历显示的第一个日期和最后一个日期
	private int downIndex; // 按下的格子索引
	private Calendar calendar;
	private Surface surface;
	private int[] date = new int[42]; // 日历显示数字
	private int curStartIndex, curEndIndex; // 当前显示的日历起始的索引
	private boolean completed = false; // 为false表示只选择了开始日期，true表示结束日期也选择了
	private OnItemClickListener onItemClickListener;
	private boolean isClickedCell = false;
	
	private int preMonthLeft = 0;
	private int preMonthRight = 0;
	private int nextMonthLeft = 0;
	private int nextMonthRight = 0;
	
	
	public CalendarView(Context context) {
		super(context);
		this.setBackgroundColor(getResources().getColor(android.R.color.transparent));
	}

	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setBackgroundColor(getResources().getColor(android.R.color.transparent));
	}
	/**
	 * 初始化数据
	 * @param holidayItemBean 
	 * @param showDateView 要显示的月份
	 * @param showHolidayDate 要标识红色的日期
	 */
	public void prepareView(HolidayItemBean holidayItemBean, String showDateView, String theHolidayDate){
		onWorkList.clear();
		onHolidayList.clear();
		mHolidayItemBean = holidayItemBean;
		
		
		for(String dateStr : mHolidayItemBean.getOnWorkList()){
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(DateTimeTool.convertToDate(dateStr));
			onWorkList.add(cal);
		}
		for(String dateStr : mHolidayItemBean.getOnHolidayList()){
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(DateTimeTool.convertToDate(dateStr));
			onHolidayList.add(cal);
		}
		today = DateTimeTool.convertToDate(theHolidayDate);
		curDate = selectedStartDate = selectedEndDate = DateTimeTool.convertToDate(showDateView);
		calendar = Calendar.getInstance();
		calendar.setTime(curDate);
		surface = new Surface();
		surface.density = getResources().getDisplayMetrics().density;
		setBackgroundColor(surface.bgColor);
		setOnTouchListener(this);
	}
	
	public String getShowingViewDate(){
		return getYearAndmonth()+"-01";
	}
	public String getShowingViewYear(){
		return getYearAndmonth().split("-")[0]; 
	}
	public String getShowingViewMonth(){
		return getYearAndmonth().split("-")[1]; 
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		surface.width = getResources().getDisplayMetrics().widthPixels;
		surface.height = (int) (getResources().getDisplayMetrics().heightPixels*2/5);
		if (View.MeasureSpec.getMode(widthMeasureSpec) == View.MeasureSpec.EXACTLY) {
			surface.width = View.MeasureSpec.getSize(widthMeasureSpec);
		}
		if (View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.EXACTLY) {
			surface.height = View.MeasureSpec.getSize(heightMeasureSpec);
		}
		widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.width,
				View.MeasureSpec.EXACTLY);
		heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.height,
				View.MeasureSpec.EXACTLY);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,int bottom) {
		if (changed) {
			surface.init();
		}
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		try {
			// 画框
			canvas.drawPath(surface.boxPath, surface.borderPaint);
			// 年月
			String monthText = getYearAndmonth();
			float textWidth = surface.monthPaint.measureText(monthText);
			canvas.drawText(monthText, (surface.width - textWidth) / 2f,surface.monthHeight * 3 / 4f, surface.monthPaint);
			//上一月/下一月
//			canvas.drawPath(surface.preMonthBtnPath, surface.monthChangeBtnPaint);
//			canvas.drawPath(surface.nextMonthBtnPath, surface.monthChangeBtnPaint);
			//----------------
			//左边
			Bitmap bitmapLeft = BitmapFactory.decodeResource(getResources(), R.drawable.ico_minileft);
			bitmapLeft = ImageTool.getScaleImg(bitmapLeft, bitmapLeft.getWidth(), bitmapLeft.getHeight()-6);
			Rect rectSrcL = new Rect(0, 0, bitmapLeft.getWidth(), bitmapLeft.getHeight());
			preMonthLeft = (int)((surface.width - textWidth)/3f)-7;
			preMonthRight = (int)((surface.width - textWidth)/3f)+7;
			Rect rectDstL = new Rect(preMonthLeft, (int)((surface.monthHeight * 3 / 3f)-bitmapLeft.getHeight())/2, preMonthRight, (int)((surface.monthHeight * 3 / 3f)-bitmapLeft.getHeight())/2+bitmapLeft.getHeight());
			canvas.drawBitmap(bitmapLeft, rectSrcL, rectDstL, null);
			//右边
			Bitmap bitmapRight = BitmapFactory.decodeResource(getResources(), R.drawable.ico_miniright);
			bitmapRight = ImageTool.getScaleImg(bitmapRight, bitmapRight.getWidth(), bitmapRight.getHeight()-6);
			Rect rectSrcR = new Rect(0, 0, bitmapRight.getWidth(), bitmapRight.getHeight());
			nextMonthLeft = surface.width-(int)((surface.width - textWidth)/3f)-12;
			nextMonthRight = surface.width-(int)((surface.width - textWidth)/3f)+2;
			Rect rectDstR = new Rect(nextMonthLeft, (int)((surface.monthHeight * 3 / 3f)-bitmapRight.getHeight())/2, nextMonthRight, (int)((surface.monthHeight * 3 / 3f)-bitmapRight.getHeight())/2+bitmapRight.getHeight());
			canvas.drawBitmap(bitmapRight, rectSrcR, rectDstR, null);
			//----------------

			
			// 星期
			float weekTextY = surface.monthHeight + surface.weekHeight * 3 / 4f;
			// 星期字体颜色
			surface.cellBgPaint.setColor(surface.textColor);
			
			canvas.drawRect(surface.weekHeight, surface.width, surface.weekHeight, surface.width, surface.cellBgPaint);
			for (int i = 0; i < surface.weekText.length; i++) {
				//设置星期的背景
				canvas.drawRect(surface.borderWidth+surface.cellWidth*i,
						surface.monthHeight+surface.borderWidth,
						i != surface.weekText.length-1 ? surface.cellWidth*(i+1) : surface.cellWidth*(i+1)-surface.borderWidth,
						surface.monthHeight+surface.borderWidth+surface.weekHeight-surface.borderWidth, 
						surface.cellBgWeek);
				//设置星期文字
				float weekTextX = i* surface.cellWidth+ (surface.cellWidth - surface.weekPaint.measureText(surface.weekText[i])) / 2f;
				canvas.drawText(surface.weekText[i], weekTextX, weekTextY,surface.weekPaint);
				
			}

			// 计算日期
			calculateDate();
			for(Calendar cal : onWorkList){//给补班上色
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH)+1;
				int day = cal.get(Calendar.DATE);
				Date onWorkDate = DateTimeTool.convertToDate(year+"-"+month+"-"+day);
				int drawBgPosition = DateTimeTool.getIntervalDays(showFirstDate,onWorkDate);
				if(drawBgPosition>-1)
					drawCellBg(canvas, drawBgPosition, surface.cellOnWorkColor, surface.cellOnWorkBorder);
			}
			for(Calendar cal : onHolidayList){//给放假上色
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH)+1;
				int day = cal.get(Calendar.DATE);
				Date onHolidayDate = DateTimeTool.convertToDate(year+"-"+month+"-"+day);
				int drawBgPosition = DateTimeTool.getIntervalDays(showFirstDate,onHolidayDate);
				if(drawBgPosition>-1)
					drawCellBg(canvas, drawBgPosition, surface.cellOnHolidayColor, surface.cellOnHolidayBorder);
			}
			isClickedCell = false;
			
			int todayIndex = -1;
			calendar.setTime(curDate);
			String curYearAndMonth = calendar.get(Calendar.YEAR) + ""+ calendar.get(Calendar.MONTH);
			calendar.setTime(today);
			String todayYearAndMonth = calendar.get(Calendar.YEAR) + ""+ calendar.get(Calendar.MONTH);
			if (curYearAndMonth.equals(todayYearAndMonth)) {
				int todayNumber = calendar.get(Calendar.DAY_OF_MONTH);
				todayIndex = curStartIndex + todayNumber - 1;
			}
			
			boolean isToday = false;
			for (int i = 0; i < 42; i++) {
				int color = surface.textColor;
				if (isLastMonth(i)) {
					color = surface.borderColor;
				} else if (isNextMonth(i)) {
					color = surface.borderColor;
				}
				if (todayIndex != -1 && i == todayIndex) {
					color = surface.todayNumberColor;
					drawCellBorder(canvas, todayIndex);
					isToday = true;
				}
				drawCellText(canvas, i, date[i] + "", color, isToday);
				isToday = false;
			}
			super.onDraw(canvas);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void calculateDate() {
		calendar.setTime(curDate);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
		Log.d(TAG, "day in week:" + dayInWeek);
		int monthStart = dayInWeek;
		if (monthStart == 1) {
			monthStart = 8;
		}
		monthStart -= 1;  //以日为开头-1，以星期一为开头-2
		curStartIndex = monthStart;
		date[monthStart] = 1;
		// last month
		if (monthStart > 0) {
			calendar.set(Calendar.DAY_OF_MONTH, 0);
			int dayInmonth = calendar.get(Calendar.DAY_OF_MONTH);
			for (int i = monthStart - 1; i >= 0; i--) {
				date[i] = dayInmonth;
				dayInmonth--;
			}
			calendar.set(Calendar.DAY_OF_MONTH, date[0]);
		}
		showFirstDate = calendar.getTime();
		// this month
		calendar.setTime(curDate);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		calendar.get(Calendar.DAY_OF_MONTH);
		int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
		for (int i = 1; i < monthDay; i++) {
			date[monthStart + i] = i + 1;
		}
		curEndIndex = monthStart + monthDay;
		// next month
		for (int i = monthStart + monthDay; i < 42; i++) {
			date[i] = i - (monthStart + monthDay) + 1;
		}
		if (curEndIndex < 42) {
			// 显示了下一月的
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		calendar.set(Calendar.DAY_OF_MONTH, date[41]);
		showLastDate = calendar.getTime();
	}

	/**
	 * 
	 * @param canvas
	 * @param index
	 * @param text
	 */
	private void drawCellText(Canvas canvas, int index, String text, int color, boolean isToday) {
		int x = getXByIndex(index);
		int y = getYByIndex(index);
		surface.datePaint.setColor(color);
		surface.datePaint.setTextSize(30);
		float cellY = surface.monthHeight + surface.weekHeight + (y - 1)
				* surface.cellHeight + surface.cellHeight * 3 / 4f;
		float cellX = (surface.cellWidth * (x - 1))
				+ (surface.cellWidth - surface.datePaint.measureText(text))
				/ 2f;
		
		if(isToday){
			int numPlus = (int)getResources().getDimension(R.dimen.today_num_left_plus_calendar_today);
			int enPlus = (int)getResources().getDimension(R.dimen.today_en_left_plus_calendar_today);
			canvas.drawText(text, cellX+numPlus, cellY-surface.cellHeight/4, surface.todayNumPaint);
//			float todayTipCellX = (surface.cellWidth * (x - 1)) + (surface.cellWidth - surface.datePaint.measureText(surface.todayStr))/2f;
			canvas.drawText(surface.todayStr, cellX-enPlus, cellY+surface.cellHeight/5.7f, surface.todayEnPaint);
			
		}else{
			canvas.drawText(text, cellX, cellY, surface.datePaint);
		}
	}

	private void drawCellBorder(Canvas canvas, int index){
		int x = getXByIndex(index);
		int y = getYByIndex(index);
		float left = surface.cellWidth * (x - 1) + surface.borderWidth;
		float top = surface.monthHeight + surface.weekHeight + (y - 1) * surface.cellHeight + surface.borderWidth;
//		canvas.drawRect(left, top, 
//				left + surface.cellWidth- surface.borderWidth-1, 
//				top + surface.cellHeight - surface.borderWidth-1, 
//				surface.borderPaint);
		canvas.drawLine(left-surface.borderWidth, top, left + surface.cellWidth, top, surface.todayBorderPaint);//上横线
		canvas.drawLine(left-surface.borderWidth, top + surface.cellHeight - surface.borderWidth, left + surface.cellWidth, top + surface.cellHeight - surface.borderWidth, surface.todayBorderPaint);//下横线
		canvas.drawLine(left-surface.borderWidth, top, left-surface.borderWidth, top + surface.cellHeight - surface.borderWidth, surface.todayBorderPaint);//左竖线
		canvas.drawLine(left + surface.cellWidth, top, left + surface.cellWidth, top + surface.cellHeight - surface.borderWidth, surface.todayBorderPaint);//右竖线
	}
	/**
	 * 
	 * @param canvas
	 * @param index
	 * @param color
	 */
	private void drawCellBg(Canvas canvas, int index, int bgColor,int borderColor) {
		int x = getXByIndex(index);
		int y = getYByIndex(index);
		surface.cellBgPaint.setColor(bgColor);
		float left = surface.cellWidth * (x - 1) + surface.borderWidth;
		float top = surface.monthHeight + surface.weekHeight + (y - 1)
				* surface.cellHeight + surface.borderWidth;
		canvas.drawRect(left, top, 
				left + surface.cellWidth- surface.borderWidth-1, 
				top + surface.cellHeight - surface.borderWidth-1, 
				surface.cellBgPaint);
	}

	private void drawDownOrSelectedBg(Canvas canvas) {
		// down and not up
		if (downDate != null) {
			drawCellBg(canvas, downIndex, surface.cellDownColor,0);
		}
		// selected bg color
		if (!selectedEndDate.before(showFirstDate)&& !selectedStartDate.after(showLastDate)) {
			int[] section = new int[] { -1, -1 };
			calendar.setTime(curDate);
			calendar.add(Calendar.MONTH, -1);
			findSelectedIndex(0, curStartIndex, calendar, section);
			if (section[1] == -1) {
				calendar.setTime(curDate);
				findSelectedIndex(curStartIndex, curEndIndex, calendar, section);
			}
			if (section[1] == -1) {
				calendar.setTime(curDate);
				calendar.add(Calendar.MONTH, 1);
				findSelectedIndex(curEndIndex, 42, calendar, section);
			}
			if (section[0] == -1) {
				section[0] = 0;
			}
			if (section[1] == -1) {
				section[1] = 41;
			}
			for (int i = section[0]; i <= section[1]; i++) {
				drawCellBg(canvas, i, surface.cellSelectedColor,0);
			}
		}
	}
	
	
	private void findSelectedIndex(int startIndex, int endIndex,Calendar calendar, int[] section) {
		for (int i = startIndex; i < endIndex; i++) {
			calendar.set(Calendar.DAY_OF_MONTH, date[i]);
			Date temp = calendar.getTime();
			if (temp.compareTo(selectedStartDate) == 0) {
				section[0] = i;
			}
			if (temp.compareTo(selectedEndDate) == 0) {
				section[1] = i;
				return;
			}
		}
	}

	public Date getSelectedStartDate() {
		return selectedStartDate;
	}

	public Date getSelectedEndDate() {
		return selectedEndDate;
	}

	private boolean isLastMonth(int i) {
		if (i < curStartIndex) {
			return true;
		}
		return false;
	}

	private boolean isNextMonth(int i) {
		if (i >= curEndIndex) {
			return true;
		}
		return false;
	}

	private int getXByIndex(int i) {
		return i % 7 + 1; // 1 2 3 4 5 6 7
	}

	private int getYByIndex(int i) {
		return i / 7 + 1; // 1 2 3 4 5 6
	}

	// 获得当前应该显示的年月
	public String getYearAndmonth() {
		calendar.setTime(curDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		return year + "-" + (month+1);
	}
	
	//上一月
	public String clickLeftMonth(){
		calendar.setTime(curDate);
		calendar.add(Calendar.MONTH, -1);
		curDate = calendar.getTime();
		invalidate();
		
		return getYearAndmonth();
	}
	//下一月
	public String clickRightMonth(){
		calendar.setTime(curDate);
		calendar.add(Calendar.MONTH, 1);
		curDate = calendar.getTime();
		invalidate();
		return getYearAndmonth();
	}

	private void setSelectedDateByCoor(float x, float y) {
		// change month
		if (y < surface.monthHeight) {
			// pre month
			if (x>=preMonthLeft-15 && x<=preMonthRight+15){//(x < surface.monthChangeWidth) {
				calendar.setTime(curDate);
				calendar.add(Calendar.MONTH, -1);
				curDate = calendar.getTime();
			}
			// next month
			else if (x>=nextMonthLeft-15 && x<=nextMonthRight+15){//(x > surface.width - surface.monthChangeWidth) {
				calendar.setTime(curDate);
				calendar.add(Calendar.MONTH, 1);
				curDate = calendar.getTime();
			}
		}
		// cell click down
		if (y > surface.monthHeight + surface.weekHeight) {
			int m = (int) (Math.floor(x / surface.cellWidth) + 1);
			int n = (int) (Math.floor((y - (surface.monthHeight + surface.weekHeight))/ Float.valueOf(surface.cellHeight)) + 1);
			downIndex = (n - 1) * 7 + m - 1;
			Log.d(TAG, "downIndex:" + downIndex);
			calendar.setTime(curDate);
			if (isLastMonth(downIndex)) {
				calendar.add(Calendar.MONTH, -1);
			} else if (isNextMonth(downIndex)) {
				calendar.add(Calendar.MONTH, 1);
			}
			calendar.set(Calendar.DAY_OF_MONTH, date[downIndex]);
			downDate = calendar.getTime();
		}
		invalidate();
	}

	
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setSelectedDateByCoor(event.getX(), event.getY());
			break;
		case MotionEvent.ACTION_UP:
			if (downDate != null) {
				if (!completed) {
					if (downDate.before(selectedStartDate)) {
						selectedEndDate = selectedStartDate;
						selectedStartDate = downDate;
					} else {
						selectedEndDate = downDate;
					}
					completed = true;
				} else {
					selectedStartDate = selectedEndDate = downDate;
					completed = false;
				}
				selectedStartDate = selectedEndDate = downDate;
				//响应监听事件
				
				onItemClickListener.OnItemClick(selectedStartDate);
				
				downDate = null;
				isClickedCell = true;
				invalidate();
			}
			break;
		}
		return true;
	}
	
	//给控件设置监听事件
	public void setOnItemClickListener(OnItemClickListener onItemClickListener){
		this.onItemClickListener =  onItemClickListener;
	}
	//监听接口
	public interface OnItemClickListener {
		void OnItemClick(Date date);
	}

	/**
	 * 
	 * 1. 布局尺寸 2. 文字颜色，大小 3. 当前日期的颜色，选择的日期颜色
	 */
	private class Surface {
		
		public float density;
		public int width; // 整个控件的宽度
		public int height; // 整个控件的高度
		public float monthHeight; // 显示月的高度
		public float monthChangeWidth; // 上一月、下一月按钮宽度
		public float weekHeight; // 显示星期的高度
		public float cellWidth; // 日期方框宽度
		public float cellHeight; // 日期方框高度	
		public float borderWidth;
		public int bgColor = android.R.color.transparent;//Color.parseColor("#FFFFFF");
		private int textColor = Color.BLACK;
		private int textColorUnimportant = Color.parseColor("#666666");
		private int btnColor = Color.parseColor("#666666");
		private int borderColor = Color.parseColor("#b2b2b2");
		public int todayNumberColor = Color.RED;
		public int todayNumberBorderColor = Color.RED;
		public int cellDownColor = Color.parseColor("#CCFFFF");
		public int cellSelectedColor = Color.parseColor("#99CCFF");
		public int cellOnWorkColor = Color.parseColor("#ade9e4");//com.pybeta.daymatter.R.drawable.onwork_bg;//工作
		public int cellOnHolidayColor = Color.parseColor("#ffb6aa");//com.pybeta.daymatter.R.drawable.onholiday_bg;//放假
		public int cellNormalColor = Color.parseColor("#FFFFFF");//正常
		
		public int cellOnWorkBorder = Color.parseColor("#86bad3");
		public int cellOnHolidayBorder = Color.parseColor("#d67e7c");
		public int monthTextColor = Color.parseColor("#fd472b");
		public int weekBg = Color.parseColor("#ededed");
		public Paint borderPaint;
		public Paint todayBorderPaint;
		public Paint todayEnPaint;
		public Paint todayNumPaint;
		public Paint monthPaint;
		public Paint weekPaint;
		public Paint datePaint;
		public Paint monthChangeBtnPaint;
		public Paint cellBgPaint;
		public Paint cellBgWeek;
		public Path boxPath; // 边框路径
		public Path preMonthBtnPath; // 上一月按钮三角形
		public Path nextMonthBtnPath; // 下一月按钮三角形
		public String[] weekText = { "日","一", "二", "三", "四", "五", "六"};
		public String[] monthText = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
		public String todayStr = "Today";
		
		public void init() {
			float temp = height / 7f;
			monthHeight = (float) ((temp + temp * 0.3f) * 0.6);
			monthChangeWidth = monthHeight * 1.5f;
			weekHeight = (float) ((temp + temp * 0.3f) * 0.7)-10;
			cellHeight = (height - monthHeight - weekHeight) / 6f;
			cellWidth = width / 7f;
			borderPaint = new Paint();
			borderPaint.setColor(borderColor);
			borderPaint.setStyle(Paint.Style.STROKE);
			borderWidth = (float)(0.5 * density);
			borderWidth = borderWidth < 1 ? 1 : borderWidth;
			borderPaint.setStrokeWidth(2f);
			monthPaint = new Paint();
			monthPaint.setColor(textColor);
			monthPaint.setAntiAlias(false);
			float textSize = cellHeight * 0.4f;
			monthPaint.setTextSize(40);//字体大小
			monthPaint.setColor(monthTextColor);
			//monthPaint.setTypeface(Typeface.DEFAULT_BOLD);
			weekPaint = new Paint();
			weekPaint.setColor(textColor);
			weekPaint.setAntiAlias(true);
			float weekTextSize = weekHeight * 0.6f;
			weekPaint.setTextSize(26);//星期字体大小
			//weekPaint.setTypeface(Typeface.DEFAULT_BOLD);
			datePaint = new Paint();
			datePaint.setColor(textColor);
			datePaint.setAntiAlias(true);
			float cellTextSize = cellHeight * 0.5f;
			datePaint.setTextSize(cellTextSize);
			//datePaint.setTypeface(Typeface.DEFAULT_BOLD);
			boxPath = new Path();
			boxPath.addRect(0, monthHeight, width, height, Direction.CW);
			boxPath.moveTo(0, monthHeight);
			boxPath.rLineTo(width, 0);
			boxPath.moveTo(0, monthHeight + weekHeight);
			boxPath.rLineTo(width, 0);
			for (int i = 1; i < 6; i++) {
				boxPath.moveTo(0, monthHeight + weekHeight + i * cellHeight);
				boxPath.rLineTo(width, 0);
				boxPath.moveTo(i * cellWidth, monthHeight);
				boxPath.rLineTo(0, height - monthHeight);
			}
			boxPath.moveTo(6 * cellWidth, monthHeight);
			boxPath.rLineTo(0, height - monthHeight);
			preMonthBtnPath = new Path();
			int btnHeight = (int) (monthHeight * 0.6f);
			preMonthBtnPath.moveTo(monthChangeWidth / 2f, monthHeight / 2f);
			preMonthBtnPath.rLineTo(btnHeight / 2f, -btnHeight / 2f);
			preMonthBtnPath.rLineTo(0, btnHeight);
			preMonthBtnPath.close();
			nextMonthBtnPath = new Path();
			nextMonthBtnPath.moveTo(width - monthChangeWidth / 2f,monthHeight / 2f);
			nextMonthBtnPath.rLineTo(-btnHeight / 2f, -btnHeight / 2f);
			nextMonthBtnPath.rLineTo(0, btnHeight);
			nextMonthBtnPath.close();
			monthChangeBtnPaint = new Paint();
			monthChangeBtnPaint.setAntiAlias(true);
			monthChangeBtnPaint.setStyle(Paint.Style.FILL);
			monthChangeBtnPaint.setColor(btnColor);
			cellBgPaint = new Paint();
			cellBgPaint.setAntiAlias(true);
			cellBgPaint.setStyle(Paint.Style.FILL);
			cellBgPaint.setColor(cellSelectedColor);
			cellBgWeek = new Paint();//星期背景
			cellBgWeek.setAntiAlias(true);
			cellBgWeek.setStyle(Paint.Style.FILL);
			cellBgWeek.setColor(weekBg);
			todayBorderPaint = new Paint();//当前日期边框
			todayBorderPaint.setColor(todayNumberBorderColor);
			todayBorderPaint.setStyle(Paint.Style.STROKE);
			todayBorderPaint.setStrokeWidth(2f);
			todayEnPaint = new Paint();//今天英文
			todayEnPaint.setColor(todayNumberColor);
			todayEnPaint.setAntiAlias(true);
			todayEnPaint.setTextSize(cellTextSize-4);
			todayNumPaint = new Paint();//今天数字
			todayNumPaint.setColor(textColor);
			todayNumPaint.setAntiAlias(true);
			todayNumPaint.setTextSize(cellTextSize);
			
		}
	}
}
