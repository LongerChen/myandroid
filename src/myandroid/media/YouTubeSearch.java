package myandroid.media;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class YouTubeSearch {
	/**
	 * https://developers.google.com/youtube/v3/docs/search/list
	 */
	public YouTubeSearch() {
		// TODO Auto-generated constructor stub
	}

	public String apiYoutubeSearch(String apiKey, String keyWord, int maxResults) {

		try {
			URL apiUrl = new URL(
					"https://www.googleapis.com/youtube/v3/search?part=snippet&key="
							+ apiKey + "&maxResults=" + maxResults + "&q=" + URLEncoder.encode(keyWord,"utf-8"));
			URLConnection yc = apiUrl.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			String searchResult = "";
			while ((inputLine = in.readLine()) != null) {
				searchResult += inputLine;
			}
			in.close();
			System.out.println(searchResult);
			return searchResult;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
