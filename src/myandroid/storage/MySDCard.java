package myandroid.storage;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

public class MySDCard {

	/**
	 * 取得SD狀態
	 * 
	 * @param true:可用
	 * @param false:不可用
	 * 
	 */
	public static boolean getSDStatus() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 取得剩餘空間(Byte)
	 * 
	 */
	public static long getFreeByte() {
		// 取得SD卡文件路徑
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 獲取單個數據塊的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空閒的數據塊的數量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空閒大小
		return freeBlocks * blockSize; // 單位MB
	}

	/**
	 * 取得剩餘空間(KByte)
	 * 
	 */
	public static double getFreeKByte() {
		return getFreeByte() / 1024;
	}

	/**
	 * 取得剩餘空間(KByte)
	 * 
	 * @param offset
	 *            小數點留幾位
	 */
	public static double getFreeKByte(int offset) {
		double offset2 = Math.pow(10, offset);
		return ((int) (getFreeKByte() * offset2)) / offset2;
	}

	/**
	 * 取得剩餘空間(MByte)
	 * 
	 */
	public static double getFreeMByte() {
		return getFreeKByte() / 1024;
	}

	/**
	 * 取得剩餘空間(MByte)
	 * 
	 * @param offset
	 *            小數點留幾位
	 */
	public double getFreeMByte(int offset) {
		double offset2 = Math.pow(10, offset);
		return ((int) (getFreeMByte() * offset2)) / offset2;
	}

	/**
	 * 取得剩餘空間(GByte)
	 * 
	 */
	public static double getFreeGByte() {
		return getFreeMByte() / 1024;
	}

	/**
	 * 取得剩餘空間(GByte)
	 * 
	 * @param offset
	 *            小數點留幾位
	 */
	public static double getFreeGByte(int offset) {
		double offset2 = Math.pow(10, offset);
		return ((int) (getFreeGByte() * offset2)) / offset2;
	}

	/**
	 * 取得暫存目錄
	 * 
	 * @param context
	 * @return 暫存目錄
	 */
	public static File getExternalCacheDir(Context context) {

		return context.getExternalCacheDir();

	}
	/**
	 * 取得檔案目錄
	 * 
	 * @param context
	 * @return 檔案目錄
	 */
	public static File getExternalFilesDir(Context context, String type) {

		return context.getExternalFilesDir(type);

	}
}
