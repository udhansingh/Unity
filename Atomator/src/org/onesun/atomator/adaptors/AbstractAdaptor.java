/* 
Copyright 2010 Udaya Kumar (Udy)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.onesun.atomator.adaptors;

import org.apache.abdera.model.Feed;
import org.onesun.atomator.channels.Channel;
import org.onesun.atomator.model.OAuthResult;
import org.onesun.utils.http.HTTPMethod;
import org.onesun.utils.http.Request;
import org.onesun.utils.http.Response;
import org.scribe.oauth.Scribe;
import org.scribe.oauth.Token;

public abstract class AbstractAdaptor implements Adaptor {
	protected String feedURL = null;
	protected Channel channel = null;
	protected boolean fullText = false;
	
	public boolean isFullText(){
		return fullText;
	}
	
	public void setFullText(boolean flag){
		fullText = flag;
	}
	public AbstractAdaptor(){
	}
	
	public AbstractAdaptor(Channel channel){
		this();
		this.channel = channel;
	}
	
	public AbstractAdaptor(Channel channel, String feedURL) {
		this(channel);
		this.feedURL = feedURL;
	}

	public AbstractAdaptor(Channel channel, String feedURL, boolean fullText) {
		this(channel, feedURL);
		this.fullText = fullText;
	}
	
	@Override
	public void setFeedURL(String feedURL){
		this.feedURL = feedURL;
	}
	
	@Override
	public String getFeedURL(){
		return this.feedURL;
	}

	abstract Feed parseEntries(String input);
	
	@Override
	public Channel getChannel(){
		return channel;
	}
	
	@Override
	public void setChannel(Channel channel){
		this.channel = channel;
	}
	
	@Override
	public Feed refresh() {
		if(channel != null && feedURL != null){
			Request request = new Request(HTTPMethod.GET, feedURL);

			OAuthResult resultObject = channel.getOAuthResult();
			Token token = null;
			
			if(resultObject != null){
				token = new Token(resultObject.getAccessKey(), resultObject.getAccessSecret());
			}

			Scribe scribe = channel.getScribe();
			if(scribe != null && token != null){
				scribe.signRequest(request, token);
				Response response = request.send();

				String responseText = response.getBody();
				Feed feed = null;

				if(responseText != null && responseText.length() > 0){
					feed = parseEntries(responseText);
				}

				return feed;
			}
		}
		
		return null;
	}
}
