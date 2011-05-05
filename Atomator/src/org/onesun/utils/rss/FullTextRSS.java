package org.onesun.utils.rss;

import java.io.UnsupportedEncodingException;


public class FullTextRSS extends AbstractVerboseRSS {
	public FullTextRSS(){
	}
	
	public FullTextRSS(String url) throws UnsupportedEncodingException {
		super(url);
	}
	
	public String toString(){
		return FullTextRSS.class.getSimpleName();
	}
}