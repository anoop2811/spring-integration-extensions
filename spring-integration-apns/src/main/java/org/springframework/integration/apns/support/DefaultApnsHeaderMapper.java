package org.springframework.integration.apns.support;

import java.util.Map;

import org.springframework.integration.MessageHeaders;
import org.springframework.integration.apns.core.ApnsHeaders;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.util.Assert;

import com.relayrides.pushy.apns.ExpiredToken;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;

public class DefaultApnsHeaderMapper implements
		HeaderMapper<Object> {
	
	public void fromHeaders(MessageHeaders headers, Object target) {
		Assert.isTrue(target instanceof ApnsPayloadBuilder,
				"the default header mapper requires a target of ApnsPayloadBuilder"); 
		ApnsPayloadBuilder apnsPayloadBuilder = (ApnsPayloadBuilder) target;
		Object badgeCount = headers.get(ApnsHeaders.BADGE_COUNT);
		if(badgeCount == null) {
			badgeCount = "";
		}
		if(badgeCount instanceof Number) {
			apnsPayloadBuilder.setBadgeNumber(((Number)badgeCount).intValue());
		} else if(badgeCount instanceof String) {
			try {
				apnsPayloadBuilder.setBadgeNumber(Integer.parseInt((String) badgeCount));
			} catch (Exception e) {
				//logger.info();
			}
		}
		
		Object soundFileName = headers.get(ApnsHeaders.SOUNDFILE_NAME);
		
		if(soundFileName == null) {
			apnsPayloadBuilder.setSoundFileName("default");
		} else {
			Assert.isTrue(soundFileName instanceof String,
					"the header '" + ApnsHeaders.SOUNDFILE_NAME + 
					"' must contain a String indicating sound file name to be played on notification");
			apnsPayloadBuilder.setSoundFileName((String)soundFileName);
		}
		
		Object launchImageFilename = headers.get(ApnsHeaders.LAUNCH_IMAGE_NAME);
		if(launchImageFilename != null) {
			Assert.isTrue(launchImageFilename instanceof String,
					"the header '" + ApnsHeaders.LAUNCH_IMAGE_NAME + 
					"' must contain a String indicating launch image to be shown on notification");
			apnsPayloadBuilder.setLaunchImage((String)launchImageFilename);
		}
		
		Object localizedActionButtonKey = headers.get(ApnsHeaders.LOCALIZED_ACTION_BUTTON_KEY);
		if(localizedActionButtonKey != null) {
			Assert.isTrue(localizedActionButtonKey instanceof String,
					"the header '" + ApnsHeaders.LOCALIZED_ACTION_BUTTON_KEY + 
					"' must contain a String");
			apnsPayloadBuilder.setLocalizedActionButtonKey((String)localizedActionButtonKey);
		}
		
		Object showActionButton = headers.get(ApnsHeaders.SHOW_ACTION_BUTTON);
		if(showActionButton != null) {
			Assert.isTrue(showActionButton instanceof String,
					"the header '" + ApnsHeaders.SHOW_ACTION_BUTTON + 
					"' must contain a String");
			apnsPayloadBuilder.setShowActionButton(Boolean.parseBoolean((String)showActionButton));
		}
	}

	
	public Map<String, Object> toHeaders(Object source) {
		Assert.isTrue(source instanceof ExpiredToken,
				"the default header mapper requires the source to be of type ExpiredToken"); 

		return null;
	}



	

}
