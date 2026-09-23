package com.ys.hsm.notification.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.hsm.notification.dto.ComplaintEvent;
import com.ys.hsm.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ComplaintEventConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(
            topics = "complaint-events",
            groupId = "notification-service-group"
    )
    public void consumeComplaintEvent(String message) {

        try {
            ComplaintEvent event =
                    objectMapper.readValue(message, ComplaintEvent.class);

            log.info(
                    "Complaint event received: {}",
                    event.getComplaintNumber()
            );

            notificationService.processComplaintEvent(event);

        } catch (Exception e) {

            log.error(
                    "Failed to process complaint event",
                    e
            );

            throw new RuntimeException(
                    "Failed to process complaint event",
                    e
            );
        }
    }
}