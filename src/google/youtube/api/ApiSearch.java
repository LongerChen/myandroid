package google.youtube.api;

import google.youtube.Youtube;
import google.youtube.search.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;

import myandroid.async.task.AsyncApi2;
import myandroid.http.HttpMethod;
import myandroid.parser.JParser;

/**
 * @author Tony
 * 
 */
public class ApiSearch extends AsyncApi2<Response> {
	String key;
	String part = "snippet";
	String maxResults;
	String pageToken;
	String q;

	/**
	 * @param key
	 *            api server key
	 */
	public ApiSearch(String key) {
		setKey(key);
	}

	public void setPart(String part) {
		this.part = part;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public void setMaxResults(String maxResults) {
		this.maxResults = maxResults;
	}

	public void setPageToken(String pageToken) {
		this.pageToken = pageToken;
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
		try {
			param += q == null ? "" : "&q=" + URLEncoder.encode(q, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			param += q == null ? "" : "&q=" + q;
		}
		param += maxResults == null ? "" : "&maxResults=" + maxResults;
		param += pageToken == null ? "" : "&pageToken=" + pageToken;
		return Youtube.apiUrlSearch + param;
	}

	@Override
	protected Response onParse(String source) {
		try {
new JParser();
			//			Response response = new Response();
//			PageInfo pageInfo = new PageInfo();
//			List<Video> items = new ArrayList<Video>();
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
//				JSONObject idJ = itemJ.getJSONObject("id");
//				JSONObject snippetJ = itemJ.getJSONObject("snippet");
//				JSONObject thumbnailsJ = snippetJ.getJSONObject("thumbnails");
//				Video item = new Video();
//				Id id = new Id();
//				Snippet snippet = new Snippet();
//				Thumbnails thumbnails = new Thumbnails();
//				item.setKind(itemJ.getString("kind"));
//				item.setEtag(itemJ.getString("etag"));
//				id.setKind(idJ.getString("kind"));
//				if (idJ.has("videoId"))
//					id.setVideoId(idJ.getString("videoId"));
//				if (idJ.has("playlistId"))
//					id.setPlaylistId(idJ.getString("playlistId"));
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
//				item.setId(id);
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
