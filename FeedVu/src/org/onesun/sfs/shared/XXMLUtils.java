package org.onesun.sfs.shared;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class XXMLUtils {
	public static String chopQName(String text, String qName){
		String[] tokens = text.split(qName);
		if(tokens != null && tokens.length >= 1){
			text = tokens[tokens.length - 1];
		}
		
		return text;
	}
	
	public static String getQNameChoppedValue(Element element, String tag, String qName){
		String value = getValue(element, tag);
		
		if(value != null){
			return chopQName(value, qName);
		}
		else {
			return null;
		}
	}
	
	public static String getValue(Element element, String tag){
		String textValue = null;
		
		NodeList nodes = element.getElementsByTagName(tag);
		if(nodes != null && nodes.getLength() > 0){
			Element currentElement = (Element)nodes.item(0);
			Node node = currentElement.getFirstChild();
			
			if(node != null) {
				textValue = node.getNodeValue();
			}
		}
		
		return textValue;
	}
	
	public static Element getElement(Element element, String tag){
		NodeList nodes = element.getElementsByTagName(tag);
		if(nodes != null && nodes.getLength() > 0){
			
			Element currentElement = (Element)nodes.item(0);
			
			String nodeName = null; 
			if(currentElement != null) {
				nodeName = currentElement.getNodeName();
			}
			
			if(nodeName != null && nodeName.compareTo(tag) == 0){
				return currentElement;
			}
		}
		
		return null;
	}
}