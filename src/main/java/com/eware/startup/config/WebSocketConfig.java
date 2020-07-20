package com.eware.startup.config;

import com.eware.startup.service.handler.InboundMessageHandler;
import com.eware.startup.service.handler.OutboundMessageHandler;
import com.eware.startup.service.mapper.event.UserEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfig {

    @Value("${websocket.inbound_url}")
    private String inboundUrl;

    @Value("${websocket.outbound_url}")
    private String outboundUrl;

    @Value("${websocket.replay_messages}")
    private int replayMessages;

    @Bean
    public UnicastProcessor<UserEvent> eventPublisher() {
        return UnicastProcessor.create();
    }

    @Bean
    public Flux<UserEvent> events(UnicastProcessor<UserEvent> eventPublisher) {
        //replay last 25 messages
        return eventPublisher.replay(replayMessages).autoConnect();
    }

    @Bean
    HandlerMapping webSocketMapping(InboundMessageHandler inboundMessageHandler, OutboundMessageHandler ouboundMessageHandler) {
        Map<String, WebSocketHandler> urlMap = new HashMap<>();
        urlMap.put(inboundUrl, inboundMessageHandler);
        urlMap.put(outboundUrl, ouboundMessageHandler);
        Map<String, CorsConfiguration> corsConfigurationMap = new HashMap<>();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfigurationMap.put(inboundUrl, corsConfiguration);
        corsConfigurationMap.put(outboundUrl, corsConfiguration);

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(10);
        mapping.setUrlMap(urlMap);
        mapping.setCorsConfigurations(corsConfigurationMap);

        return mapping;
    }

    @Bean
    WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
