package myandroid.media;

import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;

public class MyAudioMatch {
	public MyAudioMatch() {
		// TODO Auto-generated constructor stub
	}

	public FingerprintSimilarity matchTwoWave(Wave source1, Wave source2) {
		
		return source1.getFingerprintSimilarity(source2);
	}
}
