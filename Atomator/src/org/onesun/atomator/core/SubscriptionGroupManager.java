package org.onesun.atomator.core;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.onesun.atomator.model.SubscriptionGroupEntry;
import org.onesun.utils.SecurityUtils;
import org.onesun.utils.XMLUtils;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SubscriptionGroupManager {
	private static Logger logger = Logger.getLogger(SubscriptionManager.class);
	
	private static Resource resource = null;
	
	public static Resource getResource() {
		return resource;
	}
	private static Map<String, SubscriptionGroupEntry> entries = new HashMap<String, SubscriptionGroupEntry>();

	public static Map<String, SubscriptionGroupEntry> getEntries(){
		return entries;
	}
	
	public void setResource(Resource resource) {
		SubscriptionGroupManager.resource = resource;
	}
	
	private SubscriptionGroupManager(){
	}
	
	public void init(){
		if(Configuration.isLoadFromXML()){
			try {
				InputStream is = resource.getInputStream();
				if(is != null) {
					Document document = XMLUtils.toDocument(is);
	
					if(document != null) { 
						parseDocument(document);
					}
				}
	
			} catch (Exception e) {
				logger.error("SubscriptionManager group list parser: Exception while parsing XML document " + e.getMessage());
			}
		}
		else {
			List<SubscriptionGroupEntry> dbEntries = DAOFactory.getSubscriptionGroupDAO().get("", new Object[]{});
			for(SubscriptionGroupEntry entry : dbEntries){
				entries.put(entry.getIdentity(), entry);
			}
			
			processEntries(entries, false);
		}
		
		logger.info("SubscriptionManager Groups Initialized");
	}
	
	
	/**
	 * Parse the subscription groups from the subscripton-group-list.xml
	 * @param document
	 */
	private void parseDocument(Document document){
		Element element = document.getDocumentElement();

		NodeList updates = element.getElementsByTagName("entry");
		if(updates != null && updates.getLength() > 0){
			for(int index = 0; index < updates.getLength(); index++){
				Element item = (Element)updates.item(index);

				try{
					/* 
					Example
					-------
					<entry>
						<identity>social-media</identity>
						<description>Social Feeds</description>
						<name>social-media</name>
					</entry>
					*/
					String identity = XMLUtils.getValue(item, "identity");
					String description = XMLUtils.getValue(item, "description");
					String name = XMLUtils.getValue(item, "name");
					String user = XMLUtils.getValue(item, "user");

					if(identity.compareToIgnoreCase(SubscriptionManager.GENERATED) == 0){
						identity = SecurityUtils.makeHash(description);
					}
					

					SubscriptionGroupEntry entry = new SubscriptionGroupEntry(
							identity, user, description, name
					);
					

					entries.put(identity, entry);
				}catch(Exception e){
					logger.error("Exception while extracting subscriptions : " + e.getMessage());
				}
			}
		}
		
		processEntries(entries, true);
	}	
	
	/**
	 * Process the list of entries, persisting the subscribption groups to the database
	 * @param entries
	 * @param persist
	 */
	private void processEntries(Map<String, SubscriptionGroupEntry> entries, boolean persist){
		logger.info("Listing subscripton group information");
		for(String key : entries.keySet()){
			SubscriptionGroupEntry entry = entries.get(key);
			DAOFactory.getSubscriptionGroupDAO().append(entry);
			logger.info("Adding Entries: "+ key + " >>> " + entry);
		}
	}
	
}
