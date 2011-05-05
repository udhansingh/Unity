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

import java.util.Date;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.onesun.atomator.channels.Channel;
import org.onesun.utils.AbderaUtils;
import org.onesun.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class LinkedInAdaptor extends AbstractAdaptor {
	private static Logger logger = Logger.getLogger(LinkedInAdaptor.class);
	
	public LinkedInAdaptor() {
		super();
	}

	public LinkedInAdaptor(Channel channel){
		super(channel);
	}
	
	private Entry toEntry(Element item){
		Factory abderaFactory = AbderaUtils.getAbderaFactory();
		Entry entry = abderaFactory.newEntry();
		
		// Obtain "timestamp"
		long timestamp = Long.parseLong(XMLUtils.getValue(item, "timestamp"));
		Date date = new Date(timestamp);
		entry.setPublished(date);
		entry.setUpdated(date);

		// Obtain Auther Info
		String firstName = XMLUtils.getValue(item, "first-name");
		if(firstName == null) {
			firstName = "FNU";
		}

    	String lastName = XMLUtils.getValue(item, "last-name");
    	if(lastName == null) {
    		lastName = "LNU";
    	}
    	
    	entry.addAuthor(firstName + " " + lastName);
    	
    	// Obtain Title
    	String title = XMLUtils.getValue(item, "current-status");
    	
    	// If title is null; then body might contain an update
    	if(title == null) {
    		title = XMLUtils.getValue(item, "body");
    	}
    	// TODO: membership and connections must be upconverted
    	
    	entry.setTitle(title);
		
		return entry;
	}
	
	private Feed parseDocument(Document document){
		Factory abderaFactory = AbderaUtils.getAbderaFactory();
		Feed feed = abderaFactory.newFeed();
		
		Element element = document.getDocumentElement();
		
		// Entry from LinkedIn is tagged as "update"
		NodeList updates = element.getElementsByTagName("update");
		if(updates != null && updates.getLength() > 0){
			for(int index = 0; index < updates.getLength(); index++){
				Element item = (Element)updates.item(index);
				
				// Interested only in status updates
				String updateType = XMLUtils.getValue(item, "update-type");
				if(updateType != null && (updateType.compareToIgnoreCase("STAT") == 0)){
					Entry entry = toEntry(item);
					
					if(entry != null){
						// DO NOT ADD FEEDS THAT HAVE NO TITLE - all updates must be upconverted
						// TODO: Check method "toEntry"
						String title = entry.getTitle();
						if((title != null) && ((StringUtils.trim(title)).length() > 0)) {
							feed.addEntry(entry);
						}
					}
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
			logger.error("LinkedIn parser: Exception: Wonder if a maintainance is going on at LinkedIn " + e.getMessage());
		}
		
		return null;
	}
}
