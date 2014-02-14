package myandroid.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.os.AsyncTask;

public class MyUnZip2 extends AsyncTask<File, Integer, Void> {
	public static final int ERRORCODE_NOT_EXISTS = -1;
	public static final int ERRORCODE_NOT_ZIP = -3;
	public static final int ERRORCODE_FILE_IO = -7;

	public static final int STATUS_PREPARE = -4;
	public static final int STATUS_EXISTS = -2;
	public static final int STATUS_COMPELETE = -5;

	OnErrorListener onErrorListener = null;
	OnProgressUpdateListener onProgressUpdateListener = null;
	OnCompeleteListener onCompeleteListener = null;
	OnStatusChangeListener onStatusChangeListener = null;

	Enumeration<ZipEntry> entrys = null;
	ZipFile zipFile = null;
	File sourceFile = null;
	File outputPath = null;
	int fileCount = 0;

	boolean isSuccess = false;

	public MyUnZip2() {

	}

	public MyUnZip2(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	public MyUnZip2 postZipFile(File sourceFile) {
		this.sourceFile = sourceFile;

		return this;
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected Void doInBackground(File... params) {
		publishProgress(STATUS_PREPARE);

		this.outputPath = params[0];

		if (sourceFile.exists()) {
			publishProgress(STATUS_EXISTS);
			try {
				zipFile = new ZipFile(sourceFile);
				entrys = (Enumeration<ZipEntry>) zipFile.entries();
				fileCount = zipFile.size();

				/* 輸出目錄不存在則建立目錄 */
				if (!outputPath.exists())
					outputPath.mkdirs();

				int unzipIndex = 0;/* 作為紀錄解壓縮到第幾個檔案 */
				while (entrys.hasMoreElements()) {
					ZipEntry entry = entrys.nextElement();/* 取得下一個壓縮檔 */
					File outputFile = new File(outputPath.getAbsolutePath()
							+ "/" + entry.getName());/* 儲存目錄 */

					/* 當資料類型為資料夾時，檢查outputPath對應目錄是否存在，不存在則建立目錄 */
					if (entry.isDirectory()) {
						if (!outputFile.exists())
							outputFile.mkdirs();
					} else {
						/* 檔案若不存在則建立空檔案 */
						if (!outputFile.exists())
							outputFile.createNewFile();
						InputStream is = zipFile.getInputStream(entry);/* 取得壓縮檔的InputStream */
						FileOutputStream fos = new FileOutputStream(outputFile);/* 輸出串流 */
						byte[] buffer = new byte[1024];
						int flag = 0;
						/* 檔案輸出 */
						while ((flag = is.read(buffer)) != -1) {
							fos.write(buffer, 0, flag);
						}
						fos.flush();
						fos.close();
						is.close();
					}
					unzipIndex++;
					float percent = (float) unzipIndex / (float) fileCount
							* 100;
					publishProgress((int) percent);
					if (percent == 100) {
						isSuccess = true;
						publishProgress(STATUS_COMPELETE);
					}
				}
			} catch (ZipException e) {
				publishProgress(ERRORCODE_NOT_ZIP);
			} catch (IOException e) {
				publishProgress(ERRORCODE_FILE_IO);
			}
		} else {
			publishProgress(ERRORCODE_NOT_EXISTS);
		}

		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		switch (values[0]) {
		case ERRORCODE_NOT_EXISTS:
		case ERRORCODE_NOT_ZIP:
		case ERRORCODE_FILE_IO:
			if (onErrorListener != null)
				onErrorListener.onError(values[0]);
			break;

		case STATUS_EXISTS:
		case STATUS_PREPARE:
		case STATUS_COMPELETE:
			if (onStatusChangeListener != null)
				onStatusChangeListener.onStatusChange(values[0]);
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
			onCompeleteListener.onComplete(isSuccess);
	}

	public MyUnZip2 setOnErrorListener(OnErrorListener onErrorListener) {
		this.onErrorListener = onErrorListener;
		return this;
	}

	public MyUnZip2 setOnCompeleteListener(
			OnCompeleteListener onCompeleteListener) {
		this.onCompeleteListener = onCompeleteListener;
		return this;
	}

	public MyUnZip2 setOnProgressUpdateListener(
			OnProgressUpdateListener onProgressUpdateListener) {
		this.onProgressUpdateListener = onProgressUpdateListener;
		return this;
	}

	public MyUnZip2 setOnStatusChangeListener(
			OnStatusChangeListener onStatusChangeListener) {
		this.onStatusChangeListener = onStatusChangeListener;
		return this;
	}

	public interface OnErrorListener {
		/**
		 * 當錯誤發生
		 * 
		 * @param ErrorCode
		 *            ErrorCode會有三種狀況<br>
		 *            <li>ERRORCODE_NOT_EXISTS - 當檔案不存在<br> <li>
		 *            ERRORCODE_NOT_ZIP - 非ZIP檔<br> <li>ERRORCODE_FILE_IO - 存取失敗
		 */
		public void onError(int ErrorCode);
	}

	public interface OnStatusChangeListener {
		/**
		 * 
		 * @param StatusCode
		 *            StatusCode會有三種狀況<br>
		 *            <li>STATUSCODE_PREPARE - 準備<br> <li>STATUS_EXISTS - 取得
		 *            <br> <li>STATUSCODE_COMPELETE - 完成
		 */
		public void onStatusChange(int StatusCode);
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
		 * 取得結果
		 * 
		 * @param result
		 *            <li>true - 失敗<li>false - 成功
		 */
		public void onComplete(boolean isSuccess);
	}
}
