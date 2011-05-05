package org.onesun.atomator.test;

import org.apache.abdera.model.Entry;
import org.apache.log4j.Logger;
import org.onesun.atomator.delegates.AbstractDelegate;
import org.onesun.atomator.delegates.DelegateObject;

public class MessengerDelegate extends AbstractDelegate {
	private static Logger logger = Logger.getLogger(MessengerDelegate.class);
	
	private static Messenger messenger = null;
	
	public void setMessenger(Messenger messenger) {
		MessengerDelegate.messenger = messenger;
	}
	
	public void init(){
	}

	private void broadcast(DelegateObject object) {
		for(Entry entry : object.getEntries()){
			if(messenger != null){
				MessageBuffer mb = new MessageBuffer();
				String message = mb.makeMessage(entry, null);

				if(message != null && message.length() > 0){
					messenger.send(message);
				}
			}
		}
	}

	@Override
	public DelegateObject run(DelegateObject object) {
		if(messenger.isEnabled()){
			String channelName = object.getAdaptor().getChannel().getEntry().getDescription();
			String user = object.getAdaptor().getChannel().getEntry().getUser();
			
			logger.info("Callback invoked --- "
					+ " broadcasting entries # " + object.getEntries().size()
					+ " for [" + channelName + "][" + user + "]");

			broadcast(object);	
		}
		
		return object;
	}
}