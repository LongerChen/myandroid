package myandroid.api;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import myandroid.async.AsyncRunnable;
import myandroid.http.HttpFactory;
import myandroid.http.HttpParams;
import myandroid.http.HttpResponse;

public class FbUserPhoto extends AsyncRunnable<Bitmap> {
	protected Map<String, Object> params = new HashMap<String, Object>();
	final public static String SQUARE = "square";
	final public static String SMALL = "small";
	final public static String NORMAL = "normal";
	final public static String LARGE = "large";
	String fbid;

	public FbUserPhoto(String fbid) {
		this.fbid = fbid;
	}

	public FbUserPhoto(String fbid, String type, int width, int height) {
		this.fbid = fbid;
		params.put("type", type);
		params.put("width", width);
		params.put("height", height);
	}

	@Override
	protected Bitmap onExecute() {
		HttpURLConnection connection = HttpFactory
				.get("http://graph.facebook.com/" + fbid + "/picture?"
						+ HttpParams.convert(params));
		return BitmapFactory.decodeStream(HttpResponse
				.getInputStream(connection));
	}

}
