Spring Integration Extension for Apple push notification
=================================================

# Introduction

## Apple push notification service
With the maturing of the mobile platform, the need to connect with the consumers is ever increasing. This could be a notification event for say a server outage or maybe even a first class application feature like sending offers etc. Apple has the iphone and ipad as part of their mobility platforms and provide for the creator of a mobile app to interact with their users using the Apple push notification service or APNS in short. However there are steps that the application creator would have to perform to be able to communicate with a device. More details about the apple push notification can be gathered by visiting the apple developers page

*Spring Integration Extension for APNS* provides Spring Integration adapters for the various services provided by the Apple push notification service and uses Pushy framework internally to do the same. For more information regarding Pushy, please visit their github page. 

## Spring Integration's extensions to APNS

Currently this extension supports the following adapters and their current status is as mentioned below:

* Outbound Channel Adapter (Status: Testing underway)
* Outbound Gateway (Status: Testing underway)
* Feedback inbound Channel Adapter (Status: In progress)

#Adapters

## Outbound Channel Adapter
### Example Usage:
```xml

	<int-apns:outbound-channel-adapter
		certificate-path="src/test/resources/test-cert.p12"
		key-store-password="test123" is-sandbox="false" channel="test-input" />
```
The attributes are as described below:
	certificate-path	- The path to the certificate file that is received suring registration through the apple developer portal
	key-store-password	- The certificate password
	is-sandbox			- Indicates if this push notification is to go through apple's sandbox environment

*Spring Integration* messages sent to the outbound adapter should have the following header

	apns_deviceToken		      -  A device token is an opaque identifier of a device that APNs gives to the device when it first connects with it

The message could have the following optional headers:

	apns_badgeCount			      - This is the badge count or the number that would show up against your application icon on the iphone or ipad.

	apns_soundFileName		      - This is the name of the sound file packaged with the app that would be played when a notification is received on the phone/tablet

	apns_launchImageName	      - The name of the image file in the application bundle that would be used as the launch image when the user taps the action button or moves the action slider.

	apns_showActionButton		  - A boolean flag to determine if an action button should be shown on a delivered alert.

	apns_localizedActionButtonKey - The key of a string in the receiving app's localized string list to be used as the label of the action button

The *payload* of the spring integration message has to be a String and the total size of the payload and headers mentioned above that gets pushed as to Apple should not exceed 256 bytes	
	
## Outbound Channel Adapter
Work in progress

## Feedback Inbound Adapter
Work in progress

# Further Resources
* [Apple Push Notification Service] : https://developer.apple.com/library/ios/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/Chapters/ApplePushService.html
* [Pushy] : https://github.com/relayrides/pushy/tree/pushy-0.2

## Getting support

Check out the [Spring Integration forums][] and the [spring-integration][spring-integration tag] tag
on [Stack Overflow][]. [Commercial support][] is available, too.

## Related GitHub projects

* [Spring Integration][]
* [Spring Integration Samples][]
* [Spring Integration Templates][]
* [Spring Integration Dsl Groovy][]
* [Spring Integration Dsl Scala][]
* [Spring Integration Pattern Catalog][]

For more information, please also don't forget to visit the [Spring Integration][] website.

[Spring Integration]: https://github.com/SpringSource/spring-integration
[Commercial support]: http://springsource.com/support/springsupport
[Spring Integration forums]: http://forum.springsource.org/forumdisplay.php?42-Integration
[spring-integration tag]: http://stackoverflow.com/questions/tagged/spring-integration
[Spring Integration Samples]: https://github.com/SpringSource/spring-integration-samples
[Spring Integration Templates]: https://github.com/SpringSource/spring-integration-templates/tree/master/si-sts-templates
[Spring Integration Dsl Groovy]: https://github.com/SpringSource/spring-integration-dsl-groovy
[Spring Integration Dsl Scala]: https://github.com/SpringSource/spring-integration-dsl-scala
[Spring Integration Pattern Catalog]: https://github.com/SpringSource/spring-integration-pattern-catalog
[Stack Overflow]: http://stackoverflow.com/faq
