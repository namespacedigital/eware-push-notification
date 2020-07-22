package com.eware.pushnotification.service.subscriber;

import com.eware.pushnotification.service.mapper.event.UserEvent;

public interface WSMessageSubscriber {
    void onNext(UserEvent event);
    void onError(Throwable error);
    void onComplete();
}
