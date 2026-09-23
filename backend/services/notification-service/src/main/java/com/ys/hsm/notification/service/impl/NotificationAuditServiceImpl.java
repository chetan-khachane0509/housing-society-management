package com.ys.hsm.notification.service.impl;

import com.ys.hsm.notification.entity.NotificationLog;
import com.ys.hsm.notification.repository.NotificationLogRepository;
import com.ys.hsm.notification.service.NotificationAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationAuditServiceImpl
        implements NotificationAuditService {

    private final NotificationLogRepository notificationLogRepository;

    @Override
    public void logSuccess(
            NotificationLog notificationLog
    ) {

        notificationLog.setStatus("SENT");

        if (notificationLog.getCreatedAt() == null) {
            notificationLog.setCreatedAt(
                    LocalDateTime.now()
            );
        }

        notificationLog.setUpdatedAt(
                LocalDateTime.now()
        );

        notificationLogRepository.save(notificationLog);
    }

    @Override
    public void logFailure(
            NotificationLog notificationLog
    ) {

        notificationLog.setStatus("FAILED");

        if (notificationLog.getCreatedAt() == null) {
            notificationLog.setCreatedAt(
                    LocalDateTime.now()
            );
        }

        notificationLog.setUpdatedAt(
                LocalDateTime.now()
        );

        notificationLogRepository.save(notificationLog);
    }
}