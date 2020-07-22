package com.eware.pushnotification.service.subscriber.impl;

import com.eware.pushnotification.service.mapper.event.UserEvent;
import com.eware.pushnotification.service.subscriber.WSMessageSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.UnicastProcessor;

import java.util.Optional;

@Component
public class WSMessageSubscriberImpl implements WSMessageSubscriber {
    private UnicastProcessor<UserEvent> eventPublisher;
    private Optional<UserEvent> lastReceivedEvent = Optional.empty();

    @Autowired
    public WSMessageSubscriberImpl(UnicastProcessor<UserEvent> eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void onNext(UserEvent event) {
        lastReceivedEvent = Optional.of(event);
        eventPublisher.onNext(event);
    }

    @Override
    public void onError(Throwable error) {
        //TODO log error
        error.printStackTrace();
    }

    @Override
    public void onComplete() {
        lastReceivedEvent.ifPresent(event -> eventPublisher.onNext(
            UserEvent.type(UserEvent.Type.USER_LEFT)
                     .withPayload()
                     .user(event.getUser())
                     .build()));
    }
}
