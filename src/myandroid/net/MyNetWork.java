package myandroid.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MyNetWork {
	/**
	 * 取得NetWork狀態
	 * 
	 * @param true:可用
	 * @param false:不可用
	 * 
	 */
	public static boolean getNetStatus(Context context) {
		ConnectivityManager cm  = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networInfo = cm.getActiveNetworkInfo();
		return networInfo != null && networInfo.isAvailable();
	}
}
