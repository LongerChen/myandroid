package myandroid.dialog;

import myandroid.layout.RelativeLayout;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.view.WindowManager.LayoutParams;

public class ImageDialog extends Dialog implements
		android.view.View.OnClickListener {
	protected ImageLayout layout;

	public ImageDialog(Context context, Bitmap ad, int resourceIdOfClose) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setCancelable(false);
		setContentView(layout = new ImageLayout(context), new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		layout.setCloseImage(resourceIdOfClose);
		layout.setAdImage(ad);
		layout.closeImage.setOnClickListener(this);
		layout.adImage.setOnClickListener(this);
		Window window = getWindow();
		LayoutParams params = window.getAttributes();
		params.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		params.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		window.setAttributes(params);
	}

	@Override
	public void onClick(View v) {
		if (v == layout.closeImage)
			onCloseClick();
		if (v == layout.adImage)
			onImageClick();
	}

	protected void onImageClick() {

	}

	protected void onCloseClick() {
		dismiss();
	}

	class ImageLayout extends RelativeLayout {
		ImageView adImage;
		ImageView closeImage;

		public ImageLayout(Context context) {
			super(context);
			adImage = addView(new ImageView(context), M, M, new int[] {});
			closeImage = addView(new ImageView(context), 10, 10,
					new int[] { ALIGN_PARENT_RIGHT });
			setMargin(adImage, 5, 5, 5, 5);
		}

		public void setCloseImage(int resourceIdOfClose) {
			closeImage.setImageResource(resourceIdOfClose);
		}

		public void setAdImage(Bitmap ad) {
			adImage.setImageBitmap(ad);
		}

	}
}
