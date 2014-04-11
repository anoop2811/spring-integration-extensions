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
package com.springframework.integration.apns.support;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

import com.relayrides.pushy.apns.ApnsEnvironment;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;

/**
 * Contains utility methods used by the Apns components.
 * 
 * @author Anoop Gopalakrishnan
 * @since 1.0
 * 
 */
public final class ApnsUtils {
	
	public static final String PKCS12_KEY_STORE = "PKCS12";

	/** Prevent instantiation. */
	private ApnsUtils() {
		throw new AssertionError();
	}

	public static PushManager<SimpleApnsPushNotification> initializePushManager(
			String certificatePath, String keyStorePassword,
			int concurrentConnections, boolean tlsRequired, boolean isSandbox)
			throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
		PushManager<SimpleApnsPushNotification> pushManager = null;
		FileInputStream keystoreInputStream = null;
		try {
			if (tlsRequired) {
				Assert.hasLength(certificatePath);
				Assert.hasLength(keyStorePassword);
			}
			keystoreInputStream = new FileInputStream(certificatePath);
			ApnsEnvironment apnsEnvironment = ApnsEnvironment
					.getProductionEnvironment();
			if (isSandbox) {
				apnsEnvironment = ApnsEnvironment.getSandboxEnvironment();
			}
			KeyStore keyStore = KeyStore.getInstance(PKCS12_KEY_STORE);
			keyStore.load(keystoreInputStream, keyStorePassword.toCharArray());
			pushManager = new PushManager<SimpleApnsPushNotification>(
					apnsEnvironment, keyStore, keyStorePassword.toCharArray(),
					concurrentConnections);
		} finally {
			IOUtils.closeQuietly(keystoreInputStream);
			return pushManager;
		}

	}

}
