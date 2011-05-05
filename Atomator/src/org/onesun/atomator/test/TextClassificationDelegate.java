package org.onesun.atomator.test;

import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.Entry;
import org.apache.log4j.Logger;
import org.onesun.atomator.core.Configuration;
import org.onesun.atomator.delegates.AbstractDelegate;
import org.onesun.atomator.delegates.DelegateObject;
import org.onesun.textmining.uclassify.ServiceType;
import org.onesun.textmining.uclassify.UClassifyService;
import org.onesun.utils.AbderaUtils;

public class TextClassificationDelegate extends AbstractDelegate {
	public static final String CLASSIFICATIONS_TAG = "classifications";
	private static Logger logger = Logger.getLogger(TextClassificationDelegate.class);
	private static Abdera abdera = AbderaUtils.getAbdera();

	private static ServiceType[] types = null;
	
	public static ServiceType[] getTypes() {
		return types;
	}

	public static void setTypes(ServiceType[] types) {
		TextClassificationDelegate.types = types;
	}

	@Override
	public DelegateObject run(DelegateObject object) {
		for(Entry entry : object.getEntries()){
			String title = entry.getTitle();
			
			Element element = AbderaUtils.getElement(Configuration.getQname(), CLASSIFICATIONS_TAG, entry);
			
			if(element == null){
				element = abdera.getFactory().newExtensionElement(new QName(Configuration.getQname(), CLASSIFICATIONS_TAG));
			}
			
			for(ServiceType type : types){
				Element tElement = abdera.getFactory().newElement(new QName(Configuration.getQname(), type.getXmlFriendlyName()), element);
				
				try {

					logger.info("Running " + type.getXmlFriendlyName() + " classification for article titled: " + title);
					
					UClassifyService ucservice = new UClassifyService(
							entry.getSummary(),
							type, 
							new ClassificationResultHandler()
					);
					
					Map<String, Double> results = ucservice.process();

					if(results != null){
						// Merge results into entry object
						for(String key : results.keySet()){
							Double result = results.get(key);

							String validKey = null;
							if(type == ServiceType.ANALYZE_AGE_GROUP){
								validKey = type.getXmlFriendlyName() + "-" + key;
							}
							else {
								validKey = key;
							}
							
							Element aElement = abdera.getFactory().newElement(new QName(Configuration.getQname(), validKey), tElement);
							aElement.setText(Double.toString(result));
						}
						
					}
				} catch (Exception e) {
					logger.error("Exception while classifying entry: " + e.getMessage());
					e.printStackTrace();
				}
			}

			entry.addExtension(element);
		}
		
		// The entries are enriched with classification details
		return object;
	}
}