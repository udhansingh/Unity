package org.onesun.atomator.model;


public class PropertyEntry {
	public PropertyEntry(){
	}
	
	public PropertyEntry(String type, String key, String value) {
		super();
		this.type = type;
		this.key = key;
		this.value = value;
	}
	
	public String toString(){
		return(
				"[type:" + type + "]"
				+ " [key:" + key + "]"
				+ " [value:" + value + "]"
				);
	}
	
	private String type = null;
	private String key = null;
	private String value = null;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
