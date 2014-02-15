package myandroid.async.task;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import myandroid.async.AsyncRunnable;
import myandroid.http.HttpResponse;
import myandroid.tools.Develop;

public abstract class AsyncApi<R> extends AsyncRunnable<R> {
	HttpURLConnection connection;
	protected Map<String, Object> params = new HashMap<String, Object>();

	public AsyncApi(Object[]... values) {
		for (Object[] o : values)
			params.put(o[0].toString(), o[1].toString());
	}

	public AsyncApi(Map<String, Object> params) {
		this.params = params;
	}

	public void setConnection(HttpURLConnection connection) {
		this.connection = connection;
	}

	public HttpURLConnection getConnection() {
		return connection;
	}

	@Override
	protected R onExecute() {
		HttpURLConnection connection = getDefaultConnection();
		if (connection == null)
			connection = getConnection();
		if (connection == null)
			return null;
		String response = HttpResponse.getString(connection);
		Develop.i(this, "url:" + connection.getURL().toString());
		Develop.i(this, "httpmethod:" + connection.getRequestMethod());
		Develop.i(this, "response:\n" + response);
		return response == null ? null : onParse(response);
	}

	abstract protected HttpURLConnection getDefaultConnection();

	abstract protected R onParse(String source);

}
