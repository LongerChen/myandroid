package myandroid.async.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import myandroid.async.AsyncRunnable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;

public class AsyncBitmapDownload extends AsyncRunnable<File> {
	String url;
	File output;
	CompressFormat format;
	Options ops;
	int quality;

	public void initialize(String url, File output, Options ops,
			CompressFormat format, int quality) {
		this.url = url;
		this.output = output;
		this.format = format;
		this.quality = quality;
		this.ops = ops;
	}

	@Override
	protected File onExecute() {
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(new URL(url)
					.openConnection().getInputStream(), null, ops);
			bitmap.compress(format, quality, new FileOutputStream(output));
			return output;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
