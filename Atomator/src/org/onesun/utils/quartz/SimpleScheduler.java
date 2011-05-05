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

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.TriggerUtils;
import org.quartz.impl.calendar.DailyCalendar;

public class SimpleScheduler extends AbstractScheduler {
	private String triggerName = null;
	private String groupName = null;

	public SimpleScheduler(Class<?> jobClass, String triggerName, String groupName) throws SchedulerException {
		super(jobClass);
		
		this.triggerName = triggerName;
		this.groupName = groupName;
	}

	public void addJob(String jobName) throws SchedulerException{
		JobDetail job = new JobDetail(jobName, trigger.getGroup(), jobClass);
		
		scheduler.scheduleJob(job, trigger);
	}
	
	public void setInterval(ScheduleUnit grain, int value){
		switch(grain){
			default:
				trigger = TriggerUtils.makeMinutelyTrigger(value);
			break;
				
			case SECOND: 
				trigger = TriggerUtils.makeSecondlyTrigger(value);
			break;
			
			case HOUR: 
				trigger = TriggerUtils.makeHourlyTrigger(value);
			break;

			case DAY:
				int hour = value / 60;
				int minutes = value % 60;
				trigger = TriggerUtils.makeDailyTrigger(hour, minutes);
			break;
		}
		
		trigger.setName(triggerName);
		trigger.setGroup(groupName);
	}
	
	public void setCalendar(Date start, Date end) throws SchedulerException{
		DailyCalendar dailyCalendar = new DailyCalendar(start.getTime(), end.getTime());
		dailyCalendar.setInvertTimeRange(true);
		
		scheduler.addCalendar("dailyCalendar", dailyCalendar, true, true);
		trigger.setCalendarName("dailyCalendar");
	}
	
	@Override
	public void start() throws SchedulerException{
		scheduler.start();
	}
}
