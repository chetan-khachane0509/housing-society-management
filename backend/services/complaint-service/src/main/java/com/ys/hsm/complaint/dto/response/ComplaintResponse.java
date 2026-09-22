package com.ys.hsm.complaint.dto.response;

import com.ys.hsm.complaint.enums.ComplaintCategory;
import com.ys.hsm.complaint.enums.ComplaintStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintResponse {
    private String complaintNumber;

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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
