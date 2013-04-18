package com.photoshare.quartz.utils;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

public class WSClient {

	private RPCServiceClient serviceClient;
	private Options options;
	private EndpointReference targetEPR;

	public WSClient(String endpoint) throws AxisFault {
		serviceClient = new RPCServiceClient();
		options = serviceClient.getOptions();
		targetEPR = new EndpointReference(endpoint);
		options.setTo(targetEPR);
	}

	public void cleanup() {
		try {
			serviceClient.cleanup();
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Object[] invokeOp(String targetNamespace, String opName,
			Object[] opArgs, Class<?>[] opReturnType) throws AxisFault,
			ClassNotFoundException {
		// 设定操作的名称
		QName opQName = new QName(targetNamespace, opName);
		// 设定返回值
		// Class<?>[] opReturn = new Class[] { opReturnType };
		// 操作需要传入的参数已经在参数中给定，这里直接传入方法中调用
		return serviceClient.invokeBlocking(opQName, opArgs, opReturnType);
	}

	private void invokeRobust(String targetNamespace, String opName,
			Object[] opArgs) throws AxisFault, ClassNotFoundException {
		// 设定操作的名称
		QName opQName = new QName(targetNamespace, opName);
		// 设定返回值
		// Class<?>[] opReturn = new Class[] { opReturnType };
		// 操作需要传入的参数已经在参数中给定，这里直接传入方法中调用
		serviceClient.invokeRobust(opQName, opArgs);
	}

	public void callback(String targetNamespace, String jobId, String data) {
		String opName = "callback";
		Object[] opArgs = new Object[] { jobId, data };
		try {
			invokeRobust(targetNamespace, opName, opArgs);
		} catch (AxisFault e) {

		} catch (ClassNotFoundException e) {

		}
	}

	public void simpleSchedule(int second, int repeatCnt, String url,
			String targetNamespace, String data) {
		String opName = "simpleSchedule";
		Object[] opArgs = new Object[] { second, repeatCnt, url,
				targetNamespace, data };
		Class<?>[] returnTypes = new Class<?>[] { String.class };
		Object[] retVal = null;
		try {
			retVal = invokeOp(targetNamespace, opName, opArgs, returnTypes);
			System.out.println(retVal[0]);
		} catch (AxisFault e) {

		} catch (ClassNotFoundException e) {

		}
	}
}
