package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
	private static final String defaultPath = "log.txt";

	public Logger() {
	}

	public synchronized void log(String user, String message)
			throws IOException {
		PrintWriter p = new PrintWriter(new FileOutputStream(new File(
				defaultPath), true));
		p.append("User: " + user + " Message: " + message + " Time: "
				+ System.currentTimeMillis() + "\n");
		p.close();
	}

}
