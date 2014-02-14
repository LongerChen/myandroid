package myandroid.net;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;

/**
 * 使用AsyncTask為基底的Http下載類
 * 
 * @author Tony
 * 
 */
public class MyHttpGetFile extends AsyncTask<String, Integer, Void> {
	public static final int ERRORCODE_NOT_FOUND = -1;
	public static final int ERRORCODE_TIME_OUT = -9;
	public static final int ERRORCODE_IO_OUT = -10;

	public static final int STATUSCODE_PREPARE = -31;
	public static final int STATUSCODE_FOUND = -41;
	public static final int STATUSCODE_START = -51;
	public static final int STATUSCODE_COMPLETE = -91;


	File output;
	boolean isRunning = false;
	boolean isSuccess = false;

	OnErrorListener onErrorListener = null;
	OnCompeleteListener onCompeleteListener = null;
	OnProgressUpdateListener onProgressUpdateListener = null;
	OnStatusChangeListener onStatusChangeListener = null;

	/**
	 * 
	 * @param context
	 *            Activity本體
	 * @param output
	 *            儲存路徑
	 */

	public MyHttpGetFile() {

	}

	public void postOutput(File output) {
		this.output = output;
	}

	@Override
	protected void onPreExecute() {
		publishProgress(STATUSCODE_PREPARE);
		isRunning = true;
	}

	@Override
	protected Void doInBackground(String... params) {
		try {
			URL url = new URL(params[0]);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			// 判斷網址是否存在
			if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND)
				publishProgress(ERRORCODE_NOT_FOUND);
			else {
				publishProgress(STATUSCODE_FOUND);
				float fileLength = urlConnection.getContentLength();
				InputStream is = urlConnection.getInputStream();

				if (output == null)
					output = File.createTempFile("temp", null);
				if (!output.exists())
					output.createNewFile();
				FileOutputStream fos = new FileOutputStream(output);
				int flag = 0;
				float readData = 0;
				byte[] data = new byte[1024];
				int percent = 0;
				publishProgress(STATUSCODE_START);
				while (isRunning && (flag = is.read(data)) != -1) {
					readData += flag;
					fos.write(data, 0, flag);
					int tempPercent = (int) (readData / fileLength * 100);
					if (percent != tempPercent) {
						publishProgress(tempPercent);
						percent = tempPercent;
					}
				}

				// 若中斷則刪除檔案，否則post成功訊息
				if (isRunning) {
					publishProgress(STATUSCODE_COMPLETE);
					isSuccess = true;
				} else
					output.delete();
				fos.flush();
				fos.close();
				is.close();
			}
		} catch (MalformedURLException e) {
			publishProgress(ERRORCODE_TIME_OUT);
		} catch (IOException e) {
			publishProgress(ERRORCODE_IO_OUT);
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		switch (values[0]) {
		case STATUSCODE_PREPARE:
		case STATUSCODE_FOUND:
		case STATUSCODE_START:
		case STATUSCODE_COMPLETE:
			if (onStatusChangeListener != null)
				onStatusChangeListener.onStatusChange(values[0]);
			break;
		case ERRORCODE_NOT_FOUND:
		case ERRORCODE_TIME_OUT:
		case ERRORCODE_IO_OUT:
			if (onErrorListener != null)
				onErrorListener.onError(values[0]);
			break;
		default:
			if (onProgressUpdateListener != null)
				onProgressUpdateListener.onProgressUpdate(values[0]);
			break;
		}
	}

	@Override
	protected void onPostExecute(Void result) {
		if (onCompeleteListener != null)
			onCompeleteListener.onComplete(isSuccess, output);
		isRunning = false;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void cancel() {
		isRunning = false;
	}
	public OnErrorListener getOnErrorListener() {
		return onErrorListener;
	}

	public void setOnErrorListener(OnErrorListener onErrorListener) {
		this.onErrorListener = onErrorListener;
	}

	public OnCompeleteListener getOnCompeleteListener() {
		return onCompeleteListener;
	}

	public void setOnCompeleteListener(OnCompeleteListener onCompeleteListener) {
		this.onCompeleteListener = onCompeleteListener;
	}

	public OnProgressUpdateListener getOnProgressUpdateListener() {
		return onProgressUpdateListener;
	}

	public void setOnProgressUpdateListener(
			OnProgressUpdateListener onProgressUpdateListener) {
		this.onProgressUpdateListener = onProgressUpdateListener;
	}

	public OnStatusChangeListener getOnStatusChangeListener() {
		return onStatusChangeListener;
	}

	public void setOnStatusChangeListener(
			OnStatusChangeListener onStatusChangeListener) {
		this.onStatusChangeListener = onStatusChangeListener;
	}

	public interface OnErrorListener {
		/**
		 * 當錯誤發生
		 * 
		 * @param ErrorCode
		 *            ErrorCode會有二種狀況<br>
		 *            ERRORCODE_HTTP_NOT_FOUND - 當檔案不存在<br>
		 *            ERRORCODE_TIME_OUT - 連線逾時<br>
		 *            ERRORCODE_IO_OUT - 存取失敗
		 */
		public void onError(int ErrorCode);
	}

	public interface OnProgressUpdateListener {
		/**
		 * 進度更新時
		 * 
		 * @param progress
		 *            進度為0~100%
		 */
		public void onProgressUpdate(int progress);
	}

	public interface OnCompeleteListener {
		/**
		 * 完成下載
		 * 
		 * @param isSuccess
		 *            下載成功或失敗
		 * @param output
		 *            檔案儲存路徑
		 */
		public void onComplete(boolean isSuccess, File outputPath);
	}

	public interface OnStatusChangeListener {
		/**
		 * 
		 * @param StatusCode
		 *            StatusCode會有三種狀況<br>
		 *            <li>STATUSCODE_PREPARE - 準備<br> <li>STATUSCODE_FOUND - 取得
		 *            <br> <li>STATUSCODE_COMPELETE - 完成
		 */
		public void onStatusChange(int StatusCode);
	}
}
