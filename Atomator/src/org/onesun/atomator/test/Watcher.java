package org.onesun.atomator.test;

public class Watcher {
	public final static String DROP = "DROP";
	public final static String WATCH = "WATCH";
	
	private String topic;
	private String mode;
	
	public Watcher(String topic, String mode){
		this.topic = topic;
		this.mode = mode;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
}
