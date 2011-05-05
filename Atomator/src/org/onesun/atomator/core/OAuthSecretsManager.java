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

import org.apache.log4j.Logger;
import org.onesun.atomator.model.SecretEntry;
import org.onesun.utils.XMLUtils;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class OAuthSecretsManager {
	public static final String AUTHORIZE_URL = "authorizeURL";
	
	private static Logger logger = Logger.getLogger(OAuthSecretsManager.class);
	
	private static Map<String, SecretEntry> entries = new HashMap<String, SecretEntry>();
	private static Resource resource = null;
	
	public static Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		OAuthSecretsManager.resource = resource;
	}
	
	public static SecretEntry get(String identity){
		return entries.get(identity);
	}

	private void parseDocument(Document document){
		Element element = document.getDocumentElement();

		NodeList updates = element.getElementsByTagName("entry");
		if(updates != null && updates.getLength() > 0){
			for(int index = 0; index < updates.getLength(); index++){
				Element item = (Element)updates.item(index);

				try{
					String identity = XMLUtils.getValue(item, "identity");
					String consumerKey = XMLUtils.getValue(item, "consumerKey");
					String consumerSecret = XMLUtils.getValue(item, "consumerSecret");
					String requestTokenURL = XMLUtils.getValue(item, "requestTokenURL");
					String accessTokenURL = XMLUtils.getValue(item, "accessTokenURL");
					String authorizeURL = XMLUtils.getValue(item, AUTHORIZE_URL);

					SecretEntry entry = new SecretEntry(consumerKey, consumerSecret,
							requestTokenURL, accessTokenURL, authorizeURL);

					entries.put(identity, entry);
				}catch(Exception e){
					logger.error("Exception while extracting keys : " + e.getMessage());
				}
			}
		}
		
		logger.info("Listing keystore information");
		for(String key : entries.keySet()){
			SecretEntry entry = entries.get(key);
			
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
			logger.error("KeyStore parser: Exception while parsing XML document " + e.getMessage());
		}
	}
}
