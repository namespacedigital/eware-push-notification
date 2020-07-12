package org.project.startup.service.business;

import org.project.startup.domain.entity.ClientEntity;
import org.springframework.web.reactive.socket.WebSocketSession;

public interface WebSocketSessionService {
   ClientEntity map(WebSocketSession session);

   void removeClient(ClientEntity clientEntity);
}
