package myandroid.api;

import java.net.HttpURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import myandroid.async.task.AsyncApi;
import myandroid.http.HttpFactory;

public class ApiAppVersion extends AsyncApi<String> {
	String packageName;

	public ApiAppVersion(String packageName) {
		this.packageName = packageName;
	}

	@Override
	protected String onParse(String source) {
		if(source==null)
			return null;
		try {
			JSONObject response = new JSONObject(source);
			String version = response.getString("version");
			return version.equals("null") ? null : version;
		} catch (JSONException e) {
			return null;
		}
	}

	@Override
	protected HttpURLConnection getDefaultConnection() {
		return HttpFactory.get("https://androidquery.appspot.com/api/market?app=" + packageName);
	}

}
