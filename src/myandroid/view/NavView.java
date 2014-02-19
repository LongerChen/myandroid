package myandroid.view;

import android.content.Context;
import android.view.View;
import myandroid.layout.RelativeLayout;

public class NavView extends RelativeLayout {
	private RelativeLayout leftLayout;
	private RelativeLayout righLayout;
	private RelativeLayout midLayout;
	private int height = 0;

	public NavView(Context context, int height, int sideWidth) {
		super(context);
		this.height = height;
		leftLayout = addView(new RelativeLayout(context), sideWidth, height,
				new int[] { ALIGN_PARENT_LEFT });
		righLayout = addView(new RelativeLayout(context), sideWidth, height,
				new int[] { ALIGN_PARENT_RIGHT });
		midLayout = addView(new RelativeLayout(context), W, height, leftLayout,
				new int[] { RIGHT_OF }, righLayout, new int[] { LEFT_OF });
	}
	
	public void setHeight(int height) {
		this.height = height;
		int measureHeight = s.viewPxFromWidth(height);
		leftLayout.getLayoutParams().height = measureHeight;
		leftLayout.getLayoutParams().width = measureHeight;
		righLayout.getLayoutParams().height = measureHeight;
		righLayout.getLayoutParams().width = measureHeight;
		midLayout.getLayoutParams().height = measureHeight;
		getLayoutParams().height = measureHeight;
	}
	
	@Override
	public void setLayoutParams(android.view.ViewGroup.LayoutParams params) {
		params.height = s.viewPxFromWidth(height);
		super.setLayoutParams(params);
	}

	public RelativeLayout getLeftLayout() {
		return leftLayout;
	}

	public RelativeLayout getMidLayout() {
		return midLayout;
	}

	public RelativeLayout getRighLayout() {
		return righLayout;
	}

	public boolean isRighLayout(View view) {
		return view == righLayout;
	}

	public boolean isLeftLayout(View view) {
		return view == leftLayout;
	}

}
