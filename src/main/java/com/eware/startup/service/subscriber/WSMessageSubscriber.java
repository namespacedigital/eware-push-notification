package com.eware.startup.service.subscriber;

import com.eware.startup.service.mapper.event.UserEvent;

public interface WSMessageSubscriber {
    void onNext(UserEvent event);
    void onError(Throwable error);
    void onComplete();
}
