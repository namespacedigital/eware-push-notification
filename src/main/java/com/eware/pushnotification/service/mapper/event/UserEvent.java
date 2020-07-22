package com.eware.pushnotification.service.mapper.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.eware.pushnotification.service.mapper.pojo.User;
import com.eware.pushnotification.service.mapper.helper.Payload;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class UserEvent {
    public enum Type {
        SYSTEM_NOTIFICATION, CHAT_MESSAGE, USER_JOINED, USER_STATS, USER_LEFT, PRIVATE_CHAT_MESSAGE
    }

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    private Type type;
    private final int id;
    private Payload payload;
    private final long timestamp;
    private String message;

    @JsonCreator
    public UserEvent(@JsonProperty("type") Type type, @JsonProperty("payload") Payload payload) {
        this.type = type;
        this.payload = payload;
        this.id = ID_GENERATOR.addAndGet(1);
        this.timestamp = System.currentTimeMillis();
    }


    public Type getType() {
        return type;
    }

    public Payload getPayload() {
        return payload;
    }

    @JsonIgnore
    public User getUser() {
        return getPayload().getUser();
    }

    public Map<String, Object> getProperties() {
        return getPayload().getProperties();
    }

    public void setProperties(String name, Object value) {
        getPayload().setProperties(name, value);
    }


    public int getId() {
        return id;
    }


    public long getTimestamp() {
        return timestamp;
    }

    /**
     * static
     *
     * @param type
     * @return
     */
    public static UserEventBuilder type(Type type) {
        return new UserEventBuilder().type(type);
    }
}
