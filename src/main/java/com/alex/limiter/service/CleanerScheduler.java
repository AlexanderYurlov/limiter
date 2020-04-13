package com.alex.limiter.service;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import com.alex.limiter.config.LimiterProperties;

@Slf4j
@Component
public class CleanerScheduler {

    private final LimiterProperties limiterProperties;

    public CleanerScheduler(LimiterProperties limiterProperties) {
        this.limiterProperties = limiterProperties;
    }

    @Scheduled(cron = "${limiter.limit.cron:0/30 * * * * *}")
    public void clean() {
        log.info("Start cleaning ip storage");
        var storage = LimitStorageService.getStorage();

        var itr = storage.entrySet().iterator();

        while (itr.hasNext()) {
            var entry = itr.next();

            var queue = entry.getValue();
            synchronized (entry.getValue()) {

                var peekLast = queue.peekFirst();
                var currentTime = new Date().getTime();
                if (currentTime - peekLast > limiterProperties.getTimePeriodMls()) {
                    itr.remove();
                }
            }
        }
    }
}
