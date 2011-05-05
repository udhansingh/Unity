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

import org.apache.log4j.Logger;
import org.onesun.atomator.core.Configuration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;


public class HashEntryDAOImpl implements HashEntryDAO {
	private static Logger logger = Logger.getLogger(HashEntryDAOImpl.class);
	
	public static final String HASH_ENTRY_TABLE = "hash_entries";

	public void init() {
		String sql = null; 

		if(Configuration.isReinitUserData() == true){
			sql = "DROP TABLE IF EXISTS " + HASH_ENTRY_TABLE  + " CASCADE";
			Configuration.getJdbcTemplate().execute(sql);
		}
		
		sql = "CREATE TABLE " + HASH_ENTRY_TABLE + " (" +
			"hash VARCHAR(256) NOT NULL PRIMARY KEY, " +
			"user_id VARCHAR(256) NOT NULL, " +
			"time_stamp TIMESTAMP, " +
			"CONSTRAINT UNIQUE_SIMPLE_HASH UNIQUE (hash) " +
			")";
			
		try {
			Configuration.getJdbcTemplate().execute(sql);
		}
		catch(Exception e){
			logger.error("Could not create " + HASH_ENTRY_TABLE + e.getMessage());
		}
	}
	
	@Override
	public String append(String user, Date timestamp, String hash) {
		String query = "INSERT INTO " + HASH_ENTRY_TABLE + 
			" (user_id, time_stamp, hash) " +
			"VALUES " +
			"(?,?,?)";
		
		/**
		 * Specify the values 
		 */
		
		try {
			Configuration.getJdbcTemplate().update(query, new Object[] { 
				user,
				timestamp,
				hash 
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
	public Date get(String user, String hash){
		/**
		 * Implement the RowMapper callback interface
		 */
		try {
			String query = "SELECT time_stamp from " + HASH_ENTRY_TABLE + " WHERE hash=? AND user_id=?";
			return (Date)Configuration.getJdbcTemplate().query(query, new Object[]{hash, user}, 
				new RowMapper() {
					public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
						return toDateObject(resultSet);
					}
				});
		}
		catch(EmptyResultDataAccessException e){
			return null;
		}
	}
	
	private Date toDateObject(ResultSet resultSet) throws SQLException {
		Date date = resultSet.getDate("time_stamp");
		return date;
	}

	@Override
	public void delete(String user, Date olderThan) {
		try {
			String query = "DELETE from " + HASH_ENTRY_TABLE + " WHERE time_stamp<? AND user_id=?";
			Configuration.getJdbcTemplate().update(query, new Object[] { 
					olderThan,
					user
				});
		}
		catch(EmptyResultDataAccessException e){
		}
	}
}
