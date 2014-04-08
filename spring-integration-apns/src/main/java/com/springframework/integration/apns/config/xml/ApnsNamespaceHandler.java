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
package com.springframework.integration.apns.config.xml;

import org.springframework.integration.config.xml.AbstractIntegrationNamespaceHandler;

/**
 * The namespace handler for the Spring integration APNS namespace
 *
 * @author Anoop Gopalakrishnan
 * @since 1.0
 *
 */
public class ApnsNamespaceHandler extends AbstractIntegrationNamespaceHandler {

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
	 */
	public void init() {
		this.registerBeanDefinitionParser("feedback-channel-adapter",  new ApnsFeedbackChannelAdapterParser());
		this.registerBeanDefinitionParser("outbound-gateway", new ApnsOutboundGatewayParser());
		this.registerBeanDefinitionParser("outbound-channel-adapter", new ApnsOutboundChannelAdapterParser());
	}
}
