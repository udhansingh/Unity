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
import org.onesun.atomator.channels.Channel;
import org.onesun.utils.XMLUtils;
import org.onesun.utils.AbderaUtils;
import org.onesun.utils.http.HTTPMethod;
import org.onesun.utils.http.Request;
import org.onesun.utils.http.Response;
import org.scribe.oauth.Scribe;
import org.scribe.oauth.Token;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class YahooMailAdaptor extends AbstractAdaptor {
	public YahooMailAdaptor() {
		super();
	}

	public YahooMailAdaptor(Channel channel) {
		super(channel);
	}

	public YahooMailAdaptor(Channel channel, String feedURL){
		super(channel, feedURL);
	}

	private Entry toEntry(Element item) {
		if(item == null){
			return null;
		}
		
		// Entry entry = abderaFactory.newEntry();
		
    	
		return null;
	}
	
	@Override
	public Feed refresh() {
		try {
			if(channel != null){
				if(feedURL != null){
					Request request = new Request(HTTPMethod.GET, feedURL);
	
					Token token = new Token(channel.getOAuthResult().getAccessKey(), 
							channel.getOAuthResult().getAccessSecret());
					
					Scribe scribe = channel.getScribe();
					if(scribe != null){
						scribe.signRequest(request, token);
						
						Response response = request.send();
		
						String responseText = response.getBody();
						Feed feed = null;
		
						if(responseText != null && responseText.length() > 0){
							feed = parseEntries(responseText);
						}
		
						return feed;
					}
				}
			}
			else {
				// Specific refreshes must be handled in the implementing class
			}
		}catch(Exception e){

		}
		
		return null;
	}
	
	private Feed parseDocument(Document document){
		Factory abderaFactory = AbderaUtils.getAbderaFactory();
		Feed feed = abderaFactory.newFeed();
		
		Element element = document.getDocumentElement();
		
		// TODO: Parse Yahoo Mail objects : entry is not the interested object
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
			System.err.println("Yahoo Mail parser: Exception while parsing feed " + e.getMessage());
		}
		
		return null;
	}
}
