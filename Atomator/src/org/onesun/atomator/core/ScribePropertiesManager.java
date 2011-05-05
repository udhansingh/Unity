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
package org.onesun.atomator.core;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.onesun.atomator.model.PropertyEntry;
import org.onesun.utils.XMLUtils;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ScribePropertiesManager {
	private static Logger logger = Logger.getLogger(ScribePropertiesManager.class);
	
	private static Map<String, PropertyEntry> entries = new HashMap<String, PropertyEntry>();
	
	public static Map<String, PropertyEntry> getEntries(){
		return entries;
	}
	
	private static Resource resource = null;
	
	public static Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		ScribePropertiesManager.resource = resource;
	}
	
	private void parseDocument(Document document){
		Element element = document.getDocumentElement();

		NodeList updates = element.getElementsByTagName("entry");
		if(updates != null && updates.getLength() > 0){
			for(int index = 0; index < updates.getLength(); index++){
				Element item = (Element)updates.item(index);

				try{
					String identity = XMLUtils.getValue(item, "identity");
					Element eprops = XMLUtils.getElement(item, "properties");
					
					if(eprops != null){
						String type = StringUtils.trim(XMLUtils.getValue(eprops, "type"));
						String key = StringUtils.trim(XMLUtils.getValue(eprops, "name"));
						String value = StringUtils.trim(XMLUtils.getValue(eprops, "value"));
						
						if(key != null && value != null && type != null){
							PropertyEntry propertyEntry = new PropertyEntry(type, key, value);
							
							entries.put(identity, propertyEntry);
						}
					}
					
				}catch(Exception e){
					logger.error("Exception while extracting subscriptions : " + e.getMessage());
				}
			}
		}
		
		logger.info("Listing properties information");
		for(String key : entries.keySet()){
			PropertyEntry entry = entries.get(key);
			
			logger.info(key + " >>> " + entry);
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
			logger.error("Scribe additional properties parser: Exception while parsing XML document " + e.getMessage());
		}
	}
}
