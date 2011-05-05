package org.onesun.utils.rss;

import org.apache.wink.common.model.rss.RssFeed;

public interface VerboseRSS extends Cloneable {
	RssFeed getEnrichedFeed();
	
	void setUrl(String url);
	
	Object clone() throws CloneNotSupportedException;
}
