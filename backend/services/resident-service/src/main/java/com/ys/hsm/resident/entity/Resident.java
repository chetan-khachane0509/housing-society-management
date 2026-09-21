package com.ys.hsm.resident.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "residents")
public class Resident {

    @Id
    private String id;

    private String firstName;
    private String lastName;

    private String email;
    private String mobile;

    private String societyId;
    private String wingId;
    private String flatId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
