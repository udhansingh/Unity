package org.onesun.atomator.core;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class Configuration {
	private static Logger logger = Logger.getLogger(Configuration.class);
	
	public void setDataSource(DataSource dataSource) {
		Configuration.dataSource = dataSource;
		Configuration.jdbcTemplate = new JdbcTemplate(Configuration.dataSource);
	}

	public static JdbcTemplate getJdbcTemplate(){
		return jdbcTemplate;
	}
	
	public static String getQname() {
		return qname;
	}

	public void setQname(String qname) {
		Configuration.qname = qname;
	}

	public static String getQnamePrefix() {
		return qnamePrefix;
	}

	public void setQnamePrefix(String qnamePrefix) {
		Configuration.qnamePrefix = qnamePrefix;
	}

	public static boolean isReinitAuthResults() {
		return reinitAuthResults;
	}

	public void setReinitAuthResults(boolean reinitAuthResults) {
		Configuration.reinitAuthResults = reinitAuthResults;
	}

	public static boolean isReinitUserData() {
		return reinitUserData;
	}

	public void setReinitUserData(boolean reinitUserData) {
		Configuration.reinitUserData = reinitUserData;
	}

	public static String getCallbackURL() {
		return callbackURL;
	}

	public void setCallbackURL(String callbackURL) {
		Configuration.callbackURL = callbackURL;
	}

	public static Authenticator getAuthenticator() {
		return authenticator;
	}

	public void setAuthenticator(Authenticator authenticator) {
		Configuration.authenticator = authenticator;
	}

	public static boolean isLoadFromXML() {
		return loadFromXML;
	}

	public void setLoadFromXML(boolean loadFromXML) {
		Configuration.loadFromXML = loadFromXML;
	}
	
	public void init(){
		logger.info("Configuration initialized");
	}

	public void setHttpConnectionTimeout(int httpConnectionTimeout) {
		Configuration.httpConnectionTimeout = httpConnectionTimeout * 1000;
	}

	public static int getHttpConnectionTimeout() {
		return httpConnectionTimeout;
	}

	private static int httpConnectionTimeout = (10 * 1000);
	private static DataSource dataSource = null;
	private static JdbcTemplate jdbcTemplate = null;
	
	private static String qname = null;
	private static String qnamePrefix = null;
	
	private static boolean reinitAuthResults = false;
	private static boolean reinitUserData = false;
	private static boolean loadFromXML = false;
	
	private static String callbackURL = "oob";
	private static Authenticator authenticator = null;
}
