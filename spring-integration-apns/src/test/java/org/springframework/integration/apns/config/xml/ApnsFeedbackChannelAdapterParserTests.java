/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.springframework.integration.apns.config.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.apns.inbound.ApnsFeedbackInboundChannelAdapter;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.integration.test.util.TestUtils;

import com.relayrides.pushy.apns.PushManager;


/**
 * @author Anoop Gopalakrishnan
 * @since 1.0
 *
 */
public class ApnsFeedbackChannelAdapterParserTests {

	private ConfigurableApplicationContext context;

	private ApnsFeedbackInboundChannelAdapter consumer;

	@Test
	public void testInboundChannelAdapterParser() throws Exception {

		setUp("ApnsFeedbackChannelAdapterParserTests.xml", getClass(), "apnsFeedbackAdapter");

		final AbstractMessageChannel outputChannel = TestUtils.getPropertyValue(this.consumer, "outputChannel", AbstractMessageChannel.class);
		assertEquals("out", outputChannel.getComponentName());

		PushManager pushManager = TestUtils.getPropertyValue(consumer, "pushManager", PushManager.class);
		assertNotNull(pushManager);
		
		String certificatePath = TestUtils.getPropertyValue(consumer, "certificatePath", String.class);
		assertNotNull(certificatePath);
		
		String keyStorePassword = TestUtils.getPropertyValue(consumer, "keyStorePassword", String.class);
		assertNotNull(keyStorePassword);

		boolean autoStartup = TestUtils.getPropertyValue(consumer, "autoStartup" , Boolean.class);
		assertTrue(autoStartup);
	}

	@After
	public void tearDown(){
		if(context != null){
			context.close();
		}
	}

	public void setUp(String name, Class<?> cls, String consumerId){
		context    = new ClassPathXmlApplicationContext(name, cls);
		consumer   = this.context.getBean(consumerId, ApnsFeedbackInboundChannelAdapter.class);
	}

}
