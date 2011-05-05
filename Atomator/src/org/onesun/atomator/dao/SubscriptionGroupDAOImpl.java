package org.onesun.atomator.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.onesun.atomator.core.Configuration;
import org.onesun.atomator.model.SubscriptionGroupEntry;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

public class SubscriptionGroupDAOImpl implements SubscriptionGroupDAO {

private static Logger logger = Logger.getLogger(SubscriptionDAOImpl.class);
	
	public static final String SUBSCRIPTION_GROUP_TABLE = "subscription_group";

	public void init() {
		String sql = null; 

		if(Configuration.isReinitUserData() == true){
			sql = "DROP TABLE IF EXISTS " + SUBSCRIPTION_GROUP_TABLE  + " CASCADE";
			Configuration.getJdbcTemplate().execute(sql);
		}
		
		sql = "CREATE TABLE " + SUBSCRIPTION_GROUP_TABLE + " (" +
			"identity VARCHAR(256) NOT NULL PRIMARY KEY, " +
			"user_id VARCHAR(256)  NOT NULL, " +
			"name VARCHAR(256), " +
			"description VARCHAR(1024), " +
			"CONSTRAINT UNIQUE_SUBSCRIPTION_GROUP_IDENTITY UNIQUE (identity) " +
			")";
			
		try {
			Configuration.getJdbcTemplate().execute(sql);
		}
		catch(Exception e){
			logger.error("Could not create " + SUBSCRIPTION_GROUP_TABLE + e.getMessage());
		}
	}
	
	@Override
	public String append(SubscriptionGroupEntry entry) {
		String query = "INSERT INTO " + SUBSCRIPTION_GROUP_TABLE + 
			" (identity, user_id, description, name) " +
			"VALUES " +
			"(?,?,?,?)";
		
		/**
		 * Specify the values 
		 */
		
		try {
			Configuration.getJdbcTemplate().update(query, new Object[] { 
				entry.getIdentity(),
				entry.getUser(),
				entry.getDescription(),
				entry.getName()
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
	public List<SubscriptionGroupEntry> get(String whereClause, Object[] object){
		/**
		 * Implement the RowMapper callback interface
		 */
		try {
			String query = "SELECT * from " + SUBSCRIPTION_GROUP_TABLE + whereClause;
			return Configuration.getJdbcTemplate().query(query, object, 
				new RowMapper() {
					public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
						return toSubscriptionGroupEntry(resultSet);
					}
				});
		}
		catch(EmptyResultDataAccessException e){
			return null;
		}
	}
	
	private SubscriptionGroupEntry toSubscriptionGroupEntry(ResultSet resultSet) throws SQLException {
		String identity = (resultSet.getString("identity"));
		String user = (resultSet.getString("user"));
		String description = (resultSet.getString("description"));
		String name = (resultSet.getString("name"));
		
		// TODO: ChannelType and description into DB
		SubscriptionGroupEntry entry = new SubscriptionGroupEntry(identity, user, description, name);
		
		return entry;
	}

	@Override
	public boolean removeByIdentity(String user, String identity) {
		String query = "DELETE FROM " + SUBSCRIPTION_GROUP_TABLE  + " WHERE identity=? AND user_id=?";

		/**
		 * Specify the values 
		 */
		try {
			try {
				Configuration.getJdbcTemplate().update(query, new Object[] { identity, user });
				
				return true;
			}catch(Exception e){
				logger.error("Could not remove subscription group entry for " + identity + " from " + SUBSCRIPTION_GROUP_TABLE  + e.getMessage());
			}
		}
		catch(EmptyResultDataAccessException e){
		}
		
		return false;
	}

	
}
