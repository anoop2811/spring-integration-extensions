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

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.xml.AbstractChannelAdapterParser;
import org.springframework.integration.config.xml.AbstractPollingInboundChannelAdapterParser;
import org.springframework.integration.config.xml.IntegrationNamespaceUtils;
import org.w3c.dom.Element;

import com.springframework.integration.apns.inbound.ApnsPollingFeedbackChannelAdapter;



/**
 * The Spring Integration APNS Inbound Channel adapter parser
 *
 * @author Anoop Gopalakrishnan
 * @since 1.0
 *
 */
public class ApnsFeedbackChannelAdapterParser extends AbstractPollingInboundChannelAdapterParser{


//	protected AbstractBeanDefinition doParse(Element element, ParserContext parserContext, String channelName) {
//		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ApnsPollingFeedbackChannelAdapter.class);
//		builder.addPropertyReference("outputChannel", channelName);
//		
//		//TODO: Allow to read multiple certificates from a directory and corresponding passwords
//		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "certificate-path");
//		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "certificate-path");
//		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "key-store-password");
//		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "is-sandbox");
//		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "concurrent-connections");
//		return builder.getBeanDefinition();
//	}

	@Override
	protected BeanMetadataElement parseSource(Element element,
			ParserContext parserContext) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ApnsPollingFeedbackChannelAdapter.class);
		//TODO: Allow to read multiple certificates from a directory and corresponding passwords
		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "certificate-path");
		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "certificate-path");
		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "key-store-password");
		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "is-sandbox");
		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "concurrent-connections");
		return builder.getBeanDefinition();
	}
}
