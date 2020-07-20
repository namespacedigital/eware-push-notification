package com.eware.startup.service.mapper.helper;

import com.eware.startup.service.mapper.pojo.MessageEntity;

import java.util.HashMap;
import java.util.Map;

public class MessagePayload {
	   private MessageEntity message;

	    private Map<String, Object> properties = new HashMap<>();

	    public MessagePayload(MessageEntity message, Map<String, Object> properties){
	        this(message);
	        this.properties = properties;
	    }

	    private MessagePayload(MessageEntity message) {
	        this.message = message;
	    }

	    public MessageEntity getMessage() {
	        return message;
	    }

	    private void setProperties(String name, Object value){
	        properties.put(name, value);
	    }

	    private Map<String, Object> getProperties(){
	        return properties;
	}
}
