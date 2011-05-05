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
package org.onesun.utils;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class SQLUtils {
	private static final Logger logger = Logger.getLogger(SQLUtils.class);
	
	public static String quote(String text) {
		return "'" + text + "'";
	}

	public static String clobToString(Clob clob) throws SQLException {
		Reader reader = clob.getCharacterStream();
		StringBuffer stringBuffer = new StringBuffer();

		int length = (int) clob.length();
		if (length > 0) {
			char[] buffer = new char[length];
			try {
				int processed = 0;
				int count = -1;
				while ((count = reader.read(buffer)) != -1) {
					stringBuffer.append(buffer);
					
					processed += count;
				}

				if(length == processed){
					logger.info("clobToString: processed bytes #" + processed);
				}
				return stringBuffer.toString();
			} catch (IOException e) {
			} finally {
			}
		}

		return null;
	}

	public static String toCSVString(List<String> list, boolean quote) {
		List<String> newList = new ArrayList<String>();

		for(String value : list){
			if(quote == true){
				newList.add(quote(value));
			}
			else {
				newList.add(value);
			}
		}
		
		if(newList != null && newList.size() > 0){
			String values = newList.toString();
			values = values.substring(1, values.length() - 1);
			
			return values;
		}
		
		return null;
	}
}
