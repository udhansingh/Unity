package org.onesun.atomator.test;

import java.util.HashMap;

import org.apache.camel.Exchange;
import org.apache.camel.Route;
import org.apache.camel.impl.RoutePolicySupport;
import org.apache.log4j.Logger;

public class CustomRoutePolicy extends RoutePolicySupport {
	private static Logger logger = Logger.getLogger(CustomRoutePolicy.class);
			
	@SuppressWarnings("unchecked")
	HashMap theMap;

	public void onInit(Route route) {
		logger.info("CustomRoutePolicy initialised()");
	}

	// Convert ActiveMQMap to Normal JMS Text Message format
	@SuppressWarnings("unchecked")
	public void onExchangeBegin(Route route, Exchange exchange) {
		// We must do this if we we want Camel to route these messages
		theMap = (HashMap) exchange.getIn().getBody();
		exchange.getIn().setBody(theMap.get("ColumnName"));
	}
}