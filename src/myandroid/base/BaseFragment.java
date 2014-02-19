package myandroid.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

public class BaseFragment extends Fragment implements OnClickListener {
	protected Activity activity;

	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public void onClick(View v) {

	}
}
