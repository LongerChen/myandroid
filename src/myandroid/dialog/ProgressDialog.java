package myandroid.dialog;

import myandroid.layout.RelativeLayout;
import myandroid.layout.Sizer;
import myandroid.view.MatchView;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class ProgressDialog extends Dialog {
	MatchView progress;
	RotateAnimation anim;
	Sizer s;

	public ProgressDialog(Context context, int resourceIdOfImage) {
		super(context);
		s = new Sizer(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setCancelable(false);
		addContentView(progress = new MatchView(context,MatchView.W),
				new LayoutParams(s.viewPxFromWidth(20), RelativeLayout.W));
		progress.setImageResource(resourceIdOfImage);
		anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF,
				.5f, Animation.RELATIVE_TO_SELF, .5f);
		anim.setRepeatCount(Animation.INFINITE);
		anim.setInterpolator(new LinearInterpolator());
		anim.setDuration(1000);
	}

	public void setDuration(int durationMillis) {
		anim.setDuration(durationMillis);
	}

	@Override
	public void show() {
		super.show();
		progress.startAnimation(anim);
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

}
