package com.ys.hsm.complaint.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintEvent {

    private String complaintNumber;

    private String residentName;

    private String residentEmail;

    private String residentMobile;

    private String status;

    private String message;
}