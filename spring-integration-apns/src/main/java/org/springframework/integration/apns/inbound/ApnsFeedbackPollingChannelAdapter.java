package org.springframework.integration.apns.inbound;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.integration.Message;
import org.springframework.integration.apns.support.ApnsUtils;
import org.springframework.integration.context.IntegrationObjectSupport;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.endpoint.AbstractPollingEndpoint;
import org.springframework.integration.support.MessageBuilder;

import com.relayrides.pushy.apns.ApnsEnvironment;
import com.relayrides.pushy.apns.ExpiredToken;
import com.relayrides.pushy.apns.FeedbackConnectionException;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
/**
 * A polling channel adapter for getting the feedback service from Apple.
 * This service is usually a scheduled service where the apple service would 
 * return the currently invalid application ids so that the callers could update
 * their own systems to avoid having to push messages to these invalid applications
 * 
 * 
 * @author Anoop Gopalakrishnan
 * @since 1.0.0
 *
 */
public class ApnsFeedbackPollingChannelAdapter extends AbstractPollingEndpoint {

	private Resource certificatePath;
	private String keyStorePassword;
	private String apnsHost;
	private int apnsPort;
	private boolean tlsRequired = true;
	private int concurrentConnections = 1;
	private PushManager<SimpleApnsPushNotification> pushManager;
	private Logger logger = Logger.getLogger(ApnsFeedbackPollingChannelAdapter.class);

	@Override
	public void onInit() {
		super.onInit();
		try {
			ApnsEnvironment apnsEnvironment = new ApnsEnvironment(apnsHost, apnsPort, apnsHost, apnsPort);

			pushManager = ApnsUtils.initializePushManager(certificatePath, keyStorePassword,
					concurrentConnections, apnsEnvironment);
			//pushManager.start();
		} catch (KeyStoreException e) {
			throw new IllegalStateException("Unable to load key store", e);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Caught Exception while generating a key store", e);
		} catch (CertificateException e) {
			throw new IllegalStateException("Caught Exception while generating a key store", e);
		} catch (IOException e) {
			throw new IllegalStateException("Caught Exception while configuring a client connection to Apple servers", e);
		}
		
		
	}

	public Message<List<ExpiredToken>> receiveMessage() {
		List<ExpiredToken> expiredTokens = null;
		try {
			expiredTokens = pushManager.getExpiredTokens();
		} catch (InterruptedException e) {
			throw new IllegalStateException("Caught exception while retrieving expired tokens", e);
		} catch (FeedbackConnectionException e) {
			throw new IllegalStateException("Caught exception while retrieving expired tokens", e);
		}
		if (expiredTokens == null) {
			expiredTokens =  new ArrayList<ExpiredToken>();
			logger.debug("No expired tokens received from the apple feedback service");
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



	@Override
	protected void handleMessage(Message<?> message) {
		
		
	}

}
