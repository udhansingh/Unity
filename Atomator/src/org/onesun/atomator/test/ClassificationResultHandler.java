package org.onesun.atomator.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.onesun.textmining.uclassify.ResultHandler;
import org.onesun.textmining.uclassify.ServiceType;

public class ClassificationResultHandler implements ResultHandler {
	@Override
	public Map<String, Double> process(ServiceType serviceType, Map<String, Double> results) {
		if(serviceType.getClassifier() == null || serviceType.getUrl() == null || results == null) {
			return null;
		}

		// TODO: User specific Pre-Processing 
		// return the processed objects to the pipeline
		
		Map<String, Double> processed = Collections.synchronizedMap(new HashMap<String, Double>());
		
		for(String key : results.keySet()){
			Double result = results.get(key);
			
			// interested in match >= 25%
			if(result >= 25) {
				processed.put(key, result);
			}
		}
		
		if(processed.size() > 0){
			return processed;
		}
		else {
			return results;
		}
	}
}
