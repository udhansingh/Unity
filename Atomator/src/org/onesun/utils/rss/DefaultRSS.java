package org.onesun.utils.rss;

import java.io.UnsupportedEncodingException;

public class DefaultRSS extends AbstractVerboseRSS {
	public DefaultRSS(){
		this.serviceUrl = this.url = "";
	}
	
	public DefaultRSS(String url) throws UnsupportedEncodingException {
		this.serviceUrl = this.url = url;
	}

	public String toString(){
		return DefaultRSS.class.getSimpleName();
	}
}