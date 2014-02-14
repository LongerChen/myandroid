package myandroid.parser;

import google.youtube.search.Response;

import org.json.JSONException;
import org.json.JSONObject;

import myandroid.async.task.AsyncApi2;
import myandroid.http.HttpMethod;

public class ApiParser extends AsyncApi2<Response>{

	@Override
	protected String getDefaultHttpMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.GET;
	}

	@Override
	protected String getDefaultRawData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDefaultUrl() {
		// TODO Auto-generated method stub
		return "https://www.googleapis.com/youtube/v3/search?key=AIzaSyCuYh5xABe5AMUspD-hm15wY-hPDjCQKwk&part=snippet&q=%E8%80%81%E8%99%8E&maxResults=50";
	}

	@Override
	protected Response onParse(String source) {
		JParser parser = new JParser(); 
		try {
			return JParser.toObject(new JSONObject(source), Response.class);
		} catch (JSONException e) {
			return null;
		}
		
	}

}
