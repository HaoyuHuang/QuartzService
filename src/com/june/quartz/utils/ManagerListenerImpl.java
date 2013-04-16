package com.june.quartz.utils;

import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

import com.june.quartz.job.JobDetailWrapper;

public class ManagerListenerImpl implements ManagerListener {

	private DomFactory domFactory = DomFactory.getDefault();

	@Override
	public void onEvent(OperationType type, List<JobDetailWrapper> jobList) {
		switch (type) {
		case Add:
			break;
		case Delete:
			for (JobDetailWrapper job : jobList) {
				domFactory.unschedule(job.getJob().getKey());
			}
			break;
		case Pause:
			break;
		case Resume:
			break;
		case Schedule:
			break;
		default:
			break;
		}
	}

	@Override
	public void onEvent(OperationType type, JobDetailWrapper job) {
		// TODO Auto-generated method stub
		switch (type) {
		case Add:
			break;
		case Delete:
			domFactory.unschedule(job.getJob().getKey());
			break;
		case Pause:
			break;
		case Resume:
			break;
		case Schedule:
			break;
		default:
			break;
		}
	}

	@Override
	public void onEvent(OperationType type, JobDetailWrapper[] jobList) {
		switch (type) {
		case Add:
			break;
		case Delete:
			for (JobDetailWrapper job : jobList) {
				domFactory.unschedule(job.getJob().getKey());
			}
			break;
		case Pause:
			break;
		case Resume:
			break;
		case Schedule:
			break;
		default:
			break;
		}
	}

	@Override
	public void onEvent(OperationType type, JobDetailWrapper job,
			Trigger trigger) {
		switch (type) {
		case Add:
			break;
		case Delete:
			domFactory.unschedule(job.getJob().getKey());
			break;
		case Pause:
			break;
		case Resume:
			break;
		case Schedule:
			if (trigger instanceof SimpleTrigger) {
				domFactory.schedule(job.getJob(), (SimpleTrigger) trigger);
			} else if (trigger instanceof CronTrigger) {
				domFactory.schedule(job.getJob(), (CronTrigger) trigger);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onEvent(OperationType type, JobDetail job, Trigger trigger) {
		switch (type) {
		case Add:
			break;
		case Delete:
			domFactory.unschedule(job.getKey());
			break;
		case Pause:
			break;
		case Resume:
			break;
		case Schedule:
			if (trigger instanceof SimpleTrigger) {
				domFactory.schedule(job, (SimpleTrigger) trigger);
			} else if (trigger instanceof CronTrigger) {
				domFactory.schedule(job, (CronTrigger) trigger);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onEvent(OperationType type, JobKey job) {
		// TODO Auto-generated method stub
		switch (type) {
		case Add:
			break;
		case Delete:
			domFactory.unschedule(job);
			break;
		case Pause:
			break;
		case Resume:
			break;
		case Schedule:
			break;
		default:
			break;
		}
	}

}
