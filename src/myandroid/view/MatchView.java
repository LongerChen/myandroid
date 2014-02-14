package myandroid.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class MatchView extends View {
	final public static int H = 0;
	final public static int W = 1;

	int hw = -1;

	int height = 0;
	int width = 0;

	Bitmap bm;
	Matrix matrix = new Matrix();
	Paint paint = new Paint();

	public MatchView(Context context, int hw) {
		super(context);
		this.hw = hw;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (bm == null)
			return;
		canvas.drawBitmap(bm, matrix, paint);
		canvas.save();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (bm == null)
			return;

		float scale = 1;
		switch (hw) {
		case H:
			if (height == LayoutParams.MATCH_PARENT)
				scale = (float) MeasureSpec.getSize(heightMeasureSpec)
						/ (float) bm.getHeight();
			else if (height == LayoutParams.WRAP_CONTENT)
				scale = 1;
			else
				scale = (float) height / (float) bm.getHeight();
			break;
		case W:
			if (width == LayoutParams.MATCH_PARENT)
				scale = (float) MeasureSpec.getSize(widthMeasureSpec)
						/ (float) bm.getWidth();
			else if (width == LayoutParams.WRAP_CONTENT)
				scale = 1;
			else
				scale = (float) width / (float) bm.getWidth();
			break;
		}
		matrix.setScale(scale, scale);
		setMeasuredDimension((int) (bm.getWidth() * scale),
				(int) (bm.getHeight() * scale));
	}

	@Override
	public void setLayoutParams(LayoutParams params) {
		super.setLayoutParams(params);
		height = params.height;
		width = params.width;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		height = h;
		width = w;
	}

	public void setImageBitmap(Bitmap bm) {
		this.bm = bm;
		measure(getWidth(), getHeight());
		invalidate();
	}

	public void setImageResource(int resId) {
		setImageBitmap(BitmapFactory.decodeResource(getResources(), resId));
	}

}
