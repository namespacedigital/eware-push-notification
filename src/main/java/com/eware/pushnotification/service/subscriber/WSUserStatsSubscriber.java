package com.eware.pushnotification.service.subscriber;

import com.eware.pushnotification.service.mapper.event.UserEvent;

public interface WSUserStatsSubscriber {
    void onChatMessage(UserEvent event);
}
