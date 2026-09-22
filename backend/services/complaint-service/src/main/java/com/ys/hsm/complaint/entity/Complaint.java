package com.ys.hsm.complaint.entity;

import com.ys.hsm.complaint.enums.ComplaintCategory;
import com.ys.hsm.complaint.enums.ComplaintStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "complaints")
public class Complaint {
    @Id
    private String id;

    private String complaintNumber;

    private String residentId;

    private String residentName;
    private String residentEmail;
    private String residentMobile;

    private String societyId;
    private String wingId;
    private String flatId;

    private ComplaintCategory category;

    private String title;
    private String description;

    private ComplaintStatus status;

    private String assignedTo;
    private String rejectionReason;
    private LocalDateTime assignedAt;

    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
