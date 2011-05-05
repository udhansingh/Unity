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
package org.onesun.atomator.test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Person;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.onesun.atomator.core.Configuration;
import org.onesun.utils.AbderaUtils;
import org.onesun.utils.TimeUtils;

public class QueryTechnique {
	private static Logger logger = Logger.getLogger(QueryTechnique.class);
	
	private QueryTechnique(){}
	
	public enum Dimension {
		ENDPOINT,
		USER,
		HALFYEAR,
		QUARTER,
		MONTH,
		DAY
	}
	
	public static List<Entry> entries = null;
	public static List<Entry> problematic = new ArrayList<Entry>();
	
	public static List<Entry> getFeeds() {
		return entries;
	}
	
	public static void setFeeds(List<Entry> feeds) {
		QueryTechnique.entries = feeds;
	}
	
	public static void aggregateBy(Dimension what){
		if(entries == null){
			return;
		}
		
		Map<String, List<Entry>> cube = new HashMap<String, List<Entry>>();
		
		Calendar cal = Calendar.getInstance();
		
		int index = 0;
		for(Entry entry : entries){
			index++;
			
			try {
				Date date = entry.getPublished();
				if(date == null){
					date = new Date();
					cal.setTime(date);
				}
			}catch(Exception e){
				logger.error("Error parsing date for Entry #" + index);
				problematic.add(entry);
			}
			
			String key = null;
			
			switch(what){
			case HALFYEAR:
				key = TimeUtils.Month.halfYear(cal.get(Calendar.MONTH));
				break;
				
			case QUARTER:
				key = TimeUtils.Month.quarter(cal.get(Calendar.MONTH));
				break;
				
			case MONTH:
				key = TimeUtils.Month.name(cal.get(Calendar.MONTH));
				break;

			case DAY:
				key = TimeUtils.Day.name(cal.get(Calendar.DAY_OF_WEEK));
				break;

			case ENDPOINT:
				key = AbderaUtils.chopQName(
						AbderaUtils.getValue(Configuration.getQname(), "endpoint", entry)
						, Configuration.getQname()
					);
				break;
				
			default:
				try {
					Person person = entry.getAuthor();
					if(person != null){
						key = StringUtils.capitalize(person.getName());
					}
					
					if(key == null){
						key = StringUtils.capitalize(
								AbderaUtils.chopQName(
									AbderaUtils.getValue(Configuration.getQname(), "endpoint", entry)
									,Configuration.getQname()
								)
							);
					}
				}
				catch(Exception e){
					StringWriter writer = new StringWriter();

					try {
						entry.writeTo(writer);
						logger.error(writer.toString());
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
					
					logger.error("Error parsing authro for Entry #" + index);
					problematic.add(entry);
				}
				break;
			}
			
			List<Entry> agg = null;
			
			if(cube.containsKey(key)){
				agg = cube.get(key);
				
				agg.add(entry);
			}else {
				agg = new ArrayList<Entry>();
				agg.add(entry);
				
				cube.put(key, agg);
			}
		}
		
		// Report
		int total = 0;
		for(String key : cube.keySet()) {
			List<Entry> list = cube.get(key);
			logger.info("Listing for: " + key + " (" + list.size() + ")");
			
			total += list.size();
		}
		logger.info("Total feeds: " + total);
		
		logger.info("*******************************************\n");
		for(Entry entry : problematic){
			StringWriter writer = new StringWriter();
			
			try {
				entry.writeTo(writer);
				logger.info("\n" + writer.toString() + "\n");
			} catch (IOException e) {
			}
		}
	}
}
