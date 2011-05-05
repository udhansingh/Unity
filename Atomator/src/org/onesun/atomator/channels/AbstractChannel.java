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
package org.onesun.atomator.channels;

import java.util.List;

import org.onesun.atomator.model.OAuthResult;
import org.onesun.atomator.model.SubscriptionEntry;

public abstract class AbstractChannel implements Channel {
	protected SubscriptionEntry entry = null;
	protected OAuthResult oauthResult = null;
	protected boolean oauthSource = false;
	protected boolean authenticated = false;
	protected List<String> feedUrls = null;
	
	public AbstractChannel(){
		super();
	}
	
	@Override
	public SubscriptionEntry getEntry() {
		return entry;
	}
	
	@Override
	public void setEntry(SubscriptionEntry entry) {
		this.entry = entry;
	}
	
	@Override
	public boolean isOAuthSource(){
		return oauthSource;
	}
	
	public AbstractChannel(SubscriptionEntry entry){
		this.entry = entry;
	}

	@Override
	public OAuthResult getOAuthResult() {
		return oauthResult;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated; 
	}

	@Override
	public void setOAuthResult(OAuthResult entry) {
		oauthResult = entry;		
	}
	
	@Override
	abstract public void setup();
}
