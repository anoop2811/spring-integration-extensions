/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.springframework.integration.apns.outbound;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.Message;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.integration.support.MessageBuilder;

import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.RejectedNotificationListener;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;
import com.springframework.integration.apns.core.ApnsHeaders;
import com.springframework.integration.apns.core.DefaultApnsNotificationListener;
import com.springframework.integration.apns.support.ApnsUtils;
import com.springframework.integration.apns.support.DefaultApnsHeaderMapper;


/**
 * A {@link MessageHandler} implementation that sends push notification  by delegating
 * to a {@link PushManager} instance. If the 'producesReply' flag is set to true (the default)
 * then a reply Message will be generated with a payload as 'ENQUEUED'.This is because the
 * Pushy framework uses NIO asynch communication and hence once the push notification is enqueued
 * into the sending is managed in another thread. Apple does not give any response if the message
 * was successful, but would do so if there was an error in the sending. The sending error can be
 * trapped by using an instance of type {@link RejectedNotificationListener}. A default
 * implementation that throws an exception with the 
 * payload will contain the response status as an instance of the {@link HttpStatus} enum.
 * When there is a response body, the {@link HttpStatus} enum instance will instead be
 * copied to the MessageHeaders of the reply. In both cases, the response headers will
 * be mapped to the reply Message's headers by this handler's {@link HeaderMapper} instance.
 *
 * @author Anoop Gopalakrishnan
 * @since 1.0
 *
 */
public class ApnsOutboundGateway  extends AbstractReplyProducingMessageHandler implements InitializingBean {

	private String certificatePath;
	private String keyStorePassword;
	private boolean tlsRequired = true;
	private boolean isSandbox = false;
	private int concurrentConnections = 1;
	private PushManager<SimpleApnsPushNotification> pushManager;
	private boolean producesReply = true;	//false for outbound-channel-adapter, true for outbound-gateway
	private HeaderMapper<Object> headerMapper = new DefaultApnsHeaderMapper();


	/**
	 *
	 */
	@Override
	protected void doInit() {
		try {
			try {
				pushManager = ApnsUtils.initializePushManager(certificatePath, keyStorePassword, concurrentConnections, tlsRequired, isSandbox);
				pushManager.registerRejectedNotificationListener(new DefaultApnsNotificationListener());
			} catch (KeyStoreException e) {
				throw new IllegalStateException("Unable to load key store", e);
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalStateException("Caught Exception while generating a key store", e);
			} catch (CertificateException e) {
				throw new IllegalStateException("Caught Exception while generating a key store", e);
			}
			pushManager.start();
		} catch (IOException e) {
			throw new IllegalStateException("Caught Exception while trying to start connection to the apns server", e);
		}
		super.doInit();
		
	}

	@Override
	protected Object handleRequestMessage(Message<?> requestMessage) {
		Object token = requestMessage.getHeaders().get(ApnsHeaders.DEVICE_TOKEN);
		if(token == null) {
			throw new IllegalArgumentException("The device token header is mandatory");
		}
		final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
		headerMapper.fromHeaders(requestMessage.getHeaders(), payloadBuilder);
		payloadBuilder.setAlertBody(requestMessage.getPayload().toString());
		final String payload = payloadBuilder.buildWithDefaultMaximumLength();
		byte[] deviceToken = TokenUtil.tokenStringToByteArray((String) token);
		pushManager.enqueuePushNotification(
		        new SimpleApnsPushNotification(deviceToken, payload));
		final String result = "ENQUEUED";

		if (result == null || !producesReply) {
			return null;
		}

		return MessageBuilder.withPayload(result).copyHeaders(requestMessage.getHeaders()).build();

	}

	/**
	 * If set to 'false', this component will act as an Outbound Channel Adapter.
	 * If not explicitly set this property will default to 'true'.
	 *
	 * @param producesReply Defaults to 'true'.
	 *
	 */
	public void setProducesReply(boolean producesReply) {
		this.producesReply = producesReply;
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

	public HeaderMapper<Object> getHeaderMapper() {
		return headerMapper;
	}

	public void setHeaderMapper(HeaderMapper<Object> headerMapper) {
		this.headerMapper = headerMapper;
	}

	public boolean isProducesReply() {
		return producesReply;
	}
	
	

}
