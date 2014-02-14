package myandroid.async;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import myandroid.tools.Develop;

public abstract class CopyOfAsyncApi<R> extends AsyncRunnable3<R> {

	HttpURLConnection connect = null;
	String rawData = null;
	String url = null;
	String httpMethod = null;

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	abstract protected String getDefaultHttpMethod();

	abstract protected String getDefaultRawData();

	abstract protected String getDefaultUrl();

	@Override
	protected void onPrepare() {
		url = url == null ? getDefaultUrl() : url;
		rawData = rawData == null ? getDefaultRawData() : rawData;
		httpMethod = httpMethod == null ? getDefaultHttpMethod() : httpMethod;
		Develop.v(this, "GetUrl:::" + url);

		try {
			connect = (HttpURLConnection) new URL(url).openConnection();
			connect.setRequestMethod(httpMethod);
		} catch (IOException e) {
			if (url == null && httpMethod == null)
				Develop.e(this, "Call initialize before execute");
			else
				Develop.e(this, "Illegal url or httpMethod");
		}
	}

	@Override
	protected R onExecute() {
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
			return onParse(source);
		} catch (IOException e) {
			postResoult(AsyncStatus.FAILURE, null);
			e.printStackTrace();
			Develop.e(this, "no InputStream could be created");
			return null;
		}

	}

	abstract protected R onParse(String source);

	@Override
	protected void onResoult(R result, int status) {
		// TODO Auto-generated method stub

	}

//	@Override
//	protected void onFinish(R r) {
//		postResoult(r == null ? AsyncStatus.FAILURE : AsyncStatus.SUCCESS, r);
//	}

}
