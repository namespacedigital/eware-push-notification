package com.eware.pushnotification.domain.service;

import com.eware.pushnotification.domain.entity.ClientEntity;


public interface ClientDomainService {
    Iterable<ClientEntity> findClients();
    ClientEntity saveClient(ClientEntity clientEntity);

    Iterable<ClientEntity> getClientsByUsername(String username);
    ClientEntity getClientByWebSocketSessionId(String webSocketSessionId);
    ClientEntity updateClient(ClientEntity clientEntity);
    void deleteClient(ClientEntity clientEntity);

}
