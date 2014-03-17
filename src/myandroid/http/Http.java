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

	public Http url(String url) {
		try {
			connect = (HttpURLConnection) new URL(url).openConnection();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Http.this;
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

	public HttpURLConnection body(String body) {
		if (connect == null) {
			Develop.e(this, "Call url() first");
			return connect;
		}
		if (connect.getRequestMethod().equals(HttpMethod.GET))
			return connect;

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
		return connect;
	}

	public static HttpURLConnection connect(String url) {
		return create(url, HttpMethod.CONNECT);
	}

	public static HttpURLConnection delete(String url) {
		return create(url, HttpMethod.DELETE);
	}

	public static HttpURLConnection get(String url) {
		return create(url, HttpMethod.GET);
	}

	public static HttpURLConnection head(String url) {
		return create(url, HttpMethod.HEAD);
	}

	public static HttpURLConnection options(String url) {
		return create(url, HttpMethod.OPTIONS);
	}

	public static HttpURLConnection post(String url) {
		return create(url, HttpMethod.POST);
	}

	public static HttpURLConnection post(String url, String body) {
		return create(url, HttpMethod.POST, body);
	}

	public static HttpURLConnection put(String url) {
		return create(url, HttpMethod.PUT);
	}

	public static HttpURLConnection put(String url, String body) {
		return create(url, HttpMethod.PUT, body);
	}

	public static HttpURLConnection trace(String url) {
		return create(url, HttpMethod.TRACE);
	}

	private static HttpURLConnection create(String url, String httpmethod) {
		return create(url, httpmethod, null);
	}

	private static HttpURLConnection create(String url, String httpmethod,
			String body) {
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod(httpmethod);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (httpmethod != HttpMethod.GET)
			connection.setDoOutput(true);
		if (body == null || connection == null)
			return connection;
		try {
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());

			byte[] data = body.getBytes();
			out.write(data, 0, data.length);
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return connection;
	}

}
