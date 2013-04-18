package com.photoshare.quartz.job;

import java.io.Serializable;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.JobKey;

import com.photoshare.quartz.utils.QuartzUtils;

public class JobDetailWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5426393132179770137L;

	private JobDetail job;

	private boolean isPaused = true;

	private boolean isFresh = true;

	private Date nextFireTime;

	private Date endTime;

	public JobDetailWrapper(JobDetail job, boolean isPaused) {
		super();
		this.job = job;
		this.isPaused = isPaused;
	}

	public JobDetailWrapper(JobDetail job) {
		super();
		this.job = job;
	}

	public JobDetailWrapper() {

	}

	public JobDetail getJob() {
		return job;
	}

	public void setJob(JobDetail job) {
		this.job = job;
	}

	public String getName() {
		return job.getKey().getName();
	}

	public String getDescription() {
		return job.getDescription();
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}

	public boolean isFresh() {
		return isFresh;
	}

	public void setFresh(boolean isFresh) {
		this.isFresh = isFresh;
	}

	public String getFormatedNextFireTime() {
		String retVal = QuartzUtils.MSG_NO_TRIGGER_PLUG_IN;
		if (nextFireTime != null) {
			retVal = QuartzUtils.format(nextFireTime);
		}
		if (QuartzUtils.isBlank(retVal)) {
			retVal = QuartzUtils.MSG_NO_TRIGGER_PLUG_IN;
		}
		return retVal;
	}

	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	public String getFormatedEndTime() {
		String retVal = QuartzUtils.MSG_NO_END_TIME_PLUG_IN;
		if (endTime != null) {
			retVal = QuartzUtils.format(endTime);
		}
		if (QuartzUtils.isBlank(retVal)) {
			retVal = QuartzUtils.MSG_NO_TRIGGER_PLUG_IN;
		}
		return retVal;
	}

	public boolean isEnd() {
		if (nextFireTime != null || isFresh)
			return false;
		if (endTime != null && QuartzUtils.before(endTime, new Date()))
			return true;
		return true;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getNextFireTime() {
		return nextFireTime;
	}

	public JobKey getJobKey() {
		return job.getKey();
	}

}
