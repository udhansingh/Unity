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
package org.onesun.atomator.model;


public class SubscriptionEntry {
	public SubscriptionEntry(){
	}

	public SubscriptionEntry(String action, String channelType, String description, String identity, String user, String feedURL, boolean fullText, String feedGroup) {
		this(action, channelType, description, identity, user, feedURL, (fullText == true) ? "true" : "false", feedGroup);
	}
	
	public SubscriptionEntry(String action, String channelType, String description, String identity, String user, String feedURL, String fullText, String feedGroup) {
		super();
		
		this.action = action;
		this.description = description;
		this.channelType = channelType;
		this.identity = identity;
		this.user = user;
		this.feedURL = feedURL;
		this.feedGroup = feedGroup;
		
		if(fullText != null){
			this.fullText = !(fullText.compareToIgnoreCase("false") == 0);
		}
	}
	
	private boolean enabled = true;
	private String action = null;
	private String identity = null;
	private String user = null;
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	private String description = null;
	private String channelType = null;
	private String feedURL = null;
	private boolean fullText = false;
	private String feedGroup = null;
	
	public boolean isFullText(){
		return fullText;
	}
	
	public String toString(){
		return "[action:" + action + "], " +
		"[channelName:" + description + "], " +
		"[channelType:" + channelType + "], " +
		"[identity:" + identity + "], " +
		"[user:" + user + "], " +
		"[enabled:" + enabled + "], " +
		"[feedURL:" + feedURL + "], " +
		"[isFullText" + fullText + "], "+
		"[feedGroup:" + feedGroup + "]";
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getFeedURL() {
		return feedURL;
	}
	public void setFeedURL(String feedURL) {
		this.feedURL = feedURL;
	}
	public void setFeedGroup(String feedGroup) {
		this.feedGroup = feedGroup;
	}
	public String getFeedGroup() {
		return feedGroup;
	}
}
