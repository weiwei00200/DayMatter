package com.pybeta.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 *@Project Name:	DateMatter
 *@Copyright:		FanYue
 *@Version:			1.0.0.1
 *@File_Name:		CustomLinearLayout.java	
 *@AuthorName		Sammie.Zhang
 *@CreateDate:		2014-2-24
 *@Desc				
 *@ModifyHistory:
 */
public class CustomLinearLayout extends ViewGroup {

	private int mCellWidth;
	private int mCellHeight;
	
	public void setCellWidth(int cellWidth){
		this.mCellWidth=cellWidth;
		requestLayout();
	}
	
	public void setCellHeight(int cellHeight){
		this.mCellHeight=cellHeight;
		requestLayout();
	}
	
	public CustomLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomLinearLayout(Context context,AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
	}
	
	public CustomLinearLayout(Context context,AttributeSet attrs,int defStyle) {
		// TODO Auto-generated constructor stub
		super(context,attrs,defStyle);
	}
	
	/**
	 * �����ӿؼ�����
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		int cellWidth = mCellWidth;
		int cellHeight = mCellHeight;
		int columns = (r - l) / cellWidth;
		if (columns < 0) {
			columns = 1;
		}
		int x = 0;
		int y = 0;
		int i = 0;
		int count = getChildCount();
		for (int j = 0; j < count; j++) {
			final View childView = getChildAt(j);
			// ��ȡ�ӿؼ�Child�Ŀ��
			int w = childView.getMeasuredWidth();
			int h = childView.getMeasuredHeight();
			// �����ӿؼ��Ķ������
			int left = x + ((cellWidth - w) / 2);
			int top = y + ((cellHeight - h) / 2);
			// �����ӿؼ�
			childView.layout(left, top, left + w, top + h);

			if (i >= (columns - 1)) {
				i = 0;
				x = 0;
				y += cellHeight;
			} else {
				i++;
				x += cellWidth;

			}
		}
	}
	
	/**
	 * ����ؼ����ӿؼ���ռ����
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// ������������
		int cellWidthSpec = MeasureSpec.makeMeasureSpec(mCellWidth, MeasureSpec.AT_MOST);
		int cellHeightSpec = MeasureSpec.makeMeasureSpec(mCellHeight, MeasureSpec.AT_MOST);
		// ��¼ViewGroup��Child���ܸ���
		int count = getChildCount();
		// �����ӿռ�Child�Ŀ��
		for (int i = 0; i < count; i++) {
			View childView = getChildAt(i);
			/*
			 * 090 This is called to find out how big a view should be. 091 The
			 * parent supplies constraint information in the width and height
			 * parameters. 092 The actual mesurement work of a view is performed
			 * in onMeasure(int, int), 093 called by this method. 094 Therefore,
			 * only onMeasure(int, int) can and must be overriden by subclasses.
			 * 095
			 */
			childView.measure(cellWidthSpec, cellHeightSpec);
		}
		// ���������ؼ���ռ�����С
		// ע��setMeasuredDimension��resolveSize���÷�
		setMeasuredDimension(resolveSize(mCellWidth * count, widthMeasureSpec),
				resolveSize(mCellHeight * count, heightMeasureSpec));
		// setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

		// ����Ҫ���ø���ķ���
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	/**
	 * Ϊ�ؼ���ӱ߿�
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// ��ȡ���ֿؼ����
		int width = getWidth();
		int height = getHeight();
		// ��������
		Paint mPaint = new Paint();
		// ���û��ʵĸ�������
		mPaint.setColor(Color.BLUE);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(10);
		mPaint.setAntiAlias(true);
		// �������ο�
		Rect mRect = new Rect(0, 0, width, height);
		// ���Ʊ߿�
		canvas.drawRect(mRect, mPaint);
		// ��������ø���ķ���
		super.dispatchDraw(canvas);
	}


}
