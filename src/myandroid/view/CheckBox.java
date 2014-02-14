package myandroid.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.TransitionDrawable;

public class CheckBox extends android.widget.CheckBox {
	Bitmap check = null;
	Bitmap unCheck = null;
	TransitionDrawable transitionDrawable = null;

	public CheckBox(Context context) {
		super(context);
		setBackgroundColor(Color.CYAN);
		setText("aaaaaaaasssssssdddddddddffffffffffaaaaa");
	}

	public void setCheckImage(int checkRes, int unCheckRes) {
		check = BitmapFactory.decodeResource(getResources(), checkRes);
		unCheck = BitmapFactory.decodeResource(getResources(), unCheckRes);
	}

	private Bitmap reSize(Bitmap bitmap) {
		Matrix m = new Matrix();
		float scale = (float) getHeight() / (float) bitmap.getHeight();
		if (scale == 1.0f)
			return bitmap;
		m.setScale(scale, scale);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), m, true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (check == null || unCheck == null)
			return;
		if (isChecked()) {
			check = reSize(check);
			setButtonDrawable(new BitmapDrawable(check));
		} else {
			unCheck = reSize(unCheck);
			setButtonDrawable(new BitmapDrawable(unCheck));
		}
	}
}
