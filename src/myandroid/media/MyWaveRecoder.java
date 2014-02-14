package myandroid.media;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;

public class MyWaveRecoder extends AsyncTask<String, Void, Void> {
	// 音頻獲取源
	private int audioSource = MediaRecorder.AudioSource.MIC;
	// 設置音頻採樣率，44100是目前的標準，但是某些設備仍然支持22050，16000，11025
	private static int sampleRateInHz = 44100;
	// 設置音頻的錄製的聲道CHANNEL_IN_STEREO為雙聲道，CHANNEL_CONFIGURATION_MONO為單聲道
	private static int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
	// 音頻數據格式:PCM 16位每個樣本。保證設備支持。 PCM 8位每個樣本。不一定能得到設備支持。
	private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	// 緩衝區字節大小
	private int bufferSizeInBytes = 0;
	private AudioRecord audioRecord;
	private boolean isRecord = false;// 設置正在錄製的狀態
	// AudioName裸音頻數據文件
	private static String AudioName = "/sdcard/love.raw";

	private OnCompeleteListener onCompeleteListener;

	@Override
	protected void onPreExecute() {
		// 獲得緩衝區字節大小
		bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
				channelConfig, audioFormat);
		// 創建AudioRecord對象
		audioRecord = new AudioRecord(audioSource, sampleRateInHz,
				channelConfig, audioFormat, bufferSizeInBytes);
		isRecord = true;
	}

	@Override
	protected Void doInBackground(String... params) {
		audioRecord.startRecording();
		if (isRecord) {
			writeDateTOFile();
			copyWaveFile(AudioName, params[0]);
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Void result) {
		if (audioRecord != null) {
			System.out.println("stopRecord");
			isRecord = false;// 停止文件寫入
			audioRecord.stop();
			audioRecord.release();// 釋放資源
			audioRecord = null;
		}
		if (onCompeleteListener != null)
			onCompeleteListener.onCompelete();
	}

	/**
	 * 這裡將數據寫入文件，但是並不能播放，因為AudioRecord獲得的音頻是原始的裸音頻，
	 * 如果需要播放就必須加入一些格式或者編碼的頭信息。但是這樣的好處就是你可以對音頻的裸數據進行處理，比如你要做一個愛說話的TOM
	 * 貓在這裡就進行音頻的處理，然後重新封裝所以說這樣得到的音頻比較容易做一些音頻的處理。
	 */
	private void writeDateTOFile() {
		// new一個byte數組用來存一些字節數據，大小為緩衝區大小
		byte[] audiodata = new byte[bufferSizeInBytes];
		FileOutputStream fos = null;
		int readsize = 0;
		try {
			File file = new File(AudioName);
			if (file.exists()) {
				file.delete();
			}
			fos = new FileOutputStream(file);// 建立一個可存取字節的文件
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (isRecord == true) {
			readsize = audioRecord.read(audiodata, 0, bufferSizeInBytes);
			if (AudioRecord.ERROR_INVALID_OPERATION != readsize) {
				try {
					fos.write(audiodata);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			fos.close();// 關閉寫入流
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 這裡得到可播放的音頻文件
	private void copyWaveFile(String inFilename, String outFilename) {
		FileInputStream in = null;
		FileOutputStream out = null;
		long totalAudioLen = 0;
		long totalDataLen = totalAudioLen + 36;
		long longSampleRate = sampleRateInHz;
		int channels = 2;
		long byteRate = 16 * sampleRateInHz * channels / 8;
		byte[] data = new byte[bufferSizeInBytes];
		try {
			in = new FileInputStream(inFilename);
			out = new FileOutputStream(outFilename);
			totalAudioLen = in.getChannel().size();
			totalDataLen = totalAudioLen + 36;
			WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
					longSampleRate, channels, byteRate);
			while (in.read(data) != -1) {
				out.write(data);
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 這裡提供一個頭信息。插入這些信息就可以得到可以播放的文件。為我為啥插入這44個字節，這個還真沒深入研究，不過你隨便打開一個wav
	 * 音頻的文件，可以發現前面的頭文件可以說基本一樣哦。每種格式的文件都有自己特有的頭文件。
	 */
	private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen,
			long totalDataLen, long longSampleRate, int channels, long byteRate)
			throws IOException {
		byte[] header = new byte[44];
		header[0] = 'R'; // RIFF/WAVE header
		header[1] = 'I';
		header[2] = 'F';
		header[3] = 'F';
		header[4] = (byte) (totalDataLen & 0xff);
		header[5] = (byte) ((totalDataLen >> 8) & 0xff);
		header[6] = (byte) ((totalDataLen >> 16) & 0xff);
		header[7] = (byte) ((totalDataLen >> 24) & 0xff);
		header[8] = 'W';
		header[9] = 'A';
		header[10] = 'V';
		header[11] = 'E';
		header[12] = 'f'; // 'fmt ' chunk
		header[13] = 'm';
		header[14] = 't';
		header[15] = ' ';
		header[16] = 16; // 4 bytes: size of 'fmt ' chunk
		header[17] = 0;
		header[18] = 0;
		header[19] = 0;
		header[20] = 1; // format = 1
		header[21] = 0;
		header[22] = (byte) channels;
		header[23] = 0;
		header[24] = (byte) (longSampleRate & 0xff);
		header[25] = (byte) ((longSampleRate >> 8) & 0xff);
		header[26] = (byte) ((longSampleRate >> 16) & 0xff);
		header[27] = (byte) ((longSampleRate >> 24) & 0xff);
		header[28] = (byte) (byteRate & 0xff);
		header[29] = (byte) ((byteRate >> 8) & 0xff);
		header[30] = (byte) ((byteRate >> 16) & 0xff);
		header[31] = (byte) ((byteRate >> 24) & 0xff);
		header[32] = (byte) (2 * 16 / 8); // block align
		header[33] = 0;
		header[34] = 16; // bits per sample
		header[35] = 0;
		header[36] = 'd';
		header[37] = 'a';
		header[38] = 't';
		header[39] = 'a';
		header[40] = (byte) (totalAudioLen & 0xff);
		header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
		header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
		header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
		out.write(header, 0, 44);
	}

	public void stop() {
		isRecord = false;
	}

	public OnCompeleteListener getOnCompeleteListener() {
		return onCompeleteListener;
	}

	public void setOnCompeleteListener(OnCompeleteListener onCompeleteListener) {
		this.onCompeleteListener = onCompeleteListener;
	}

	public interface OnCompeleteListener {
		void onCompelete();
	};
}
