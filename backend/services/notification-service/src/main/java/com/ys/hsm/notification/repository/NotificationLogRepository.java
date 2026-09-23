package com.ys.hsm.notification.repository;

import com.ys.hsm.notification.entity.NotificationLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationLogRepository
        extends MongoRepository<NotificationLog, String> {

    List<NotificationLog> findByComplaintNumber(
            String complaintNumber
    );

    List<NotificationLog> findByComplaintNumberAndChannel(
            String complaintNumber,
            String channel
    );
}