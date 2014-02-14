package myandroid.async.task;

import myandroid.async.AsyncRunnable;
import myandroid.http.HttpConnect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class AsyncUrlBitmap extends AsyncRunnable<Bitmap> {
	String url;
	Options ops;

	public AsyncUrlBitmap(String url, Options ops) {
		this.url = url;
		this.ops = ops;
	}

	@Override
	protected Bitmap onExecute() {
		return BitmapFactory.decodeStream(
				new HttpConnect(url).connectForStream(), null, ops);
	}

}
