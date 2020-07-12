package org.project.startup.domain.repository;

import org.project.startup.domain.entity.ClientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends CrudRepository<ClientEntity, String> {
    ClientEntity findByWebSocketSessionId(String webSocketSessionId);

    List<ClientEntity> findByUsername(String username);
}