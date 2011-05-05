package org.onesun.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

public class BufferedStreamingOutput implements StreamingOutput {
	private StringBuffer buffer = null;
	
	public StringBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(StringBuffer buffer) {
		this.buffer = buffer;
	}

	public BufferedStreamingOutput() {
	}
	
	// @Override
	public void write(OutputStream outputStream) throws IOException, WebApplicationException {
		PrintWriter out = new PrintWriter(outputStream);

		try{
			if(buffer != null) out.write(buffer.toString());
		}
		finally{
			out.close();
		}
	}
}