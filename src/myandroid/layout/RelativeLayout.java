package myandroid.layout;

import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

public class RelativeLayout extends android.widget.RelativeLayout {
	protected Sizer s = null;
	protected Resources res;
	protected Context context;
	final public static int W = LayoutParams.WRAP_CONTENT;
	final public static int M = LayoutParams.MATCH_PARENT;
	int id = 0;

	public RelativeLayout(Context context) {
		super(context);
		this.context = context;
		s = new Sizer(context);
		res = getResources();
	}

	/**
	 * 
	 * @param view
	 *            欲增加的View
	 * @param size
	 *            長/寬
	 * @param relativeView1
	 *            相對的View1
	 * @param relativeValues1
	 *            相對位置1
	 * @param relativeView2
	 *            相對的View2
	 * @param relativeValues2
	 *            相對位置2
	 * @return 設置後將View回傳
	 */
	public <T> T addRectView(T view, float size, View relativeView1,
			int[] relativeValues1, View relativeView2, int[] relativeValues2) {
		return addView(view, size, size, relativeView1, relativeValues1,
				relativeView2, relativeValues2);
	}

	/**
	 * 
	 * @param view
	 *            欲增加的View
	 * @param size
	 *            長/寬
	 * @param relativeView
	 *            相對的View
	 * @param relativeValues
	 *            相對位置
	 * @return 設置後將View回傳
	 */
	public <T> T addRectView(T view, float size, View relativeView,
			int[] relativeValues) {
		return addView(view, size, size, relativeView, relativeValues);
	}

	/**
	 * 
	 * @param view
	 *            欲增加的View
	 * @param size
	 *            長/寬
	 * @param relativeValues
	 *            相對位置
	 * @return 設置後將View回傳
	 */
	public <T> T addRectView(T view, float size, int[] relativeValues) {
		addView(view, size, size, null, relativeValues);
		return view;
	}

	/**
	 * 
	 * @param view
	 *            欲增加的View
	 * @param width
	 *            寬度
	 * @param height
	 *            高度
	 * @param relativeValues
	 *            相對位置
	 * @return 設置後將View回傳
	 */
	public <T> T addView(T view, float width, float height, int[] relativeValues) {
		addView(view, width, height, null, relativeValues);
		return view;
	}

	/**
	 * 
	 * @param view
	 *            欲增加的View
	 * @param width
	 *            寬度
	 * @param height
	 *            高度
	 * @param relativeView
	 *            相對的View
	 * @param relativeValues
	 *            相對位置
	 * @return 設置後將View回傳
	 */
	public <T> T addView(T view, float width, float height, View relativeView,
			int[] relativeValues) {
		((View) view).setId(++id);
		RelativeLayout.LayoutParams layoutParams = new LayoutParams(
				s.viewPxFromWidth(width), s.viewPxFromWidth(height));
		for (int relativeValue : relativeValues)
			if (relativeView != null)
				layoutParams.addRule(relativeValue, relativeView.getId());
			else
				layoutParams.addRule(relativeValue);
		addView((View) view, layoutParams);
		return view;
	}

	/**
	 * 
	 * @param view
	 *            欲增加的View
	 * @param width
	 *            寬度
	 * @param height
	 *            高度
	 * @param relativeView1
	 *            相對的View1
	 * @param relativeValues1
	 *            相對位置1
	 * @param relativeView2
	 *            相對的View2
	 * @param relativeValues2
	 *            相對位置2
	 * @return 設置後將View回傳
	 */
	public <T> T addView(T view, float width, float height, View relativeView1,
			int[] relativeValues1, View relativeView2, int[] relativeValues2) {

		addView(view, width, height, relativeView1, relativeValues1);

		for (int relativeValue : relativeValues2)
			((LayoutParams) ((View) view).getLayoutParams()).addRule(
					relativeValue, relativeView2.getId());
		return view;
	}

	/**
	 * 重新計算Size
	 * 
	 * @param widthPercent
	 *            寬度百分比
	 * @return 修正後Size
	 */
	// public int getRelativeSize(float widthPercent) {
	// return sc.calLayoutPx(widthPercent);
	// }

	/**
	 * 
	 * @param view
	 *            要改Margin的View
	 * @param left
	 *            左邊
	 * @param top
	 *            上面
	 * @param right
	 *            右邊
	 * @param bottom
	 *            下面
	 */
	public void setMargin(View view, float left, float top, float right,
			float bottom) {
		((RelativeLayout.LayoutParams) view.getLayoutParams()).setMargins(
				s.viewPxFromWidth(left), s.viewPxFromWidth(top),
				s.viewPxFromWidth(right), s.viewPxFromWidth(bottom));
	}

	/**
	 * 
	 * @param view
	 *            要改Padding的View
	 * @param left
	 *            左邊
	 * @param top
	 *            上面
	 * @param right
	 *            右邊
	 * @param bottom
	 *            下面
	 */
	public void setPadding(View view, float left, float top, float right,
			float bottom) {
		view.setPadding(s.viewPxFromWidth(left), s.viewPxFromWidth(top),
				s.viewPxFromWidth(right), s.viewPxFromWidth(bottom));
	}

	/**
	 * 
	 * @param view
	 *            要改Padding的View
	 * @param padding
	 *            上下左右padding
	 */
	public void setPadding(View view, float padding) {
		int calPadding = s.viewPxFromWidth(padding);
		view.setPadding(calPadding, calPadding, calPadding, calPadding);
	}

	/**
	 * 
	 * @param padding
	 *            上下左右padding
	 */
	public void setPadding(float padding) {
		int calPadding = s.viewPxFromWidth(padding);
		super.setPadding(calPadding, calPadding, calPadding, calPadding);
	}

	/**
	 * 
	 * @param padding
	 *            上下左右padding
	 */
	public void setPadding(float left, float top, float right, float bottom) {
		super.setPadding(s.viewPxFromWidth(left), s.viewPxFromWidth(top),
				s.viewPxFromWidth(right), s.viewPxFromWidth(bottom));
	}

	public void setSmallBackgroundResource(int resid) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(resid);
		setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeStream(is,
				null, opt)));
	}
}
