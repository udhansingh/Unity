package org.onesun.atomator.test;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.abdera.model.Entry;
import org.apache.log4j.Logger;
import org.onesun.atomator.adaptors.Adaptor;
import org.onesun.atomator.core.Configuration;
import org.onesun.atomator.delegates.AbstractDelegate;
import org.onesun.atomator.delegates.DelegateObject;
import org.onesun.utils.AbderaUtils;

public class WatcherDelegate extends AbstractDelegate {
	private static Logger logger = Logger.getLogger(WatcherDelegate.class);
	
	private static Messenger messenger = null;
	
	public void setMessenger(Messenger messenger) {
		WatcherDelegate.messenger = messenger;
	}
	
	public void init(){
	}
	
	public static boolean addWatcher(Watcher watcher){
		if(!contains(watcher)){
			watchers.add(watcher);
			
			return true;
		}
		else {
			return false;
		}
	}
	
	private static List<Watcher> watchers = Collections.synchronizedList(
			new ArrayList<Watcher>());

	public static boolean contains(Watcher item){
		int index = 0;

		while(index < watchers.size()){
			Watcher watcher = watchers.get(index);

			if(watcher.getTopic().compareToIgnoreCase(item.getTopic()) == 0){
				return true;
			}

			index++;
		}

		return false;
	}

	public static List<Entry> process(List<Entry> entries){
		int droppedCount = 0;
		int watchedCount = 0;

		if(watchers == null || watchers.size() <= 0){
			return entries;
		}
		else {
			List<Entry> newEntries = new ArrayList<Entry>();
			for(Entry entry : entries){
				try{
					StringWriter writer = new StringWriter();
					entry.writeTo(writer);

					int dropped = 0;
					int watched = 1;

					for(Watcher watcher : watchers){
						String topic = watcher.getTopic().toLowerCase();
						String mode = watcher.getMode().toLowerCase();
						
						if(writer.toString().toLowerCase().indexOf(topic) >= 0){
							if(mode.compareToIgnoreCase(Watcher.DROP) == 0) {
								logger.info("Dropping info for ::: " + topic);
								dropped++;
							}
							else if(mode.compareToIgnoreCase(Watcher.WATCH) == 0){
								// Enrich with topic name
								
								String qualifiedTopic = Configuration.getQname() + topic;
								
								AbderaUtils.addElement(
										Configuration.getQname(),
										Configuration.getQnamePrefix(),
										"watchedTopic", 
										qualifiedTopic, 
										entry);

								MessageBuffer mb = new MessageBuffer();
								String message = mb.makeMessage(entry, null);
								
								if(message != null && message.length() > 0){
									messenger.send(message);
								}
								
								watched++;
								watchedCount++;
							}
						}
					}

					droppedCount += dropped;
					
					// determine if it must be dropped or added;
					if(watched > dropped){
						newEntries.add(entry);
					}
				}catch(Exception e){
				}
			}

			logger.info("Watcher Delegate: got #" + entries.size() + " dropped #" + droppedCount + " watched=" + watchedCount + " returning #" + newEntries.size());
			return newEntries;
		}
	}

	@Override
	public DelegateObject run(DelegateObject object) {
		if(messenger.isEnabled()){
			Adaptor adaptor = object.getAdaptor();
			String channelName = adaptor.getChannel().getEntry().getDescription();
			String user = adaptor.getChannel().getEntry().getUser();
			
			logger.info("WatcherDelegate: Entries to process # " + object.getEntries().size() 
				+ " for [" + channelName + "][" + user + "]");
			
			List<Entry> newEntries = process(object.getEntries());
	
			return (new DelegateObject(adaptor, newEntries));
		}
		else {
			return object;
		}
	}
}
