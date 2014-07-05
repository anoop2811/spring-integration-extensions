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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.apns.MockApnsFeedbackServer;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.test.util.TestUtils;

import com.relayrides.pushy.apns.ExpiredToken;
import com.relayrides.pushy.apns.PushManager;


/**
 * @author Anoop Gopalakrishnan
 * @since 1.0
 *
 */
public class ApnsFeedbackChannelAdapterParserTests {

	private ConfigurableApplicationContext context;

	private SourcePollingChannelAdapter consumer;
	
	private MockApnsFeedbackServer server;

	@Test
	public void testInboundChannelAdapterParser() throws Exception {

		setUp("ApnsFeedbackChannelAdapterParserTests.xml", getClass(), "apnsFeedbackAdapter");

		final AbstractMessageChannel outputChannel = TestUtils.getPropertyValue(this.consumer, "outputChannel", AbstractMessageChannel.class);
		assertEquals("out", outputChannel.getComponentName());

		PushManager pushManager = TestUtils.getPropertyValue(consumer, "pushManager", PushManager.class);
		assertNotNull(pushManager);
		
		String certificatePath = TestUtils.getPropertyValue(consumer, "certificatePath", String.class);
		assertNotNull(certificatePath);
		
		String apnsHost = TestUtils.getPropertyValue(consumer, "apnsHost", String.class);
		assertNotNull(apnsHost);

		String apnsPort = TestUtils.getPropertyValue(consumer, "apnsPort", String.class);
		assertNotNull(apnsPort);

		
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

	public void setUp(String name, Class<?> cls, String consumerId) throws Exception{
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(2014, Calendar.FEBRUARY, 20);
		ExpiredToken expiredToken = getNewExpiredToken("12345", cal.getTime());
		List<ExpiredToken> expiredTokenList = new ArrayList<ExpiredToken>();
		expiredTokenList.add(expiredToken);
		server = new MockApnsFeedbackServer(5153, expiredTokenList);
		server.start();
		context    = new ClassPathXmlApplicationContext(name, cls);
		consumer   = this.context.getBean(consumerId, SourcePollingChannelAdapter.class);
	}
	

	
	public ExpiredToken getNewExpiredToken(String appId, Date date) {
		ExpiredToken expiredToken = null;
		try {
			Calendar cal = GregorianCalendar.getInstance();
			cal.set(2014, Calendar.FEBRUARY, 20);
			
			Constructor<ExpiredToken> constructor;
			constructor = ExpiredToken.class.getDeclaredConstructor(byte[].class, Date.class);
			constructor.setAccessible(true);
			expiredToken = constructor.newInstance(appId.getBytes(), date);
			System.out.println(expiredToken);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return expiredToken;
      
	}

}
