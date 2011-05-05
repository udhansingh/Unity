package org.onesun.sfs.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.xml.client.Element;

public abstract class DataModel {
	private DataModel(){}
	
	private static Map<String, List<Element>> map = new HashMap<String, List<Element>>();
	
	private static void add(String key, Element element){
		List<Element> list = map.get(key);
		if(list == null){
			list = new ArrayList<Element>();
			
			// add the first element
			list.add(element);
			
			// hash it
			map.put(key, list);
		}
		else {
			// add arriving elements
			list.add(element);
		}
	}
	
	public static void update(Element element){
		// add to all
		add("All", element);
		
		// add to named channel
		String channelName = XXMLUtils.getQNameChoppedValue(element, "endpoint", Constants.QNAME);
		if(channelName != null){
			add(channelName, element);
		}
	}
	
	public static List<Element> getList(String key){
		for(String mapKey : map.keySet()){
			if(key.startsWith(mapKey)){
				return map.get(mapKey);
			}
		}
		
		return null;
	}

	public static Set<String> getKeyset() {
		return map.keySet();
	}
}