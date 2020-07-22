package com.eware.pushnotification.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Processor {

	String NEW_COMMENTS = "newComments";
	String CLIENT_TO_BROKER = "clientToBroker";
	String BROKER_TO_CLIENT = "brokerToClient";

	String USER_HEADER = "User";
	String UUID_HEADER = "UUID";

	@Input(NEW_COMMENTS)
	SubscribableChannel newComments();

	@Output(CLIENT_TO_BROKER)
	MessageChannel clientToBroker();

	@Input(BROKER_TO_CLIENT)
	SubscribableChannel brokerToClient();
}
