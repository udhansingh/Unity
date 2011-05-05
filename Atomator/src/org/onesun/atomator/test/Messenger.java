package org.onesun.atomator.test;

import org.apache.camel.EndpointInject;
import org.apache.camel.InOnly;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;

@InOnly
public class Messenger {
	private static Logger logger = Logger.getLogger(Messenger.class);
	protected String topic = null;
	protected boolean enabled = false;

	@EndpointInject
	protected ProducerTemplate producer;

	public void init(){
		logger.info(Messenger.class + " initialized" + " enabled:" + enabled + " topic:" + topic);
	}

	public void send(String message) {
		try {
			producer.sendBody(topic, message);
		}
		catch(Exception e){
			logger.info(Messenger.class + " Exception broadcasting message");
		}
	}
	
	public void send(String user, String message) {
		try {
			producer.sendBodyAndHeader(topic, message, "user", user);
		}
		catch(Exception e){
			logger.info(Messenger.class + " Exception broadcasting message");
		}
	}
	
	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
