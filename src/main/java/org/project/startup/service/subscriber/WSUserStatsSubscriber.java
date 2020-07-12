package org.project.startup.service.subscriber;

import org.project.startup.service.mapper.event.UserEvent;

public interface WSUserStatsSubscriber {
    void onChatMessage(UserEvent event);
}
