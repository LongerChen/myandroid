package myandroid.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Scanner;

import myandroid.tools.Develop;

public class HttpConnect {
	HttpURLConnection connect = null;

	public HttpConnect(String url) {
		try {
			connect = (HttpURLConnection) new URL(url).openConnection();
		} catch (MalformedURLException e) {
			Develop.e(this, "error url:::" + url);
		} catch (IOException e) {
			Develop.e(this, "error url:::" + url);
		}
	}

	public URL getUrl() {
		return connect.getURL();
	}

	public void setHttpMethod(String httpMethod) {
		if (connect == null)
			Develop.e(this, "instance first");
		else if (httpMethod == null)
			Develop.e(this, "httpMethod null");
		else
			try {
				connect.setRequestMethod(httpMethod);
			} catch (ProtocolException e) {
				Develop.e(this, "unsupport method");
			}
	}

	public void setTimeOutt(int timeoutMillis) {
		if (connect == null)
			Develop.e(this, "instance first");
		else
			connect.setConnectTimeout(timeoutMillis);
	}

	public void setRequestProperty(String field, String value) {
		if (connect == null)
			Develop.e(this, "instance first");
		else
			connect.setRequestProperty(field, value);
	}

	public void setRawData(String rawData) {
		if (connect == null)
			Develop.e(this, "instance first");
		else if (rawData == null)
			Develop.e(this, "rawData null");
		else
			try {
				DataOutputStream out = new DataOutputStream(
						connect.getOutputStream());
				byte[] data = rawData.getBytes();
				out.write(data, 0, data.length);
				out.flush();
				out.close();
				Develop.i(this, Develop.divider + "rawData" + Develop.divider);
				Develop.v(this, rawData);
				Develop.i(this, Develop.divider + "rawData" + Develop.divider);
			} catch (IOException e) {
				Develop.e(this, "This httpMethod doesn't support rawData");
			}
	}

	public void setParams(Map<String, String> params) {
		try {
			connect.setDoInput(true);
			connect.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(
					connect.getOutputStream());
			for (String key : params.keySet()) {
				Develop.e(this, key + ":" + params.get(key));
				out.writeBytes(URLEncoder.encode(key, "UTF-8") + "="
						+ URLEncoder.encode(params.get(key), "UTF-8") + "&");
				out.flush();
				out.close();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String connectForString() {
		if (connect == null)
			return null;
		try {
			connect.connect();
			Develop.i(this, Develop.divider + "START CONNECT" + Develop.divider);
			InputStream is = connect.getInputStream();
			String source = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
			connect.disconnect();
			Develop.v(this, "RequestUrl:::" + connect.getURL());
			Develop.v(this, "RequestMethod:::" + connect.getRequestMethod());
			Develop.v(this, "ResponseCode:::" + connect.getResponseCode());
			Develop.i(this, Develop.divider + "END CONNECT" + Develop.divider);
			Develop.i(this, Develop.divider + "Response" + Develop.divider);
			Develop.v(this, source);
			Develop.i(this, Develop.divider + "Response" + Develop.divider);
			return source;
		} catch (IOException e) {
			Develop.e(this, "no InputStream could be created");
			return null;
		}
	}

	public InputStream connectForStream() {
		if (connect == null)
			return null;
		try {
			Develop.v(this, "RequestUrl:::" + connect.getURL());
			connect.connect();
			Develop.i(this, Develop.divider + "START CONNECT" + Develop.divider);
			Develop.v(this, "Size:::" + connect.getContentLength() + " Byte");
			Develop.v(this, "ResponseCode:::" + connect.getResponseCode());
			Develop.i(this, Develop.divider + "END CONNECT" + Develop.divider);
			return connect.getInputStream();
		} catch (IOException e) {
			Develop.e(this, "no InputStream could be created");
			return null;
		}
	}
}
