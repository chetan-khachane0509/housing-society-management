package com.ys.hsm.notification.service.impl;

import com.ys.hsm.notification.dto.ComplaintEvent;
import com.ys.hsm.notification.entity.NotificationLog;
import com.ys.hsm.notification.provider.SmsProvider;
import com.ys.hsm.notification.service.NotificationAuditService;
import com.ys.hsm.notification.service.SmsNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsNotificationServiceImpl
        implements SmsNotificationService {

    private final SmsProvider smsProvider;
    private final NotificationAuditService notificationAuditService;

    @Override
    public void sendComplaintNotification(
            ComplaintEvent event) {

        String message =
                "HSMS Complaint Update - "
                        + event.getComplaintNumber()
                        + ". Status: "
                        + event.getStatus()
                        + ". "
                        + event.getMessage();

        NotificationLog notificationLog =
                NotificationLog.builder()
                        .complaintNumber(
                                event.getComplaintNumber()
                        )
                        .recipient(
                                event.getResidentMobile()
                        )
                        .channel("SMS")
                        .message(message)
                        .build();

        try {

            smsProvider.sendSms(
                    event.getResidentMobile(),
                    message
            );

            notificationAuditService.logSuccess(
                    notificationLog
            );

        } catch (Exception e) {

            notificationLog.setErrorMessage(
                    e.getMessage()
            );

            notificationAuditService.logFailure(
                    notificationLog
            );

            throw e;
        }
    }
}