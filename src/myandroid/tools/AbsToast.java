package myandroid.tools;

import java.util.Timer;
import java.util.TimerTask;

import myandroid.layout.Sizer;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class AbsToast {
	TextView textView;
	static WindowManager manager;
	static Sizer s;
	Timer timer = new Timer();

	static WindowManager.LayoutParams params = new WindowManager.LayoutParams(
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
			PixelFormat.TRANSLUCENT);
	static AbsToast absToast;

	private AbsToast(Context context, CharSequence text) {
		textView = textView == null ? new TextView(context) : textView;
		manager = manager == null ? (WindowManager) context
				.getSystemService(Application.WINDOW_SERVICE) : manager;
		s = s == null ? new Sizer(context) : s;
		params.y = s.viewPxFromHeight(30);
	}

	public static AbsToast makeToast(Context context, CharSequence text) {
		absToast = absToast == null ? new AbsToast(context, text) : absToast;
		absToast.textView.setText(text);
		absToast.textView
				.setBackgroundColor(Color.argb(0x55, 0x55, 0x55, 0x55));
		int p = s.viewPxFromWidth(3);
		absToast.textView.setPadding(p, p, p, p);
		return absToast;
	}

	public void show() {
		if (absToast.textView.getParent() == null)
			manager.addView(absToast.textView, params);
		new CountDownTimer(3000, 3000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				if (absToast.textView.getParent() != null)
					manager.removeView(absToast.textView);
			}
		}.start();
	}
}
