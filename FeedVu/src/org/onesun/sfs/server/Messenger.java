package org.onesun.sfs.server;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.EndpointInject;
import org.apache.camel.InOnly;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;

@InOnly
public class Messenger {
	private static Logger logger = Logger.getLogger(Messenger.class);
	protected String topic = null;

	@EndpointInject
	protected ProducerTemplate producer;
	
	@EndpointInject
	protected ConsumerTemplate consumer;

	public void init(){
		logger.info(Messenger.class + " initialized");
	}

	public void send(String message) {
		try {
			producer.sendBody(topic, message);
		}
		catch(Exception e){
			logger.info(Messenger.class + " Exception broadcasting message");
		}
	}
	
	public String receive(){
		try {
			return consumer.receiveBody(topic, String.class);
		}
		catch(Exception e){
			logger.info(Messenger.class + " Exception receiving message");
		}
		return null;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
}