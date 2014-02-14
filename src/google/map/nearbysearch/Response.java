package google.map.nearbysearch;

import google.map.data.GeoPoint;

import java.util.List;


public class Response {
	List<String> debug_info;
	List<String> html_attributions;
	String next_page_token;
	List<GeoPoint> results;
	String status;

	public List<String> getDebug_info() {
		return debug_info;
	}

	public void setDebug_info(List<String> debug_info) {
		this.debug_info = debug_info;
	}

	public List<String> getHtml_attributions() {
		return html_attributions;
	}

	public void setHtml_attributions(List<String> html_attributions) {
		this.html_attributions = html_attributions;
	}

	public String getNext_page_token() {
		return next_page_token;
	}

	public void setNext_page_token(String next_page_token) {
		this.next_page_token = next_page_token;
	}

	public List<GeoPoint> getResults() {
		return results;
	}

	public void setResults(List<GeoPoint> results) {
		this.results = results;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
