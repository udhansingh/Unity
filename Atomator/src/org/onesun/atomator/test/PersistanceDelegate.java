package org.onesun.atomator.test;

import org.apache.abdera.model.Entry;
import org.apache.log4j.Logger;
import org.onesun.atomator.delegates.AbstractDelegate;
import org.onesun.atomator.delegates.DelegateObject;
import org.onesun.atomator.test.dao.AbderaEntryDAO;

public class PersistanceDelegate extends AbstractDelegate {
	private static Logger logger = Logger.getLogger(PersistanceDelegate.class);
	
	@Override
	public DelegateObject run(DelegateObject object) {
		String channelName = object.getAdaptor().getChannel().getEntry().getDescription();
		String user = object.getAdaptor().getChannel().getEntry().getUser();
		
		logger.info("Callback invoked --- "
				+ " persisting entries # " + object.getEntries().size()
				+ " for [" + channelName + "][" + user + "]");
		
		persistEntries(object);
		
		return object;
	}
	
	public static void persistEntries(DelegateObject object){
		if(abderaEntryDAO != null && object.getEntries().size() > 0){
			for(Entry entry : object.getEntries()){
				String channelName = object.getAdaptor().getChannel().getEntry().getDescription();
				String user = object.getAdaptor().getChannel().getEntry().getUser();
				
				abderaEntryDAO.append(user, channelName, entry);
			}
		}
	}
	
	private static AbderaEntryDAO abderaEntryDAO = null;
	
	public void setAbderaEntryDAO(AbderaEntryDAO abderaEntryDAO) {
		PersistanceDelegate.abderaEntryDAO = abderaEntryDAO;
	}

	public void init(){
		logger.info("PersistanceDelegate initialized");
	}
}