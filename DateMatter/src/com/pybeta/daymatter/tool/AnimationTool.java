package com.pybeta.daymatter.tool;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

public class AnimationTool {
	
	private static IAnimationListener mListener = null;
	public static Animation getAnimation(final View view,final int fromX,final int toX,final int fromY,final int toY,final AnimationType type,final int screenHeight) {
		Animation tranAnim = new TranslateAnimation(fromX,toX,fromY,toY); //按照绝对值来设置移动距离
        tranAnim.setDuration(300); 
        tranAnim.setInterpolator(new LinearInterpolator());//设置插入器
        tranAnim.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				int left = 0;
				int top = 0;
				if(type == AnimationType.Horizontal){
					left = view.getLeft()+ (int)(toX - fromX);
					top = view.getTop();
				}else if(type == AnimationType.Vertical){
					left = view.getLeft();
					top = view.getTop() + (int)(toY - fromY);
				}
				int width = view.getWidth();
				int height = view.getHeight();
				
				view.clearAnimation();
				view.layout(left, top, left + width, top + height);//重新设置view的大小
				AnimationTool.mListener.endAnim();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				AnimationTool.mListener.repeatAnim();
			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				AnimationTool.mListener.startAnim();
			}
        });
        return tranAnim;
	}

	public static Animation getAnimationInWorldTime(final View view,final int fromX,final int toX,final int fromY,final int toY,final AnimationType type,final int screenHeight,final AnimationTypeWorldTime typeCloseOrOpen) {
		Animation tranAnim = null;
		if(typeCloseOrOpen == AnimationTypeWorldTime.OPEN){
			tranAnim = new TranslateAnimation(fromX,toX,fromY,toY); //按照绝对值来设置移动距离
		}else if(typeCloseOrOpen == AnimationTypeWorldTime.CLOSE){
			tranAnim = new TranslateAnimation(fromX,toX,-fromY,toY); //按照绝对值来设置移动距离
		}
        tranAnim.setDuration(300); 
        tranAnim.setInterpolator(new DecelerateInterpolator());//设置插入器
        tranAnim.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				int left = 0;
				left = view.getLeft();
				int width = view.getWidth();
				
				view.clearAnimation();
				if(typeCloseOrOpen == AnimationTypeWorldTime.OPEN){
					view.layout(left, 0, left + width, screenHeight);//重新设置view的大小
				}else if(typeCloseOrOpen == AnimationTypeWorldTime.CLOSE){
					view.layout(left, fromY, left + width, screenHeight);//重新设置view的大小
				}
				AnimationTool.mListener.endAnim();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				AnimationTool.mListener.repeatAnim();
			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				AnimationTool.mListener.startAnim();
			}
        });
        return tranAnim;
	}
	
	
	
	public enum AnimationType{
		Horizontal,
		Vertical
	}
	
	public enum AnimationTypeWorldTime{
		OPEN,
		CLOSE
	}
	public static void setAnimListener(IAnimationListener listener){
		AnimationTool.mListener = listener;
	}
	
	public interface IAnimationListener{
		void startAnim();
		void endAnim();
		void repeatAnim();
	}
}
