package com.ys.hsm.auth.service.impl;

import com.ys.hsm.auth.entity.User;
import com.ys.hsm.auth.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    /**
     * @param user
     */
    @Override
    public void sendVerificationEmail(User user) {

    }

    /**
     * @param user
     */
    @Override
    public void sendPasswordResetEmail(User user) {

    }
}
