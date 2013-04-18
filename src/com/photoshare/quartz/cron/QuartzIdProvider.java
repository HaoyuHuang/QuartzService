package com.photoshare.quartz.cron;

import java.util.UUID;

public class QuartzIdProvider {
	public String getId() {
		synchronized (this) {
			return UUID.randomUUID().toString().replaceAll("-", "");
		}
	}

	private QuartzIdProvider() {

	}

	private static QuartzIdProvider idProvider = new QuartzIdProvider();

	public static QuartzIdProvider Instance() {
		return idProvider;
	}
	
	public static void main(String[] args) {
		System.out.println(idProvider.getId().length());
		System.out.println(idProvider.getId());
		System.out.println(idProvider.getId());
	}

}
