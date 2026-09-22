package com.ys.hsm.complaint.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignComplaintRequest {

    @NotBlank(message = "Assigned user is required")
    private String assignedTo;
}