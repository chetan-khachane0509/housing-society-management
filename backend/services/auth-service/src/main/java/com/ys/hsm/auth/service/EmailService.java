package com.ys.hsm.auth.service;

import com.ys.hsm.auth.entity.User;

public interface EmailService {

    void sendVerificationEmail(User user);

    void sendPasswordResetEmail(User user, String resetToken);
}
