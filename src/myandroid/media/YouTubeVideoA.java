package myandroid.media;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 從Youtube Api取得實際影片網址，<br>
 * API路徑：http://www.youtube.com/get_video_info?video_id=<br>
 * 這支API回傳的資料經過三次UrlEncode，未Decoder時用"&"分隔，<br>
 * 其中包含url_encoded_fmt_stream_map那一段就是影片網址資訊，<br>
 * 取得url_encoded_fmt_stream_map內容後，先解碼一次<br>
 * 再用","分隔可以取得本影片的每一種格式及詳細資料，由於參數排列是隨機，所以需要再做處理。<br>
 * 
 * 參考網址： <br>
 * <li>http://x1gma.net/wordpress/?p=54<br> <li>
 * http://hkgoldenmra.blogspot.tw/2013/05/youtube.html<br>
 * 
 * @author Tony
 * 
 */
public class YouTubeVideoA {

	/**
	 * 
	 * @param video_id
	 *            Youtube影片id
	 * @return 所有格式URL及Info
	 */
	public Map<String, String> getVideoUrl(String video_id) {
		/* 用Map來存放分析結果 */
		Map<String, String> videoUrlMap = new HashMap<String, String>();

		/* 將id傳入，取得API內容 */
		String video_info = apiYoutubeInfo(video_id);

		/* 若取得成功則分析內容 */
		if (video_info != null) {
			/* 將每一筆資料以","分隔出來 */
			String[] _video_infoList = URLDecoder.decode(video_info).split(",");

			for (String s : _video_infoList) {
				Map<String, String> urlParam = new HashMap<String, String>();
				/* 將每個參數用"&"分隔 */
				String[] params = URLDecoder.decode(URLDecoder.decode(s))
						.split("&");
				for (String param : params) {
					/*
					 * 若為第一個參數則將url與參數分開存入map 否則照一般程序以"="分隔即可
					 */
					if (param.contains("?")) {
						// System.out.println(param);
						String url = param.split("\\?")[0];
						urlParam.put(url.split("=")[0], url.split("=")[1]);
						urlParam.put(param.split("\\?")[1].split("=")[0],
								param.split("\\?")[1].split("=")[1]);
					} else {
						// String p = URLDecoder.decode(param);
						urlParam.put(param.split("=")[0], param.split("=")[1]);

					}
				}

				/* 從urlParam中取出參數並組成Url */
				String videoUrl = urlParam.get("url") + "?";
				for (String key : urlParam.keySet())
					if (!key.endsWith("url"))
						videoUrl += "&" + key + "=" + urlParam.get(key);
				videoUrl = videoUrl.replace("?&", "?");
				videoUrl = videoUrl.replace("&sig=", "&signature=");
				String type = "unKnow";
				String quality = "unKnow";
				type = urlParam.get("type").replace("video/", "")
						.replace("; codecs", "");
				if (urlParam.get("quality").contains("small"))
					quality = "240p";
				if (urlParam.get("quality").contains("medium"))
					quality = "360p";
				if (urlParam.get("quality").contains("large"))
					quality = "480p";
				if (urlParam.get("quality").contains("hd720"))
					quality = "720p";
				if (urlParam.get("quality").contains("hd1080"))
					quality = "1080p";
				videoUrlMap.put(type + " - " + quality, videoUrl);
			}
		}
		return videoUrlMap;

	}

	/**
	 * 
	 * @param video_id
	 *            YouTube影片id
	 * @return 透過API取得的Info
	 */
	public String apiYoutubeInfo(String video_id) {

		try {
			URL apiUrl = new URL(
					"http://www.youtube.com/get_video_info?video_id="
							+ video_id);
			URLConnection connect = apiUrl.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connect.getInputStream()));
			String inputLine;
			String video_info = "";
			while ((inputLine = in.readLine()) != null) {
				video_info += inputLine;
			}
			in.close();
			System.out.println(video_info);
			/* 分割需要部分 */
			if (video_info.contains("fail")) {
				System.out.println("解析失敗，video_id錯誤");
				return null;
			} else if (video_info.contains("ok")) {
				System.out.println("解析成功");
				return video_info.split("url_encoded_fmt_stream_map=")[1]
						.split("&")[0];
			} else {
				System.out.println("解析失敗");
				return null;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
