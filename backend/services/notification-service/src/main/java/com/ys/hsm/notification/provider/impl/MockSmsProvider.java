package com.ys.hsm.notification.provider.impl;

import com.ys.hsm.notification.provider.SmsProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MockSmsProvider implements SmsProvider {

    @Override
    public void sendSms(
            String mobile,
            String message
    ) {

        log.info(
                "Mock SMS sent to mobile: {} | Message: {}",
                mobile,
                message
        );
    }
}