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
package org.onesun.utils.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public abstract class AbstractScheduler implements QuartzScheduler{
	protected SchedulerFactory schedulerFactory = null;
	protected Scheduler scheduler = null;
	protected Class<?> jobClass = null;
	protected Trigger trigger = null;
	
	public AbstractScheduler(Class<?> jobClass) throws SchedulerException{ 
		this.jobClass = jobClass;
		
		schedulerFactory = new StdSchedulerFactory();
		scheduler = schedulerFactory.getScheduler();
	}
	
	public void startDelayed(int delay) throws SchedulerException {
		scheduler.startDelayed(delay);
	}
}
