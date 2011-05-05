package org.onesun.atomator.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.onesun.atomator.channels.Channel;

public class Subscriptions {
	private Set<Channel> channels = new HashSet<Channel>();
	private Map<String, SubscriptionEntry> subscriptions = new HashMap<String, SubscriptionEntry>();

	public Map<String, SubscriptionEntry> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(Map<String, SubscriptionEntry> subscriptions) {
		this.subscriptions = subscriptions;
	}

	public void put(String identity, SubscriptionEntry entry) {
		subscriptions.put(identity, entry);
	}

	public Set<String> keySet() {
		return subscriptions.keySet();
	}

	public SubscriptionEntry get(String key) {
		return subscriptions.get(key);
	}

	public void setChannels(Set<Channel> channels) {
		this.channels = channels;
	}

	public Set<Channel> getChannels() {
		return channels;
	}

	public void add(Channel channel) {
		channels.add(channel);
	}
}
