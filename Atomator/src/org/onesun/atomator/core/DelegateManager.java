package org.onesun.atomator.core;

import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.abdera.model.Entry;
import org.onesun.atomator.delegates.Delegate;
import org.onesun.atomator.delegates.DelegateObject;

public class DelegateManager {
	private static SortedMap<Integer, Delegate> delegates = Collections.synchronizedSortedMap(new TreeMap<Integer, Delegate>());
	
	private static int nextNumber = -1;
	private static int next(){
		return ++nextNumber;
	}
	
	public static void register(boolean enabled, Delegate delegate){
		register(next(), enabled, delegate);
	}
	
	public static void register(int order, boolean enabled, Delegate delegate){
		delegate.setEnabled(enabled);
		delegates.put(order, delegate);
	}
	
	public static void setEnabled(boolean enabled){
		for(Integer key : delegates.keySet()){
			Delegate delegate = delegates.get(key);
			
			delegate.setEnabled(enabled);
		}
	}
	
	public static void runAll(DelegateObject object){
		// NOTE: Do not parallelise this
		// The delegates must be executed in the registered order
		// to work on the results from previous stage
		for(final Integer key : delegates.keySet()){
			DelegateManager.run(key, object);				
		}
	}
	
	public static void run(Integer key, DelegateObject object){
		Delegate delegate = delegates.get(key);
		
		if(delegate != null && delegate.isEnabled()){
			// The object from the previous cycle must be used
			// This will ensure that the pipeline is executed
			// with right data
			if(object != null) {
				List<Entry> entries = object.getEntries();

				if(entries != null && entries.size() > 0){
					object = delegate.run(object);
				}
			}
		}
	}
}
