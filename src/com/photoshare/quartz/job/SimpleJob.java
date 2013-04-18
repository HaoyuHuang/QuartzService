/* 
 * Copyright 2005 - 2009 Terracotta, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */

package com.photoshare.quartz.job;

import java.util.Date;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import com.photoshare.quartz.utils.DomFactory;
import com.photoshare.quartz.utils.ServerUtils;

/**
 * <p>
 * A simple callback job that gets fired off many times
 * </p>
 * 
 */
public class SimpleJob implements Job {

	/**
	 * Empty constructor for job initilization
	 */
	public SimpleJob() {

	}

	/**
	 * <p>
	 * Called by the <code>{@link org.quartz.Scheduler}</code> when a
	 * <code>{@link org.quartz.Trigger}</code> fires that is associated with the
	 * <code>Job</code>.
	 * </p>
	 * 
	 * @throws JobExecutionException
	 *             if there is an exception while executing the job.
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		JobKey jobKey = context.getJobDetail().getKey();
		System.out.println("Executing job: " + jobKey + " executing at "
				+ new Date() + ", fired by: " + context.getTrigger().getKey());
		String endPointReference = "";
		String targetNamespace = "";
		String data = "";
		String jobId = context.getJobDetail().getKey().getName();
		if (context.getMergedJobDataMap().size() > 0) {
			Set<String> keys = context.getMergedJobDataMap().keySet();
			for (String key : keys) {
				String val = context.getMergedJobDataMap().getString(key);
				System.out.println(" - jobDataMap entry: " + key + " = " + val);
				if ("url".equals(key)) {
					endPointReference = val;
				} else if ("data".equals(key)) {
					data = val;
				} else if ("targetNamespace".equals(key)) {
					targetNamespace = val;
				}
			}
		}
		if (context.getTrigger().getNextFireTime() == null) {
			DomFactory.getDefault().unschedule(context.getJobDetail().getKey());
		}
		ServerUtils.callback(endPointReference, targetNamespace, jobId, data);
	}
}
