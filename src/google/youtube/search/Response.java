package google.youtube.search;

import google.youtube.data.Video;

import java.util.ArrayList;
import java.util.List;


public class Response extends google.youtube.data.Response {
	List<Video> items = new ArrayList<Video>();

	public List<Video> getItems() {
		return items;
	}

	public void setItems(List<Video> items) {
		this.items = items;
	}

}
