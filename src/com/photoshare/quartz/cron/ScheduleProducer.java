package com.photoshare.quartz.cron;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public final class ScheduleProducer {

	private static Scheduler scheduler;

	private ScheduleProducer() {

	}

	public static final Scheduler getScheduler() {
		if (scheduler != null) {
			return scheduler;
		} else {
			try {
				return scheduler = new StdSchedulerFactory().getScheduler();
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

}
