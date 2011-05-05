package org.onesun.atomator.test;

import org.apache.abdera.model.Entry;
import org.onesun.atomator.core.Configuration;
import org.onesun.atomator.delegates.AbstractDelegate;
import org.onesun.atomator.delegates.DelegateObject;
import org.onesun.utils.http.HTTPMethod;
import org.onesun.utils.http.Request;
import org.onesun.utils.http.Response;

public class ExtractionDelegate extends AbstractDelegate {
	// private static Logger logger = Logger.getLogger(ExtractionDelegate.class);

	@Override
	public DelegateObject run(DelegateObject object) {
		for(Entry entry : object.getEntries()){
			try {
				// TODO: Should the content be extracted from entry?
				// or should content be scrapped?
				Request request = new Request(HTTPMethod.GET, entry.getId().toString(), Configuration.getHttpConnectionTimeout());
				Response response = request.send();
				String body = response.getBody();
				
				if(body != null){
					// TODO: Flat content
				}
			}catch(Exception e){
			}
		}
		
		// The entries are enriched with classification details
		return object;
	}
}