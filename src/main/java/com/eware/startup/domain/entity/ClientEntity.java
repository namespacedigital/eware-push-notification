package com.eware.startup.domain.entity;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Getter
@Setter
@Builder
@RedisHash
@ToString
public class ClientEntity {
    private String id;
    private String webSocketSessionId;
    private String username;
}
