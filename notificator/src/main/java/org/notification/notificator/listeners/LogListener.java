package org.notification.notificator.listeners;

import org.notification.notificator.dto.LogDTO;
import org.notification.notificator.service.LogOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LogListener {

    private final LogOut logService;

    @Autowired
    public LogListener(LogOut logService) {
        this.logService = logService;
    }

    @KafkaListener(
            topics = "${spring.kafka.kafka-topic.consumer}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void getLogs(LogDTO log) {
        logService.printLog(log);
    }
}
