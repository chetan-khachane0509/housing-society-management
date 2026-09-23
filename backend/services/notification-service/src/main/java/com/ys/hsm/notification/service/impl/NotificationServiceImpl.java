package com.ys.hsm.notification.service.impl;

import com.ys.hsm.notification.dto.ComplaintEvent;
import com.ys.hsm.notification.service.NotificationService;
import com.ys.hsm.notification.service.EmailNotificationService;
import com.ys.hsm.notification.service.SmsNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final EmailNotificationService emailNotificationService;
    private final SmsNotificationService smsNotificationService;

    @Value("${notification.email.enabled:true}")
    private boolean emailEnabled;

    @Value("${notification.sms.enabled:true}")
    private boolean smsEnabled;

    @Override
    public void processComplaintEvent(ComplaintEvent event) {

        log.info(
                "Processing notification for complaint: {}",
                event.getComplaintNumber()
        );

        if (emailEnabled) {

            emailNotificationService.sendComplaintNotification(event);
        }

        if (smsEnabled) {

            smsNotificationService.sendComplaintNotification(event);
        }
    }
}