package myandroid.base;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class BaseFragment<A extends Activity> extends Fragment
		implements OnClickListener {
	protected A activity;
	Context context;

	@Override
	public void onAttach(Activity activity) {
		this.activity = (A) activity;
		this.context = activity;
		super.onAttach(activity);
	}

	@Override
	public void onClick(View v) {

	}
}
