/**
 * 系统项目名称
 * com.aohe.chequn.util
 * ToastUtil.java
 * 
 * 2012-2-24-下午5:33:55
 *  2012AoHe公司-版权所有
 * 
 */
package com.pybeta.daymatter.utils;

/**
 * 
 * ToastUtil
 * 
 * Jameslin
 * 2012-2-24 下午5:33:55
 * 
 * @version 1.0.0
 * 
 */
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastUtil {

	private static Toast toast = null;

	public static int LENGTH_LONG = Toast.LENGTH_LONG;

	private static int LENGTH_SHORT = Toast.LENGTH_SHORT;

	public static void cancelToast(){
		if(toast != null)
			toast.cancel();
	}
	
	/**
	 * 
	 * 普通文本消息提示
	 * 
	 * @param context
	 * 
	 * @param text
	 * 
	 * @param duration
	 */

	public static void TextToast(Context context, CharSequence text,
			int duration) {
//		if(toast != null)
//			toast.cancel();
//		cancelToast();
		if(toast == null)
//		 创建一个Toast提示消息
		toast = Toast.makeText(context, text, duration);
		else
			toast.setText(text);
		// 设置Toast提示消息在屏幕上的位置

//		toast.setGravity(Gravity.CENTER, 0, 80);

		// 显示消息

		toast.show();
	}

	/**
	 * 
	 * 带图片消息提示
	 * 
	 * @param context
	 * 
	 * @param ImageResourceId
	 * 
	 * @param text
	 * 
	 * @param duration
	 */

	public static void ImageToast(Context context, int ImageResourceId,
			CharSequence text, int duration) {

		// 创建一个Toast提示消息

		toast = Toast.makeText(context, text, Toast.LENGTH_LONG);

		// 设置Toast提示消息在屏幕上的位置

		toast.setGravity(Gravity.CENTER, 0, 0);

		// 获取Toast提示消息里原有的View

		View toastView = toast.getView();

		// 创建一个ImageView

		ImageView img = new ImageView(context);

		img.setImageResource(ImageResourceId);

		// 创建一个LineLayout容器

		LinearLayout ll = new LinearLayout(context);

		// 向LinearLayout中添加ImageView和Toast原有的View

		ll.addView(img);

		ll.addView(toastView);

		// 将LineLayout容器设置为toast的View

		toast.setView(ll);

		// 显示消息

		toast.show();

	}

}