package myandroid.async.task;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import myandroid.async.AsyncRunnable;
import myandroid.tools.Develop;

public class AsyncZip extends AsyncRunnable<File> {
	File output;
	File source;
	List<String> files = new ArrayList<String>();
	final int BUFFER_SIZE = 1024;

	public void initialize(File folder, File output) {
		this.output = output;
		this.source = folder;
		Develop.e(this, source.exists());
	}

	@Override
	protected File onExecute() {
		try {
			BufferedInputStream origin = null;
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(output)));
			if (source.isDirectory()) {
				converToFiles(source);
			} else
				files.add(source.getAbsolutePath());

			byte data[] = new byte[BUFFER_SIZE];

			for (int i = 0; i < files.size(); i++) {

				FileInputStream fi = new FileInputStream(files.get(i));
				origin = new BufferedInputStream(fi, BUFFER_SIZE);
				ZipEntry entry = new ZipEntry(files.get(i).replace(
						source.getAbsolutePath() + "/", ""));
				Develop.e(this,
						files.get(i)
								.replace(source.getAbsolutePath() + "/", ""));
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();

			}
			out.flush();
			out.close();

			return output;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void converToFiles(File folder) {
		for (File file : folder.listFiles()) {
			if (file.isDirectory())
				converToFiles(file);
			else
				files.add(file.getAbsolutePath());
		}
	}

}
