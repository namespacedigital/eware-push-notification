package org.project.startup.service.subscriber;

import org.project.startup.service.mapper.event.UserEvent;

public interface WSMessageSubscriber {
    void onNext(UserEvent event);
    void onError(Throwable error);
    void onComplete();
}
