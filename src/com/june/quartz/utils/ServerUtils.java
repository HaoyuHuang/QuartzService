package com.june.quartz.utils;

import org.apache.axis2.AxisFault;

public class ServerUtils {

	public static final String SERVER_URL = "url";

	public static final String SERVER_TARGET_NAMESPACE = "targetNamespace";

	public static final String SERVER_DATA = "data";

	public static void callback(String endPointReference,
			String targetNamespace, String jobId, String data) {
		try {
			WSClient client = new WSClient(endPointReference);
			client.callback(targetNamespace, jobId, data);
		} catch (AxisFault e) {

		}

	}
}
