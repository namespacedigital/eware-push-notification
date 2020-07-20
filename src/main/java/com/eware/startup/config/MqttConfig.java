package com.eware.startup.config;

import com.eware.startup.service.mapper.event.MessageEvent;
import com.eware.startup.service.mapper.event.UserEvent;
import com.eware.startup.service.mapper.pojo.MessageEntity;
import com.eware.startup.service.Processor;
import com.eware.startup.common.UserUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.MessageBuilder;

@Configuration
public class MqttConfig {

    private static final String MQTT_INPUT_CHANNEL = "mqttInputChannel";
    private static final String MQTT_SYSTEM_USER = "MQTT_SYSTEM_USER";
    private static final String SENDER = "sender";

    @Value("${mqtt.broker_url}")
    private String brokerUrl;
    @Value("${mqtt.client}")
    private String client;
    @Value("${mqtt.topic}")
    private String topic;

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(brokerUrl, client, topic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = MQTT_INPUT_CHANNEL)
    public MessageHandler handler(Processor chatServiceStreams) {
        return message -> {
            String mqttMessage = message.getPayload().toString();
            MessageBuilder.withPayload(mqttMessage).setHeader(SENDER, MQTT_SYSTEM_USER);

            MessageEvent messageEvent = MessageEvent.type(MessageEvent.Type.PRIVATE_CHAT_MESSAGE)
                                                    .withPayload()
                                                    .message(new MessageEntity(mqttMessage, message.getHeaders()))
                                                    .build();


            UserEvent userEvent = UserUtil.toEvent(messageEvent.getPayload().getMessage().getMessagePayload());
            userEvent.setProperties(SENDER, MQTT_SYSTEM_USER);

            chatServiceStreams.clientToBroker().send(
                MessageBuilder
                    .withPayload(UserUtil.toJSON(userEvent))
                    .setHeader(Processor.USER_HEADER, MQTT_SYSTEM_USER)
                    .build());
        };
    }
}
