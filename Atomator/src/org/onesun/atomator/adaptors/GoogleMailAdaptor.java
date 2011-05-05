/* 
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
*/
package org.onesun.atomator.adaptors;

import java.text.ParseException;
import java.util.Date;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.log4j.Logger;
import org.onesun.atomator.channels.Channel;
import org.onesun.utils.AbderaUtils;
import org.onesun.utils.TimeUtils;
import org.onesun.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GoogleMailAdaptor extends AbstractAdaptor {
	private static Logger logger = Logger.getLogger(GoogleMailAdaptor.class);
	
	public GoogleMailAdaptor() {
		super();
	}

	public GoogleMailAdaptor(Channel channel) {
		super(channel);
	}

	public GoogleMailAdaptor(Channel channel, String feedURL){
		super(channel, feedURL);
	}

	private Entry toEntry(Element item) {
		if(item == null){
			return null;
		}
		
		Factory abderaFactory = AbderaUtils.getAbderaFactory();
		Entry entry = abderaFactory.newEntry();
		
		String dateStr = null; 

		dateStr = XMLUtils.getValue(item, "issued");
		Date date = null;
		try {
			if(dateStr != null) date = TimeUtils.utcDateFormat.parse(dateStr);
		} catch (ParseException e) {
		}

		if(date != null) {
			entry.setPublished(date);
		}


		date = null;
		dateStr = XMLUtils.getValue(item, "modified");
		try {
			if(dateStr != null) date = TimeUtils.utcDateFormat.parse(dateStr);
		} catch (ParseException e) {
		}
		
		if(date != null) {
			entry.setUpdated(date);
		}

		String name = XMLUtils.getValue(item, "name");
		String email = XMLUtils.getValue(item, "email");

		if(name == null){
			name = "";
		}
		
		if(email != null){
			name += "[" + email + "]";
		}
		
		if(name != null) {
			entry.addAuthor(name);
		}
		
    	String title = XMLUtils.getValue(item, "title");
    	if(title != null) {
    		entry.setTitle(title);
    	}

    	String summary = XMLUtils.getValue(item, "summary");
    	if(summary != null) {
    		entry.setSummary(summary);
    	}
    	
    	Element linkElement = XMLUtils.getElement(item, "link");
    	String link = null; 
    	if(linkElement != null) {
    		link = linkElement.getAttribute("href");
    	}
    	
    	if(link != null){
    		entry.addLink(link);
    	}
    	
		return entry;
	}
	
	private Feed parseDocument(Document document){
		Factory abderaFactory = AbderaUtils.getAbderaFactory();
		Feed feed = abderaFactory.newFeed();
		
		Element element = document.getDocumentElement();
		
		// Entry from LinkedIn is tagged as "update"
		NodeList entries = element.getElementsByTagName("entry");
		if(entries != null && entries.getLength() > 0){
			for(int index = 0; index < entries.getLength(); index++){
				Element item = (Element)entries.item(index);

				try{
					Entry entry = toEntry(item);
					
					if(entry != null){
						feed.addEntry(entry);
					}
				}finally {
				}
			}
		}
		
		if(feed != null && feed.getEntries().size() > 0){
			return feed;
		}else {
			return null;
		}
	}
	
	@Override
	protected Feed parseEntries(String input) {
		try {
			Document document = XMLUtils.toDocument(input);
			if(document != null) {
				return parseDocument(document);
			}
		} catch (Exception e) {
			logger.error("Google Mail parser: Exception while parsing feed " + e.getMessage());
		}
		
		return null;
	}
}
