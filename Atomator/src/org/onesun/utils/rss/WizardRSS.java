package org.onesun.utils.rss;

import java.io.UnsupportedEncodingException;

import org.apache.wink.common.model.rss.RssFeed;
import org.onesun.atomator.core.Configuration;
import org.onesun.utils.http.HTTPMethod;
import org.onesun.utils.http.Request;
import org.onesun.utils.http.Response;

public class WizardRSS extends AbstractVerboseRSS {
	public WizardRSS(){
	}
	
	public WizardRSS(String url) throws UnsupportedEncodingException {
		super();
		
		this.setUrl(url);
	}
	
	public String toString(){
		return WizardRSS.class.getSimpleName();
	}
	
	@Override
	protected RssFeed downloadRSS(){
		Request request = new Request(HTTPMethod.GET, 
				url, Configuration.getHttpConnectionTimeout());
		
		Response response = request.send();
		
		String refreshText = response.getHeader("refresh");
		if(refreshText != null && refreshText.length() > 0){
			String[] tokens = refreshText.split("url=");
			
			if(tokens.length >= 1) {
				url = tokens[1];
				
				return super.downloadRSS();
			}
		}
		
		return null;
	}
}