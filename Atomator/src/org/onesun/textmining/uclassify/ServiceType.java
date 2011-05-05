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

public enum ServiceType {
	ANALYZE_AGE_GROUP("http://uclassify.com/browse/uClassify", "Ageanalyzer", "age-group")
	, DETECT_LANGUAGE("http://uclassify.com/browse/uClassify", "Text%20Language", "text-language")
	, ANALYZE_MOOD("http://www.uclassify.com/browse/prfekt", "Mood", "mood")
	, TONE("http://uclassify.com/browse/prfekt", "Tonality", "tonality")
	, ANALYZE_GENERAL_TOPICS("http://uclassify.com/browse/uClassify", "Topics", "topics")
	;

	private String url = null;
	private String classifier = null;
	private String xmlFriendlyName = null;

	public String getXmlFriendlyName() {
		return xmlFriendlyName;
	}

	public String getUrl() {
		return url;
	}

	public String getClassifier(){
		return classifier;
	}
	
	private ServiceType(String url, String classifier, String xmlFriendlyName) {
		this.url = url;
		this.classifier = classifier;
		this.xmlFriendlyName = xmlFriendlyName;
	}
}