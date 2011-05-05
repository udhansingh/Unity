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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.log4j.Logger;
import org.onesun.atomator.channels.Channel;
import org.onesun.utils.AbderaUtils;
import org.onesun.utils.TimeUtils;
import org.onesun.utils.XMLUtils;
import org.onesun.utils.http.HTTPMethod;
import org.onesun.utils.http.Request;
import org.onesun.utils.http.Response;
import org.scribe.oauth.Scribe;
import org.scribe.oauth.Token;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class StreamworkAdaptor extends AbstractAdaptor {
	private static Logger logger = Logger.getLogger(StreamworkAdaptor.class);
	
	private enum StreamworkTag {
		EVENT("event"), ITEM("item");
		
		private String tagName = null;
		StreamworkTag(String tagName){
			this.tagName = tagName;
		}
		
		private String getTagName(){
			return this.tagName;
		}
	}
	
	public StreamworkAdaptor() {
		super();

		// TODO: Put the streamwork feed extraction
		this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
		this.documentBuilderFactory.setNamespaceAware(true); // never forget this!
	}

	private DocumentBuilderFactory documentBuilderFactory = null;
	
	public StreamworkAdaptor(Channel channel){
		super(channel);
	}

	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	private List<Entry> documentToEntries(Document document, StreamworkTag tag){
		
		List<Entry> entries = new ArrayList<Entry>();

		Element rootElement = document.getDocumentElement();
		
		// Status from Streamwork is tagged as "event"
		NodeList nodes = rootElement.getElementsByTagName(tag.getTagName());
		
		if(nodes != null && nodes.getLength() > 0){
			for(int index = 0; index < nodes.getLength(); index++){
				Element element = (Element)nodes.item(index);
				Entry entry = null;
				
				switch(tag){
				case EVENT:
					entry = eventToEntry(element);
					if(entry != null){
						entries.add(entry);
					}
				break;
				
				case ITEM:
					entry = itemToEntry(element);
					if(entry != null) {
						entries.add(entry);
					}
				break;
				}
			}
		}
		
		return entries;
	}
	
	private Entry itemToEntry(Element element){
		String itemId = element.getAttribute("id");
		boolean flag = XMLUtils.exists(element, "text_item");
		
		if(flag == true){
			// final String URL = "https://streamwork.com/v1/items/" + itemId + "/description";
			final String URL = "https://streamwork.com/v1/items/" + itemId;
			
			Request request = new Request(HTTPMethod.GET, URL);
			
			Token token = new Token(channel.getOAuthResult().getAccessKey(), 
					channel.getOAuthResult().getAccessSecret());
			
			Scribe scribe = channel.getScribe();
			scribe.signRequest(request, token);
			Response response = request.send();

			String responseText = response.getBody();

			if(responseText != null && responseText.length() > 0){
				DocumentBuilder builder = null;
				Document document = null;
				
				try {
					builder = documentBuilderFactory.newDocumentBuilder();
				} catch (ParserConfigurationException e) {
				}
				
				try {
					InputStream is = new ByteArrayInputStream(responseText.getBytes());
					document = builder.parse(is);

					Entry entry = itemDocumentToEntry(document);
					
					if(entry != null){
						StringWriter writer = new StringWriter();
						entry.writeTo(writer);
					
						System.out.println("\n**********\nStreamwork Item as Entry: \n" + writer.toString() + "\n*************\n");
					}
					
					return entry;
				} catch (Exception e) {
					logger.error("Streamwork parser: Exception: Wonder if a maintainance is going on at Streamwork " + e.getMessage());
				}
			}
		}
		
		return null;
	}
	
	public List<Entry> getEvents(String id){
		if(id != null){
			final String URL = feedURL + "/" + id + "/events";
			
			Request request = new Request(HTTPMethod.GET, URL);
			
			Token token = new Token(channel.getOAuthResult().getAccessKey(), 
					channel.getOAuthResult().getAccessSecret());
			
			Scribe scribe = channel.getScribe();
			scribe.signRequest(request, token);
			Response response = request.send();

			String responseText = response.getBody();

			if(responseText != null && responseText.length() > 0){
				DocumentBuilder builder = null;
				Document document = null;
				
				try {
					builder = documentBuilderFactory.newDocumentBuilder();
				} catch (ParserConfigurationException e) {
				}
				
				try {
					InputStream is = new ByteArrayInputStream(responseText.getBytes());
					document = builder.parse(is);

					return documentToEntries(document, StreamworkTag.EVENT);
				} catch (Exception e) {
					logger.error("Streamwork parser: Exception: Wonder if a maintainance is going on at Streamwork " + e.getMessage());
				}
			}
		}
		return null;
	}
	
	
	public List<Entry> getItems(String activityId) {
		if(activityId != null){
			final String URL = feedURL + "/" + activityId + "/items";
			
			Request request = new Request(HTTPMethod.GET, URL);
			
			Token token = new Token(channel.getOAuthResult().getAccessKey(), 
					channel.getOAuthResult().getAccessSecret());
			
			Scribe scribe = channel.getScribe();
			scribe.signRequest(request, token);
			Response response = request.send();

			String responseText = response.getBody();

			if(responseText != null && responseText.length() > 0){
				DocumentBuilder builder = null;
				Document document = null;
				
				try {
					builder = documentBuilderFactory.newDocumentBuilder();
				} catch (ParserConfigurationException e) {
				}
				
				try {
					InputStream is = new ByteArrayInputStream(responseText.getBytes());
					document = builder.parse(is);

					return documentToEntries(document, StreamworkTag.ITEM);
				} catch (Exception e) {
					logger.error("Streamwork parser: Exception: Wonder if a maintainance is going on at Streamwork " + e.getMessage());
				}
			}
		}
		return null;
	}
	
	private Entry eventToEntry(Element element){
		Factory abderaFactory = AbderaUtils.getAbderaFactory();
		Entry entry = abderaFactory.newEntry();

		entry.addContributor(channel.getEntry().getDescription());
		
		String eventType = element.getAttribute("type");
		if(eventType != null) {
			entry.addCategory(eventType);
		}
		
		String summary = XMLUtils.getValue(element, "item-title");
		if(summary == null){
			summary = XMLUtils.getValue(element, "activity-status");
		}
		
		if(summary != null){
			entry.setSummary(summary);
		}
		
		String strPubDate = element.getAttribute("created_at");
		Date pubDate = null;
		if(strPubDate != null){
			try {
				pubDate = TimeUtils.utcDateFormat.parse(strPubDate);
			} catch (ParseException e) {
			}

			if(pubDate != null){
				entry.setPublished(pubDate);
			}
		}
		
		String title = XMLUtils.getValue(element, "activity-title");
		if(title != null){
			String augmentedTitle = "[" + simpleDateFormat.format(pubDate) + "] " + eventType + " : " + title;
			
			if(summary != null){
				augmentedTitle += " : " + summary;
			}
			
			entry.setTitle(augmentedTitle);
		}
		
		
		
		Element initiator = XMLUtils.getElement(element, "initiator");
		if(initiator != null){
			String author = initiator.getAttribute("name");
			if(author != null){
				entry.addAuthor(author);
			}
		}
		
		return entry;
	}
	
	private Entry itemDocumentToEntry(Document document){
		Element element = document.getDocumentElement();

		if(element != null){
			Factory abderaFactory = AbderaUtils.getAbderaFactory();
			Entry entry = abderaFactory.newEntry();
			
			Element tmp = XMLUtils.getElement(element, "creator");
			if(tmp != null){
				String author = tmp.getAttribute("name");

				if(author != null){
					entry.addAuthor(author);
				}
			}

			String description = XMLUtils.getValue(element, "description");
			if(description != null){
				entry.setTitle(description);
			}

			tmp = XMLUtils.getElement(element, "text_item");
			if(tmp != null){
				String summary = XMLUtils.getValue(tmp, "text_content");

				if(summary != null){
					entry.setSummaryAsHtml(summary);
				}
			}
			
			return entry;
		}
		
		return null;
	}
	private Feed activityDocumentToFeed(Document document){
		Feed feed = null;
		Factory abderaFactory = AbderaUtils.getAbderaFactory();
		Element rootElement = document.getDocumentElement();
		
		// Activity from Streamwork is tagged as "activity"
		NodeList activity = rootElement.getElementsByTagName("activity");
		if(activity != null && activity.getLength() > 0){
			for(int index = 0; index < activity.getLength(); index++){
				Element element = (Element)activity.item(index);
				
				String id = element.getAttribute("id");
				
				// Interested only in status updates
				String status = XMLUtils.getValue(element, "status");
				logger.info("Streamwork parser Status: " + status + " id = " + id);
				
				if(status.compareToIgnoreCase("open") == 0){
					if(id != null){
						List<Entry> events = getEvents(id);
						if(events.size() > 0){
							feed = abderaFactory.newFeed();
							
							for(Entry event : events){
								feed.addEntry(event);
							}
						}
					}
				}
			}
		}
		
		return feed;
	}
	
	@Override
	protected Feed parseEntries(String input) {
		DocumentBuilder builder = null;
		Document document = null;
		
		try {
			builder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		}
		
		try {
			InputStream is = new ByteArrayInputStream(input.getBytes());
			document = builder.parse(is);

			return activityDocumentToFeed(document);
		} catch (Exception e) {
			logger.error("Streamwork parser: Exception: Wonder if a maintainance is going on at Streamwork " + e.getMessage());
		}
		
		return null;
	}
}
