package myandroid.async.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import myandroid.async.AsyncRunnable;
import myandroid.file.FileMethod;
import myandroid.tools.Develop;

public class AsyncFile extends AsyncRunnable<File> {
	File input = null;
	File output = null;
	int fileMethod = 0;

	public void initialize(File input, File output, int fileMethod) {
		this.input = input;
		this.output = output;
		this.fileMethod = fileMethod;
	}

	@Override
	protected File onExecute() {
		if (input == null || fileMethod == FileMethod.NONE)
			return null;

		switch (fileMethod) {
		case FileMethod.DELETE:
			input.delete();
			return output;
			// break;
		case FileMethod.COPY:
			try {
				FileInputStream fis = new FileInputStream(input);
				FileOutputStream fos = new FileOutputStream(output);
				byte[] buffer = new byte[1024];
				while (fis.read(buffer) != -1)
					fos.write(buffer);
				fos.close();
				fis.close();
				return output;
			} catch (IOException e) {
				Develop.e(this, "IOException");
				return null;
			}
			// break;
		case FileMethod.MOVE:
			input.renameTo(output);
			return output;
			// break;
		default:
			Develop.e(this, "Illegal fileMethod");
			return null;
			// break;
		}
	}

}
