package myandroid.async.task;

import java.io.File;

import myandroid.async.AsyncRunnable3;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class AsyncBitmap extends AsyncRunnable3<Bitmap> {
	File file;
	Options opts;

	public void initialize(File file, Options opts) {
		this.file = file;
		this.opts = opts;
	}

	@Override
	protected void onPrepare() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Bitmap onExecute() {
		return BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
	}

//	@Override
//	protected void onFinish(Bitmap r) {
//		postResoult(r == null ? AsyncStatus.FAILURE : AsyncStatus.SUCCESS, r);
//	}

	@Override
	protected void onResoult(Bitmap result, int status) {
		// TODO Auto-generated method stub

	}

}
