package myandroid.net;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

/**
 * 使用AsyncTask為基底取得Http本文
 * 
 * @author Tony
 * 
 */
public class MyHttpGetTxt extends AsyncTask<String, Integer, String> {
	public static final int ERRORCODE_NOT_FOUND = -1;
	public static final int ERRORCODE_TIME_OUT = -2;

	public static final int STATUSCODE_PREPARE = -3;
	public static final int STATUSCODE_FOUND = -4;
	public static final int STATUSCODE_COMPELETE = -5;

	OnErrorListener onErrorListener = null;
	OnCompeleteListener onCompeleteListener = null;
	OnStatusChangeListener onStatusChangeListener = null;

	public MyHttpGetTxt() {

	}

	@Override
	protected void onPreExecute() {
		publishProgress(STATUSCODE_PREPARE);
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(params[0]);
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_NOT_FOUND)
				publishProgress(ERRORCODE_NOT_FOUND);
			else {
				publishProgress(STATUSCODE_FOUND);
				return EntityUtils.toString(response.getEntity());
			}
		} catch (ParseException e) {
			publishProgress(ERRORCODE_TIME_OUT);
		} catch (IOException e) {
			publishProgress(ERRORCODE_TIME_OUT);
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		switch (values[0]) {
		case STATUSCODE_PREPARE:
		case STATUSCODE_FOUND:
		case STATUSCODE_COMPELETE:
			if (onStatusChangeListener != null)
				onStatusChangeListener.onStatusChange(values[0]);
			break;
		case ERRORCODE_NOT_FOUND:
		case ERRORCODE_TIME_OUT:
			if (onErrorListener != null)
				onErrorListener.onError(values[0]);
			break;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		if (onCompeleteListener != null)
			onCompeleteListener.onComplete(result);
		publishProgress(STATUSCODE_COMPELETE);
	}

	public void setOnErrorListener(OnErrorListener onErrorListener) {
		this.onErrorListener = onErrorListener;
	}

	public void setOnCompeleteListener(OnCompeleteListener onCompeleteListener) {
		this.onCompeleteListener = onCompeleteListener;
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
		 *            <li>ERRORCODE_HTTP_NOT_FOUND - 當檔案不存在<br>
		 *            <li>ERRORCODE_TIME_OUT - 連線逾時
		 */
		public void onError(int ErrorCode);
	}

	public interface OnStatusChangeListener {
		/**
		 * 
		 * @param StatusCode
		 *            StatusCode會有三種狀況<br>
		 *            <li>STATUSCODE_PREPARE - 準備<br>
		 *            <li>STATUSCODE_FOUND - 取得<br>
		 *            <li>STATUSCODE_COMPELETE - 完成
		 */
		public void onStatusChange(int StatusCode);
	}

	public interface OnCompeleteListener {
		/**
		 * 取得結果
		 * 
		 * @param result
		 *            <li>null - 失敗<li>String result - 成功
		 */
		public void onComplete(String result);
	}
}
