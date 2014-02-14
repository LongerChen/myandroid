package myandroid.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class BaseActivity extends Activity {
	protected Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this;
	}

	public void startActivity(Class<?> cls) {
		super.startActivity(new Intent(context, cls));
	}

	public void startActivityWithNoAnim(Class<?> cls) {
		startActivity(cls);
		overridePendingTransition(0, 0);
	}
}
