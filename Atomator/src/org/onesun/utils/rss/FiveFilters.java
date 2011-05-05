package org.onesun.utils.rss;

import java.io.UnsupportedEncodingException;

public class FiveFilters extends AbstractVerboseRSS {
	public FiveFilters(){
	}
	
	public FiveFilters(String url) throws UnsupportedEncodingException {
		super(url);
	}

	public String toString(){
		return FiveFilters.class.getSimpleName();
	}
}