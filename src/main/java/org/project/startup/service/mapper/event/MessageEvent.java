package org.project.startup.service.mapper.event;

import org.project.startup.service.mapper.helper.MessagePayload;

public class MessageEvent {
    public enum Type {
        PRIVATE_CHAT_MESSAGE, GLOBAL_NOTIFICATION, PRIVATE_NOTIFICATION,
    }

    private Type type;
    private MessagePayload payload;

    public MessageEvent(Type type, MessagePayload payload) {
        this.type = type;
        this.payload = payload;
    }

    public Type getType() {
        return type;
    }

    public MessagePayload getPayload() {
        return payload;
    }

    public static MessageEventBuilder type(Type type) {
        return new MessageEventBuilder().type(type);
    }
}
