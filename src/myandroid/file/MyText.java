package myandroid.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MyText {

	/**
	 * 取代TXT檔內的字元
	 * 
	 * @param source
	 *            TXT檔案位置
	 * @param oldString
	 *            舊字元
	 * @param newString
	 *            新字元
	 */
	public static void replace(File source, String oldString, String newString) {

		try {
			BufferedReader in;
			StringBuffer sb = new StringBuffer();
			in = new BufferedReader(new FileReader(source));
			String text = null;
			while ((text = in.readLine()) != null) {
				sb.append(text.replaceAll(oldString, newString));
				sb.append("\r\n");
			}

			in.close();

			FileWriter out = new FileWriter(source);
			out.write(sb.toString());
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
