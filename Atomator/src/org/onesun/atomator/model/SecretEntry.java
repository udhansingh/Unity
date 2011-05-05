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
package org.onesun.atomator.model;

public class SecretEntry {
	public SecretEntry(){
	}
	
	public SecretEntry(String consumerKey, String consumerSecret,
			String requestTokenURL, String accessTokenURL, String authorizeURL) {
		super();
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.requestTokenURL = requestTokenURL;
		this.accessTokenURL = accessTokenURL;
		this.authorizeURL = authorizeURL;
	}
	
	public String toString(){
		return "[consumerKey:" + consumerKey + "], "
		+ "[consumerSecret:" + consumerSecret + "], "
		+ "[requestTokenURL:" + requestTokenURL + "], "
		+ "[accessTokenURL:" + accessTokenURL + "], "
		+ "[authorizeURL:" + authorizeURL + "]";
	}
	
	private String consumerKey = null;
	private String consumerSecret = null;
	private String requestTokenURL = null;
	private String accessTokenURL = null;
	private String authorizeURL = null;

	public String getConsumerKey() {
		return consumerKey;
	}
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}
	public String getConsumerSecret() {
		return consumerSecret;
	}
	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}
	public String getRequestTokenURL() {
		return requestTokenURL;
	}
	public void setRequestTokenURL(String requestTokenURL) {
		this.requestTokenURL = requestTokenURL;
	}
	public String getAccessTokenURL() {
		return accessTokenURL;
	}
	public void setAccessTokenURL(String accessTokenURL) {
		this.accessTokenURL = accessTokenURL;
	}
	public String getAuthorizeURL() {
		return authorizeURL;
	}
	public void setAuthorizeURL(String authorizeURL) {
		this.authorizeURL = authorizeURL;
	}
}
