package com.pybeta.daymatter.tool;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageTool {
	/**
	 * 修改Bitmap为制定宽高的Bitmap
	 * @param bitmap
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap getScaleImg(Bitmap bitmap, int newWidth, int newHeight) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,true);
		bitmap.recycle();
		bitmap = null;
		
		return resultBitmap;

	}
}
