package myandroid.async.task;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import myandroid.async.AsyncRunnable;
import myandroid.http.HttpFactory;
import myandroid.tools.Develop;

public class AsyncResponseCode extends AsyncRunnable<Map<String, Integer>> {

	Map<String, String> urls = new HashMap<String, String>();

	public AsyncResponseCode(Map<String, String> urls) {
		this.urls = urls == null ? this.urls : urls;
	}

	@Override
	protected Map<String, Integer> onExecute() {
		Map<String, Integer> responseCodes = new HashMap<String, Integer>();
		for (String key : urls.keySet()) {
			HttpURLConnection connection = HttpFactory.get(urls.get(key));
			if (connection != null)
				try {
					int code = connection.getResponseCode();
					Develop.i(this, code);
					responseCodes.put(key, code);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			connection.disconnect();
		}
		return responseCodes;
	}
}
