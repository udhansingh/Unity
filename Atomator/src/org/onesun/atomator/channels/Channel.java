/* 
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
*/
package org.onesun.atomator.channels;


import org.onesun.atomator.model.OAuthResult;
import org.onesun.atomator.model.SubscriptionEntry;
import org.scribe.oauth.Scribe;

public interface Channel {
	boolean authenticate();

	boolean isAuthenticated();
	void setAuthenticated(boolean authenticated);
	
	void setOAuthResult(OAuthResult entry);
	OAuthResult getOAuthResult();
	
	SubscriptionEntry getEntry();
	void setEntry(SubscriptionEntry entry);
	
	boolean isOAuthSource();
	
	Scribe getScribe();
	
	void setup();
}
