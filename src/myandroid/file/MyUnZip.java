package myandroid.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


public class MyUnZip {
	public static final int ERRORCODE_FILE_NOT_EXISTS = -1;
	public static final int ERRORCODE_FILE_NOT_ZIP = -3;
	public static final int ERRORCODE_FILE_IO = -7;
	final int FILE_EXISTS = -2;
	final int UNZIP_PREPARE = -4;
	final int UNZIP_FAIL = -5;
	final int UNZIP_SUCCESS = -6;
	ZipFile zipFile = null;
	File outputPath = null;
	int fileCount = 0;
	Enumeration<ZipEntry> entrys = null;

	OnPrepareListener onPrepareListener = null;
	OnUnZipStartListener onUnZipStartListener = null;
	OnCompeleteListener onCompeleteListener = null;
	OnErrorListener onErrorListener = null;
	OnProgressUpdateListener onProgressUpdateListener = null;
	OnGetFileListener onGetFileListener = null;

	public MyUnZip() {

	}

	public MyUnZip(File sourceFile) {
		postZipFile(sourceFile);
	}

	public void postZipFile(File sourceFile) {
		changeStatus(UNZIP_PREPARE);
		if (sourceFile.exists()) {
			changeStatus(FILE_EXISTS);
			try {
				zipFile = new ZipFile(sourceFile);
				entrys = (Enumeration<ZipEntry>) zipFile.entries();
				fileCount = zipFile.size();
			} catch (ZipException e) {
				changeStatus(ERRORCODE_FILE_NOT_ZIP);
			} catch (IOException e) {
				changeStatus(ERRORCODE_FILE_IO);
			}
		} else
			changeStatus(ERRORCODE_FILE_NOT_EXISTS);
	}

	public void unZip(File outputPath) {
		this.outputPath = outputPath;

		/* 若zipFile或entrys為null代表初始化沒成功 */
		if ((zipFile != null) && (entrys != null)) {

			/* 輸出目錄不存在則建立目錄 */
			if (!outputPath.exists())
				outputPath.mkdirs();

			try {
				int unzipIndex = 0;/* 作為紀錄解壓縮到第幾個檔案 */
				while (entrys.hasMoreElements()) {
					ZipEntry entry = entrys.nextElement();/* 取得下一個壓縮檔 */
					if (onProgressUpdateListener != null)
						onProgressUpdateListener
								.onProgressUpdate((int) ((float) unzipIndex
										/ (float) fileCount * 100),
										entry.getName());
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
					if (percent == 100) {
						changeStatus(UNZIP_SUCCESS);
						if (onProgressUpdateListener != null)
							onProgressUpdateListener.onProgressUpdate(
									(int) percent, "Complete");
					}

				}
			} catch (IOException e) {
				changeStatus(ERRORCODE_FILE_IO);
			}
		}
	}

	private void changeStatus(int status) {
		switch (status) {
		case ERRORCODE_FILE_NOT_EXISTS:
		case ERRORCODE_FILE_NOT_ZIP:
		case ERRORCODE_FILE_IO:
			if (onErrorListener != null)
				onErrorListener.onError(status);
			break;
		case FILE_EXISTS:
			if (onGetFileListener != null)
				onGetFileListener.onGotFile();
			break;
		case UNZIP_PREPARE:
			if (onPrepareListener != null)
				onPrepareListener.onPrepare();
			break;
		case UNZIP_FAIL:
			if (onCompeleteListener != null)
				onCompeleteListener.onComplete(false, outputPath);
			break;
		case UNZIP_SUCCESS:
			if (onCompeleteListener != null)
				onCompeleteListener.onComplete(true, outputPath);
			break;
		default:
			break;
		}
	}

	public OnPrepareListener getOnPrepareListener() {
		return onPrepareListener;
	}

	public void setOnPrepareListener(OnPrepareListener onPrepareListener) {
		this.onPrepareListener = onPrepareListener;
	}

	public OnUnZipStartListener getOnUnZipStartListener() {
		return onUnZipStartListener;
	}

	public void setOnUnZipStartListener(
			OnUnZipStartListener onUnZipStartListener) {
		this.onUnZipStartListener = onUnZipStartListener;
	}

	public OnCompeleteListener getOnCompeleteListener() {
		return onCompeleteListener;
	}

	public void setOnCompeleteListener(OnCompeleteListener onCompeleteListener) {
		this.onCompeleteListener = onCompeleteListener;
	}

	public OnErrorListener getOnErrorListener() {
		return onErrorListener;
	}

	public void setOnErrorListener(OnErrorListener onErrorListener) {
		this.onErrorListener = onErrorListener;
	}

	public OnProgressUpdateListener getOnProgressUpdateListener() {
		return onProgressUpdateListener;
	}

	public void setOnProgressUpdateListener(
			OnProgressUpdateListener onProgressUpdateListener) {
		this.onProgressUpdateListener = onProgressUpdateListener;
	}

	public OnGetFileListener getOnGetFileListener() {
		return onGetFileListener;
	}

	public void setOnGetFileListener(OnGetFileListener onGetFileListener) {
		this.onGetFileListener = onGetFileListener;
	}

	/** 當解析檔案時 */
	public interface OnPrepareListener {
		/** 解析檔案中 */
		public void onPrepare();
	}

	/** 當取得ZIP檔時 */
	public interface OnGetFileListener {

		/** 取得ZIP檔 */
		public void onGotFile();
	}

	/** 當錯誤發生時 */
	public interface OnErrorListener {

		/**
		 * 當錯誤發生
		 * 
		 * @param ErrorCode
		 *            ErrorCode會有三種狀況<br>
		 *            ERRORCODE_FILE_NOT_EXISTS<br>
		 *            <li>檔案不存在<br>
		 *            ERRORCODE_FILE_NOT_ZIP<br> <li>檔案不是一個ZIP檔案<br>
		 *            ERRORCODE_FILE_IO<br> <li>也許你的壓縮檔內容有變更過，因此將資料夾誤認為檔案<br>
		 *            <li>也許你讀寫的檔案正被使用中<br> <li>當你想把檔案寫入資料夾，卻發現資料夾是一個檔案時<br> <li>
		 *            其他 IO例外<br>
		 */
		public void onError(int ErrorCode);
	}

	/** 當進度更新時 */
	public interface OnProgressUpdateListener {
		/**
		 * 進度更新時
		 * 
		 * @param progress
		 *            進度為0~100%
		 */
		public void onProgressUpdate(int progress, String fileName);
	}

	/** 當開始解壓縮時 */
	public interface OnUnZipStartListener {
		/** 開始解壓縮 */
		public void onUnZipStart();
	}

	/** 當完成解壓縮時 */
	public interface OnCompeleteListener {
		/**
		 * 完成解壓縮
		 * 
		 * @param isSuccess
		 *            解壓縮成功或失敗
		 * @param outputPath
		 *            解壓縮根目錄
		 */
		public void onComplete(boolean isSuccess, File outputPath);
	}
}
