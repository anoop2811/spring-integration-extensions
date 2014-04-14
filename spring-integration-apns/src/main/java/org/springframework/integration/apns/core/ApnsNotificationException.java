package org.springframework.integration.apns.core;

public class ApnsNotificationException  extends RuntimeException {

	

	private static final long serialVersionUID = 5342486487763625985L;
	
	private String deviceToken;
	
	public ApnsNotificationException (String deviceToken) {
		super();
		this.deviceToken = deviceToken;
	}
	
	public ApnsNotificationException (String deviceToken, String message) {
		super(message);
		this.deviceToken = deviceToken;
	}
	
	public ApnsNotificationException (String deviceToken, String message, Throwable cause) {
		super(cause);
		this.deviceToken = deviceToken;
	}
	
	public String getDeviceToken() {
		return deviceToken;
	}
}
