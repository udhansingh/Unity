package org.onesun.sfs.shared.event;

import de.novanic.eventservice.client.event.Event;

public class MessageEvent implements Event {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String message = null;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageEvent(String message){
		super();
		
		this.message = message;
	}
	
	public MessageEvent(){}
}