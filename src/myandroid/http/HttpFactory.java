package myandroid.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import myandroid.tools.Develop;

public class HttpFactory {

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
		if (body == null || connection == null)
			return connection;
		Develop.i(HttpFactory.class, "resquest:\n" + body);
		connection.setDoOutput(true);
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
