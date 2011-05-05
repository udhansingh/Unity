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
import java.util.Date;
import java.util.List;

import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.log4j.Logger;
import org.onesun.atomator.adaptors.Adaptor;
import org.onesun.atomator.dao.HashEntryDAO;
import org.onesun.atomator.delegates.DelegateObject;
import org.onesun.utils.AbderaUtils;
import org.onesun.utils.SecurityUtils;

public class DelegateWorker extends Thread {
	private static Logger logger = Logger.getLogger(DelegateWorker.class);
	
	public void init(){
		logger.info("DelegateWorker initialized");
	}
	
	private Adaptor adaptor = null;
	private String user = null;
	
	private DelegateWorker(){
		super();
	}
	
	public DelegateWorker(String user, Adaptor adaptor) {
		this();
	
		this.user = user;
		this.adaptor = adaptor;
	}
	
	private List<Entry> toEntries(Feed feed){
		if(feed != null && feed.getEntries().size() > 0) {
			List<Entry> entries = new ArrayList<Entry>();
			
			HashEntryDAO hashEntryDAO = DAOFactory.getHashEntryDAO();
			for(Entry entry : feed.getEntries()){
				String hash = SecurityUtils.makeHash(entry.getTitle());
				String qualifiedHash = Configuration.getQname() + hash;
				String qualifiedChannelName = Configuration.getQname() + adaptor.getChannel().getEntry().getDescription();
				
				// Enrich with the identity
				AbderaUtils.addElement(
						Configuration.getQname(),
						Configuration.getQnamePrefix(),
						"identity", 
						qualifiedHash, 
						entry);

				// Enrich with endpoint name
				AbderaUtils.addElement(
						Configuration.getQname(),
						Configuration.getQnamePrefix(),
						"endpoint",	// TODO: This must be replaced with channel on "server" and "client" 
						qualifiedChannelName, 
						entry);
				
				if(hashEntryDAO != null){
					String returnValue = hashEntryDAO.append(user, new Date(), hash);
					if(returnValue != null && returnValue.compareToIgnoreCase("new") == 0){
						entries.add(entry);
					}
				}
			}

			return entries;
		}
		
		return null;
	}
	
	public void run(){
		String channelName = adaptor.getChannel().getEntry().getDescription();
		logger.info("Refresh thread started for [" + channelName + "]/[" + user + "]");
		
		Feed feed = adaptor.refresh();
		List<Entry> hashed = toEntries(feed);
		
		if(hashed != null && hashed.size() > 0) {
			DelegateObject object = new DelegateObject(adaptor, hashed);
			if(object != null) {
				logger.info("Invoking delegator for channel [" + channelName + "]/[" + user + "]");
				DelegateManager.runAll(object);
			}
		}
	}
}
