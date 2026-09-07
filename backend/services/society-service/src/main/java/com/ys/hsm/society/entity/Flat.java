package com.ys.hsm.society.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "flats")
public class Flat {

    @Id
    private String id;

    private String societyId;

    private String wingId;

    private String flatNumber;

    private Integer floorNumber;

    private Integer flatSequence;

}
