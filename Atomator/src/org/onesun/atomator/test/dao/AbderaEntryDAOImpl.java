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
package org.onesun.atomator.test.dao;

import java.io.StringWriter;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.abdera.model.Entry;
import org.apache.abdera.parser.ParseException;
import org.apache.log4j.Logger;
import org.onesun.atomator.core.Configuration;
import org.onesun.utils.AbderaUtils;
import org.onesun.utils.SQLUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

public class AbderaEntryDAOImpl implements AbderaEntryDAO {
	private static Logger logger = Logger.getLogger(AbderaEntryDAOImpl.class);

	public static final String FEEDS_ENTRY_TABLE = "feed_entries";

	public void init() {
		String sql = null;

		if (Configuration.isReinitUserData() == true) {
			sql = "DROP TABLE IF EXISTS " + FEEDS_ENTRY_TABLE + " CASCADE";
			Configuration.getJdbcTemplate().execute(sql);
		}

		sql = "CREATE TABLE " + FEEDS_ENTRY_TABLE + " ("
				+ "hash VARCHAR(256) NOT NULL PRIMARY KEY, "
				+ "user_id VARCHAR(256) NOT NULL, "
				+ "channel_name VARCHAR(256), " 
				+ "time_stamp TIMESTAMP, " 
				+ "entry LONGVARCHAR, "
				+ "CONSTRAINT UNIQUE_HASH UNIQUE (hash) " + ")";

		try {
			Configuration.getJdbcTemplate().execute(sql);
		} catch (Exception e) {
			logger.error("Could not create " + FEEDS_ENTRY_TABLE
					+ e.getMessage());
		}
	}

	@Override
	public String append(String user, String channelName, Entry entry) {
		String query = "INSERT INTO " + FEEDS_ENTRY_TABLE + 
			" (" +
			"hash, " +
			"user_id, " +
			"channel_name, " +
			"time_stamp, " +
			"entry" +
			") " + 
			"VALUES " +
			"(?,?,?,?,?)";

		/**
		 * Specify the values
		 */
		String hash = AbderaUtils.chopQName(
			AbderaUtils.getValue(
				Configuration.getQname(), "identity", entry
			)
			, Configuration.getQname()
		);

		try {
			Date now = new Date();

			StringWriter writer = new StringWriter();
			entry.writeTo(writer);

			Configuration.getJdbcTemplate().update(query, new Object[] { 
				hash,
				user,
				channelName,
				now,
				writer.toString() 
			});

			return hash;
		} catch (DuplicateKeyException dke) {
			// Return hash as it exists already
			return hash;
		} catch (Exception e) {
			logger.error("Exception inserting entry: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Entry> get(String query, Object[] object) {
		/**
		 * Implement the RowMapper callback interface
		 */
		try {
			return Configuration.getJdbcTemplate().query(query, object, new RowMapper() {
				public Object mapRow(ResultSet resultSet, int rowNum)
						throws SQLException {
					return newAbderaEntry(resultSet);
				}
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	private Entry newAbderaEntry(ResultSet resultSet) throws SQLException {
		Clob clob = resultSet.getClob("entry");
		if (clob != null) {
			String entryText = SQLUtils.clobToString(clob);

			try{
				return AbderaUtils.toAbderaEntry(entryText);
			}catch (ParseException e){
				logger.error("ParseException while creating entry from text " + e.getMessage());
			}
		}

		return null;
	}
}
