package org.project.startup.domain.service;

import org.project.startup.domain.entity.ClientEntity;


public interface ClientDomainService {
    Iterable<ClientEntity> findClients();
    ClientEntity saveClient(ClientEntity clientEntity);

    Iterable<ClientEntity> getClientsByUsername(String username);
    ClientEntity getClientByWebSocketSessionId(String webSocketSessionId);
    ClientEntity updateClient(ClientEntity clientEntity);
    void deleteClient(ClientEntity clientEntity);

}
