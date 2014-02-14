package myandroid.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Scanner;

public class HttpResponse {

	public static String getString(HttpURLConnection connection) {
		if (connection == null)
			return null;
		try {
			return new Scanner(connection.getInputStream(), "UTF-8")
					.useDelimiter("\\A").next();
		} catch (IOException e) {
			return null;
		}
	}

	public static InputStream getInputStream(HttpURLConnection connection) {
		if (connection == null)
			return null;
		try {
			return connection.getInputStream();
		} catch (IOException e) {
			return null;
		}
	}
}
