package com.eware.pushnotification.service.mapper.event;

import com.eware.pushnotification.service.mapper.pojo.MessageEntity;
import com.eware.pushnotification.service.mapper.helper.MessagePayload;
import org.springframework.messaging.MessageHeaders;

import java.util.HashMap;
import java.util.Map;

public class MessageEventBuilder {
	private MessageEvent.Type type;
	private PayloadBuilder payloadBuilder = new PayloadBuilder();

	public MessageEventBuilder type(MessageEvent.Type type) {
		this.type = type;
		return this;
	}

	public PayloadBuilder withPayload() {
		return payloadBuilder;
	}

	private MessageEvent buildEvent(MessagePayload payload) {
		return new MessageEvent(type, payload);
	}
	
	public class PayloadBuilder {

		private MessageHeaders header;
		private String payload;
		private Map<String, Object> properties = new HashMap<>();

		public PayloadBuilder messageHeader(MessageHeaders header) {
			this.header = header;
			return this;
		}

		public PayloadBuilder messagePayload(String payload) {
			this.payload = payload;
			return this;
		}

		public PayloadBuilder message(MessageEntity message) {
			this.header = message.getHeader();
			this.payload = message.getMessagePayload();
			return this;
		}

		public PayloadBuilder property(String property, Object value) {
			properties.put(property, value);
			return this;
		}

		public MessageEvent build() {
			return buildEvent(new MessagePayload(new MessageEntity(payloadBuilder.payload, payloadBuilder.header), properties));
		}
	}
}
