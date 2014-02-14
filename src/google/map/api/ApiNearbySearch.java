package google.map.api;

import google.map.nearbysearch.Response;

import org.json.JSONException;
import org.json.JSONObject;

import myandroid.async.task.AsyncApi2;
import myandroid.http.HttpMethod;
import myandroid.parser.JParser;

public class ApiNearbySearch extends AsyncApi2<Response>{

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
		return "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=25.0421118,121.5413833&radius=1000&sensor=true&key=AIzaSyCuYh5xABe5AMUspD-hm15wY-hPDjCQKwk&language=zh-tw";
	}

	@Override
	protected Response onParse(String source) {
		try {
			JSONObject jObject = new JSONObject(source);
			new JParser();
			return JParser.toObject(jObject, Response.class);
		} catch (JSONException e) {
			return null;
		}
	}

}
