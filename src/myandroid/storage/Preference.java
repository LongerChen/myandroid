package myandroid.storage;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preference {
	SharedPreferences p;
	Editor e;

	public Preference(Context context, String name) {
		p = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		e = p.edit();
	}

	public void putString(String key, String value) {
		e.putString(key, value).commit();
	}

	public void putInt(String key, int value) {
		e.putInt(key, value).commit();
	}

	public void putFloat(String key, float value) {
		e.putFloat(key, value).commit();
	}

	public void putBoolean(String key, boolean value) {
		e.putBoolean(key, value).commit();
	}

	public void putBoolean(String key, long value) {
		e.putLong(key, value).commit();
	}

	public void putBoolean(String key, Set<String> value) {
		e.putStringSet(key, value).commit();
	}

	public String getString(String key, String defValue) {
		return p.getString(key, defValue);
	}

	public boolean getBoolean(String key, boolean defValue) {
		return p.getBoolean(key, defValue);
	}

	public int getInt(String key, int defValue) {
		return p.getInt(key, defValue);
	}

	public long getLong(String key, long defValue) {
		return p.getLong(key, defValue);
	}

	public float getFloat(String key, float defValue) {
		return p.getFloat(key, defValue);
	}

	public Set<String> getStringSet(String key, Set<String> defValue) {
		return p.getStringSet(key, defValue);
	}

	public void remove(String key) {
		e.remove(key).commit();
	}

}
