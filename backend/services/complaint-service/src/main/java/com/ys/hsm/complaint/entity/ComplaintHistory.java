package com.ys.hsm.complaint.entity;

import com.ys.hsm.complaint.enums.ComplaintStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "complaint_history")
public class ComplaintHistory {

    @Id
    private String id;

    private String complaintNumber;

    private ComplaintStatus previousStatus;

    private ComplaintStatus newStatus;

    private String action;

    private String remarks;

    private LocalDateTime changedAt;
}