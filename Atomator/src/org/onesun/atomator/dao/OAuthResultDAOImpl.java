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
package org.onesun.atomator.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.onesun.atomator.core.Configuration;
import org.onesun.atomator.model.OAuthResult;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

public class OAuthResultDAOImpl implements OAuthResultDAO {
	private static Logger logger = Logger.getLogger(OAuthResultDAOImpl.class);
	
	private static final String AUTH_ENTRY_TABLE = "auth_entries";
	
	public static String getTableName(){
		return AUTH_ENTRY_TABLE;
	}

	public void init() {
		String sql = null; 

		if(Configuration.isReinitAuthResults() == true){
			sql = "DROP TABLE IF EXISTS " + AUTH_ENTRY_TABLE + " CASCADE";
			Configuration.getJdbcTemplate().execute(sql);
		}
		
		sql = "CREATE TABLE " + AUTH_ENTRY_TABLE + " (" +
				"identity VARCHAR(256) NOT NULL PRIMARY KEY, " +
				"user_id VARCHAR(256) NOT NULL, " +
				"channel_name VARCHAR(256), " +
				"access_key VARCHAR(1024), " +
				"access_secret VARCHAR(1024), " +
				"verification_code VARCHAR(512), " +
				"CONSTRAINT UNIQUE_OAUTH_IDENTITY UNIQUE(identity)" +
				" )";
		
		try {
			Configuration.getJdbcTemplate().execute(sql);
		}
		catch(Exception e){
			logger.error("Could not create " + AUTH_ENTRY_TABLE + e.getMessage());
		}
	}
	
	@Override
	public void append(String user, OAuthResult entry, boolean update) {
		String query = "INSERT INTO " + AUTH_ENTRY_TABLE + 
			" (identity, user_id, channel_name, access_key, access_secret, verification_code) " +
			" VALUES (?,?,?,?,?,?)";
		
		/**
		 * Specify the values 
		 */
		try {
			Configuration.getJdbcTemplate().update(query, new Object[] { entry.getIdentity(), user, entry.getChannelName(), entry.getAccessKey(),
				entry.getAccessSecret(), entry.getVerificationCode() });
			
			logger.info("Added Authentication Results for " + entry.getChannelName());
		}catch(DataAccessException e1){
			if(e1.getCause().getMessage().contains("integrity constraint violation: unique constraint or index violation; SYS_PK_10029")){
				
				if(update == true){
					query = "UPDATE " + AUTH_ENTRY_TABLE + 
					" SET access_key=?, access_secret=?, verification_code=? WHERE identity=?";
					
					try {
						Configuration.getJdbcTemplate().update(query, new Object[] { entry.getAccessKey(),
							entry.getAccessSecret(), entry.getVerificationCode(), entry.getIdentity() });
						
						logger.info("Updated Authentication Results for " + entry.getChannelName());
					}catch(DataAccessException e2){
						logger.error("Exception while updating authentication results for " + entry.getChannelName() + " " + e2.getMessage() );
					}
				}
			}
		}
	}

	@Override
	public boolean removeByIdentity(String user, String identity) {
		String query = "DELETE FROM " + AUTH_ENTRY_TABLE + " WHERE identity=? AND user_id=?";

		/**
		 * Specify the values 
		 */
		try {
			try {
				Configuration.getJdbcTemplate().update(query, new Object[] { identity, user });
				
				return true;
			}catch(Exception e){
				logger.error("Could not remove entry for " + identity + " from " + AUTH_ENTRY_TABLE + e.getMessage());
			}
		}
		catch(EmptyResultDataAccessException e){
		}
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public OAuthResult get(String whereClause, Object[] object) {
		String query = "SELECT * FROM " + AUTH_ENTRY_TABLE + " " + whereClause;
		/**
		 * Implement the RowMapper callback interface
		 */
		try {
			return (OAuthResult) Configuration.getJdbcTemplate().queryForObject(query, object, 
				new RowMapper() {
					public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
						return newPBEntry(resultSet);
					}
				});
		}
		catch(EmptyResultDataAccessException e){
			return null;
		}
	}
	
	private OAuthResult newPBEntry(ResultSet resultSet) throws SQLException {
		OAuthResult info = new OAuthResult(null, null);
		
		info.setIdentity(resultSet.getString("identity"));
		info.setChannelName(resultSet.getString("channel_name"));
		info.setAccessKey(resultSet.getString("access_key"));
		info.setAccessSecret(resultSet.getString("access_secret"));
		info.setVerificationCode(resultSet.getString("verification_code"));
		
		return info;
	}
}
