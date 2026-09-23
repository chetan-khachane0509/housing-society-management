package com.ys.hsm.notification.service;

import com.ys.hsm.notification.dto.ComplaintEvent;

public interface SmsNotificationService {
    void sendComplaintNotification(ComplaintEvent event);
}
