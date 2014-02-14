package myandroid.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import myandroid.async.Async;
import myandroid.async.AsyncCallBack;
import myandroid.async.task.AsyncBitmap;
import myandroid.async.task.AsyncUrlBitmap;
import myandroid.tools.Develop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.widget.ImageView;

public class ImageLoad extends ImageView {

	public ImageLoad(Context context) {
		super(context);
	}

	public void loadImage(String url, final File file) {
		if (file.exists())
			setImageFile(file);
		else {
			Options ops = new Options();
			ops.inPreferredConfig = Config.RGB_565;
			AsyncUrlBitmap httpBitmap = new AsyncUrlBitmap(url, ops);
			httpBitmap.setCallBack(new AsyncCallBack<Bitmap>() {

				@Override
				public void onResult(boolean isSuccess, Bitmap obj) {
					if (!isSuccess)
						return;
					try {
						if (getWidth() != 0 && obj.getWidth() > getWidth()) {
							float offset = (float) getWidth()
									/ (float) obj.getWidth();
							obj = Bitmap.createScaledBitmap(obj,
									(int) (obj.getWidth() * offset),
									(int) (obj.getHeight() * offset), true);
						}
						obj.compress(CompressFormat.JPEG, 100,
								new FileOutputStream(file));
					} catch (FileNotFoundException e) {
						Develop.i(this, "External Storage Not Found");
					}
					setImageBitmap(obj);
				}
			});
			Async.execute(httpBitmap);
		}
	}

	public void setImageFile(File file) {
		Develop.i(this, "Load Image From File" + file.getAbsolutePath());
		AsyncBitmap fileBitmap = new AsyncBitmap();
		Options opts = new Options();
		opts.inPreferredConfig = Config.RGB_565;
		fileBitmap.initialize(file, opts);
		fileBitmap.setCallBack(new AsyncCallBack<Bitmap>() {

			@Override
			public void onResult(boolean isSuccess, Bitmap obj) {
				if (isSuccess)
					setImageBitmap(obj);
			}
		});
		Async.execute(fileBitmap);
//		setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
	}

	public void setImageUrl(String url) {
		Develop.i(this, "Load Image From Url" + url);
		Options ops = new Options();
		ops.inPreferredConfig = Config.RGB_565;
		AsyncUrlBitmap httpBitmap = new AsyncUrlBitmap(url, ops);
		httpBitmap.setCallBack(new AsyncCallBack<Bitmap>() {

			@Override
			public void onResult(boolean isSuccess, Bitmap obj) {
				setImageBitmap(obj);
			}
		});
		Async.execute(httpBitmap);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
	}
}
