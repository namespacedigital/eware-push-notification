package org.project.startup.domain.service.impl;

import org.project.startup.domain.entity.ClientEntity;
import org.project.startup.domain.repository.ClientRepository;
import org.project.startup.domain.service.ClientDomainService;
import org.springframework.stereotype.Service;



@Service
public class ClientDomainServiceImpl implements ClientDomainService {

    private ClientRepository clientRepository;

    public ClientDomainServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Iterable<ClientEntity> findClients() {
        return clientRepository.findAll();
    }

    @Override
    public ClientEntity saveClient(ClientEntity clientEntity) {
        return clientRepository.save(clientEntity);
    }

    @Override
    public ClientEntity updateClient(ClientEntity clientEntity) {
        return null;
    }

    @Override
    public Iterable<ClientEntity> getClientsByUsername(String username) {
        return clientRepository.findByUsername(username);
    }

    @Override
    public ClientEntity getClientByWebSocketSessionId(String webSocketSessionId) {
        return clientRepository.findByWebSocketSessionId(webSocketSessionId);
    }

    @Override
    public void deleteClient(ClientEntity clientEntity) {
        clientRepository.delete(clientEntity);
    }
}
