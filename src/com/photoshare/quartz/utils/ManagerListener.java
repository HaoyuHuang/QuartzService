package com.photoshare.quartz.utils;

import java.util.List;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;

import com.photoshare.quartz.job.JobDetailWrapper;

public interface ManagerListener {
	public void onEvent(OperationType type, List<JobDetailWrapper> jobList);

	public void onEvent(OperationType type, JobDetailWrapper job);

	public void onEvent(OperationType type, JobDetailWrapper[] jobList);

	public void onEvent(OperationType type, JobDetail job, Trigger trigger);

	public void onEvent(OperationType type, JobDetailWrapper job,
			Trigger trigger);

	public void onEvent(OperationType type, JobKey job);

}
