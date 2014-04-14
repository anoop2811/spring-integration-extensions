package org.springframework.integration.apns.inbound;

import java.util.List;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.Message;
import org.springframework.integration.apns.support.ApnsUtils;
import org.springframework.integration.context.IntegrationObjectSupport;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;

import com.relayrides.pushy.apns.ExpiredToken;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;

public class ApnsPollingFeedbackChannelAdapter extends IntegrationObjectSupport
		implements MessageSource<List<ExpiredToken>>, InitializingBean,
		DisposableBean {

	private String certificatePath;
	public String getCertificatePath() {
		return certificatePath;
	}


	public void setCertificatePath(String certificatePath) {
		this.certificatePath = certificatePath;
	}


	public String getKeyStorePassword() {
		return keyStorePassword;
	}


	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}


	public boolean isTlsRequired() {
		return tlsRequired;
	}


	public void setTlsRequired(boolean tlsRequired) {
		this.tlsRequired = tlsRequired;
	}


	public boolean isSandbox() {
		return isSandbox;
	}


	public void setIsSandbox(boolean isSandbox) {
		this.isSandbox = isSandbox;
	}


	public int getConcurrentConnections() {
		return concurrentConnections;
	}


	public void setConcurrentConnections(int concurrentConnections) {
		this.concurrentConnections = concurrentConnections;
	}


	public PushManager<SimpleApnsPushNotification> getPushManager() {
		return pushManager;
	}


	public void setPushManager(PushManager<SimpleApnsPushNotification> pushManager) {
		this.pushManager = pushManager;
	}

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


	public Message<List<ExpiredToken>> receive() {
		List<ExpiredToken> expiredTokens = null;
		try {
			expiredTokens = pushManager.getExpiredTokens();
		} catch (InterruptedException e) {
			throw new IllegalStateException("Caught exception while retrieving expired tokens", e);
		}
		if (expiredTokens == null) {
			return null;
		}
		return MessageBuilder.withPayload(expiredTokens).build();
	}


	public void destroy() throws Exception {
		pushManager.shutdown();

	}

	@Override
	public String getComponentType() {
		return "spring-integration-apns:feedback-channel-adapter";
	}

}
