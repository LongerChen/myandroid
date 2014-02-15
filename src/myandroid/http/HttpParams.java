package myandroid.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpParams {

	public static String convert(Map<String, Object> params) {
		String returnObj = "";
		if (params == null)
			return returnObj;
		for (String key : params.keySet()) {
			if (key == null)
				continue;
			try {
				returnObj += "&" + URLEncoder.encode(key, "UTF-8") + "=";
				returnObj += params.get(key) == null ? "" : URLEncoder.encode(
						params.get(key).toString(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// returnObj += "&" + key + "=" + params.get(key);
		}
		return returnObj.startsWith("&") ? returnObj.substring(1,
				returnObj.length()) : returnObj;
	}

	public static Map<String, Object> convert(String source) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (source == null)
			return params;
		String[] keyValue = source.split("&");
		if (keyValue == null)
			return params;
		for (String kv : keyValue) {
			String[] kvv = kv.split("=");
			if (kvv.length != 2)
				continue;
			params.put(kvv[0], kvv[1]);
		}
		return params;
	}
}
