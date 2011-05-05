package org.onesun.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamUtils {
	public static InputStream toInputStream(String input){
		return new ByteArrayInputStream(input.getBytes());
	}
	
	public static String streamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		
		return sb.toString();
	}
	
	/*
	public static String streamToString(InputStream stream) throws Exception {
		try {
			final char[] buffer = new char[4096];
			StringBuilder out = new StringBuilder();
			Reader in = new InputStreamReader(stream, "UTF-8");
			int read;
			do {
				read = in.read(buffer, 0, buffer.length);
				if (read > 0) {
					out.append(buffer, 0, read);
				}
			} while (read >= 0);
			
			in.close();
			return out.toString();
		} catch (IOException ioe) {
			throw new RuntimeException(
					"IOException: Error reading response body", ioe);
		}
	}
	*/
}
