package myandroid.async.task;

import java.util.Map;

import myandroid.async.AsyncRunnable3;
import myandroid.http.HttpConnect;
import myandroid.tools.Develop;

public abstract class AsyncApi2<R> extends AsyncRunnable3<R> {

	String rawData = null;
	String url = null;
	String httpMethod = null;
	HttpConnect connect;
	Map<String, String> params = null;

	public void initialize() {

	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setParam(Map<String, String> params) {
		this.params = params;
	}

	abstract protected String getDefaultHttpMethod();

	abstract protected String getDefaultRawData();

	abstract protected String getDefaultUrl();

	@Override
	protected void onPrepare() {
		url = url == null ? getDefaultUrl() : url;
		rawData = rawData == null ? getDefaultRawData() : rawData;
		httpMethod = httpMethod == null ? getDefaultHttpMethod() : httpMethod;
		connect = new HttpConnect(url);
		connect.setHttpMethod(httpMethod);
		if (rawData != null)
			connect.setRawData(rawData);
		if(params!=null)
			connect.setParams(params);
		Develop.v(this, "GetUrl:::" + url);
	}

	@Override
	protected R onExecute() {
		return onParse(connect.connectForString());
	}

	abstract protected R onParse(String source);

//	@Override
//	protected void onFinish(R r) {
//		postResoult(r == null ? AsyncStatus.FAILURE : AsyncStatus.SUCCESS, r);
//	}

	@Override
	protected void onResoult(R result, int status) {

	}

}
