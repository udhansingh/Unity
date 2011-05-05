/*
 **
	Copyright 2010 Udaya Kumar (Udy)

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 **	
 */
package org.onesun.utils.http;

import java.io.*;
import java.net.*;
import java.util.*;

public class Response {

	private static final String EMPTY = "";
	private static final String ENCODING = "UTF-8";

	private String body;
	private int code;
	private Map<String, String> headers;
	private InputStream stream;

	public Response(HttpURLConnection connection) throws IOException {
		try {
			connection.connect();
			code = connection.getResponseCode();
			headers = parseHeaders(connection);
			stream = isSuccessful() ? connection.getInputStream() : connection
					.getErrorStream();
		} catch (UnknownHostException e) {
			code = 404;
			body = EMPTY;
		}
	}

	public String getBody() {
		return body != null ? body : parseBody();
	}

	public int getCode() {
		return code;
	}

	public String getHeader(String name) {
		return headers.get(name);
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public InputStream getStream() {
		return stream;
	}

	private String parseBody() {
		try {
			final char[] buffer = new char[4096];
			StringBuilder out = new StringBuilder();
			Reader in = new InputStreamReader(stream, ENCODING);
			int read;
			do {
				read = in.read(buffer, 0, buffer.length);
				if (read > 0) {
					out.append(buffer, 0, read);
				}
			} while (read >= 0);
			in.close();
			body = out.toString();
			return body;
		} catch (IOException ioe) {
			throw new RuntimeException(
					"IOException: Error reading response body", ioe);
		}
	}

	private Map<String, String> parseHeaders(HttpURLConnection conn) {
		Map<String, String> headers = new HashMap<String, String>();
		for (String key : conn.getHeaderFields().keySet()) {
			headers.put(key, conn.getHeaderFields().get(key).get(0));
		}
		return headers;
	}

	private boolean isSuccessful() {
		return code >= 200 && code < 400;
	}
}