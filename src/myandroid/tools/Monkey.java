package myandroid.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Monkey {
	public Monkey() {

		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec("input keyevent 5");
			// Process proc = rt.exec("ls -all");
			// Process proc = rt.exec("su");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			String line;

			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
