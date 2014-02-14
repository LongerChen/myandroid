package google.youtube.api;

import google.youtube.Youtube;
import google.youtube.playlistitems.Response;

import org.json.JSONException;
import org.json.JSONObject;

import myandroid.async.task.AsyncApi2;
import myandroid.http.HttpMethod;
import myandroid.parser.JParser;

public class ApiPlaylistItems extends AsyncApi2<Response> {
	String key;
	String part = "snippet";
	String maxResults;
	String pageToken;
	String playlistId;

	public ApiPlaylistItems(String key) {
		setKey(key);
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public void setMaxResults(String maxResults) {
		this.maxResults = maxResults;
	}

	public void setPageToken(String pageToken) {
		this.pageToken = pageToken;
	}

	public void setPlaylistId(String playlistId) {
		this.playlistId = playlistId;
	}

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
		String param = "?key=" + key;
		param += part == null ? "" : "&part=" + part;
		param += maxResults == null ? "" : "&maxResults=" + maxResults;
		param += pageToken == null ? "" : "&pageToken=" + pageToken;
		param += playlistId == null ? "" : "&playlistId=" + playlistId;
		return Youtube.apiUrlPlaylistItems + param;
	}

	@Override
	protected Response onParse(String source) {
		try {
new JParser();
			//			Response response = new Response();
//			PageInfo pageInfo = new PageInfo();
//			List<Playlist> items = new ArrayList<Playlist>();
//			JSONObject responseJ = new JSONObject(source);
//			JSONObject pageInfoJ = responseJ.getJSONObject("pageInfo");
//			JSONArray itemsJ = responseJ.getJSONArray("items");
//			response.setKind(responseJ.getString("kind"));
//			response.setEtag(responseJ.getString("etag"));
//			if (responseJ.has("prevPageToken"))
//				response.setPrevPageToken(responseJ.getString("prevPageToken"));
//			if (responseJ.has("nextPageToken"))
//				response.setNextPageToken(responseJ.getString("nextPageToken"));
//			pageInfo.setTotalResults(pageInfoJ.getInt("totalResults"));
//			pageInfo.setResultsPerPage(pageInfoJ.getInt("resultsPerPage"));
//			for (int i = 0; i < itemsJ.length(); i++) {
//				JSONObject itemJ = itemsJ.getJSONObject(i);
//				JSONObject snippetJ = itemJ.getJSONObject("snippet");
//				JSONObject thumbnailsJ = snippetJ.getJSONObject("thumbnails");
//				Playlist item = new Playlist();
//				Snippet snippet = new Snippet();
//				Thumbnails thumbnails = new Thumbnails();
//				item.setKind(itemJ.getString("kind"));
//				item.setEtag(itemJ.getString("etag"));
//				item.setId(itemJ.getString("id"));
//				snippet.setPublishedAt(snippetJ.getString("publishedAt"));
//				snippet.setChannelId(snippetJ.getString("channelId"));
//				snippet.setTitle(snippetJ.getString("title"));
//				snippet.setDescription(snippetJ.getString("description"));
//				thumbnails.setDefaultUrl(thumbnailsJ.getJSONObject("default")
//						.getString("url"));
//				thumbnails.setMediumUrl(thumbnailsJ.getJSONObject("medium")
//						.getString("url"));
//				thumbnails.setHighUrl(thumbnailsJ.getJSONObject("high")
//						.getString("url"));
//				snippet.setThumbnails(thumbnails);
//				item.setSnippet(snippet);
//				items.add(item);
//			}
//
//			response.setPageInfo(pageInfo);
//			response.setItems(items);
//			return response;
			return JParser.toObject(new JSONObject(source), Response.class);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
