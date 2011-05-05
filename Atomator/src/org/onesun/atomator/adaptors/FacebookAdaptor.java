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

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.log4j.Logger;
import org.onesun.atomator.channels.Channel;
import org.onesun.utils.AbderaUtils;
import org.onesun.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FacebookAdaptor extends AbstractAdaptor {
	private static Logger logger = Logger.getLogger(FacebookAdaptor.class);
	
	public FacebookAdaptor() {
		super();
	}

	public FacebookAdaptor(Channel channel) {
		super(channel);
	}

	public FacebookAdaptor(Channel channel, String feedURL){
		super(channel, feedURL);
	}

	private Entry toEntry(Element item) {
		if(item == null){
			return null;
		}
		
		// Entry entry = abderaFactory.newEntry();
		
    	
		return null;
	}
	
	private Feed parseDocument(Document document){
		Factory abderaFactory = AbderaUtils.getAbderaFactory();
		Feed feed = abderaFactory.newFeed();
		
		Element element = document.getDocumentElement();
		
		// TODO: convert Facebook graph node to entry
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
			logger.error("Yahoo Mail parser: Exception while parsing feed " + e.getMessage());
		}
		
		return null;
	}
}
