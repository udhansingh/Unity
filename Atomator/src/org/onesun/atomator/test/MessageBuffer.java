package org.onesun.atomator.test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.abdera.model.Entry;

public class MessageBuffer {
	public String makeMessage(Entry entry, List<Entry> relatedEntries){
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<document>");
		
		// Add the entry
		try {
			StringWriter writer = new StringWriter();
			entry.writeTo(writer);

			buffer.append(writer.toString());
		} catch (IOException e) {
		}
		
		// add related entries
		if(relatedEntries != null && relatedEntries.size() > 0){
			buffer.append("<related>");
			
			for(Entry e : relatedEntries){
				try {
					StringWriter writer = new StringWriter();
					e.writeTo(writer);

					buffer.append(writer.toString());
				} catch (IOException ioe) {
				}
			}

			// closure of "related"
			buffer.append("</related>");
		}

		// closure of "document"
		buffer.append("</document>");
		
		return buffer.toString();
	}
}
