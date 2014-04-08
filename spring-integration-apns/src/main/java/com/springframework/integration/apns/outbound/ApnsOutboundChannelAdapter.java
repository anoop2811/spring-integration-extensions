package com.springframework.integration.apns.outbound;

import java.io.IOException;

import org.springframework.integration.Message;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.util.Assert;

import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;
import com.springframework.integration.apns.core.ApnsHeaders;
import com.springframework.integration.apns.core.DefaultApnsNotificationListener;
import com.springframework.integration.apns.support.ApnsUtils;
import com.springframework.integration.apns.support.DefaultApnsHeaderMapper;

public class ApnsOutboundChannelAdapter extends AbstractMessageHandler{

	private String certificatePath;
	private String keyStorePassword;
	private boolean tlsRequired = true;
	private boolean isSandbox = false;
	private int concurrentConnections = 1;
	private PushManager<SimpleApnsPushNotification> pushManager;
	private HeaderMapper<Object> headerMapper = new DefaultApnsHeaderMapper();

	
	
	public boolean isSandbox() {
		return isSandbox;
	}

	public void setIsSandbox(boolean isSandbox) {
		this.isSandbox = isSandbox;
	}

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

	public HeaderMapper<Object> getHeaderMapper() {
		return headerMapper;
	}

	public void setHeaderMapper(HeaderMapper<Object> headerMapper) {
		this.headerMapper = headerMapper;
	}

	/**
	 * @throws Exception 
	 *
	 */
	@Override
	protected void onInit() throws Exception {
		try {
			pushManager = ApnsUtils.initializePushManager(certificatePath, keyStorePassword, concurrentConnections, tlsRequired, isSandbox);
			pushManager.registerRejectedNotificationListener(new DefaultApnsNotificationListener());
			pushManager.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onInit();
		
	}

	@Override
	protected void handleMessageInternal(Message<?> message) throws Exception {
		Assert.isTrue(message.getPayload() instanceof String, "Only payload of type String is supported. " +
				"Consider adding a transformer to the message flow in front of this adapter.");
		Object token = message.getHeaders().get(ApnsHeaders.DEVICE_TOKEN);
		Assert.isTrue(token instanceof String,
				"the header '" + ApnsHeaders.DEVICE_TOKEN + 
				"' must contain a String indicating the device address where the message needs to be delivered");
		if(token == null) {
			throw new IllegalArgumentException("The device token header is mandatory");
		}
		final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
		headerMapper.fromHeaders(message.getHeaders(), payloadBuilder);
		payloadBuilder.setAlertBody(message.getPayload().toString());
		final String payload = payloadBuilder.buildWithDefaultMaximumLength();
		byte[] deviceToken = TokenUtil.tokenStringToByteArray(String.valueOf(token));
		pushManager.enqueuePushNotification(
		        new SimpleApnsPushNotification(deviceToken, payload));
	}
	
	

}
