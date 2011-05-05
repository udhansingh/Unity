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

import org.apache.log4j.Logger;
import org.onesun.atomator.core.AtomateService;
import org.onesun.atomator.core.DelegateManager;
import org.onesun.textmining.uclassify.ServiceType;
import org.onesun.utils.LogUtils;
import org.onesun.utils.SpringUtils;
import org.onesun.utils.quartz.QuartzScheduler;
import org.onesun.utils.quartz.ScheduleUnit;
import org.onesun.utils.quartz.SimpleScheduler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AtomatorTestApp {
	private static Logger logger = Logger.getLogger(AtomatorTestApp.class);
	
	private static ApplicationContext context = SpringUtils.getApplicationContext();
	private static List<String> users = null;
	
	private int refreshInterval = 10;
	
	public int getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(int refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	public void init(){
		if(users != null){
			logger.info(users.size() + " users loaded: " + users.toString());

			AtomateService.setUsers(users);
			
			AtomateService.init();

			// This must be done by client
			for(String user : users){
				AtomateService.authenticate(user);
			}
		}
	}
	
	public AtomatorTestApp() {
	}

	public void start() {
		try {
			QuartzScheduler refreshScheduler = new SimpleScheduler(
					RefreshJob.class, "minutelyTrigger", "demo");
			
			refreshScheduler.setInterval(ScheduleUnit.MINUTE, refreshInterval);
			refreshScheduler.addJob("minutelyRefresh");
			refreshScheduler.start();

			// QuartzScheduler purgeScheduler = new
			// SimpleScheduler(PurgeJob.class, "dailyTrigger", "demo");
			// purgeScheduler.setInterval(ScheduleUnit.DAY,
			// TimeUtils.toMinutes(17, 30)); // unit accepted is minutes
			// purgeScheduler.addJob("dailyPurge");
			// purgeScheduler.start();
		} catch (Exception e) {
			logger.error("Exception Starting Social App: "
					+ e.getMessage());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Initialize the Log factory.
		LogUtils.initLog();
		
		// Load Spring
		if (context == null) {
			context = new ClassPathXmlApplicationContext("applicationContext.xml");
		}

		// Register delegates
		TextClassificationDelegate.setTypes(
				new ServiceType[] {
						ServiceType.ANALYZE_MOOD
						, ServiceType.ANALYZE_AGE_GROUP
				}
		);
		
		// WatcherDelegate.addWatcher(new Watcher("apple", Watcher.DROP));
		
		// use API register(#, flag, Delegate) - if you want to register delegates in a particular order 
		DelegateManager.register(false, new TextClassificationDelegate());
		DelegateManager.register(false, new WatcherDelegate());
		DelegateManager.register(true, new MessengerDelegate());
		DelegateManager.register(true, new PersistanceDelegate());
		DelegateManager.register(false, new ExtractionDelegate());
		
		// Start test
		AtomatorTestApp test = new AtomatorTestApp();
		test.setRefreshInterval(5);
		
		test.start();
	}

	public void setUsers(List<String> users) {
		AtomatorTestApp.users = users;
	}

	public static List<String> getUsers() {
		return users;
	}
}
