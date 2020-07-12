package org.project.startup.service.mapper.event;

import org.project.startup.service.mapper.helper.Payload;
import org.project.startup.service.mapper.pojo.User;

import java.util.HashMap;
import java.util.Map;


public class UserEventBuilder {
	private UserEvent.Type type;
	private PayloadBuilder payloadBuilder = new PayloadBuilder();

	public UserEventBuilder type(UserEvent.Type type) {
		this.type = type;
		return this;
	}

	public PayloadBuilder withPayload() {
		return payloadBuilder;
	}

	private UserEvent buildEvent(Payload payload) {
		return new UserEvent(type, payload);
	}

	public class PayloadBuilder {

		private String username;
		private String avatar;
		private Map<String, Object> properties = new HashMap<>();

		public PayloadBuilder userUsername(String username) {
			this.username = username;
			return this;
		}

		public PayloadBuilder userAvatar(String avatar) {
			this.avatar = avatar;
			return this;
		}

		public PayloadBuilder user(User user) {
			this.username = user.getUsername();
			this.avatar = user.getAvatar();
			return this;
		}

		public PayloadBuilder systemUser() {
			user(User.systemUser());
			return this;
		}

		public PayloadBuilder property(String property, Object value) {
			properties.put(property, value);
			return this;
		}

		public UserEvent build() {
			return buildEvent(new Payload(new User(payloadBuilder.username, payloadBuilder.avatar), properties));
		}
	}
}
