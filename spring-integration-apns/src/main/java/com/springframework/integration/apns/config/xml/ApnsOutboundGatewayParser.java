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

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.xml.AbstractConsumerEndpointParser;
import org.springframework.integration.config.xml.IntegrationNamespaceUtils;
import org.w3c.dom.Element;

import com.springframework.integration.apns.outbound.ApnsOutboundGateway;

/**
 * The parser for the Apns Outbound Gateway.
 *
 * @author Anoop Gopalakrishnan
 * @since 1.0
 *
 */
public class ApnsOutboundGatewayParser extends AbstractConsumerEndpointParser {
	
	

	@Override
	protected boolean shouldGenerateId() {
		return false;
	}

	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}


	
	@Override
	protected String getInputChannelAttributeName() {
		return "request-channel";
	}
	
	

	@Override
	protected BeanDefinitionBuilder parseHandler(Element element,
			ParserContext parserContext) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ApnsOutboundGateway.class);
		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "certificate-path");
		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "key-store-password");
		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "is-sandbox");
		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "tls-required");
		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "concurrent-connections");
		IntegrationNamespaceUtils.setReferenceIfAttributeDefined(builder, element, "error-channel");
		IntegrationNamespaceUtils.setReferenceIfAttributeDefined(builder, element, "input-channel");
		IntegrationNamespaceUtils.setReferenceIfAttributeDefined(builder, element, "output-channel");
		IntegrationNamespaceUtils.setReferenceIfAttributeDefined(builder, element, "header-mapper");
		
		return builder;
	}

}
