package myandroid.net;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import myandroid.storage.MySDCard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

/**
 * 使用AsyncTask為基底的Http下載類
 * 
 * @author Tony
 * 
 */
public class MyDownload extends AsyncTask<String, Integer, Long> {
	public static final int ERRORCODE_HTTP_NOT_FOUND = -1;
	public static final int ERRORCODE_STORAGE_OUT = -3;
	public static final int ERRORCODE_NETWORK_OUT = -4;
	public static final int ERRORCODE_SDCARD_OUT = -5;
	public static final int ERRORCODE_TIME_OUT = -9;

	final int HTTP_FOUND = -2;
	final int DOWNLOAD_SUCCESS = -6;
	final int DOWNLOAD_PREPARE = -7;
	final int DOWNLOAD_START = -8;
	Context context;
	File output;
	boolean isRunning = false;
	boolean hasNet = false;
	boolean isSuccess = false;

	OnPrepareListener onPrepareListener = null;
	OnHttpFoundListener onHttpFoundListener = null;
	OnDownloadStartListener onDownloadStartListener = null;
	OnErrorListener onErrorListener = null;
	OnCompeleteListener onCompeleteListener = null;
	OnProgressUpdateListener onProgressUpdateListener = null;

	/**
	 * 
	 * @param context
	 *            Activity本體
	 * @param output
	 *            儲存路徑
	 */
	public MyDownload(Context context, File output) {
		this.context = context;
		this.output = output;
	}

	public MyDownload(Context context) {
		this.context = context;
	}

	public void postOutput(File output) {
		this.output = output;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

		if (hasNet = MyNetWork.getNetStatus(context))
			publishProgress(DOWNLOAD_PREPARE);
		isRunning = true;
	}

	@Override
	protected Long doInBackground(String... params) {
		if (hasNet)
			try {
				URL url = new URL(params[0]);
				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();

				urlConnection.setRequestMethod("GET");
				urlConnection.connect();
				// 判斷網址是否存在
				if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND)
					publishProgress(ERRORCODE_HTTP_NOT_FOUND);
				// 判斷SDCARD是否存在
				else if (!MySDCard.getSDStatus())
					publishProgress(ERRORCODE_SDCARD_OUT);
				else {
					float fileLength = urlConnection.getContentLength();
					// 判斷檔案大小是否大於SD容量
					if (fileLength > MySDCard.getFreeByte())
						publishProgress(ERRORCODE_STORAGE_OUT);
					else {
						publishProgress(HTTP_FOUND);
						InputStream is = urlConnection.getInputStream();

						if (output == null)
							output = File.createTempFile("temp", null,
									MySDCard.getExternalCacheDir(context));
						if (!output.exists())
							output.createNewFile();
						FileOutputStream fos = new FileOutputStream(output);
						int flag = 0;
						float readData = 0;
						byte[] data = new byte[1024];
						int percent = 0;
						publishProgress(DOWNLOAD_START);
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
							context.sendBroadcast(new Intent(
									Intent.ACTION_MEDIA_MOUNTED,
									Uri.parse("file://"
											+ Environment
													.getExternalStorageDirectory())));
							publishProgress(DOWNLOAD_SUCCESS);
						} else
							output.delete();
						fos.flush();
						fos.close();
						is.close();
					}
				}
			} catch (MalformedURLException e) {
				publishProgress(ERRORCODE_TIME_OUT);
			} catch (IOException e) {
				publishProgress(ERRORCODE_TIME_OUT);
			}
		else
			publishProgress(ERRORCODE_NETWORK_OUT);
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		switch (values[0]) {
		case DOWNLOAD_PREPARE:
			if (onPrepareListener != null)
				onPrepareListener.onPrepare();
			break;
		case ERRORCODE_HTTP_NOT_FOUND:
		case ERRORCODE_STORAGE_OUT:
		case ERRORCODE_NETWORK_OUT:
		case ERRORCODE_SDCARD_OUT:
		case ERRORCODE_TIME_OUT:
			if (onErrorListener != null)
				onErrorListener.onError(values[0]);
			break;
		case HTTP_FOUND:
			if (onHttpFoundListener != null)
				onHttpFoundListener.onHttpFound();
			break;
		case DOWNLOAD_START:
			if (onDownloadStartListener != null)
				onDownloadStartListener.onDownloadStart();
			break;
		case DOWNLOAD_SUCCESS:
			isSuccess = true;
			break;
		default:
			if (onProgressUpdateListener != null)
				onProgressUpdateListener.onProgressUpdate(values[0]);
			break;
		}
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Long result) {
		super.onPostExecute(result);
		if (onCompeleteListener != null)
			onCompeleteListener.onComplete(isSuccess, output);
		isRunning = false;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void stop() {
		isRunning = false;
	}

	public OnPrepareListener getOnPrepareListener() {
		return onPrepareListener;
	}

	public void setOnPrepareListener(OnPrepareListener onPrepareListener) {
		this.onPrepareListener = onPrepareListener;
	}

	public OnHttpFoundListener getOnHttpFoundListener() {
		return onHttpFoundListener;
	}

	public void setOnHttpFoundListener(OnHttpFoundListener onHttpFoundListener) {
		this.onHttpFoundListener = onHttpFoundListener;
	}

	public OnDownloadStartListener getOnDownloadStartListener() {
		return onDownloadStartListener;
	}

	public void setOnDownloadStartListener(
			OnDownloadStartListener onDownloadStartListener) {
		this.onDownloadStartListener = onDownloadStartListener;
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

	public interface OnPrepareListener {
		/** 解析檔案中 */
		public void onPrepare();
	}

	public interface OnErrorListener {
		/**
		 * 當錯誤發生
		 * 
		 * @param ErrorCode
		 *            ErrorCode會有三種狀況<br>
		 *            ERRORCODE_HTTP_NOT_FOUND - 當檔案不存在<br>
		 *            ERRORCODE_STORAGE_OUT - 儲存空間不夠<br>
		 *            ERRORCODE_NETWORK_OUT - 無網路 ERRORCODE_SDCARD_OUT - 無SD卡
		 *            ERRORCODE_TIME_OUT - 連線逾時
		 */
		public void onError(int ErrorCode);
	}

	public interface OnHttpFoundListener {
		/** 取得網址 */
		public void onHttpFound();
	}

	public interface OnDownloadStartListener {
		/** 開始下載 */
		public void onDownloadStart();
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
}
