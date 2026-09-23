package com.ys.hsm.notification.provider;

public interface SmsProvider {

    void sendSms(
            String mobile,
            String message
    );
}