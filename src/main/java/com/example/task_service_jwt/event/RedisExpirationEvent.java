package com.example.task_service_jwt.event;

import com.example.task_service_jwt.entity.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisExpirationEvent {

    @EventListener
    public void handleRedisKeyExpiredEvent(RedisKeyExpiredEvent<RefreshToken> event){
        RefreshToken expiredRefreshToken= (RefreshToken) event.getValue();
        if (expiredRefreshToken == null){
            throw new RuntimeException("Refresh token is null in handleRedisKeyExpiredEvent function");
        }
        log.info("Refresh token with key={} has expired! Refresh token is:\s{}",expiredRefreshToken.getId(),expiredRefreshToken.getToken());
    }
}
