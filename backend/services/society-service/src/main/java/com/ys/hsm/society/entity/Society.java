package com.ys.hsm.society.entity;

import com.ys.hsm.society.enums.RegistrationStatus;
import com.ys.hsm.society.enums.SocietyStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "societies")
public class Society {

    @Id
    private String id;

    private String societyName;

    @Indexed(unique = true)
    private String registrationNumber;
    private RegistrationStatus registrationStatus;
    private String address;
    private String city;
    private String state;
    private String pinCode;

    private Integer totalWings;
    private Integer totalFlats;

    private SocietyStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
