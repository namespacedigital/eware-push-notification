package org.project.startup.service.subscriber.impl;

import org.project.startup.service.mapper.event.UserEvent;
import org.project.startup.service.subscriber.WSMessageSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.UnicastProcessor;

import java.util.Optional;

import static org.project.startup.service.mapper.event.UserEvent.Type.USER_LEFT;

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
            UserEvent.type(USER_LEFT)
                     .withPayload()
                     .user(event.getUser())
                     .build()));
    }
}
