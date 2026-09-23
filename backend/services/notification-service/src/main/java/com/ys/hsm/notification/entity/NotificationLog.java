package com.ys.hsm.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notification_logs")
public class NotificationLog {

    @Id
    private String id;

    private String complaintNumber;

    private String residentId;

    private String recipient;

    private String channel;

    private String status;

    private String message;

    private String errorMessage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}