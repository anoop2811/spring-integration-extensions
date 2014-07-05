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
package org.springframework.integration.apns.support;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.relayrides.pushy.apns.ApnsEnvironment;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.PushManagerFactory;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;

/**
 * Contains utility methods used by the Apns components.
 * 
 * @author Anoop Gopalakrishnan
 * @since 1.0
 * 
 */
public final class ApnsUtils {
	
	/** Prevent instantiation. */
	private ApnsUtils() {
		throw new AssertionError();
	}

	public static PushManager<SimpleApnsPushNotification> initializePushManager(
			Resource certificate, String keyStorePassword,
			int concurrentConnections, ApnsEnvironment apnsEnvironment)
			throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
		PushManager<SimpleApnsPushNotification> pushManager = null;
		try {
			Assert.notNull(certificate.getFile());
			Assert.isTrue(certificate.getFile().exists());
			Assert.hasLength(keyStorePassword);
			String certificatePath = certificate.getFile().getAbsolutePath();
			PushManagerFactory<SimpleApnsPushNotification> pushManagerFactory = new PushManagerFactory<SimpleApnsPushNotification>(
					apnsEnvironment,
					PushManagerFactory.createDefaultSSLContext(
							certificatePath, keyStorePassword));
			pushManager = pushManagerFactory.buildPushManager();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Unable to create a connection to the apple server, please check the certificate and/or key store password");
		} catch (KeyManagementException e) {
			throw new RuntimeException("Unable to create a connection to the apple server, please check the certificate");
		}
		
		return pushManager;

	}
	

	

}
