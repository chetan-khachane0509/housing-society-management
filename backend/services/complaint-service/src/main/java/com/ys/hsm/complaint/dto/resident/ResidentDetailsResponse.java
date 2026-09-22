package com.ys.hsm.complaint.dto.resident;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResidentDetailsResponse {

    private String id;

    private String firstName;
    private String lastName;

    private String email;
    private String mobile;

    private String societyId;
    private String wingId;
    private String flatId;
}