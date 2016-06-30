package com.snowstore.pontus.audit;

public class ApiSession {

	private static final ThreadLocal<String> customerId = new ThreadLocal<String>();

	public static void setCustomerId(String id) {
		customerId.set(id);
	}

	public static String getCustomerId() {
		return customerId.get();
	}
}
