package com.eware.startup.service.business;

import com.eware.startup.domain.entity.ClientEntity;
import org.springframework.web.reactive.socket.WebSocketSession;

public interface WebSocketSessionService {
   ClientEntity map(WebSocketSession session);

   void removeClient(ClientEntity clientEntity);
}
