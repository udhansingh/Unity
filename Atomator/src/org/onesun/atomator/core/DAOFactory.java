package org.onesun.atomator.core;

import org.apache.log4j.Logger;
import org.onesun.atomator.dao.HashEntryDAO;
import org.onesun.atomator.dao.OAuthResultDAO;
import org.onesun.atomator.dao.SubscriptionDAO;
import org.onesun.atomator.dao.SubscriptionGroupDAO;

public class DAOFactory {
	private static Logger logger = Logger.getLogger(DAOFactory.class);
	
	private static HashEntryDAO hashEntryDAO = null;
	private static OAuthResultDAO oauthResultDAO = null;
	private static SubscriptionDAO subscriptionDAO = null;
	private static SubscriptionGroupDAO subscriptionGroupDAO = null; 
	
	public static HashEntryDAO getHashEntryDAO() {
		return hashEntryDAO;
	}

	public void setHashEntryDAO(HashEntryDAO hashEntryDAO) {
		DAOFactory.hashEntryDAO = hashEntryDAO;
	}

	public static OAuthResultDAO getOauthResultDAO() {
		return oauthResultDAO;
	}

	public void setOauthResultDAO(OAuthResultDAO oauthResultDAO) {
		DAOFactory.oauthResultDAO = oauthResultDAO;
	}

	public static SubscriptionDAO getSubscriptionDAO() {
		return subscriptionDAO;
	}

	public void setSubscriptionDAO(SubscriptionDAO subscriptionDAO) {
		DAOFactory.subscriptionDAO = subscriptionDAO;
	}
	
	public void init(){
		logger.info("DAOFactory initialized");
	}

	public void setSubscriptionGroupDAO(SubscriptionGroupDAO subscriptionGroupDAO) {
		DAOFactory.subscriptionGroupDAO = subscriptionGroupDAO;
	}

	public static SubscriptionGroupDAO getSubscriptionGroupDAO() {
		return subscriptionGroupDAO;
	}
}
