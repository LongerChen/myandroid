package myandroid.async.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import myandroid.async.AsyncRunnable;
import myandroid.http.HttpConnect;
import myandroid.tools.Develop;

public class AsyncDownloadA extends AsyncRunnable<Integer> {
	File output;
	HttpConnect httpConnect;

	public void initialize(String url, File output) {
		httpConnect = new HttpConnect(url);
		this.output = output;
	}

	@Override
	protected Integer onExecute() {

		if (output.exists())
			Develop.e(this, "File exists, override file");
		else {
			Develop.e(this, "File doesn't exists, create file");
			File folder = new File(output.getParent());
			if (!folder.exists())
				folder.mkdirs();
		}
		
		try {
			Develop.i(this, Develop.divider + "START DOWNLOAD"
					+ Develop.divider);
			InputStream is = httpConnect.connectForStream();
			FileOutputStream fos = new FileOutputStream(output);
			Develop.v(this, "OutputFile:::" + output.getAbsolutePath());
			byte[] data = new byte[1024];
			int flag = 0;
			while ((flag = is.read(data)) != -1)
				fos.write(data, 0, flag);
			fos.flush();
			Develop.i(this, Develop.divider + "END DOWNLOAD" + Develop.divider);
			is.close();
			fos.close();
			return 0;
		} catch (IOException e) {
			Develop.e(this, "no InputStream could be created");
			return null;
		}
	}

}
