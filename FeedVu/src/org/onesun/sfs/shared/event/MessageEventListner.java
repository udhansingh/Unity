package org.onesun.sfs.shared.event;

import de.novanic.eventservice.client.event.listener.RemoteEventListener;

public interface MessageEventListner extends RemoteEventListener {
	void onMessageEvent(MessageEvent event);
}
