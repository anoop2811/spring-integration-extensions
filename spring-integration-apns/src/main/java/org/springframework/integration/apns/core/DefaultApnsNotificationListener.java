package org.springframework.integration.apns.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.RejectedNotificationListener;
import com.relayrides.pushy.apns.RejectedNotificationReason;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;

public class DefaultApnsNotificationListener implements RejectedNotificationListener<SimpleApnsPushNotification> {
	protected final Log logger = LogFactory.getLog(getClass());
  
	
	public void handleRejectedNotification(
			PushManager<? extends SimpleApnsPushNotification> pushManager,
			SimpleApnsPushNotification notification,
			RejectedNotificationReason rejectionReason) {
        throw new IllegalStateException(String.format("%s was rejected with rejection reason %s\n", notification, rejectionReason));
		
	}

}