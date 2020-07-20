package com.eware.startup.service.mapper.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.messaging.MessageHeaders;

public class MessageEntity {
	
	private String payload;
	private MessageHeaders header;

	@JsonCreator
    public MessageEntity(String payload, MessageHeaders header) {
        this.payload = payload;
        this.header = header;
    }

    public String getMessagePayload() {
        return payload;
    }

    public MessageHeaders getHeader() {
        return header;
    }
}
