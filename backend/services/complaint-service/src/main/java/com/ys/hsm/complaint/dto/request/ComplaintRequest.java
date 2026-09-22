package com.ys.hsm.complaint.dto.request;

import com.ys.hsm.complaint.enums.ComplaintCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintRequest {
    @NotNull(message = "Complaint category is required")
    private ComplaintCategory category;

    @NotBlank(message = "Complaint title is required")
    private String title;

    @NotBlank(message = "Complaint description is required")
    private String description;
}
