package com.june.quartz.cron;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.impl.matchers.NameMatcher.triggerNameEquals;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.june.quartz.job.JobDetailWrapper;
import com.june.quartz.job.SimpleJob;
import com.june.quartz.utils.JuneTriggerListener;
import com.june.quartz.utils.ManagerListener;
import com.june.quartz.utils.ManagerListenerImpl;
import com.june.quartz.utils.OperationType;
import com.june.quartz.utils.QuartzUtils;
import com.june.quartz.utils.ServerUtils;

public class ScheduleManager {
	private Scheduler scheduler = ScheduleProducer.getScheduler();

	private TriggerStore triggerStore = TriggerStore.Instance();

	private ManagerListener listener = new ManagerListenerImpl();

	private ScheduleManager() {

	}

	private static final ScheduleManager scheduleManager = new ScheduleManager();

	public static final ScheduleManager getDefault() {
		return scheduleManager;
	}

	public synchronized String schedule(String url, String data,
			String targetNamespace, int second, int repeatCount) {
		if (!QuartzUtils.checkUrl(url) || QuartzUtils.isBlank(targetNamespace)
				|| !QuartzUtils.isSecondValid(second)
				|| !QuartzUtils.isRepeatCountValid(repeatCount))
			return "fail";

		JobDetail job = newJob(SimpleJob.class)
				.withIdentity("job" + QuartzIdProvider.Instance().getId())
				.withDescription("axis calling job")
				.requestRecovery(false)
				.usingJobData(ServerUtils.SERVER_URL, url)
				.usingJobData(ServerUtils.SERVER_TARGET_NAMESPACE,
						targetNamespace)
				.usingJobData(ServerUtils.SERVER_DATA, data).build();
		Trigger trigger = TriggerStore.Instance().create(second, repeatCount);
		return scheduleJob(job, trigger);
	}

	public synchronized String schedule(String cronExpression, String url,
			String targetNamespace, String xmlData) {
		if (QuartzUtils.isBlank(url) || QuartzUtils.isBlank(targetNamespace)
				|| QuartzUtils.isCronExpressionValid(cronExpression))
			return "fail";

		JobDetail job = newJob(SimpleJob.class)
				.withIdentity("job" + QuartzIdProvider.Instance().getId())
				.withDescription("axis calling job").requestRecovery(false)
				.storeDurably(false).usingJobData(ServerUtils.SERVER_URL, url)
				.usingJobData(ServerUtils.SERVER_TARGET_NAMESPACE, xmlData)
				.usingJobData(ServerUtils.SERVER_DATA, xmlData).build();
		Trigger trigger = TriggerStore.Instance().create(cronExpression);
		return scheduleJob(job, trigger);
	}

	public synchronized String schedule(String cronExpression, Date startDate,
			Date endDate, String url, String targetNamespace, String xmlData) {
		if (!QuartzUtils.checkUrl(url) || QuartzUtils.isBlank(targetNamespace)
				|| !QuartzUtils.isCronExpressionValid(cronExpression)
				|| !QuartzUtils.before(startDate, endDate))
			return "fail";

		JobDetail job = newJob(SimpleJob.class)
				.withIdentity("job" + QuartzIdProvider.Instance().getId())
				.withDescription("axis calling job")
				.requestRecovery(false)
				.storeDurably(false)
				.usingJobData(ServerUtils.SERVER_URL, url)
				.usingJobData(ServerUtils.SERVER_TARGET_NAMESPACE,
						targetNamespace)
				.usingJobData(ServerUtils.SERVER_DATA, xmlData).build();
		Trigger trigger = TriggerStore.Instance().create(cronExpression,
				startDate, endDate);
		return scheduleJob(job, trigger);
	}

	public synchronized void pauseJobs(JobDetailWrapper[] jobs)
			throws IllegalStateException, SchedulerException {
		if (jobs == null)
			return;
		for (JobDetailWrapper job : jobs) {
			if (job.isPaused()) {
				throw new IllegalStateException("Job with Id"
						+ " already be removed from scheduler");
			}
		}
		for (JobDetailWrapper job : jobs) {
			scheduler.pauseJob(job.getJob().getKey());
			job.setPaused(true);
		}
		listener.onEvent(OperationType.Pause, jobs);
	}

	public synchronized void deleteJobs(JobDetailWrapper[] jobs)
			throws IllegalStateException, SchedulerException {
		if (jobs == null)
			return;
		for (JobDetailWrapper job : jobs) {
			scheduler.deleteJob(job.getJob().getKey());
		}
		listener.onEvent(OperationType.Delete, jobs);
	}

