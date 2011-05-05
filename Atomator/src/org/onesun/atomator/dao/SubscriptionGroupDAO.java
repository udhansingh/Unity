package org.onesun.atomator.dao;

import java.util.List;

import org.onesun.atomator.model.SubscriptionGroupEntry;

public interface SubscriptionGroupDAO {
	String append(SubscriptionGroupEntry entry);
	List<SubscriptionGroupEntry> get(String whereClause, Object[] object);
	boolean removeByIdentity(String user, String identity);
}
