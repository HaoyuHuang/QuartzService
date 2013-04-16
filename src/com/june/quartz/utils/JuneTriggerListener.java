package com.june.quartz.utils;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.listeners.TriggerListenerSupport;

public abstract class JuneTriggerListener extends TriggerListenerSupport {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "listener";
	}

	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			CompletedExecutionInstruction triggerInstructionCode) {
		super.triggerComplete(trigger, context, triggerInstructionCode);
		onTriggerComplete();
	}

	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		// TODO Auto-generated method stub
		super.triggerFired(trigger, context);
		onTriggerFired();
	}

	@Override
	public void triggerMisfired(Trigger trigger) {
		// TODO Auto-generated method stub
		super.triggerMisfired(trigger);
	}

	protected abstract void onTriggerFired();

	protected abstract void onTriggerComplete();
	
}
