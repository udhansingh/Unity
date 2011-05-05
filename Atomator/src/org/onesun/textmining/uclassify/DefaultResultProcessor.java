/*
 **
	Copyright 2010 Udaya Kumar (Udy)

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 **	
 */
package org.onesun.textmining.uclassify;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.onesun.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DefaultResultProcessor extends AbstractResultProcessor {
	private static Logger logger = Logger.getLogger(DefaultResultProcessor.class);
	
	public DefaultResultProcessor(String resultText) {
		super(resultText);
	}
	
	private Map<String, Double> parseDocument(Document document){
		Map<String, Double> results = Collections.synchronizedMap(new HashMap<String, Double>());
		
		Element element = document.getDocumentElement();
		
		Element statusElement = XMLUtils.getElement(element, "status");
		
		String successString = statusElement.getAttribute("success");
		boolean success = (successString != null) 
				? Boolean.parseBoolean(successString) 
				: false;
		
		if(success == true){
			NodeList entries = element.getElementsByTagName("class");
			if(entries != null && entries.getLength() > 0){
				for(int index = 0; index < entries.getLength(); index++){
					Element item = (Element)entries.item(index);
	
					String className = item.getAttribute("className");
					String percentString = item.getAttribute("p");
					
					Double percent = (percentString != null) 
						? (Double.parseDouble(percentString) * 100)
						: -1;
						
					results.put(className, percent);
				}
			}
			
			if(results.size() > 0){
				return results;
			}
		}
		
		return null;
	}	

	@Override
	protected void processResultText() {
		try {
			Document document = XMLUtils.toDocument(resultText);

			this.results = parseDocument(document);
		} catch (Exception e) {
			logger.error("processResultText Document Parser Exception: Looks like a malformed XML " + e.getMessage());
		}
	}
}
