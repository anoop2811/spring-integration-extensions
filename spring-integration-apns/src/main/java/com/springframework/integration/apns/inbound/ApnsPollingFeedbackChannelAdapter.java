package com.springframework.integration.apns.inbound;

import java.util.List;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.Message;
import org.springframework.integration.context.IntegrationObjectSupport;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;

import com.relayrides.pushy.apns.ExpiredToken;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.springframework.integration.apns.support.ApnsUtils;

public class ApnsPollingFeedbackChannelAdapter extends IntegrationObjectSupport
		implements MessageSource<List<ExpiredToken>>, InitializingBean,
		DisposableBean {

	private String certificatePath;
	private String keyStorePassword;
	private boolean tlsRequired = true;
	private boolean isSandbox = false;
	private int concurrentConnections = 1;
	private PushManager<SimpleApnsPushNotification> pushManager;

	@Override
	public void onInit() throws Exception {
		pushManager = ApnsUtils.initializePushManager(certificatePath, keyStorePassword,
				concurrentConnections, tlsRequired, isSandbox);
		pushManager.start();
		super.onInit();
	}

	@Override
	public Message<List<ExpiredToken>> receive() {
		List<ExpiredToken> expiredTokens = null;
		try {
			expiredTokens = pushManager.getExpiredTokens();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (expiredTokens == null) {
			return null;
		}
		return MessageBuilder.withPayload(expiredTokens).build();
	}

	@Override
	public void destroy() throws Exception {
		pushManager.shutdown();

	}

	@Override
	public String getComponentType() {
		return "spring-integration-apns:feedback-channel-adapter";
	}

}
