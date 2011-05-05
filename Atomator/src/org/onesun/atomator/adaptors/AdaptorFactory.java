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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.onesun.atomator.channels.Channel;
import org.onesun.utils.XMLUtils;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AdaptorFactory {
	public static final String GENERIC_ADAPTOR_NAME = "generic";
	private static Logger logger = Logger.getLogger(AdaptorFactory.class);
	private static Map<String, Class<?>> adaptors = new HashMap<String, Class<?>>();
	private static Map<String, Set<String>> urlEntries = new HashMap<String, Set<String>>();
	
	private static Resource resource = null;
	
	public static Resource getResource() {
		return resource;
	}

	public static Map<String, Class<?>> getAdaptors() {
		return adaptors;
	}

	public void setResource(Resource resource) {
		AdaptorFactory.resource = resource;
	}
	
	private AdaptorFactory() {
	}
	
	public static List<String> getFeedURLs(String identity){
		Set<String> set = urlEntries.get(identity);
		if(set != null){
			List<String> list = new ArrayList<String>();
			list.addAll(set);
			
			return list;
		}
		else {
			return null;
		}
	}
	
	private void parseDocument(Document document){
		Element element = document.getDocumentElement();

		NodeList updates = element.getElementsByTagName("entry");
		if(updates != null && updates.getLength() > 0){
			logger.info("Reading adaptors.xml : loading #" + updates.getLength() + " adaptors");
			for(int index = 0; index < updates.getLength(); index++){
				Element item = (Element)updates.item(index);

				try{
					String identity = XMLUtils.getValue(item, "identity");
					String adaptorName = XMLUtils.getValue(item, "adaptor");
					
					NodeList feedUrls = item.getElementsByTagName("feedURL");
					if(feedUrls != null && feedUrls.getLength() > 0){
						Set<String> uriSet = null;
						
						uriSet = urlEntries.get(identity);
						if(uriSet == null){
							uriSet = new HashSet<String>();
						}
						
						for(int findex = 0; findex < feedUrls.getLength(); findex++){
							Element feedURL = (Element)feedUrls.item(findex);
							
							uriSet.add(feedURL.getTextContent());
						}
						
						urlEntries.put(identity, uriSet);
					}
						
					
					Class<?> clazz = (Class<?>) Class.forName(adaptorName);
					logger.info("Loaded class for adaptor: " + adaptorName);
					
					adaptors.put(identity, clazz);
				} catch (ClassNotFoundException e){
					logger.error("Class Not Found Exception while loading adaptors : " + e.getMessage());
				}
			}
		}
	}	
	
	public void init(){
		try {
			InputStream is = resource.getInputStream();
			if(is != null) {
				Document document = XMLUtils.toDocument(is);
				if(document != null) { 
					parseDocument(document);
				}
			}

		} catch (Exception e) {
			logger.error("Adaptors parser: Exception while parsing XML document " + e.getMessage());
		}
	}
	
	public static Adaptor newAdaptor(Channel channel, String feedURL, boolean fullText){
		Adaptor adaptor = null;
		
		String type = channel.getEntry().getChannelType();
		if(adaptors.containsKey(type) == false){
			type = GENERIC_ADAPTOR_NAME;
		}
		
		logger.info("Resolving class [asked]: " + channel + " [giving]: " + type);
		Class<?> clazz = adaptors.get(type);
		if(clazz != null){
			try {
				adaptor = (Adaptor) clazz.newInstance();
				adaptor.setChannel(channel);
				
				if(feedURL != null){
					adaptor.setFeedURL(feedURL);
				}
				
				adaptor.setFullText(fullText);
			} catch (InstantiationException e) {
				logger.error("Instantiation Exception while loading adaptors : " + e.getMessage());
				e.printStackTrace();
				
			} catch (IllegalAccessException e) {
				logger.error("Illegal Access Exception while loading adaptors : " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		return adaptor;
	}
}
