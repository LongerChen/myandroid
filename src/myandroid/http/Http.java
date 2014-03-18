package myandroid.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import myandroid.tools.Develop;

public class Http {
	static HttpURLConnection connect;

	public Http(String url) {
		try {
			connect = (HttpURLConnection) new URL(url).openConnection();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Http header(Map<String, String> headers) {
		if (headers == null)
			return this;
		if (connect == null) {
			Develop.e(this, "Call url() first");
			return this;
		}
		for (String key : headers.keySet())
			connect.setRequestProperty(key, headers.get(key));
		return this;
	}

	public Http method(String httpMethod) {
		try {
			connect.setRequestMethod(httpMethod);
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		return this;
	}

	public Http body(String body) {
		if (connect == null) {
			Develop.e(this, "Call url() first");
			return this;
		}
		if (connect.getRequestMethod().equals(HttpMethod.GET))
			return this;

		connect.setDoOutput(true);
		try {
			DataOutputStream out = new DataOutputStream(
					connect.getOutputStream());

			byte[] data = body.getBytes();
			out.write(data, 0, data.length);
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	public HttpURLConnection create() {
		return connect;
	}

}
