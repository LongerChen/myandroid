package google.map.api;

import org.json.JSONException;
import org.json.JSONObject;

import google.map.directions.Response;
import myandroid.async.task.AsyncApi2;
import myandroid.http.HttpMethod;
import myandroid.parser.JParser;

public class ApiDirections extends AsyncApi2<Response> {

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
		return "http://maps.googleapis.com/maps/api/directions/json?origin=25.0420992,121.5413757&destination=25.048334,121.533641&sensor=false&units=metric&mode=walking&language=zh-tw";
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
