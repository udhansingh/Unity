package org.onesun.sfs.server;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.onesun.sfs.client.MessengerRemoteService;
import org.onesun.sfs.shared.event.MessageEvent;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.service.RemoteEventServiceServlet;

public class MessengerRemoteServiceImpl extends RemoteEventServiceServlet 
	implements MessengerRemoteService, MessageListener 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init(){
	}
	
	@Override
	public void start() {
	}
	
	private void sendToClient(Event event){
		addEvent(MessengerRemoteService.SERVER_MESSAGE_DOMAIN, event);
	}

	@Override
	public void onMessage(Message message) {
		String messageText = null;
		try {
			messageText = ((TextMessage)message).getText();  
		} catch (JMSException e) {
			e.printStackTrace();
		}

		if(messageText != null && messageText.length() > 0){
			if(message instanceof ActiveMQTextMessage) { 
				sendToClient(new MessageEvent(messageText));
			}
		}
	}
}