	public synchronized void deleteJob(JobDetail job) {
		if (job == null)
			return;
		try {
			scheduler.deleteJob(job.getKey());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		listener.onEvent(OperationType.Delete, job, null);
	}

	public synchronized String unshedule(String jobId) {
		if (!QuartzUtils.isJobKey(jobId))
			return "fail";
		JobKey key = new JobKey(jobId);
		try {
			scheduler.deleteJob(key);
			listener.onEvent(OperationType.Delete, key);
			return "success";
		} catch (SchedulerException e) {
			return "fail";
		}
	}

	public synchronized void resumeJobs(JobDetailWrapper[] jobs)
			throws IllegalStateException, SchedulerException {
		if (jobs == null)
			return;
		for (JobDetailWrapper job : jobs) {
			if (!job.isPaused()) {
				throw new IllegalStateException("Job with Id"
						+ " already be removed from scheduler");
			}
		}
		for (JobDetailWrapper job : jobs) {
			scheduler.resumeJob(job.getJob().getKey());
			job.setPaused(false);
		}
		listener.onEvent(OperationType.Resume, jobs);
	}

	public synchronized boolean start() {
		try {
			if (scheduler.isStarted()) {
				return false;
			}
			scheduler.start();
		} catch (SchedulerException e) {
			return false;
		}
		return true;
	}

	public synchronized boolean stop() {
		try {
			if (scheduler.isStarted()) {
				scheduler.shutdown(true);
				return true;
			}
		} catch (SchedulerException e) {
			return false;
		}
		return false;
	}

	public synchronized String scheduleJob(JobDetailWrapper job,
			String cronExpression) {
		Trigger trigger = triggerStore.create(cronExpression);
		try {
			scheduler.scheduleJob(job.getJob(), trigger);
		} catch (SchedulerException e) {
			return "fail";
		}
		job.setNextFireTime(trigger.getNextFireTime());
		job.setPaused(false);
		job.setFresh(false);
		listener.onEvent(OperationType.Schedule, job, trigger);
		return "success";
	}

	public synchronized String scheduleJob(JobDetail job, Trigger trigger) {
		try {
			scheduler.scheduleJob(job, trigger);
			listener.onEvent(OperationType.Schedule, job, trigger);
		} catch (SchedulerException e) {
			return "fail";
		}
		return job.getKey().getName();
	}

	public synchronized Date scheduleJob(JobDetailWrapper job,
			String cronExpression, final JuneTriggerListener triggerListener)
			throws SchedulerException {
		Trigger trigger = triggerStore.create(cronExpression);
		scheduler.scheduleJob(job.getJob(), trigger);
		scheduler.getListenerManager().addTriggerListener(triggerListener,
				triggerNameEquals(trigger.getKey().getName()));
		job.setNextFireTime(trigger.getNextFireTime());
		job.setPaused(false);
		job.setFresh(false);
		listener.onEvent(OperationType.Schedule, job, trigger);
		return trigger.getNextFireTime();
	}

	public synchronized Date scheduleJob(JobDetailWrapper job,
			String cronExpression, Date startDate,
			final JuneTriggerListener triggerListener)
			throws SchedulerException {
		Trigger trigger = triggerStore.create(cronExpression, startDate);
		scheduler.scheduleJob(job.getJob(), trigger);
		scheduler.getListenerManager().addTriggerListener(triggerListener,
				triggerNameEquals(trigger.getKey().getName()));
		job.setNextFireTime(trigger.getNextFireTime());
		job.setPaused(false);
		job.setFresh(false);
		listener.onEvent(OperationType.Schedule, job, trigger);
		return trigger.getNextFireTime();
	}

	public synchronized Date scheduleJob(JobDetailWrapper job,
			String cronExpression, Date startDate, Date endDate,
			final JuneTriggerListener triggerListener)
			throws SchedulerException {
		Trigger trigger = triggerStore.create(cronExpression, startDate,
				endDate);
		scheduler.scheduleJob(job.getJob(), trigger);
		scheduler.getListenerManager().addTriggerListener(triggerListener,
				triggerNameEquals(trigger.getKey().getName()));
		job.setNextFireTime(trigger.getNextFireTime());
		job.setEndTime(endDate);
		job.setPaused(false);
		job.setFresh(false);
		listener.onEvent(OperationType.Schedule, job, trigger);
		return trigger.getNextFireTime();
	}

	public synchronized Date scheduleJobWithEndDate(JobDetailWrapper job,
			String cronExpression, Date endDate,
			final JuneTriggerListener triggerListener)
			throws SchedulerException {
		Trigger trigger = triggerStore.createWithEndDate(cronExpression,
				endDate);
		scheduler.scheduleJob(job.getJob(), trigger);
		scheduler.getListenerManager().addTriggerListener(triggerListener,
				triggerNameEquals(trigger.getKey().getName()));
		job.setNextFireTime(trigger.getNextFireTime());
		job.setEndTime(endDate);
		job.setPaused(false);
		job.setFresh(false);
		listener.onEvent(OperationType.Schedule, job, trigger);
		return trigger.getNextFireTime();
	}

	public synchronized Date addTrigger(JobDetailWrapper job,
			String cronExpression) throws SchedulerException {
		Trigger trigger = triggerStore.addJobToTrigger(cronExpression,
				job.getJob());
		job.setNextFireTime(trigger.getNextFireTime());
		scheduler.scheduleJob(trigger);
		return trigger.getNextFireTime();
	}

}
