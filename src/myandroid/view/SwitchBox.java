package myandroid.view;

import com.myandroid.R;

import myandroid.layout.Sizer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.widget.CompoundButton;

public class SwitchBox extends CompoundButton {
	boolean checked = false;
	boolean touch = false;

	OnCheckedChangeListener onCheckedChangeListener;

	Sizer s;
	Paint paint = new Paint();
	Matrix matrix = new Matrix();
	Rect touchRect = new Rect();
	private PorterDuffXfermode mXfermode;

	Bitmap btnPress;
	Bitmap btnNormal;
	Bitmap btnCurrent;
	Bitmap frame;
	Bitmap mask;
	Bitmap bottom;

	float touchX = 0;

	public SwitchBox(Context context) {
		super(context);
		s = new Sizer(context);
		btnPress = BitmapFactory.decodeResource(getResources(),
				R.drawable.switch_btndown);
		btnNormal = BitmapFactory.decodeResource(getResources(),
				R.drawable.switch_btnup);
		mask = BitmapFactory.decodeResource(getResources(),
				R.drawable.switch_mask);
		btnCurrent = btnNormal;
		frame = BitmapFactory.decodeResource(getResources(),
				R.drawable.switch_frame);

		final float density = getResources().getDisplayMetrics().density;
		mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
		paint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int canvasW = canvas.getWidth();
		int canvasH = canvas.getHeight();

		canvas.saveLayerAlpha(new RectF(0, 0, canvasW, canvasH), 255,
				Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
						| Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
						| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
						| Canvas.CLIP_TO_LAYER_SAVE_FLAG);

		touchRect.set(checked ? canvasW - canvasH : 0, 0, checked ? canvasW
				: canvasH, canvasH);
		float maskScale = (float) canvas.getHeight() / (float) mask.getHeight();
		matrix.reset();
		matrix.setScale(maskScale, maskScale);
		matrix.postTranslate((canvasW - mask.getWidth() * maskScale) / 2, 0);

		canvas.drawBitmap(mask, matrix, paint);
		paint.setXfermode(mXfermode);
		matrix.reset();
		matrix.setScale(maskScale, maskScale);
		if (touch) {
			float x = touchX - canvasW / 2;
			// Develop.e(this, x);
			x = x < -canvasW / 2 ? -canvasW / 2 : x;
			x = x > 0 ? 0 : x;
			matrix.postTranslate(x, 0);
		} else
			matrix.postTranslate(checked ? 0 : -canvasW / 2, 0);
		if (bottom != null)
			canvas.drawBitmap(bottom, matrix, paint);
		paint.setXfermode(null);

		matrix.reset();
		matrix.setScale(maskScale, maskScale);
		matrix.postTranslate((canvasW - mask.getWidth() * maskScale) / 2, 0);
		canvas.drawBitmap(frame, matrix, paint);

		matrix.reset();
		float btnScale = (float) canvas.getHeight()
				/ (float) btnCurrent.getWidth();
		matrix.setScale(btnScale, btnScale);
		if (!touch)
			touchX = isChecked() ? canvasW - canvasH / 2 : 0;
		matrix.postTranslate(touchX < 0 ? 0
				: touchX > canvasW - canvasH ? canvasW - canvasH : touchX, 0);
		canvas.drawBitmap(btnCurrent, matrix, paint);
	}

	@Override
	public boolean isChecked() {
		return checked;
	}

	@Override
	public void setChecked(boolean checked) {
		this.checked = checked;
		invalidate();
		if (onCheckedChangeListener != null)
			onCheckedChangeListener.onCheckedChanged(this, checked);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = (int) (width * 0.4);
		setMeasuredDimension(width, height);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		int x = (int) e.getX();
		int y = (int) e.getY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (touchRect.contains(x, y))
				touch = true;
			btnCurrent = btnPress;
			break;
		case MotionEvent.ACTION_MOVE:
			if (touch)
				touchX = e.getX() - getHeight() / 2;
			break;
		case MotionEvent.ACTION_OUTSIDE:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			btnCurrent = btnNormal;
			if (touch) {
				checked = Float.compare(e.getX(), getWidth() / 2) >= 0;
			} else
				checked = !checked;
			touch = false;
			if (onCheckedChangeListener != null)
				onCheckedChangeListener.onCheckedChanged(this, checked);
			break;
		}
		if (isEnabled())
			invalidate();
		return isEnabled();
	}

	public void setBottomImage(Bitmap bottom) {
		this.bottom = bottom;
	}

	public void setBottomImage(int resId) {
		bottom = BitmapFactory.decodeResource(getResources(), resId);
	}

	@Override
	public void setOnCheckedChangeListener(
			OnCheckedChangeListener onCheckedChangeListener) {
		this.onCheckedChangeListener = onCheckedChangeListener;
	}

}
