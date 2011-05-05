package org.onesun.atomator.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Services {
	private Map<String, ServiceEntry> services = Collections.synchronizedMap(new HashMap<String, ServiceEntry>());

	public void setServices(Map<String, ServiceEntry> services) {
		this.services = services;
	}

	public Map<String, ServiceEntry> getServices() {
		return services;
	}

	public Set<String> keySet() {
		return services.keySet();
	}

	public ServiceEntry get(String key) {
		return services.get(key);
	}

	public void put(String identity, ServiceEntry serviceEntry) {
		services.put(identity, serviceEntry);
	}
}
