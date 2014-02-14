package google.youtube.playlists;

import google.youtube.data.Playlist;

import java.util.ArrayList;
import java.util.List;


public class Response extends google.youtube.data.Response {
	List<Playlist> items = new ArrayList<Playlist>();

	public List<Playlist> getItems() {
		return items;
	}

	public void setItems(List<Playlist> items) {
		this.items = items;
	}

}
