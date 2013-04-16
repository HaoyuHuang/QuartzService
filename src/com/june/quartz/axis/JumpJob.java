package com.june.quartz.axis;

import java.util.Date;

import com.june.quartz.cron.ScheduleManager;

public class JumpJob {

	public String simpleSchedule(int second, int repeatCount, String url,
			String targetNamespace, String data) {
		return ScheduleManager.getDefault().schedule(url, data,
				targetNamespace, second, repeatCount);
	}

	public String cronSchedule(String cronExpression, Date startDate,
			Date endDate, String url, String targetNamespace, String data) {
		return ScheduleManager.getDefault().schedule(cronExpression, startDate,
				endDate, url, targetNamespace, data);
	}

	public String unschedule(String jobId) {
		return ScheduleManager.getDefault().unshedule(jobId);
	}

}
