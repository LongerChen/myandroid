package myandroid.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

public class Develop {
	final public static String divider = "====================";
	static boolean DEBUG = false;

	public static void v(Object c, Object log) {
		if (DEBUG)
			Log.v(getClassName(c), getLog(log));
	}

	public static void e(Object c, Object log) {
		if (DEBUG)
			Log.e(getClassName(c), getLog(log));
	}

	public static void d(Object c, Object log) {
		if (DEBUG)
			Log.d(getClassName(c), getLog(log));
	}

	public static void i(Object c, Object log) {
		if (DEBUG)
			Log.i(getClassName(c), getLog(log));
	}

	public static void w(Object c, Object log) {
		if (DEBUG)
			Log.w(getClassName(c), getLog(log));
	}

	public static void toast(Context context, Object obj, int duration) {
		Toast.makeText(context, obj.toString(), duration).show();
	}

	public static void toast(Context context, int resId, int duration) {
		Toast.makeText(context, context.getString(resId), duration).show();
	}

	private static String getClassName(Object c) {
		return c == null ? "isNull" : c.getClass().getName();
	}

	private static String getLog(Object log) {
		return log == null ? "isNull" : log.toString();
	}

	public static void setDebug(boolean isDebug) {
		DEBUG = isDebug;
	}

	public static boolean isDebug() {
		return DEBUG;
	}

	/**
	 * Set Developer Label at layout TOP_LEFT Load畫面上顯示"Developer"字樣
	 */

	public static void addDevText(Context context, final ViewGroup layout) {
		final TextView devText = new TextView(context);
		if (DEBUG) {
			layout.addView(devText, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);

			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"MM/dd HH:mm");
			String s = dateFormat.format(getBuildDate(context));
			devText.setText("This is Developer " + s);
			
			devText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
			devText.setBackgroundColor(Color.BLACK);
			devText.setTextColor(Color.WHITE);
			AnimationSet animSet = new AnimationSet(false);
			animSet.addAnimation(new TranslateAnimation(0, 0, -devText
					.getLineHeight(), 0));
			animSet.addAnimation(new AlphaAnimation(0, 1));
			animSet.setDuration(500);
			devText.startAnimation(animSet);
			new CountDownTimer(5000, 5000) {

				@Override
				public void onTick(long arg0) {

				}

				@Override
				public void onFinish() {
					layout.removeView(devText);
				}
			}.start();
		}
	}

	public static Date getBuildDate(Context context) {
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(), 0);
			ZipFile zf = new ZipFile(ai.sourceDir);
			ZipEntry ze = zf.getEntry("classes.dex");
			long time = ze.getTime();
			return new Date(time);
		} catch (Exception e) {
			return new Date();
		}
	}

	public static void addDevText(Context context, String title) {

		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_FULLSCREEN,
				PixelFormat.TRANSLUCENT);
		final TextView textView = new TextView(context);
		textView.setBackgroundColor(Color.argb(200, 0, 0, 0));
		textView.setTextColor(Color.GRAY);
		textView.setText(title);

		final WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		manager.addView(textView, params);
		Animation in = new AlphaAnimation(0, 1);
		in.setDuration(1000);
		final Animation out = new AlphaAnimation(1, 0);
		out.setDuration(1000);
		textView.startAnimation(in);
		new CountDownTimer(2000, 3000) {

			@Override
			public void onTick(long millisUntilFinished) {
				textView.startAnimation(out);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				manager.removeView(textView);
			}
		}.start();
	}
}
