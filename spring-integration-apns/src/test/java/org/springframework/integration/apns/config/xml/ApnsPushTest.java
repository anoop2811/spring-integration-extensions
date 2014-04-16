/*
 * MONITISE CONFIDENTIAL
 * ____________________
 *
 * Copyright 2003 - 2012 Monitise Group Limited
 * All Rights Reserved. www.monitisegroup.com
 *
 * NOTICE: All information contained herein is, and remains
 * the property of Monitise Group Limited or its group
 * companies. The intellectual and technical concepts contained
 * herein are proprietary to Monitise Group Limited and Monitise
 * group companies and may be covered by U.S. and
 * Foreign Patents, patents in process, and are protected by
 * trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior
 * written permission is obtained from Monitise Group Limited. Any
 * reproduction of this material must contain this notice
 *
 */

package org.springframework.integration.apns.config.xml;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.apns.core.ApnsHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * 
 * @author Anoop Gopalakrishnan
 * @since 1.0
 * 
 * */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/META-INF/spring/test-context.xml" })
@DirtiesContext
public class ApnsPushTest {
	@Resource(name="test-input")
	MessageChannel channel;
	
	@Test
	public void testApplePush() throws InterruptedException {
//		Message message = createMessage();
//		channel.send(message);
		Thread.sleep(2500000);
	}

	public Message createMessage() {
		return MessageBuilder.withPayload("")
				.setHeader(ApnsHeaders.BADGE_COUNT, 0)
				.setHeader(ApnsHeaders.DEVICE_TOKEN, "3f8ca503c2c888951d90429086f3add528d7fa1ee30f90efb4a7ab4a625d4542")
				//.setHeader(ApnsHeaders.SHOW_ACTION_BUTTON, "false")
				.build();
	}
	
	

}
