package myandroid.async.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import myandroid.async.Async;
import myandroid.async.AsyncRunnable;
import myandroid.async.AsyncStatus;
import myandroid.http.HttpFactory;
import myandroid.http.HttpResponse;
import myandroid.tools.Develop;

public class AsyncDownload extends AsyncRunnable<Integer> {
	String url;
	File output;
	boolean stop = false;

	public AsyncDownload(String url, File output) {
		this.url = url;
		this.output = output;
	}

	@Override
	protected Integer onExecute() {
		HttpURLConnection connection = HttpFactory.get(url);
		float contentLength = connection.getContentLength();
		float loadingLength = 0;
		InputStream is = HttpResponse.getInputStream(connection);
		try {
			Develop.e(this, "ResponseCode:" + connection.getResponseCode());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (is == null)
			return null;
		try {
			FileOutputStream out = new FileOutputStream(output);
			byte[] buffer = new byte[512];
			int flag = 0;
			int percent = 0;
			while ((flag = is.read(buffer)) != -1) {
				out.write(buffer, 0, flag);
				loadingLength += flag;
				if (percent != (int) (loadingLength / contentLength * 100)) 
					obtainMessage(AsyncStatus.SUCCESS, percent).sendToTarget();
				percent = (int) (loadingLength / contentLength * 100);
				if (stop) {
					output.delete();
					obtainMessage(AsyncStatus.SUCCESS, 0).sendToTarget();
					break;
				}
			}
			out.flush();
			out.close();
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stop ? 0 : 100;
	}

	public void stop() {
		this.stop = true;
	}
}
