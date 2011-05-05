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

import java.util.Map;
import java.util.Properties;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.onesun.atomator.core.Authenticator;
import org.onesun.atomator.core.OAuthSecretsManager;
import org.onesun.atomator.model.OAuthResult;
import org.onesun.atomator.model.SubscriptionEntry;
import org.onesun.utils.AbderaUtils;
import org.scribe.oauth.Scribe;
import org.scribe.oauth.Token;


public class AuthenticatedChannel extends AbstractChannel {
	
	protected Abdera abdera = AbderaUtils.getAbdera();
	protected Factory abderaFactory = AbderaUtils.getAbderaFactory();
	protected String authorizeURL = null;
	protected Scribe scribe = null;
	protected Authenticator authenticator = null;
	protected Properties baseProperties = null;
	protected Map<String, String> extendedParams = null;
	
	@Override
	public Scribe getScribe(){
		return scribe;
	}

	public boolean isOAuthSource(){
		return oauthSource;
	}
	
	public AuthenticatedChannel(){
		super();
	}
	
	public AuthenticatedChannel(SubscriptionEntry entry){
		super(entry);
		oauthSource = true;
	}
	
	public AuthenticatedChannel(SubscriptionEntry entry, Properties baseProperties, Map<String, String> extendedParams, Authenticator authenticator){
		this(entry);

		this.authorizeURL = baseProperties.getProperty(OAuthSecretsManager.AUTHORIZE_URL);
		this.authenticator = authenticator;
		this.baseProperties = baseProperties;
		this.extendedParams = extendedParams;
	}
	
	@Override
	public void setup(){
		this.scribe = new Scribe(baseProperties);		
	}
	
	@Override
	public boolean authenticate(){
		try {
			if(oauthSource == true && (oauthResult == null || oauthResult.getVerificationCode() == null)) {
				oauthResult = new OAuthResult(entry.getIdentity(), entry.getDescription());

				// Invoke the authenticator to get the verification code
				Token requestToken = scribe.getRequestToken(extendedParams);

				if(authenticator != null && requestToken != null){
					authenticator.setAuthorizeURL(authorizeURL);
					authenticator.setRequestToken(requestToken);
					authenticator.setOAuthResult(oauthResult);
					authenticator.setScribe(scribe);

					authenticated = authenticator.authenticate();
				}
			}
		} catch(Exception e){
			System.out.println("Exception while authenticating: " + e.getMessage());
		}
		
		return authenticated;
	}
}
