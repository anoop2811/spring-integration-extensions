package org.springframework.integration.apns.outbound;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.apns.core.ApnsHeaders;
import org.springframework.integration.apns.core.DefaultApnsNotificationListener;
import org.springframework.integration.apns.support.ApnsUtils;
import org.springframework.integration.apns.support.DefaultApnsHeaderMapper;
import org.springframework.integration.context.IntegrationObjectSupport;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.util.Assert;

import com.relayrides.pushy.apns.ApnsEnvironment;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.RejectedNotificationListener;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;


/**
 * A {@link MessageHandler} implementation that sends push notification  by delegating
 * to a {@link PushManager} instance. Apple does not give any response if the message
 * was successful, but would do so if there was an error in the sending. The sending error is
 * trapped by using a default instance of type {@link RejectedNotificationListener}. 
 *
 * @author Anoop Gopalakrishnan
 * @since 1.0
 * @see DefaultApnsNotificationListener
 *
 */

public class ApnsOutboundChannelAdapter extends IntegrationObjectSupport implements MessageHandler{

	private Resource certificatePath;
	private String keyStorePassword;
	private String apnsHost;
	private int apnsPort;
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

	public Resource getCertificatePath() {
		return certificatePath;
	}

	public void setCertificatePath(Resource certificatePath) {
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
	
	public String getApnsHost() {
		return apnsHost;
	}

	public void setApnsHost(String apnsHost) {
		this.apnsHost = apnsHost;
	}

	public int getApnsPort() {
		return apnsPort;
	}

	public void setApnsPort(int apnsPort) {
		this.apnsPort = apnsPort;
	}


	/**
	 * @throws Exception 
	 *
	 */
	@Override
	protected void onInit() throws Exception {
		try {
			ApnsEnvironment apnsEnvironment = new ApnsEnvironment(apnsHost, apnsPort, null, 0);
			pushManager = ApnsUtils.initializePushManager(certificatePath, keyStorePassword, concurrentConnections, apnsEnvironment);
			pushManager.registerRejectedNotificationListener(new DefaultApnsNotificationListener());
			pushManager.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onInit();
		
	}

	
	public void handleMessage(Message<?> message) throws MessagingException {
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
		try {
			pushManager.getQueue().put(
			        new SimpleApnsPushNotification(deviceToken, payload));
		} catch (InterruptedException e) {
			throw new RuntimeException("Unable to send push notification to endpoint [" + token + "]", e);
		}
	}
}
