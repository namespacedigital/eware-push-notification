package com.eware.pushnotification.service.business;

import com.eware.pushnotification.domain.entity.ClientEntity;
import org.springframework.web.reactive.socket.WebSocketSession;

public interface WebSocketSessionService {
   ClientEntity map(WebSocketSession session);

   void removeClient(ClientEntity clientEntity);
}
