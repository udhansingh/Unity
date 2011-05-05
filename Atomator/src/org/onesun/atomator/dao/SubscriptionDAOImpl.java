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
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.onesun.atomator.core.Configuration;
import org.onesun.atomator.model.SubscriptionEntry;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;


public class SubscriptionDAOImpl implements SubscriptionDAO {
	private static Logger logger = Logger.getLogger(SubscriptionDAOImpl.class);
	
	public static final String SUBSCRIPTION_TABLE = "subscription";

	public void init() {
		String sql = null; 

		if(Configuration.isReinitUserData() == true){
			sql = "DROP TABLE IF EXISTS " + SUBSCRIPTION_TABLE  + " CASCADE";
			Configuration.getJdbcTemplate().execute(sql);
		}
		
		sql = "CREATE TABLE " + SUBSCRIPTION_TABLE + " (" +
			"identity VARCHAR(256) NOT NULL PRIMARY KEY, " +
			"user_id VARCHAR(256)  NOT NULL, " +
			"description VARCHAR(1024), " +
			"channel_type VARCHAR(256), " +
			"enabled BOOLEAN, " +
			"full_text BOOLEAN, " +
			"action VARCHAR(256), " +
			"feedURL VARCHAR(2048), " +
			"time_stamp TIMESTAMP, " +
			"feed_group_id VARCHAR(256), " +
			"CONSTRAINT UNIQUE_SUBSCRIPTION_IDENTITY UNIQUE (identity) " +
			")";
			
		try {
			Configuration.getJdbcTemplate().execute(sql);
		}
		catch(Exception e){
			logger.error("Could not create " + SUBSCRIPTION_TABLE + e.getMessage());
		}
	}
	
	@Override
	public String append(SubscriptionEntry entry) {
		String query = "INSERT INTO " + SUBSCRIPTION_TABLE + 
			" (identity, user_id, description, channel_type, action, enabled, full_text, feedURL, time_stamp, feed_group_id) " +
			"VALUES " +
			"(?,?,?,?,?,?,?,?,?,?)";
		
		/**
		 * Specify the values 
		 */
		
		try {
			Configuration.getJdbcTemplate().update(query, new Object[] { 
				entry.getIdentity(),
				entry.getUser(),
				entry.getDescription(),
				entry.getChannelType(),
				entry.getAction(),
				entry.isEnabled(),
				entry.isFullText(),
				entry.getFeedURL(),
				new Date(),
				entry.getFeedGroup()
			});
			
			return "new";
		}catch(DuplicateKeyException dke){
			// Return hash as it exists already
			return "duplicate";
		}
		catch(Exception e){
			logger.error("Exception inserting entry: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubscriptionEntry> get(String whereClause, Object[] object){
		/**
		 * Implement the RowMapper callback interface
		 */
		try {
			String query = "SELECT * from " + SUBSCRIPTION_TABLE + whereClause;
			return Configuration.getJdbcTemplate().query(query, object, 
				new RowMapper() {
					public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
						return toSubscriptionEntry(resultSet);
					}
				});
		}
		catch(EmptyResultDataAccessException e){
			return null;
		}
	}
	
	private SubscriptionEntry toSubscriptionEntry(ResultSet resultSet) throws SQLException {
		String identity = (resultSet.getString("identity"));
		String user = (resultSet.getString("user_id"));
		String description = (resultSet.getString("description"));
		String channelType = (resultSet.getString("channel_type"));
		String action = (resultSet.getString("action"));
		String feedURL = (resultSet.getString("feedURL"));
		boolean enabled = (resultSet.getBoolean("enabled"));
		boolean fullText = (resultSet.getBoolean("full_text"));
		String feedGroupId = (resultSet.getString("feed_group_id"));

		// TODO: ChannelType and description into DB
		SubscriptionEntry entry = new SubscriptionEntry(action, description, channelType, identity, user, feedURL, fullText, feedGroupId);
		entry.setEnabled(enabled);
		
		return entry;
	}

	@Override
	public boolean removeByIdentity(String user, String identity) {
		String query = "DELETE FROM " + SUBSCRIPTION_TABLE  + " WHERE identity=? AND user_id=?";

		/**
		 * Specify the values 
		 */
		try {
			try {
				Configuration.getJdbcTemplate().update(query, new Object[] { identity, user });
				
				return true;
			}catch(Exception e){
				logger.error("Could not remove entry for " + identity + " from " + SUBSCRIPTION_TABLE  + e.getMessage());
			}
		}
		catch(EmptyResultDataAccessException e){
		}
		
		return false;
	}
}
