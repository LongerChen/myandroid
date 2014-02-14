package myandroid.net;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import android.os.AsyncTask;
import android.util.Log;

/**
 * 使用 POST 或 GET 其實是有差別的，我們先說明一下 HTTP Method，<br>
 * 在 HTTP 1.1 的版本中定義了八種 Method (方法)，如下所示：<br>
 * <li>OPTIONS <li>GET <li>HEAD <li>POST <li>PUT <li>DELETE <li>TRACE <li>
 * CONNECT<br>
 * 先舉個例子，如果 HTTP 代表現在我們現實生活中寄信的機制，那麼信封的撰寫格式就是 HTTP。<br>
 * 我們姑且將信封外的內容稱為 http-header，信封內的書信稱為 message-body，那麼 HTTP Method就是你要告訴郵差的寄信規則。
 * <br>
 * 假設 GET 表示信封內不得裝信件的寄送方式，如同是明信片一樣，你可以把要傳遞的資訊寫在信封上，寫滿為止，價格比較便宜。<br>
 * 然而 POST 就是信封內有裝信件的寄送方式，不但信封可以寫東西，信封內還可以置入你想要寄送的資料或檔案，價格較貴。<br>
 * 使用 GET 的時候我們直接將要傳送的資料以 Query String加在我們要寄送的地址後面，然後交給郵差傳送。<br>
 * 使用 POST 的時候則是將寄送地址寫在信封上，另外將要傳送的資料寫在另一張信紙後，將信紙放到信封裡面，交給郵差傳送。
 * 
 * <li>http://blog.toright.com/archives/1203
 * 
 * @author Tony
 * 
 */
public class MyNetProcess extends AsyncTask<Void, Integer, Object> {
	public final static int ACTIONCODE_GET = -1;
	public final static int ACTIONCODE_POST = -2;
	public final static int ACTIONCODE_DOWNLOAD = -3;

	public static final int ERRORCODE_OUT_FOUND = -4;
	public static final int ERRORCODE_OUT_TIME = -5;
	public static final int ERRORCODE_OUT_IO = -6;

	public static final int STATUSCODE_PREPARE = -7;
	public static final int STATUSCODE_START = -8;
	public static final int STATUSCODE_COMPLETE = -9;

	OnErrorListener onErrorListener = null;
	OnCompeleteListener onCompeleteListener = null;
	OnProgressUpdateListener onProgressUpdateListener = null;
	OnStatusChangeListener onStatusChangeListener = null;

	String url = null;
	File output = null;
	boolean isSuccess = false;
	boolean isRunning = true;
	int httpMethod = 0;
	Map<String, String> param = new HashMap<String, String>();
	HttpURLConnection urlConnection;

	@Override
	protected void onPreExecute() {
		publishProgress(STATUSCODE_PREPARE);
		try {
			urlConnection = (HttpURLConnection) new URL(this.url)
					.openConnection();
			switch (httpMethod) {
			case ACTIONCODE_GET:
				urlConnection.setRequestMethod("GET");
				break;
			case ACTIONCODE_POST:
				urlConnection.setRequestMethod("POST");
				/** 設置是否向urlConnection輸出，因為這個是post請求，參數要放在 http正文內，因此需要設為true **/
				urlConnection.setDoOutput(true);
				break;
			case ACTIONCODE_DOWNLOAD:
				break;

			}
		} catch (IOException e) {
			publishProgress(ERRORCODE_OUT_IO);
		}
	}

