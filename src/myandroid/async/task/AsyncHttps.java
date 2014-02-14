package myandroid.async.task;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import myandroid.async.AsyncRunnable3;
import myandroid.async.AsyncStatus;
import myandroid.tools.Develop;

public class AsyncHttps extends AsyncRunnable3<String> {

	HttpsURLConnection connect = null;
	String rawData = null;
	String url = null;
	String httpMethod = null;
	SSLSocketFactory sslFactory;

	public void initialize(String url, String httpMethod) {
		this.url = url;
		this.httpMethod = httpMethod;
	}

	public void setTimeOutt(int timeoutMillis) {
		connect.setConnectTimeout(timeoutMillis);
	}

	public void setRequestProperty(String field, String value) {
		connect.setRequestProperty(field, value);
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public void setSslFactory(SSLSocketFactory sslFactory) {
		this.sslFactory = sslFactory;
	}

	@Override
	protected void onPrepare() {
		try {
			connect = (HttpsURLConnection) new URL(url).openConnection();
			if (sslFactory != null)
				connect.setSSLSocketFactory(sslFactory);
			connect.setRequestMethod(httpMethod);
		} catch (IOException e) {
			if (url == null && httpMethod == null)
				Develop.e(this, "Call initialize before execute");
			else
				Develop.e(this, "Illegal url or httpMethod");
		}
	}

	@Override
	protected String onExecute() {
		if (connect == null)
			return null;

		if (rawData != null)
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

		try {
			connect.connect();
			Develop.i(this, Develop.divider + "START CONNECT" + Develop.divider);
			InputStream is = connect.getInputStream();
			String source = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
			connect.disconnect();
			Develop.v(this, "RequestUrl:::" + url);
			Develop.v(this, "RequestMethod:::" + connect.getRequestMethod());
			Develop.v(this, "ResponseCode:::" + connect.getResponseCode());
			Develop.i(this, Develop.divider + "END CONNECT" + Develop.divider);
			Develop.i(this, Develop.divider + "Response" + Develop.divider);
			Develop.v(this, source);
			Develop.i(this, Develop.divider + "Response" + Develop.divider);
			return source;
		} catch (IOException e) {
			e.printStackTrace();
			postResoult(AsyncStatus.FAILURE, null);
			Develop.e(this, "no InputStream could be created");
			return null;
		}
	}

//	@Override
//	protected void onFinish(String r) {
//		postResoult(r == null ? AsyncStatus.FAILURE : AsyncStatus.SUCCESS, r);
//	}

	@Override
	protected void onResoult(String result, int status) {

	}

}
