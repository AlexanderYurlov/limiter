package com.alex.limiter.service;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import lombok.Getter;

import com.alex.limiter.config.LimiterProperties;
import com.alex.limiter.config.interceptor.SessionData;

@Service
public class LimitStorageService {

    @Getter
    private static final Map<String, Deque<Long>> storage = new ConcurrentHashMap<>();

    private final LimiterProperties limiterProperties;

    private final SessionData requestSessionData;

    public LimitStorageService(LimiterProperties limiterProperties, SessionData requestSessionData) {
        this.limiterProperties = limiterProperties;
        this.requestSessionData = requestSessionData;
    }

    /**
     * Сделать так, чтобы это ограничение можно было применять быстро к новым методам и не только к контроллерам,
     * а также к методам классов сервисного слоя.
     */
    public void isAccessibleValidation() throws CallLimitExceededException {
        if (!isAccessible()) {
            throw new CallLimitExceededException("Call Limit exceeded");
        }
    }

    public boolean isAccessible() {
        Deque<Long> emptyQueue = new LinkedList<>();
        storage.putIfAbsent(requestSessionData.getIp(), emptyQueue);
        Deque<Long> queue = storage.get(requestSessionData.getIp());
        synchronized (queue) {
            if (queue.size() < limiterProperties.getMaxCallQuantity()) {
                queue.add(requestSessionData.getTime());
                return true;
            } else {
                return tryRemoveFirst(queue, requestSessionData.getTime());
            }
        }
    }

    private boolean tryRemoveFirst(Deque<Long> queue, long currentTime) {
        var earliestTime = queue.peek();
        if (currentTime - earliestTime > limiterProperties.getTimePeriodMls()) {
            queue.remove();
            if (queue.size() < limiterProperties.getMaxCallQuantity()) {
                queue.add(currentTime);
                return true;
            }
            return tryRemoveFirst(queue, currentTime);
        } else {
            return false;
        }
    }
}
