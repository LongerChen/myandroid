package google.youtube;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import myandroid.net.MyNetProcess;

public class MyYouTube {
	MyNetProcess myNetProcess = null;

	public void apiVideoSearch(String apiKey, String keyWord, int maxResults,
			final OnVideoSearchListener onVideoSearchListener) {
		myNetProcess = new MyNetProcess()
				.setOnCompeleteListener(new MyNetProcess.OnCompeleteListener() {

					@Override
					public void onComplete(boolean isSuccess, Object object) {
						if (isSuccess && onVideoSearchListener != null)
							onVideoSearchListener.onCompelete(object);
					}
				});
		myNetProcess
				.doGet("https://www.googleapis.com/youtube/v3/search?part=snippet&regionCode=TW&key="
						+ apiKey
						+ "&maxResults="
						+ maxResults
						+ "&q="
						+ URLEncoder.encode(keyWord));

	}

	public void apiVideoInfo(String video_id,
			final OnVideoInfoListener onVideoInfoListener) {
		myNetProcess = new MyNetProcess()
				.setOnCompeleteListener(new MyNetProcess.OnCompeleteListener() {

					@Override
					public void onComplete(boolean isSuccess, Object object) {
						if (isSuccess && onVideoInfoListener != null)
							onVideoInfoListener.onCompelete(object.toString());
					}
				});
		myNetProcess.doGet("http://www.youtube.com/get_video_info?video_id="
				+ video_id);
	}

	public void getVideoUrl(String video_id, final OnVideoUrlListener onVideoUrlListener) {

		/* 將id傳入，取得API內容 */
		apiVideoInfo(video_id, new OnVideoInfoListener() {

			@Override
			public void onCompelete(String video_info) {
				if (video_info.contains("fail")) {
					System.out.println("解析失敗，video_id錯誤");
				} else if (video_info.contains("ok")) {
					System.out.println("解析成功");
					/* 用Map來存放分析結果 */
					Map<String, String> videoUrlMap = new HashMap<String, String>();
					video_info = video_info
							.split("url_encoded_fmt_stream_map=")[1].split("&")[0];
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
						if(onVideoUrlListener!=null)
							onVideoUrlListener.onCompelete(videoUrlMap);
					}
				} else {
					System.out.println("解析失敗");
				}
			}
		});

	}

	public interface OnVideoInfoListener {
		public void onCompelete(String video_info);
	}

	public interface OnVideoSearchListener {
		public void onCompelete(Object object);
	}
	public interface OnVideoUrlListener {
		public void onCompelete(Map<String, String> videoUrlMap);
	}
}
