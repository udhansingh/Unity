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

import java.util.List;

import org.apache.abdera.model.Entry;
import org.onesun.atomator.core.AtomateService;
import org.onesun.atomator.test.QueryTechnique.Dimension;
import org.onesun.atomator.test.dao.AbderaEntryDAO;
import org.onesun.atomator.test.dao.AbderaEntryDAOImpl;
import org.onesun.utils.SpringUtils;
import org.onesun.utils.LogUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class QueryTechniqueTestApp {
	private static ApplicationContext context = SpringUtils.getApplicationContext();

	private static AbderaEntryDAO abderaEntryDAO = null;
	
	public void setAbderaEntryDAO(AbderaEntryDAO abderaEntryDAO) {
		QueryTechniqueTestApp.abderaEntryDAO = abderaEntryDAO;
	}
	public static List<Entry> getEntries(String query, Object[] object){
		if(abderaEntryDAO != null){
			return abderaEntryDAO.get(query, object);
		}
		else 
			return null;
	}
	
	public static void main(String[] args) {
		// App Logic
		LogUtils.initLog();

		// The first thing to do
		if(context == null){
			context = new ClassPathXmlApplicationContext("applicationContext.xml");
		}
		
		// Initialize first
		AtomateService.init();
		

		final String user = null;
		String query = "SELECT * FROM " + AbderaEntryDAOImpl.FEEDS_ENTRY_TABLE + " WHERE user_id=?";
		List<Entry> entries = getEntries(query, new Object[] {user});

		QueryTechnique.setFeeds(entries);

		// QueryTechnique.aggregateBy(Dimension.USER);

		System.out.println("----------------");

		QueryTechnique.aggregateBy(Dimension.DAY);

//		System.out.println("----------------");
//
//		QueryTechnique.aggregateBy(Dimension.QUARTER);
//
//		System.out.println("----------------");
//
//		QueryTechnique.aggregateBy(Dimension.HALFYEAR);
//
//		System.out.println("----------------");
//
//		QueryTechnique.aggregateBy(Dimension.ENDPOINT);
	}
}
