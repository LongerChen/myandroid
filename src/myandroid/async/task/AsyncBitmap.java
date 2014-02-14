package myandroid.async.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import myandroid.async.AsyncRunnable;
import myandroid.http.HttpFactory;
import myandroid.http.HttpResponse;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

public class AsyncBitmap extends AsyncRunnable<Bitmap> {
	boolean cache = false;
	Activity activity;
	File file;
	String url;
	Options opts = new Options();
	View v;

	public AsyncBitmap(Activity activity) {
		this.activity = activity;
	}

	public void load(View v, File file) {
		load(v, BitmapFactory.decodeFile(file.getAbsolutePath()));
	}

	public void load(View v, Bitmap bm) {
		if (v instanceof ImageView)
			((ImageView) v).setImageBitmap(bm);
		else
			v.setBackground(new BitmapDrawable(activity.getResources(), bm));
	}

	public void load(View v, String url, File file) {
		load(v, url, file, new Options());
	}

	public void load(View v, String url) {
		load(v, url, new Options());
	}

	public void load(View v, String url, Options opts) {
		this.v = v;
		this.opts = opts;
		this.url = url;
		execute();
	}

	public void load(int v, File file) {
		load(v, BitmapFactory.decodeFile(file.getAbsolutePath()));
	}

	public void load(int v, Bitmap bm) {
		load(createView(v), bm);
	}

	public void load(int v, String url) {
		load(createView(v), url);
	}

	public void load(int v, String url, File file) {
		load(createView(v), url, file);
	}

	public void load(int v, File file, Options opts) {
		load(v, BitmapFactory.decodeFile(file.getAbsolutePath(), opts));
	}

	public void load(View v, File file, Options opts) {
		load(v, BitmapFactory.decodeFile(file.getAbsolutePath(), opts));
	}

	public void load(int v, String url, Options opts) {
		load(createView(v), url, opts);
	}

	public void load(View v, String url, File file, Options opts) {
		cache = url != null && file != null;
		if (file.exists())
			load(v, file);
		this.file = file;
		this.opts = opts;
	}

	public void load(int v, String url, File file, Options opts) {
		load(createView(v), url, file, opts);
	}

	private View createView(int v) {
		return activity.findViewById(v);
	}

	@Override
	protected Bitmap onExecute() {
		if (cache && file.exists())
			return BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
		Bitmap bm = BitmapFactory.decodeStream(
				HttpResponse.getInputStream(HttpFactory.get(url)), null, opts);
		try {
			bm.compress(CompressFormat.PNG, 100, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bm;
	}

	@Override
	public void onResult(Bitmap r) {
		if (v == null || r == null)
			return;
		load(v, r);
	}

}