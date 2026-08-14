package com.ys.hsm.auth.service.impl;

import com.ys.hsm.auth.entity.User;
import com.ys.hsm.auth.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(User user) {

    }


    public void sendPasswordResetEmail(User user, String resetToken) {

        String resetLink = "http://localhost:3000/reset-password?token=" + resetToken;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("HSMS - Password Reset");
        mailMessage.setText(
                """
                Hello %s,

                We received a request to reset your HSMS password.

                Please click the following link to reset your password:

                %s

                This link is valid for a limited time.

                If you did not request a password reset, please ignore this email.

                Regards,
                HSMS Team
                """.formatted(user.getFirstName(), resetLink)
        );
        mailSender.send(mailMessage);
    }
}
