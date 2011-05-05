package org.onesun.sfs.client;

import org.onesun.sfs.shared.event.MessageEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

public class ConnectorSample {
	public void connect(){
		RemoteEventService remoteEventService = RemoteEventServiceFactory.getInstance().getRemoteEventService();
		remoteEventService.addListener(MessengerRemoteService.SERVER_MESSAGE_DOMAIN,  
			new RemoteEventListener() {
				@Override
				public void apply(Event anEvent) {
					if(anEvent instanceof MessageEvent){
						String message = ((MessageEvent)anEvent).getMessage();
						
						try {
							NodeList nodes = null;
							
							try {
								Document messageDocument = XMLParser.parse(message);
								nodes = messageDocument.getElementsByTagName("some tag name");
							}catch(DOMException domex){
								throw new Exception("XMLParseException while parsing message");
							}

							if(nodes != null && nodes.getLength() > 0){
								for(int index = 0; index < nodes.getLength(); index++){
									Element element = (Element) nodes.item(index);

									// Update DataModel
									// Updae View

								}
							}
						}
						catch(Exception e){
							GWT.log(e.getMessage());
						}
					}
				}
			}
		);
		
		MessengerRemoteService.Util.getInstance().start(new VoidAsyncCallback());
	}
	
	private class VoidAsyncCallback implements AsyncCallback<Void>
    {
        public void onFailure(Throwable aThrowable) {}

        public void onSuccess(Void aResult) {}
    }
}
