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

public class OAuthResult {
	public OAuthResult(){
	}
	
	private String identity = null;
	private String channelName = null;
	private String accessKey = null;
	private String accessSecret = null;
	private String verificationCode = null;
	
	public boolean isNull(){
		return(accessKey == null && accessSecret == null && verificationCode == null);
	}

	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public OAuthResult(String identity, String channelName){
		this.identity = identity;
		this.channelName = channelName;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getAccessSecret() {
		return accessSecret;
	}
	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}
	public String getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
}