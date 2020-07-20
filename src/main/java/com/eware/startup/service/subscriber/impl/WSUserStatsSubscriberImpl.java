package com.eware.startup.service.subscriber.impl;

import com.eware.startup.service.mapper.event.UserEvent;
import com.eware.startup.service.mapper.pojo.User;
import com.eware.startup.service.subscriber.WSUserStatsSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static java.util.Arrays.asList;

@Component
public class WSUserStatsSubscriberImpl implements WSUserStatsSubscriber {

    private Map<String, Stats> userStatsMap = new ConcurrentHashMap<>();

    @Autowired
    public WSUserStatsSubscriberImpl(Flux<UserEvent> events, UnicastProcessor eventPublisher) {

        events
            .filter(type(UserEvent.Type.CHAT_MESSAGE, UserEvent.Type.USER_JOINED))
            .subscribe(this::onChatMessage);

        events
            .filter(type(UserEvent.Type.USER_LEFT))
            .map(UserEvent::getUser)
            .map(User::getUsername)
            .subscribe(userStatsMap::remove);

        events
            .filter(type(UserEvent.Type.USER_JOINED))
            .map(event -> UserEvent.type(UserEvent.Type.USER_STATS)
                                   .withPayload()
                                   .systemUser()
                                   .property("stats", new HashMap<>(userStatsMap))
                                   .build()
            )
            .subscribe(eventPublisher::onNext);
    }

    private static Predicate<UserEvent> type(UserEvent.Type... types) {
        return event -> asList(types).contains(event.getType());
    }

    @Override
    public void onChatMessage(UserEvent event) {
        String username = event.getUser().getUsername();
        Stats stats = userStatsMap.computeIfAbsent(username, s -> new Stats(event.getUser()));
        stats.onChatMessage(event);
    }

    private static class Stats {
        private User user;
        private long lastMessage;
        private AtomicInteger messageCount = new AtomicInteger();

        public Stats(User user) {
            this.user = user;
        }

        public void onChatMessage(UserEvent event) {
            lastMessage = event.getTimestamp();
            if (UserEvent.Type.CHAT_MESSAGE == event.getType()) messageCount.incrementAndGet();
        }

        public User getUser() {
            return user;
        }

        public long getLastMessage() {
            return lastMessage;
        }

        public int getMessageCount() {
            return messageCount.get();
        }
    }
}