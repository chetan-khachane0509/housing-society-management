package com.ys.hsm.notification.service.impl;

import com.ys.hsm.notification.dto.ComplaintEvent;
import com.ys.hsm.notification.entity.NotificationLog;
import com.ys.hsm.notification.service.NotificationAuditService;
import com.ys.hsm.notification.service.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationServiceImpl
        implements EmailNotificationService {

    private final JavaMailSender mailSender;
    private final NotificationAuditService notificationAuditService;

    @Override
    public void sendComplaintNotification(
            ComplaintEvent event) {

        String messageText =
                "Dear " + event.getResidentName() + ",\n\n"
                        + "Your complaint has been updated.\n\n"
                        + "Complaint Number: "
                        + event.getComplaintNumber() + "\n"
                        + "Status: "
                        + event.getStatus() + "\n"
                        + "Message: "
                        + event.getMessage() + "\n\n"
                        + "Regards,\n"
                        + "HSMS Support Team";

        NotificationLog notificationLog =
                NotificationLog.builder()
                        .complaintNumber(
                                event.getComplaintNumber()
                        )
                        .recipient(
                                event.getResidentEmail()
                        )
                        .channel("EMAIL")
                        .message(messageText)
                        .build();

        try {

            SimpleMailMessage mailMessage =
                    new SimpleMailMessage();

            mailMessage.setTo(
                    event.getResidentEmail()
            );

            mailMessage.setSubject(
                    "HSMS Complaint Update - "
                            + event.getComplaintNumber()
            );

            mailMessage.setText(messageText);

            mailSender.send(mailMessage);

            notificationAuditService.logSuccess(
                    notificationLog
            );

            log.info(
                    "Complaint notification email sent for complaint: {}",
                    event.getComplaintNumber()
            );

        } catch (Exception e) {

            notificationLog.setErrorMessage(
                    e.getMessage()
            );

            notificationAuditService.logFailure(
                    notificationLog
            );

            log.error(
                    "Failed to send complaint notification email for complaint: {}",
                    event.getComplaintNumber(),
                    e
            );

            throw e;
        }
    }
}