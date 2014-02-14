package myandroid.layout;

import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout.LayoutParams;

/**
 * 計算Size的工具
 * 
 * @author Tony
 * 
 */
public class Sizer {
	/** LayoutParams.MATCH_PARENT */
	public final int M = LayoutParams.MATCH_PARENT;
	/** LayoutParams.WRAP_CONTENT */
	public final int W = LayoutParams.WRAP_CONTENT;
	/** 螢幕寬 */
	public int widthPixels = 0;
	/** 螢幕高 */
	public int heightPixels = 0;
	/** 螢幕百分之一寬度 */
	public float widthPercent = 0;
	/** 螢幕百分之一高度 */
	public float heightPercent = 0;

	/**
	 * 計算View、Layout、Text的工具
	 * 
	 * @author Tony
	 * 
	 */
	public Sizer(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		widthPixels = dm.widthPixels;
		heightPixels = dm.heightPixels;
		widthPercent = widthPixels / 100.0f;
		heightPercent = heightPixels / 100.0f;
	}

	public Sizer(int widthPixels, int heightPixels) {
		this.widthPixels = widthPixels;
		this.heightPixels = heightPixels;
		widthPercent = widthPixels / 100.0f;
		heightPercent = heightPixels / 100.0f;
	}

	/**
	 * 以螢幕寬度(單位px)為比例計算Text大小(單位px)
	 * 
	 * @param size
	 *            0.0f~100.0f
	 * @return Text Size (單位px)
	 */
	public float textPxFromWidth(float size) {
		return pxFromWidth(size);
	}

	/**
	 * 以螢幕高度(單位px)為比例計算Text大小(單位px)
	 * 
	 * @param size
	 *            0.0f~100.0f
	 * @return Text Size (單位px)
	 */
	public float textPxFromHeight(float size) {
		return pxFromHeight(size);
	}

	/**
	 * 以螢幕寬度(單位px)為比例換算Size
	 * 
	 * @param size
	 *            0.0f~100.0f
	 * @return View Size (單位px)
	 */
	public int viewPxFromWidth(float size) {
		return (int) (size == W || size == M ? size : pxFromWidth(size));
	}

	/**
	 * 以螢幕高度(單位px)為比例換算Size
	 * 
	 * @param size
	 *            0.0f~100.0f
	 * @return View Size (單位px)
	 */
	public int viewPxFromHeight(float size) {
		return (int) (size == W || size == M ? size : pxFromHeight(size));
	}

	/**
	 * 以螢幕寬度(單位px)為比例換算Size
	 * 
	 * @param size
	 *            0.0f~100.0f
	 * @return Size (單位px)
	 */
	public float pxFromWidth(float size) {
		return widthPercent * size;
	}

	/**
	 * 以螢幕高度(單位px)為比例換算Size
	 * 
	 * @param size
	 *            0.0f~100.0f
	 * @return Size (單位px)
	 */
	public float pxFromHeight(float size) {
		return heightPercent * size;
	}

	public boolean is16To9() {
		return widthPixels / heightPixels == 0.5625;
	}
}
