package myandroid.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpsFactory {

	public static HttpsURLConnection connect(String url) {
		return create(url, HttpMethod.CONNECT);
	}

	public static HttpsURLConnection delete(String url) {
		return create(url, HttpMethod.DELETE);
	}

	public static HttpsURLConnection get(String url) {
		return create(url, HttpMethod.GET);
	}

	public static HttpsURLConnection head(String url) {
		return create(url, HttpMethod.HEAD);
	}

	public static HttpsURLConnection options(String url) {
		return create(url, HttpMethod.OPTIONS);
	}

	public static HttpsURLConnection post(String url) {
		return create(url, HttpMethod.POST);
	}

	public static HttpsURLConnection post(String url, String body) {
		return create(url, HttpMethod.POST, body);
	}

	public static HttpsURLConnection put(String url) {
		return create(url, HttpMethod.PUT);
	}

	public static HttpsURLConnection put(String url, String body) {
		return create(url, HttpMethod.PUT, body);
	}

	public static HttpsURLConnection trace(String url) {
		return create(url, HttpMethod.TRACE);
	}

	private static HttpsURLConnection create(String url, String httpmethod) {
		HttpsURLConnection connection = null;
		try {
			connection = (HttpsURLConnection) new URL(url).openConnection();
			connection.setRequestMethod(httpmethod);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	private static HttpsURLConnection create(String url, String httpmethod,
			String body) {
		HttpsURLConnection connection = create(url, httpmethod);
		try {
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());
			out.writeBytes(body);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return connection;
	}

}
