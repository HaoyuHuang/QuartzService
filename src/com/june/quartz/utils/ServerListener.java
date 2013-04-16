package com.june.quartz.utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.SchedulerException;

import com.june.quartz.cron.QuartzIdProvider;
import com.june.quartz.cron.ScheduleProducer;
import com.june.quartz.cron.TriggerStore;

public class ServerListener implements ServletContextListener {

	private ServletContext context;

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		context = event.getServletContext();
		try {
			ScheduleProducer.getScheduler().start();
			TriggerStore.Instance();
			QuartzIdProvider.Instance();
			DomFactory.getDefault().init();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
