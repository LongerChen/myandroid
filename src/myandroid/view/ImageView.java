package myandroid.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.BitmapFactory.Options;

public class ImageView extends android.widget.ImageView {
	Bitmap bm;
	Matrix matrix = new Matrix();
	Paint paint = new Paint();

	public ImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (bm == null)
			return;
		canvas.drawBitmap(bm, matrix, paint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (bm == null)
			return;
		int width = MeasureSpec.getSize(widthMeasureSpec);
		float scale = (float) width / (float) bm.getWidth();
		matrix.setScale(scale, scale);
		setMeasuredDimension((int) (bm.getWidth() * scale),
				(int) (bm.getHeight() * scale));
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		this.bm = bm;
		invalidate();
		measure(getWidth(), getHeight());
	}

	@Override
	public void setImageResource(int resId) {
		Options opts = new Options();
//		opts.inPreferredConfig = Bitmap.Config.RGB_565;
//		opts.inPurgeable = true;
//		opts.inInputShareable = true;
		setImageBitmap(BitmapFactory
				.decodeResource(getResources(), resId));
	}
}
