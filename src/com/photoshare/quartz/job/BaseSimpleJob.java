package com.photoshare.quartz.job;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.photoshare.quartz.utils.QuartzUtils;

public abstract class BaseSimpleJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobDetail jobD = context.getJobDetail();
		System.out.println("[" + QuartzUtils.format(context.getFireTime())
				+ "]" + jobD.getKey().getName() + "   " + jobD.getDescription());	
		perform(context);
	}
	
	protected abstract void perform(JobExecutionContext context) throws JobExecutionException;
	
}
