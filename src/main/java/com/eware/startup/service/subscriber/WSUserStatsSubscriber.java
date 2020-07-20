package com.eware.startup.service.subscriber;

import com.eware.startup.service.mapper.event.UserEvent;

public interface WSUserStatsSubscriber {
    void onChatMessage(UserEvent event);
}
