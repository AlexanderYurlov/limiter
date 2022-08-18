package com.alex.limiter.service;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import com.alex.limiter.config.LimiterProperties;

public class LimitStorageService {

    private static final Map<String, Queue<Long>> storage = new ConcurrentHashMap<>();

    /**
     * Сделать так, чтобы это ограничение можно было применять быстро к новым методам и не только к контроллерам,
     * а также к методам классов сервисного слоя.
     */
    public static void isAccessibleValidation(String ip, long currentTime, LimiterProperties limiterProperties) throws CallLimitExceededException {
        if (!isAccessible(ip, currentTime, limiterProperties)) {
            throw new CallLimitExceededException("Call Limit exceeded");
        }
    }

    public static boolean isAccessible(String ip, long currentTime, LimiterProperties limiterProperties) {
        Queue<Long> emptyQueue = new CircularFifoQueue(limiterProperties.getMaxCallQuantity());
        storage.putIfAbsent(ip, emptyQueue);
        Queue<Long> queue = storage.get(ip);
        synchronized (queue) {
            if (queue.size() < limiterProperties.getMaxCallQuantity()) {
                queue.add(currentTime);
                return true;
            } else {
                var earliestTime = queue.peek();
                if (currentTime - earliestTime > limiterProperties.getTimePeriodMls()) {
                    queue.add(currentTime);
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

}
