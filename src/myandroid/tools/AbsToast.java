package myandroid.tools;

import java.util.Timer;
import java.util.TimerTask;

import com.myandroid.R;

import myandroid.layout.Sizer;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

public class AbsToast {
	static WindowManager manager;
	static Sizer s;

	static WindowManager.LayoutParams params = new WindowManager.LayoutParams(
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
			PixelFormat.TRANSLUCENT);

	private AbsToast() {
		
	}
	
	public static void makeToast(Context context, CharSequence text) {
		manager = manager == null ? (WindowManager) context
				.getSystemService(Application.WINDOW_SERVICE) : manager;
		s = s == null ? new Sizer(context) : s;
		params.y = s.viewPxFromHeight(30);
		
		final TextView textView = new TextView(context);
		textView.setText(text);
		textView.setBackgroundResource(R.drawable.toast_bg);
		int p = s.viewPxFromWidth(3);
		textView.setPadding(p, p, p, p);
		manager.addView(textView, params);

		new CountDownTimer(3000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFinish() {
				manager.removeView(textView);
			}
		}.start();
	}
}
