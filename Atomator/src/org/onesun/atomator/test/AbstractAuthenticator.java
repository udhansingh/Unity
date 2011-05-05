package org.onesun.atomator.test;

import org.apache.commons.lang.StringUtils;
import org.onesun.atomator.core.Authenticator;
import org.onesun.atomator.model.OAuthResult;
import org.scribe.oauth.Scribe;
import org.scribe.oauth.Token;

public abstract class AbstractAuthenticator implements Authenticator {
	protected String authorizeURL = null;
	protected Token requestToken = null;
	protected OAuthResult oauthResult = null;
	protected Scribe scribe = null;
	
	@Override
	public String getAuthorizeURL() {
		return authorizeURL;
	}

	@Override
	public void setAuthorizeURL(String authorizeURL) {
		this.authorizeURL = authorizeURL;
	}

	@Override
	public Token getRequestToken() {
		return requestToken;
	}

	@Override
	public void setRequestToken(Token requestToken) {
		this.requestToken = requestToken;
	}
	
	@Override
	public void setOAuthResult(OAuthResult oauthResult) {
		this.oauthResult = oauthResult;
	}
	
	@Override
	public void setScribe(Scribe scribe) {
		this.scribe = scribe;
	}
	
	@Override
	public boolean updateOAuthResult(String verificationCode){
		verificationCode = StringUtils.deleteWhitespace(verificationCode);
		Token accessToken = null;
		
		if(scribe != null && requestToken != null){
			Token token = new Token(requestToken.getToken(), requestToken.getSecret());
			accessToken = scribe.getAccessToken(token, verificationCode);
		}

		
		if(oauthResult != null){
			if(accessToken != null){
				System.out.println("Verifier: " + verificationCode);
				
				oauthResult.setVerificationCode(verificationCode);
				oauthResult.setAccessKey(accessToken.getToken());
				oauthResult.setAccessSecret(accessToken.getSecret());
				
				return true;
			}
			else {
				oauthResult.setVerificationCode(null);
			}
		}
		
		return false;
	}
}
