package com.june.quartz.cron;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class TriggerStore {
	// private Map<String, Trigger> triggers = new ConcurrentHashMap<String,
	// Trigger>();

	private QuartzIdProvider idProvider = QuartzIdProvider.Instance();

	private static final class TriggerStoreHolder {
		private static final TriggerStore store = new TriggerStore();
	}

	private TriggerStore() {

	}

	public static final TriggerStore Instance() {
		return TriggerStoreHolder.store;
	}

	public void put(String identity, Trigger trigger) {
		// triggers.put(identity, trigger);
	}

	public Trigger create(String cronExpression) {
		String id = "trigger" + idProvider.getId();
		Trigger retVal = newTrigger().withIdentity(id)
				.withSchedule(cronSchedule(cronExpression)).build();
		return retVal;
	}

	public Trigger create(int second, int repeatCount) {
		String id = "trigger" + idProvider.getId();
		Trigger retVal = newTrigger()
				.withIdentity(id)
				.withSchedule(
						simpleSchedule().withIntervalInSeconds(second)
								.withRepeatCount(repeatCount))
				.startAt(DateBuilder.futureDate(second, IntervalUnit.SECOND))
				.build();
		return retVal;
	}

	/**
	 * 
	 * This method used for adding additional triggers to job that is already
	 * associated with one or more other triggers. In other words, the job has
	 * once been put into the scheduler
	 * 
	 * @param cronExpression
	 * @param job
	 * @return
	 */
	public Trigger addJobToTrigger(String cronExpression, JobDetail job) {
		// Trigger retVal = exists(cronExpression);
		String id = "trigger" + idProvider.getId();
		Trigger retVal = newTrigger().withIdentity(id)
				.withSchedule(cronSchedule(cronExpression)).forJob(job).build();
		// triggers.put(id, retVal);
		return retVal;
	}

	public Trigger create(String cronExpression, Date startDate) {
		String id = "trigger" + idProvider.getId();
		Trigger retVal = newTrigger().withIdentity(id).startAt(startDate)
				.withSchedule(cronSchedule(cronExpression)).build();
		// triggers.put(id, retVal);
		return retVal;
	}

	public Trigger createWithEndDate(String cronExpression, Date endtDate) {
		String id = "trigger" + idProvider.getId();
		Trigger retVal = newTrigger().withIdentity(id).endAt(endtDate)
				.withSchedule(cronSchedule(cronExpression)).build();
		// triggers.put(id, retVal);
		return retVal;
	}

	public Trigger create(String cronExpression, Date startDate, Date endDate) {
		String id = "trigger" + idProvider.getId();
		TriggerBuilder<Trigger> builder = newTrigger();
		builder.withIdentity(id);
		if (startDate != null) {
			builder.startAt(startDate);
		} else {
			builder.startNow();
		}
		if (endDate != null) {
			builder.endAt(endDate);
		}
		Trigger retVal = builder.withSchedule(cronSchedule(cronExpression))
				.build();
		return retVal;
	}

	public void destroy() {
		// triggers.clear();
	}

}
