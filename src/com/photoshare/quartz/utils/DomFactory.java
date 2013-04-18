package com.photoshare.quartz.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SimpleTrigger;

public final class DomFactory {

	private static final String QUARTZ_FILE_PATH = "quartz_data.xml";

	private static Document document = DocumentHelper.createDocument();

	private static File file;

	private static final class DomFactoryHolder {
		private static DomFactory factory = new DomFactory();
	}

	public void init() {
		file = new File(Thread.currentThread().getContextClassLoader()
				.getResource(QUARTZ_FILE_PATH).getPath());
		InputStream in;
		try {
			in = new FileInputStream(file);
			BufferedInputStream input = new BufferedInputStream(in);
			SAXReader saxReader = new SAXReader();
			try {
				document = saxReader.read(input);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		} catch (FileNotFoundException e1) {

		}
	}

	private DomFactory() {

	}

	public static final DomFactory getDefault() {
		return DomFactoryHolder.factory;
	}

	public void schedule(JobDetail jDetail, SimpleTrigger trigger) {

		Element element = getScheduleElement();

		// process job element
		Element newJob = element.addElement("job");
		newJob.addElement("name").addText(jDetail.getKey().getName());
		newJob.addElement("group").addText(jDetail.getKey().getGroup());
		newJob.addElement("description").addText(jDetail.getDescription());
		newJob.addElement("job-class").addText(jDetail.getJobClass().getName());
		newJob.addElement("durability").addText(
				String.valueOf(jDetail.isDurable()));
		newJob.addElement("recover").addText(
				String.valueOf(jDetail.requestsRecovery()));

		JobDataMap map = jDetail.getJobDataMap();
		if (map != null) {
			Element data = newJob.addElement("job-data-map");
			for (String key : map.keySet()) {
				Element entry = data.addElement("entry");
				entry.addElement("key").addText(key);
				entry.addElement("value").addText(map.getString(key));
			}
		}

		// process trigger element
		Element newTrigger = element.addElement("trigger");
		Element cron = newTrigger.addElement("simple");
		cron.addElement("name").addText(trigger.getKey().getName());
		cron.addElement("group").addText(trigger.getKey().getGroup());
		cron.addElement("job-name").addText(jDetail.getKey().getName());
		cron.addElement("job-group").addText(jDetail.getKey().getGroup());
		if (trigger.getStartTime() != null) {
			cron.addElement("start-time").addText(
					QuartzUtils.quartzFormat(trigger.getStartTime()));
		}
		if (trigger.getEndTime() != null) {
			cron.addElement("end-time").addText(
					QuartzUtils.quartzFormat(trigger.getEndTime()));
		}
		cron.addElement("repeat-count").addText(
				String.valueOf(trigger.getRepeatCount()));
		cron.addElement("repeat-interval").addText(
				String.valueOf(trigger.getRepeatInterval()));
		save();
	}

	public void schedule(JobDetail jDetail, CronTrigger trigger) {

		Element element = getScheduleElement();

		// process job element
		Element newJob = element.addElement("job");
		newJob.addElement("name").addText(jDetail.getKey().getName());
		newJob.addElement("group").addText(jDetail.getKey().getGroup());
		newJob.addElement("description").addText(jDetail.getDescription());
		newJob.addElement("job-class")
				.addText("com.june.quartz.jobs.SimpleJob");
		newJob.addElement("durability").addText(
				String.valueOf(jDetail.isDurable()));
		newJob.addElement("recover").addText(
				String.valueOf(jDetail.requestsRecovery()));

		JobDataMap map = jDetail.getJobDataMap();
		if (map != null) {
			Element data = newJob.addElement("job-data-map");
			for (String key : map.keySet()) {
				Element entry = data.addElement("entry");
				entry.addElement("key").addText(key);
				entry.addElement("value").addText(map.getString(key));
			}
		}

		// process trigger element
		Element newTrigger = element.addElement("trigger");
		Element cron = null;
		cron = newTrigger.addElement("cron");
		cron.addElement("name").addText(trigger.getKey().getName());
		cron.addElement("group").addText(trigger.getKey().getGroup());
		cron.addElement("job-name").addText(jDetail.getKey().getName());
		cron.addElement("job-group").addText(jDetail.getKey().getGroup());
		if (trigger.getStartTime() != null) {
			cron.addElement("start-time").addText(
					QuartzUtils.quartzFormat(trigger.getStartTime()));
		}
		if (trigger.getEndTime() != null) {
			cron.addElement("end-time").addText(
					QuartzUtils.quartzFormat(trigger.getEndTime()));
		}
		cron.addElement("cron-expression").addText(trigger.getCronExpression());

		save();
	}

	public synchronized void unschedule(JobKey job) {

		Element element = getScheduleElement();
		@SuppressWarnings("unchecked")
		List<Element> existJobs = element.elements("job");
		@SuppressWarnings("unchecked")
		List<Element> existTriggers = element.elements("trigger");

		if (existJobs != null) {
			for (Element existJob : existJobs) {
				if (existJob.element("name").getText().equals(job.getName())) {
					System.out.println("delete ---- " + job.getName());
					element.remove(existJob);
				}
			}
		}
		if (existTriggers != null) {
			for (Element existTrigger : existTriggers) {
				Element trigger = null;
				if (existTrigger.element("cron") != null) {
					trigger = existTrigger.element("cron");
				} else if (existTrigger.element("simple") != null) {
					trigger = existTrigger.element("simple");
				}
				if (trigger != null
						&& trigger.element("job-name").getText()
								.equals(job.getName())) {
					element.remove(existTrigger);
				}
			}
		}
		save();

	}

	private Element getScheduleElement() {
		Element root = document.getRootElement();
		return root.element("schedule");
	}

	private void save() {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(new BufferedOutputStream(
					new FileOutputStream(file)), format);
			writer.write(document);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
