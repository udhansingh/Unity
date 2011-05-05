package org.onesun.sfs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

@RemoteServiceRelativePath("MessengerRemoteService")
public interface MessengerRemoteService extends RemoteService {
	public static final Domain SERVER_MESSAGE_DOMAIN = DomainFactory.getDomain("server_message_domain");

	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	
	public void start();
	
	public static class Util {
		private static MessengerRemoteServiceAsync instance;
		public static MessengerRemoteServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(MessengerRemoteService.class);
			}
			return instance;
		}
	}
}
