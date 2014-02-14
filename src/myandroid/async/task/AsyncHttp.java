package myandroid.async.task;

import myandroid.async.AsyncRunnable3;
import myandroid.http.HttpConnect;
import myandroid.tools.Develop;

public class AsyncHttp extends AsyncRunnable3<String> {

	HttpConnect connect;

	public void initialize(String url) {
		connect = new HttpConnect(url);
	}

	public void setHttpMethod(String httpMethod) {
		connect.setHttpMethod(httpMethod);
	}

	public void setTimeOutt(int timeoutMillis) {
		connect.setTimeOutt(timeoutMillis);
	}

	public void setRequestProperty(String field, String value) {
		connect.setRequestProperty(field, value);
	}

	public void setRawData(String rawData) {
		connect.setRawData(rawData);
	}

	@Override
	protected void onPrepare() {
		Develop.v(this, "connect to url :::" + connect.getUrl());
	}

	@Override
	protected String onExecute() {
		return connect.connectForString();
	}

//	@Override
//	protected void onFinish(String r) {
//		postResoult(r == null ? AsyncStatus.FAILURE : AsyncStatus.SUCCESS, r);
//	}

	@Override
	protected void onResoult(String result, int status) {

	}

}
