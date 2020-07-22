package com.eware.pushnotification.service.business.impl;

import com.eware.pushnotification.service.business.WebSocketSessionService;
import com.eware.pushnotification.domain.entity.ClientEntity;
import com.eware.pushnotification.domain.service.ClientDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.stream.Stream;

@Service
@Transactional
public class WebSocketSessionServiceImpl implements WebSocketSessionService {

    private final static Logger LOG = LoggerFactory.getLogger(WebSocketSessionServiceImpl.class);
    private static final String USER = "user";
    private ClientDomainService clientDomainService;

    @Autowired
    public WebSocketSessionServiceImpl(ClientDomainService clientDomainService) {
        this.clientDomainService = clientDomainService;
    }

    @Override
    public ClientEntity map(WebSocketSession session) {
        String username = getParameter(session, USER);

        ClientEntity entity = ClientEntity.builder()
                                   .username(username)
                                   .webSocketSessionId(session.getId()).build();

        ClientEntity savedClient = clientDomainService.saveClient(entity);

        LOG.info("Saved client  {}", savedClient);
        return savedClient;
    }

    private String getParameter(WebSocketSession session, String user) {
        return Stream.of(session.getHandshakeInfo().getUri()
                                .getQuery().split("&"))
                     .map(s -> s.split("="))
                     .filter(strings -> strings[0].equals(user))
                     .findFirst()
                     .map(strings -> strings[1])
                     .orElse("");
    }

    @Override
    public void removeClient(ClientEntity clientEntity) {
        Iterable<ClientEntity> clientEntities = clientDomainService.getClientsByUsername(clientEntity.getUsername());
        clientEntities.forEach(client -> clientDomainService.deleteClient(clientEntity));
    }
}
