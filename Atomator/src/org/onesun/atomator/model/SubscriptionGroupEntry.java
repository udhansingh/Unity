package org.onesun.atomator.model;

public class SubscriptionGroupEntry {
	
    private String identity = null;
	private String name = null;
	private String description = null;
	private String user = null;

	public SubscriptionGroupEntry(){
	}
	
	public SubscriptionGroupEntry(String identity, String user, String description, String name) {
		this.identity = identity;
		this.user = user;
		this.description = description;
		this.name = name;
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}	
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getIdentity() {
		return identity;
	}
	public String toString(){
		return 
			"[user:" + user + "], " +
			"[identity:" + identity + "], " +
			"[name:" + name + "], " +
			"[description:" + description + "]";
		
	}
}
