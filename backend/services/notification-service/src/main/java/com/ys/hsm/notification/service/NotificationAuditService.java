package com.ys.hsm.notification.service;

import com.ys.hsm.notification.entity.NotificationLog;

public interface NotificationAuditService {

    void logSuccess(
            NotificationLog notificationLog
    );

    void logFailure(
            NotificationLog notificationLog
    );
}