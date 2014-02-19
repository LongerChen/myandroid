package myandroid.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public abstract class BaseFragmentActivity extends FragmentActivity {
	protected Context context;
	protected Fragment instanceFragment = null;
	protected FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		fragmentManager = getSupportFragmentManager();
	}

	public void changeFragment(Class<?> fragmentClass) {
		if (isFragmentEqual(fragmentClass))
			return;
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if ((instanceFragment = newFragment(fragmentClass)) == null)
			return;
		fragmentTransaction.replace(getContainerId(), instanceFragment)
				.commit();
	}

	protected abstract int getContainerId();

	public boolean isFragmentEqual(Class<?> fragmentClass) {
		if (instanceFragment == null)
			return false;
		return instanceFragment.getClass().getName()
				.equals(fragmentClass.getName());
	}

	public Fragment newFragment(Class<?> fragmentClass) {
		try {
			return (Fragment) fragmentClass.newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public void startActivity(Class<?> cls) {
		super.startActivity(new Intent(context, cls));
	}
}
