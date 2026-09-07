package com.ys.hsm.society.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WingRequest {

    @NotBlank(message = "Wing name is required")
    @Size(max = 10, message = "Wing name cannot exceed 10 characters")
    private String wingName;

    @NotNull(message = "Total floors is required")
    @Min(value = 1, message = "Total floors must be at least 1")
    @Max(value = 200, message = "Total floors cannot exceed 200")
    private Integer totalFloors;

    @NotNull(message = "Flats per floor is required")
    @Min(value = 1, message = "Flats per floor must be at least 1")
    @Max(value = 20, message = "Flats per floor cannot exceed 20")
    private Integer flatsPerFloor;
}
