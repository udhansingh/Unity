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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.onesun.atomator.adaptors.AdaptorFactory;
import org.onesun.atomator.channels.AuthenticatedChannel;
import org.onesun.atomator.channels.Channel;
import org.onesun.atomator.channels.UnAuthenticatedChannel;
import org.onesun.atomator.model.OAuthResult;
import org.onesun.atomator.model.PropertyEntry;
import org.onesun.atomator.model.SecretEntry;
import org.onesun.atomator.model.SubscriptionEntry;
import org.onesun.atomator.model.Subscriptions;
import org.onesun.utils.SecurityUtils;
import org.onesun.utils.XMLUtils;
import org.scribe.oauth.Scribe;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class  SubscriptionManager {
	private static Logger logger = Logger.getLogger(SubscriptionManager.class);

	private static Map<String, Subscriptions> userSubscriptions = Collections.synchronizedMap(new HashMap<String, Subscriptions>());

	public static Subscriptions getSubscriptionsByUser(String user){
		return userSubscriptions.get(user);
	}
	
	public static Map<String, SubscriptionEntry> getEntriesByUser(String user){
		return userSubscriptions.get(user).getSubscriptions();
	}
	
	private static Resource resource = null;
	
	public static Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		SubscriptionManager.resource = resource;
	}

	public static final String SUBSCRIBE = ("subscribe");
	public static final String UNSUBSCRIBE = ("unsubscribe");
	public static final String AUTHORIZE = ("authorize");
	
	public static final String GENERATED = ("generated");
	
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
						<action>none</action>
						<identity>generated</identity>
						<user>john.doe@somedomain.com</user>
						<description>Twitter Status Updates</description>
						<channelType>twitter</channelType>
						<feedURL>https://api.twitter.com/1/statuses/friends_timeline.atom</feedURL>
						<fullText></fullText>
						<feedGroup></feedGroup>
					</entry>
					*/
					String action = XMLUtils.getValue(item, "action");
					String identity = XMLUtils.getValue(item, "identity");
					String user = XMLUtils.getValue(item, "user");
					String description = XMLUtils.getValue(item, "description");
					String channelType = XMLUtils.getValue(item, "channelType");
					String feedURL = XMLUtils.getValue(item, "feedURL");
					String fullText = XMLUtils.getValue(item, "fullText");
					String feedGroup = XMLUtils.getValue(item, "feedGroup");	

					if(identity.compareToIgnoreCase(SubscriptionManager.GENERATED) == 0){
						identity = SecurityUtils.makeHash(description);
					}
					

					SubscriptionEntry entry = new SubscriptionEntry(
							action, channelType, description, identity, user, feedURL, fullText, feedGroup
					);
					if(action.compareToIgnoreCase("none") == 0){
						entry.setEnabled(false);
					}

					addUserSubscription(entry);
				}catch(Exception e){
					logger.error("Exception while extracting subscriptions : " + e.getMessage());
				}
			}
		}
		
		processEntries(true);
	}
	
	private void addUserSubscription(SubscriptionEntry entry){
		Subscriptions subscriptions = userSubscriptions.get(entry.getUser());
		if(subscriptions == null){
			subscriptions = new Subscriptions();
			subscriptions.put(entry.getIdentity(), entry);
			userSubscriptions.put(entry.getUser(), subscriptions);
		}
		else {
			subscriptions.put(entry.getIdentity(), entry);
		}
	}
	
	private void processEntries(boolean persist){
		logger.info("Listing subscripton information");
		
		for(String user : userSubscriptions.keySet()){
			Subscriptions subscriptions = userSubscriptions.get(user);
			
			if(subscriptions != null){
				for(String key : subscriptions.keySet()){
					SubscriptionEntry entry = subscriptions.get(key);
					String action = entry.getAction();
					
					if(action.compareToIgnoreCase(AUTHORIZE) == 0){
						removeByIdentity(user, key);
						action = SUBSCRIBE;
					}

					if(action.compareToIgnoreCase(SUBSCRIBE) == 0){
						Channel channel = newSubscription(entry, persist);
						subscriptions.add(channel);
					}
					else if(action.compareToIgnoreCase(UNSUBSCRIBE) == 0){
						removeByIdentity(user, key);
					}
					
					logger.info(key + " >>> " + entry);
				}				
			}
		}
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
				logger.error("SubscriptionManager list parser: Exception while parsing XML document " + e.getMessage());
			}
		}
		else {
			List<SubscriptionEntry> dbEntries = DAOFactory.getSubscriptionDAO().get("", new Object[]{});
			for(SubscriptionEntry entry : dbEntries){
				addUserSubscription(entry);
			}
			
			processEntries(false);
		}
		
		logger.info("Subscriptions Initialized");
	}
	
	private SubscriptionManager(){
	}
	
	public static Properties toProperties(SecretEntry entry) {
		if (entry != null) {
			Properties properties = new Properties();

			properties.put(Scribe.ScribeOAuthParams.CONSUMER_KEY, entry
					.getConsumerKey());
			properties.put(Scribe.ScribeOAuthParams.CONSUMER_SECRET, entry
					.getConsumerSecret());
			properties.put(Scribe.ScribeOAuthParams.REQUEST_TOKEN_VERB, "POST");
			properties.put(Scribe.ScribeOAuthParams.REQUEST_TOKEN_URL, entry
					.getRequestTokenURL());
			properties.put(Scribe.ScribeOAuthParams.ACCESS_TOKEN_VERB, "POST");
			properties.put(Scribe.ScribeOAuthParams.ACCESS_TOKEN_URL, entry
					.getAccessTokenURL());
			properties.put(Scribe.ScribeOAuthParams.CALLBACK_URL, Configuration.getCallbackURL());
			
			properties.put(OAuthSecretsManager.AUTHORIZE_URL, entry.getAuthorizeURL());
			
			return properties;
		}

		return null;
	}

	public static Channel newSubscription(SubscriptionEntry subscriptionEntry, boolean persist) {
		String type = subscriptionEntry.getChannelType();
		
		Channel channel = null;
		SecretEntry entry = OAuthSecretsManager.get(type);
		Properties properties = null;
		Map<String, String> extendedParams = null;
		
		// There must be an entry for all authenticable sources
		if (entry != null) {
			// KeyStore must exist for authenticated sources
			properties = toProperties(entry);
			
			// Load any additional properties for the channels
			PropertyEntry propertyEntry = ScribePropertiesManager.getEntries().get(type);
			if(propertyEntry != null){
				if(propertyEntry.getType().compareToIgnoreCase("scribe") == 0){
					properties.put(propertyEntry.getKey(), propertyEntry.getValue());
				}
				if(propertyEntry.getType().compareToIgnoreCase("oauth.extension") == 0){
					if(extendedParams == null) {
						extendedParams = new HashMap<String, String>();
					}
					
					extendedParams.put(propertyEntry.getKey(), propertyEntry.getValue());
				}
			}
		}
		
		if(persist == true){
			DAOFactory.getSubscriptionDAO().append(subscriptionEntry);
		}
		
		if(type.compareToIgnoreCase(AdaptorFactory.GENERIC_ADAPTOR_NAME) == 0){
			channel = new UnAuthenticatedChannel(subscriptionEntry);
		} else {
			channel = new AuthenticatedChannel(subscriptionEntry, properties, extendedParams, Configuration.getAuthenticator());
		}

		if(channel != null) channel.setup();
		return channel;
	}
	
	public static OAuthResult getByIdentity(String user, String identity) {
		return DAOFactory.getOauthResultDAO().get("WHERE identity=? AND user_id=?", new Object[] { identity, user });
	}

	public static void append(String user, OAuthResult entry, boolean update) {
		DAOFactory.getOauthResultDAO().append(user, entry, update);
	}
	
	public static boolean removeByIdentity(String user, String identity) {
		userSubscriptions.remove(user);
		boolean oauthEntryRemoved = DAOFactory.getOauthResultDAO().removeByIdentity(user, identity);
		boolean subscriptionEntryRemoved = DAOFactory.getSubscriptionDAO().removeByIdentity(user, identity);
		
		return oauthEntryRemoved && subscriptionEntryRemoved;
	}
}