	@Override
	protected Object doInBackground(Void... arg) {
		publishProgress(STATUSCODE_START);
		try {
			Object returnOb = null;
			urlConnection.connect();
			if(httpMethod == ACTIONCODE_POST){
				DataOutputStream out = new DataOutputStream(
						urlConnection.getOutputStream());
				for (String key : param.keySet()) {
					out.writeBytes(URLEncoder.encode(key, "UTF-8") + "="
							+ URLEncoder.encode(param.get(key), "UTF-8") + "&");
				}
				out.flush();
				out.close();
			}
			if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
				publishProgress(ERRORCODE_OUT_FOUND);
			} else {
				InputStream is;
				switch (httpMethod) {
				case ACTIONCODE_GET:
				case ACTIONCODE_POST:
					is = urlConnection.getInputStream();
					returnOb = new Scanner(is, "UTF-8").useDelimiter("\\A")
							.next();
					break;
				case ACTIONCODE_DOWNLOAD:
					float fileLength = urlConnection.getContentLength();
					is = urlConnection.getInputStream();

					if (output == null)
						output = File.createTempFile("temp", null);
					if (!output.exists())
						output.createNewFile();
					FileOutputStream fos = new FileOutputStream(output);
					int flag = 0;
					float readData = 0;
					byte[] data = new byte[1024];
					int percent = 0;
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
					if (!isRunning)
						output.delete();
					fos.flush();
					fos.close();
					is.close();
					returnOb = output;
					break;
				}
			}

			if (isRunning) {
				publishProgress(STATUSCODE_COMPLETE);
				isSuccess = true;
			}
			return returnOb;
		} catch (MalformedURLException e) {
			publishProgress(ERRORCODE_OUT_TIME);
		} catch (IOException e) {
			Log.e("io", e.getMessage());
			publishProgress(ERRORCODE_OUT_IO);
		}

		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		switch (values[0]) {
		case STATUSCODE_PREPARE:
		case STATUSCODE_START:
		case STATUSCODE_COMPLETE:
			if (onStatusChangeListener != null)
				onStatusChangeListener.onStatusChange(values[0]);
			break;
		case ERRORCODE_OUT_FOUND:
		case ERRORCODE_OUT_TIME:
		case ERRORCODE_OUT_IO:
			isSuccess = false;
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
	protected void onPostExecute(Object result) {
		if (onCompeleteListener != null)
			onCompeleteListener.onComplete(isSuccess, result);
	}

	/**
	 * HTTP GET
	 * 
	 * @param url
	 *            網址
	 */
	public MyNetProcess doGet(String url) {
		this.url = url;
		httpMethod = ACTIONCODE_GET;
		execute();
		return this;
	}

	/**
	 * HTTP POST
	 * 
	 * @param url
	 *            網址
	 * @param param
	 *            Content
	 */
	public MyNetProcess doPost(String url, Map<String, String> param) {
		this.url = url;
		this.param = param;
		httpMethod = ACTIONCODE_POST;
		execute();
		return this;
	}

	/**
	 * 下載檔案
	 * 
	 * @param url
	 *            網址
	 * @param output
	 *            存放目錄
	 */
	public MyNetProcess doDownload(String url, File output) {
		this.url = url;
		this.output = output;
		httpMethod = ACTIONCODE_DOWNLOAD;
		execute();
		return this;
	}
	
	public void stopDownload(){
		isRunning = false;
	}

	public OnErrorListener getOnErrorListener() {
		return onErrorListener;
	}

	public MyNetProcess setOnErrorListener(OnErrorListener onErrorListener) {
		this.onErrorListener = onErrorListener;
		return this;
	}

	public OnCompeleteListener getOnCompeleteListener() {
		return onCompeleteListener;
	}

	public MyNetProcess setOnCompeleteListener(
			OnCompeleteListener onCompeleteListener) {
		this.onCompeleteListener = onCompeleteListener;
		return this;
	}

	public OnProgressUpdateListener getOnProgressUpdateListener() {
		return onProgressUpdateListener;
	}

	public MyNetProcess setOnProgressUpdateListener(
			OnProgressUpdateListener onProgressUpdateListener) {
		this.onProgressUpdateListener = onProgressUpdateListener;
		return this;
	}

	public OnStatusChangeListener getOnStatusChangeListener() {
		return onStatusChangeListener;
	}

	public MyNetProcess setOnStatusChangeListener(
			OnStatusChangeListener onStatusChangeListener) {
		this.onStatusChangeListener = onStatusChangeListener;
		return this;
	}

	public interface OnErrorListener {
		/**
		 * 錯誤發生
		 * 
		 * @param ErrorCode
		 *            ErrorCode會有三種狀況<br>
		 *            ERRORCODE_OUT_FOUND - 網址404<br>
		 *            ERRORCODE_OUT_TIME - 連線逾時<br>
		 *            ERRORCODE_OUT_IO - 存取失敗
		 */
		public void onError(int ErrorCode);
	}

	public interface OnProgressUpdateListener {
		/**
		 * 進度更新
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
		 * @param object
		 *            結果
		 */
		public void onComplete(boolean isSuccess, Object object);
	}

	public interface OnStatusChangeListener {
		/**
		 * 
		 * @param StatusCode
		 *            StatusCode會有三種狀況<br>
		 *            <li>STATUSCODE_PREPARE - 準備<br> <li>STATUSCODE_START - 取得
		 *            <br> <li>STATUSCODE_COMPELETE - 完成
		 */
		public void onStatusChange(int StatusCode);
	}
}
