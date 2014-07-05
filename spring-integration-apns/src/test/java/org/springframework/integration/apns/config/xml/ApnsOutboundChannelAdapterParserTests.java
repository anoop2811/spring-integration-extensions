package org.springframework.integration.apns.config.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.integration.apns.MockApnsServer;
import org.springframework.integration.apns.outbound.ApnsOutboundChannelAdapter;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.integration.test.util.TestUtils;

import com.relayrides.pushy.apns.PushManager;

public class ApnsOutboundChannelAdapterParserTests {


	private ConfigurableApplicationContext context;

	private EventDrivenConsumer consumer;
	
	private MockApnsServer server;

	@Test
	public void testOutboundChannelAdapterParser() throws Exception {

		setUp("ApnsOutboundChannelAdapterParserTests.xml", getClass(), "apnsOutboundAdapter");

		final AbstractMessageChannel inputChannel = TestUtils.getPropertyValue(this.consumer, "inputChannel", AbstractMessageChannel.class);
		assertEquals("out", inputChannel.getComponentName());
		
		final ApnsOutboundChannelAdapter handler = TestUtils.getPropertyValue(this.consumer, "handler", ApnsOutboundChannelAdapter.class);
		
		PushManager pushManager = TestUtils.getPropertyValue(handler, "pushManager", PushManager.class);
		assertNotNull(pushManager);
		
		
		Resource certificatePath = TestUtils.getPropertyValue(handler, "certificatePath", Resource.class);
		assertNotNull(certificatePath);
		
		String apnsHost = TestUtils.getPropertyValue(handler, "apnsHost", String.class);
		assertNotNull(apnsHost);

		Integer apnsPort = TestUtils.getPropertyValue(handler, "apnsPort", Integer.class);
		assertNotNull(apnsPort);

		
		String keyStorePassword = TestUtils.getPropertyValue(handler, "keyStorePassword", String.class);
		assertNotNull(keyStorePassword);

		boolean autoStartup = TestUtils.getPropertyValue(consumer, "autoStartup" , Boolean.class);
		assertTrue(autoStartup);
	}

	@After
	public void tearDown(){
		if(context != null){
			context.close();
		}
		server.shutDown();
	}

	public void setUp(String name, Class<?> cls, String consumerId)
			throws Exception {
		server = new MockApnsServer(5153);
		server.start();
		context    = new ClassPathXmlApplicationContext(name, cls);
		consumer   = this.context.getBean(consumerId, EventDrivenConsumer.class);
	}


}
