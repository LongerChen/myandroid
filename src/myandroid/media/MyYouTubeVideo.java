package myandroid.media;

import java.util.Map;

import android.os.AsyncTask;

public class MyYouTubeVideo extends AsyncTask<String, Void, Map<String, String>> {
	OnCompeleteListener onCompeleteListener = null;

	@Override
	protected Map<String, String> doInBackground(String... params) {
		YouTubeVideoA youTubeVideo = new YouTubeVideoA();
		return youTubeVideo.getVideoUrl(params[0]);
	}
	
	@Override
	protected void onPostExecute(Map<String, String> result) {
		if(onCompeleteListener!=null)
			onCompeleteListener.onCompelete(result);
	}

	public void setOnCompeleteListener(OnCompeleteListener onCompeleteListener) {
		this.onCompeleteListener = onCompeleteListener;
	}

	public interface OnCompeleteListener {
		public void onCompelete(Map<String, String> result);
	}
}
