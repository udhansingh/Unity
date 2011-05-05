package org.onesun.utils.rss;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.apache.wink.common.model.rss.RssFeed;
import org.onesun.atomator.core.Configuration;

public abstract class AbstractVerboseRSS implements VerboseRSS {
	private static Logger logger = Logger.getLogger(AbstractVerboseRSS.class);
	protected String serviceUrl = null;
	protected String url = null;
	
	public void init(){
	}
	
	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	
	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		try {
			this.url = serviceUrl + URLEncoder.encode(url, "UTF-8");
			logger.info("URL: " + this.url);
		}
		catch(UnsupportedEncodingException uee){
		}
	}

	public AbstractVerboseRSS(){
	}
	
	@Override 
	public RssFeed getEnrichedFeed(){
		return downloadRSS();
	}
	
	public AbstractVerboseRSS(String url){
		super();
		
		setUrl(url);		
	}
	
	protected RssFeed downloadRSS(){
		if(url != null){
			ClientConfig clientConfig = new ClientConfig();
			clientConfig.connectTimeout(Configuration.getHttpConnectionTimeout());
			
			RestClient client = new RestClient(clientConfig);
			
			Resource resource = client.resource(url);
			RssFeed rss = resource.accept(MediaType.APPLICATION_XML).get(RssFeed.class);
			
			return rss;
		}
		else {
			return null;
		}
	}
	
	@Override 
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
