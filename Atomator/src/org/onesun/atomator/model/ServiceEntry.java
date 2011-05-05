package org.onesun.atomator.model;

import org.onesun.atomator.adaptors.Adaptor;
import org.onesun.atomator.channels.Channel;

public class ServiceEntry {
	public ServiceEntry(Channel channel, Adaptor adaptor) {
		this.channel = channel;
		this.adaptor = adaptor;
	}
	
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public Adaptor getAdaptor() {
		return adaptor;
	}
	public void setAdaptor(Adaptor adaptor) {
		this.adaptor = adaptor;
	}
	
	private Channel channel = null;
	private Adaptor adaptor = null;
}
