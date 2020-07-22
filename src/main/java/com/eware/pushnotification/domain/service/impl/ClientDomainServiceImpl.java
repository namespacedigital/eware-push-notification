package com.eware.pushnotification.domain.service.impl;

import com.eware.pushnotification.domain.entity.ClientEntity;
import com.eware.pushnotification.domain.service.ClientDomainService;
import com.eware.pushnotification.domain.repository.ClientRepository;
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
