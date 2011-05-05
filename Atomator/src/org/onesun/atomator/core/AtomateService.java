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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.onesun.atomator.adaptors.Adaptor;
import org.onesun.atomator.adaptors.AdaptorFactory;
import org.onesun.atomator.channels.Channel;
import org.onesun.atomator.dao.HashEntryDAO;
import org.onesun.atomator.model.OAuthResult;
import org.onesun.atomator.model.ServiceEntry;
import org.onesun.atomator.model.Services;
import org.onesun.atomator.model.SubscriptionEntry;
import org.onesun.atomator.model.Subscriptions;

public class AtomateService {
	private static Logger logger = Logger.getLogger(AtomateService.class);

	private static List<String> users = null;
	
	private static Map<String, Services> userServices = Collections.synchronizedMap(new HashMap<String, Services>());
	
	public static List<Channel> getChannels(String userId){
		List<Channel> channels = new ArrayList<Channel>();
		
		Services services = userServices.get(userId);
		if(services != null){
			for(String key : services.keySet()){
				Channel channel = (services.get(key)).getChannel();
				channels.add(channel);
			}

			return channels;
		}
		else {
			return null;
		}
	}
	
	public static Adaptor getAdaptorByIdentity(String userId, String identity){
		Services services = userServices.get(userId);
		
		if(services != null){
			return services.get(identity).getAdaptor();
		}
		else {
			return null;
		}
	}
	
	private static void addAdaptor(String user, Channel channel){
		String key = channel.getEntry().getIdentity();
		
		if(key != null){
			SubscriptionEntry entry = SubscriptionManager.getEntriesByUser(user).get(key);
			
			String feedURL = null;
			if(entry != null) {
				feedURL = entry.getFeedURL();
			}
			else {
				List<String> list = AdaptorFactory.getFeedURLs(channel.getEntry().getChannelType());
				if(list.size() > 0) {
					feedURL = list.get(0);
				}
			}
			
			Adaptor adaptor = AdaptorFactory.newAdaptor(channel, feedURL, entry.isFullText());
			
			Services services = userServices.get(user);
			if(services != null){
				ServiceEntry serviceEntry = services.get(key); 
				
				if(adaptor != null && serviceEntry != null){
					serviceEntry.setAdaptor(adaptor);
				}
			}
		}
	}
	
	public static boolean isWatched(String userId, String identity){
		Services services = userServices.get(userId);
		
		if(services != null){
			for(String name : services.keySet()){
				if(identity.compareToIgnoreCase(name) == 0){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static void init() {
		// try to load from DB
		for(String user : users){
			Subscriptions subscriptions = SubscriptionManager.getSubscriptionsByUser(user);
			
			for(Channel channel : subscriptions.getChannels()){
				if(channel == null) continue;
				
				
				final String identity = channel.getEntry().getIdentity();
				final String channelName = channel.getEntry().getDescription();
				final String channelType = channel.getEntry().getChannelType();
				
				logger.info("SubscriptionManager init: user=" + user + " name=" + channelName + " type=" 
						+ channelType + " identity=" + identity + " Processing ... ");
				
				if(channel.isOAuthSource() == true){
					OAuthResult authEntry = SubscriptionManager.getByIdentity(user, identity);
					if(authEntry != null && authEntry.getAccessKey() != null){
						channel.setAuthenticated(true);
						channel.setOAuthResult(authEntry);
					}
				}
				else {
					OAuthResult authEntry = new OAuthResult(identity, channelName);
					authEntry.setAccessKey(null);
					authEntry.setAccessSecret(null);
					authEntry.setVerificationCode(null);
					channel.setOAuthResult(authEntry);
				}

				Services services = userServices.get(user);
				if(services != null){
					ServiceEntry serviceEntry = new ServiceEntry(channel, null);
					services.put(identity, serviceEntry);
					
					logger.info("SubscriptionManager init: user=" + user + " name=" + channelName + " type=" 
							+ channelType + " identity=" + identity + " Added to watchlist ... ");
		
					addAdaptor(user, channel);
				}
			}
		}
	}

	public static void authenticate(String user){
		Services services = userServices.get(user);
		
		if(services != null){
			for(String key : services.keySet()){
				ServiceEntry serviceEntry = services.get(key);
				
				if(serviceEntry != null){
					Channel channel = serviceEntry.getChannel();
	
					if(channel.isOAuthSource() == true){
						if((channel != null) && (channel.isAuthenticated() == false)){
							boolean authenticated = channel.authenticate();
	
							if(authenticated == true){
								OAuthResult result = channel.getOAuthResult();
								if(result != null) {
									// Try Updating : true
									SubscriptionManager.append(user, result, true);
								}
							}
						}
					}
					else if(channel.isOAuthSource() == false){
						OAuthResult result = channel.getOAuthResult();
						if(result != null) {
							// Don't try updating
							SubscriptionManager.append(user, result, false);
						}
					}
				}
			}
		}
	}

	public static void refreshAll(){
		for(final String key : userServices.keySet()){
			
			Thread task = new Thread(){
				public void run(){
					refresh(key);
				}
			};
		
			task.run();
		}
	}
	
	public static void refresh(final String user){
		Services services = userServices.get(user);
		
		if(services != null){
			for(String key : services.keySet()){
				final ServiceEntry serviceEntry = services.get(key);
				
				Thread task = new Thread(){
					public void run(){
						if(serviceEntry != null){
							Adaptor adaptor = serviceEntry.getAdaptor();
			
							if(adaptor != null){
								DelegateWorker worker = new DelegateWorker(user, adaptor);
								worker.start();
							}
						}
					}
				};
				
				task.start();
			}
		}
	}

	public static void purgeAll() {
		for(final String key : userServices.keySet()){
			Thread task = new Thread(){
				public void run(){
					purge(key);
				}
			};
		
			task.run();
		}
	}
	
	public static void purge(String user){
		HashEntryDAO hashEntryDAO = DAOFactory.getHashEntryDAO();
		hashEntryDAO.delete(user, new Date());
	}

	public static void setUsers(List<String> users) {
		AtomateService.users = users;
		
		if(users != null){
			for(String user : users){
				Services services = userServices.get(user);
				
				if(services == null){
					services = new Services();
					userServices.put(user, services);
				}
			}
		}
	}

	public static List<String> getUsers() {
		return users;
	}
}
