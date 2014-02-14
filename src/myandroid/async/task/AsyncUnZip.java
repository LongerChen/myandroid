package myandroid.async.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import myandroid.async.AsyncRunnable;

public class AsyncUnZip extends AsyncRunnable<File> {
	ZipFile zipFile;
	File output;

	public void initialize(ZipFile zipFile, File output) {
		this.zipFile = zipFile;
		this.output = output;
	}

	public void initialize(File zipFile, File output) {
		try {
			this.zipFile = new ZipFile(zipFile);
			this.output = output;
		} catch (ZipException e) {
		} catch (IOException e) {
		}
	}

	@Override
	protected File onExecute() {
		if (zipFile == null) 
			return null;
		try {
			Enumeration<ZipEntry> entrys = (Enumeration<ZipEntry>) zipFile
					.entries();

			/* 輸出目錄不存在則建立目錄 */
			if (!output.exists())
				output.mkdirs();

			while (entrys.hasMoreElements()) {
				ZipEntry entry = entrys.nextElement();/* 取得下一個壓縮檔 */
				File outputFile = new File(output.getAbsolutePath(),
						entry.getName());/* 儲存目錄 */

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
			}
			return output;
		} catch (IOException e) {
			return null;
		}
	}

}
