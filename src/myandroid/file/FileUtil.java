package myandroid.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

/**
 * 檔案處理工具
 * 
 * @author Tony
 * 
 */
public class FileUtil {
	Context context;

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public FileUtil(Context context) {
		this.context = context;
	}

	/**
	 * 從Assets中複製檔案
	 * 
	 * @param fileName
	 *            檔案名
	 * @param output
	 *            目的地
	 */
	public void copyAssetsFile(String fileName, File output) {
		try {
			createFloder(output);
			InputStream is = context.getResources().getAssets().open(fileName);
			FileOutputStream fos = new FileOutputStream(output);
			copyFile(is, fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 從Raw中複製檔案
	 * 
	 * @param resId
	 *            資料來源
	 * @param output
	 *            目的地
	 */
	public void copyRawFile(int resId, File output) {
		try {
			createFloder(output);
			InputStream is = context.getResources().openRawResource(resId);
			FileOutputStream fos = new FileOutputStream(output);
			copyFile(is, fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param input
	 *            資料來源
	 * @param output
	 *            目的地
	 */
	public void copyFile(File input, File output) {
		try {
			createFloder(output);
			InputStream is = new FileInputStream(input);
			FileOutputStream fos = new FileOutputStream(output);
			copyFile(is, fos);
			// KgzsLog.e(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 建立檔案父目錄
	 * 
	 * @param file
	 */
	public void createFloder(File file) {
		File folder = new File(file.getParent());
		if (!folder.exists())
			folder.mkdirs();
	}

	/**
	 * 從串流複製檔案
	 * 
	 * @param is
	 * @param fos
	 * @throws IOException
	 */
	public void copyFile(InputStream is, FileOutputStream fos)
			throws IOException {
		byte[] buffer = new byte[1024];
		while (is.read(buffer) != -1)
			fos.write(buffer);
	}
}
