package com.eware.startup.service.handler;


import com.eware.startup.service.mapper.event.MessageEvent;
import com.eware.startup.service.mapper.event.UserEvent;
import com.eware.startup.domain.entity.ClientEntity;
import com.eware.startup.service.Processor;
import com.eware.startup.service.business.WebSocketSessionService;
import com.eware.startup.service.subscriber.WSMessageSubscriber;
import com.eware.startup.common.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;


@Component
public class OutboundMessageHandler implements WebSocketHandler {

    private final static Logger LOG = LoggerFactory.getLogger(OutboundMessageHandler.class);
    private static final String SENDER = "sender";
    private static final String TARGET_USER = "targetUser";
    private FluxSink<Message<String>> chatMessageSink;
    private Flux<String> outputEvents;
    private WebSocketSessionService webSocketSessionService;
    private WSMessageSubscriber wsMessageSubscriber;

    @Autowired
    public OutboundMessageHandler(Flux<UserEvent> events,
                                  WebSocketSessionService webSocketSessionService,
                                  WSMessageSubscriber wsMessageSubscriber) {
        this.webSocketSessionService = webSocketSessionService;
        this.outputEvents = Flux.from(events).map(UserUtil::toJSON);
        this.wsMessageSubscriber = wsMessageSubscriber;
    }

    /**
     * indicates that this service will be listening forincoming messages on
     * the brokerToClient channel. When it receives one, it will forward it to
     * chatMessageSink.
     *
     * @param message
     * @StreamListener(ChatServiceStreams.BROKER_TO_CLIENT)
     */
    @StreamListener(Processor.BROKER_TO_CLIENT)
    public void listen(Message<String> message) {
        if (chatMessageSink != null) {
            chatMessageSink.next(message);
        }
    }

    /**
     * handle the sending of messages to websocket client
     */
    @Override
    public Mono<Void> handle(WebSocketSession session) {

        ClientEntity clientEntity = webSocketSessionService.map(session);
        //user
        String user = clientEntity.getUsername();

        //create flux to listen to broker
        Flux<Message<String>> flux = Flux.<Message<String>>create(
            emitter -> this.chatMessageSink = emitter,
            FluxSink.OverflowStrategy.IGNORE)
            .publish()
            .autoConnect();

        flux.log(user + "from-broker-to-message-event")
            .map(UserUtil::toMessageEvent)
            .log(user + "from-message-event-to-user-event")
            .map(this::transformToUserEvent)
            .log(user + "outputEvents-user-disconnect")
            .map(userEvent -> validateUserDisconnected(userEvent, clientEntity))
            .log(user + "subscribe-user-event-to-flux-incoming-message-from-broker")
            .subscribe(wsMessageSubscriber::onNext, wsMessageSubscriber::onError, wsMessageSubscriber::onComplete);

        return session.send(outputEvents
                                .log(user + "-o-outputEvents-from-json-to-user-event")
                                .map(UserUtil::toEvent)
                                .log(user + "-o-outputEvents-filter-private-message")
                                .filter(userEvent -> validatePrivateMessage(userEvent, clientEntity))
                                .log(user + "-o-outputEvents-from-user-event-to-json")
                                .map(UserUtil::toJSON)
                                .map(session::textMessage))
                      .log(user + "-o-wrap-as-websocket-message")
                      .log(user + "-o-publish-to-websocket");

    }


    private boolean validatePrivateMessage(UserEvent message, ClientEntity clientEntity) {
        if (message.getType() == UserEvent.Type.PRIVATE_CHAT_MESSAGE) {
            String targetUser = (String) message.getProperties().get(TARGET_USER);
            LOG.info("Private message from sender  {}", message.getProperties().get(SENDER));
            //if user connected is the same from incoming message targetUser or
            return clientEntity.getUsername().equals(targetUser) || clientEntity.getUsername().equals(message.getProperties().get(SENDER));
        } else {
            return true;
        }
    }


    private UserEvent validateUserDisconnected(UserEvent userEvent, ClientEntity clientEntity) {
        if (userEvent.getType() == UserEvent.Type.USER_LEFT) {
            webSocketSessionService.removeClient(clientEntity);
        }
        return userEvent;
    }

    /**
     * Transform from MessageEvent to UserEvent
     * Get the sender from MessageEvent and put in UserEvent for further filtering
     *
     * @param messageEvent
     * @return
     */
    private UserEvent transformToUserEvent(MessageEvent messageEvent) {
        String sender = messageEvent.getPayload().getMessage().getHeader().get(Processor.USER_HEADER, String.class);
        UserEvent userEvent = UserUtil.toEvent(messageEvent.getPayload().getMessage().getMessagePayload());
        userEvent.setProperties(SENDER, sender);
        return userEvent;
    }
}
