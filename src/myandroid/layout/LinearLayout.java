package myandroid.layout;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

public class LinearLayout extends android.widget.LinearLayout {
	final public int W = LayoutParams.WRAP_CONTENT;
	final public int M = LayoutParams.MATCH_PARENT;
	final protected int V = VERTICAL;
	final protected int H = HORIZONTAL;

	protected Sizer s;
	protected Context context;
	protected Resources res;

	public LinearLayout(Context context) {
		super(context);
		s = new Sizer(context);
		this.context = context;
		res = getResources();
	}

	public <T> T addMView(T view, float width, float height) {
		LayoutParams layoutParams = new LayoutParams(s.viewPxFromWidth(width),
				s.viewPxFromWidth(height));
		addView((View) view, layoutParams);
		return view;
	}

	public <T> T addMView(T view, LayoutParams params) {
		addView((View) view, params);
		return view;
	}

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
		((LinearLayout.LayoutParams) view.getLayoutParams()).setMargins(
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

	public void setPadding(float padding) {
		int p = s.viewPxFromWidth(padding);
		super.setPadding(p, p, p, p);
	}

}
