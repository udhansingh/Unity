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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.log4j.Logger;
import org.onesun.utils.http.Request;
import org.onesun.utils.http.Response;
import org.onesun.utils.http.HTTPMethod;

public class UClassifyService {
	public UClassifyService(){
	}
	
	private final static Logger logger = Logger.getLogger(UClassifyService.class);
	
	private static String uClassifyReadAccessKey = null;
	
	public void setUClassifyReadAccessKey(String uClassifyReadAccessKey){
		UClassifyService.uClassifyReadAccessKey = uClassifyReadAccessKey;
	}
	
	private boolean removeHtml = true;
	private String text = null;
	private MediaType mediaType = MediaType.XML;
	private ServiceType serviceType = null;
	private ResultHandler resultHandler = null;

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}
	
	public UClassifyService(String text, ServiceType serviceType, ResultHandler resultHandler) {
		super();
		
		this.text = text;
		this.serviceType = serviceType;
		
		this.resultHandler = resultHandler;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private String buildServiceURL(){
		if(text == null) return null;

		try {
			String encodedText = URLEncoder.encode(text, ("UTF-8"));
			
			return ( 
				serviceType.getUrl() 
				+ "/" 
				+ serviceType.getClassifier() 
				+ "/ClassifyText?readkey=" 
				+ uClassifyReadAccessKey 
				+ "&text=" 
				+ encodedText 
				+ "&output=" 
				+ mediaType.getType()
				+ "&removeHtml=" + ((removeHtml == true) ? "true" : "false")
			);
			
		}catch (UnsupportedEncodingException e){
			logger.info("Charset UTF-8 unsupported");
		}
		finally {
		}
		
		return null;
	}
	
	public Map<String, Double> process() throws Exception {
		if(uClassifyReadAccessKey == null || uClassifyReadAccessKey.length() <= 0){
			throw new Exception("uClassify API Read Key undefined");
		}
		
		String url = buildServiceURL();

		Request request = new Request(HTTPMethod.POST, url);
		
		try {
			Response response = request.send();
			
			String responseBody = response.getBody();
			
			ResultProcessor resultProcessor = new DefaultResultProcessor(responseBody);
			
			if(resultHandler != null){
				return resultHandler.process(serviceType, resultProcessor.getResults());
			}
		}
		catch(Exception e){
			logger.error(UClassifyService.class + " Exception accessing uclassify webservice");
		}
		
		return null;
	}
}
