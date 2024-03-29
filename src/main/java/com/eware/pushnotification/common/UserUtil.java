package com.eware.pushnotification.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.eware.pushnotification.service.mapper.event.MessageEvent;
import com.eware.pushnotification.service.mapper.event.UserEvent;
import com.eware.pushnotification.service.mapper.pojo.MessageEntity;
import org.springframework.messaging.Message;

import java.io.IOException;

public class UserUtil {

    public static UserEvent toEvent(String json) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, UserEvent.class);
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON:" + json, e);
        }
    }

    public static String toJSON(UserEvent event) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static MessageEvent toMessageEvent(Message<String> message) {
        return MessageEvent.type(MessageEvent.Type.PRIVATE_CHAT_MESSAGE)
                           .withPayload()
                           .message(new MessageEntity(message.getPayload(), message.getHeaders()))
                           .build();
    }
}
