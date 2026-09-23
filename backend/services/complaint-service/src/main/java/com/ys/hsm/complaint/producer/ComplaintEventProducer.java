package com.ys.hsm.complaint.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.hsm.complaint.dto.event.ComplaintEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplaintEventProducer {

    private static final String TOPIC = "complaint-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publishComplaintEvent(ComplaintEvent event) {

        try {

            String message = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(
                    TOPIC,
                    event.getComplaintNumber(),
                    message
            );

            log.info(
                    "Complaint event published to Kafka: {}",
                    event.getComplaintNumber()
            );

        } catch (JsonProcessingException e) {

            log.error(
                    "Failed to serialize complaint event: {}",
                    event.getComplaintNumber(),
                    e
            );

            throw new RuntimeException(
                    "Failed to publish complaint event",
                    e
            );
        }
    }
}