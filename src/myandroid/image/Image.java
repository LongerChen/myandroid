package myandroid.image;

import myandroid.async.AsyncCallBack;
import myandroid.async.task.AsyncUrlBitmap;
import myandroid.tools.Develop;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

public class Image {

	public static void url(final View view, String url, Options opt) {
		AsyncUrlBitmap asyncUrlBitmap = new AsyncUrlBitmap(url, opt);
		asyncUrlBitmap.setCallBack(new AsyncCallBack<Bitmap>() {

			@Override
			public void onResult(boolean isSuccess, Bitmap obj) {
				Develop.e(this, isSuccess);
				if (!isSuccess)
					return;
				if (view instanceof ImageView)
					((ImageView) view).setImageBitmap(obj);
				else
					view.setBackground(new BitmapDrawable(obj));
			}
		});
		asyncUrlBitmap.execute();
	}

	public static void url(View view, String url) {
		url(view, url, new Options());
	}
}