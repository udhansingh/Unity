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


import java.util.Date;

import org.apache.log4j.Logger;
import org.onesun.atomator.core.AtomateService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class RefreshJob implements Job {
	private Logger logger = Logger.getLogger(RefreshJob.class);
	
	@Override
	public void execute(JobExecutionContext ctxt) throws JobExecutionException {
		logger.info("Refresh Job called @ " + new Date());
		AtomateService.refreshAll();
	}
}