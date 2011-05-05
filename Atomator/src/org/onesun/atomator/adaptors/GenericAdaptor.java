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
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.Parser;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.wink.common.model.rss.RssChannel;
import org.apache.wink.common.model.rss.RssFeed;
import org.apache.wink.common.model.rss.RssItem;
import org.onesun.atomator.channels.Channel;
import org.onesun.utils.AbderaUtils;
import org.onesun.utils.TimeUtils;
import org.onesun.utils.rss.DefaultRSS;
import org.onesun.utils.rss.VerboseRSS;

public class GenericAdaptor extends AbstractAdaptor {	
	private static Logger logger = Logger.getLogger(GenericAdaptor.class);
	private static List<VerboseRSS> scrapeTools = null;
	
	public List<VerboseRSS> getScrapeTools() {
		return scrapeTools;
	}

	public void setScrapeTools(List<VerboseRSS> scrapeTools) {
		GenericAdaptor.scrapeTools = scrapeTools;
	}

	public void init(){
	}

	public GenericAdaptor() {
		super();
	}

	public GenericAdaptor(Channel channel) {
		super(channel);
	}

	public GenericAdaptor(Channel channel, String feedURL){
		super(channel, feedURL);
	}
	
	public GenericAdaptor(Channel channel, String feedURL, boolean fullText){
		super(channel, feedURL, fullText);
	}

	@Override
	protected Feed parseEntries(final String input) {
		Abdera abdera = AbderaUtils.getAbdera();

		Parser parser = abdera.getParser();
		
		InputStream is = new ByteArrayInputStream(input.getBytes());
		
		Document<Feed> document = null;
		Feed feed = null;
		
		if(is != null) document = parser.parse(is);
		if(document != null) {
			try {
				feed = document.getRoot();
			}
			catch(Exception e){
			}
		}
		
		return feed;
	}
	
	private Entry toEntry(Object object){
		Entry entry = null;
		Factory abderaFactory = AbderaUtils.getAbderaFactory();
		
		if(object instanceof RssItem){
			entry = abderaFactory.newEntry();
			
			RssItem item = (RssItem)object;
			String text;

			text = (item.getGuid() != null) ? item.getGuid().getContent() : null;
			if(text != null) entry.setId(StringEscapeUtils.escapeHtml(text));

			text = item.getTitle();
			if(text != null) entry.setTitle(text);

			text = item.getDescription();
			if(text != null) entry.setSummaryAsHtml(text);

			String pubDate = item.getPubDate();
			if(pubDate != null){
				Date date = null;
				try {
					if(pubDate.trim().length() > 0){
						date = TimeUtils.javaDateFormat.parse(pubDate);
						entry.setUpdated(date);
						entry.setPublished(date);
					}
				} catch (ParseException pe) {
				} catch (NumberFormatException nfe){
				} catch (ArrayIndexOutOfBoundsException aioobe){
					logger.info("ArrayIndexOutOfBoundsException while processing date: " + pubDate);
				}
			}
			
			text = item.getLink();
			if(entry != null) {
				try{
					entry.addLink(StringEscapeUtils.escapeHtml(text));
				}
				catch(Exception e){
				}
			}
			
			text = item.getAuthor();
			if(text != null) {
				try {
					entry.addAuthor(text);
				}catch(Exception e){
				}
			}
		}

		return entry;
	}
	
	@Override
	public Feed refresh() {
		Feed feed = null;
		String resourceURI = feedURL;
		
		if(resourceURI != null){
			logger.info("GenericAdaptor: Working on " + channel.getEntry().getDescription() + " \t\t " + resourceURI);
			
			if(resourceURI.contains("atom.xml") || resourceURI.contains(".atom") || resourceURI.contains("=atom") || resourceURI.contains("/atom")) {
				try {
					Abdera abdera = AbderaUtils.getAbdera();
					Parser parser = abdera.getParser();
					URL url = new URL(resourceURI);

					Document<Feed> doc = parser.parse(url.openStream());
					feed = doc.getRoot();
				}
				catch(Exception e){
				}
			}
			else if(resourceURI.contains("rss.xml") || resourceURI.contains(".rss") || resourceURI.contains("=rss") || resourceURI.contains("/rss") ||
					resourceURI.contains(".php") || resourceURI.contains("feed")){
				VerboseRSS verboseRSS = null;
				RssFeed rss = null;

				if(fullText == false){
					try {
						verboseRSS = new DefaultRSS(resourceURI);
						
						rss = verboseRSS.getEnrichedFeed();
						
						if(rss != null){
							feed = makeFeed(rss);
						}
						
					} catch (UnsupportedEncodingException e1) {
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					try {
						// 1st Try
						verboseRSS = (VerboseRSS)(scrapeTools.get(0)).clone();
						verboseRSS.setUrl(resourceURI);

						rss = verboseRSS.getEnrichedFeed();

						if(rss != null){
							feed = makeFeed(rss);
						}
					}
					catch(EmptyFeedException efe1){
						logger.info("First try failed: Empty feed; failed on " + verboseRSS.toString());

						try{
							// 2nd Try
							verboseRSS = (VerboseRSS)(scrapeTools.get(1)).clone();
							verboseRSS.setUrl(resourceURI);

							rss = verboseRSS.getEnrichedFeed();

							if(rss != null){
								feed = makeFeed(rss);
							}
						}catch(EmptyFeedException efe2){
							logger.info("Second try failed: Empty feed; failed on " + verboseRSS.toString());

							// Final Try
							try {
								verboseRSS = (VerboseRSS)(scrapeTools.get(2)).clone();
								verboseRSS.setUrl(resourceURI);

								rss = verboseRSS.getEnrichedFeed();

								if(rss != null){
									try {
										feed = makeFeed(rss);
									} catch (EmptyFeedException efe3) {
										logger.info("Last try failed: Empty feed; failed on " + verboseRSS.toString());
									} catch (Exception e) {
									}
								}
							}
							catch(CloneNotSupportedException cnse){
							}
							//						catch (InstantiationException ie) {
							//						} 
							//						catch (IllegalAccessException iae) {
							//						}

						} catch (UnsupportedEncodingException uee) {
						} catch (Exception e) {
						}
					}
					catch(Exception e){
						logger.error("Error processing RSS: " + e.getMessage());
					}
				}
			}
		}

		if(feed != null && feed.getEntries().size() > 0){
			logger.info("GenericAdaptor: Working on " + resourceURI + "\t\tFeed Entries to process # " + feed.getEntries().size());
			
			return feed;
		}
		else {
			logger.info("GenericAdaptor: Working on " + resourceURI + "\t\tNo feed entries to work on");
			
			return null;
		}
	}

	private Feed makeFeed(RssFeed rss) throws Exception {
		RssChannel channel = rss.getChannel();
		
		if(channel != null){
			Factory abderaFactory = AbderaUtils.getAbderaFactory();
			Feed feed = abderaFactory.newFeed();
	
			for (RssItem item : channel.getItems()) {
				Entry entry = toEntry(item);
	
				if(entry != null) {
					feed.addEntry(entry);
				}
			}
			
			if(feed.getEntries().size() == 0){
				throw new EmptyFeedException("Empty Feed document");
			}
			else {
				return feed;
			}
		}
		else {
			throw new NullChannelException("Channel is null or not set; Cannot process feeds");
		}
	}
	
	private class EmptyFeedException extends Exception {
		private static final long serialVersionUID = 1L;

		public EmptyFeedException(String message){
			super(message);
		}
	}
	
	private class NullChannelException extends Exception {
		private static final long serialVersionUID = 1L;

		public NullChannelException(String message){
			super(message);
		}
	}

}
