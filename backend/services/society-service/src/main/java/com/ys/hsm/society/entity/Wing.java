package com.ys.hsm.society.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Wings")
public class Wing {

    @Id
    private String id;

    private String societyId;

    private String wingName;

    private Integer totalFloors;

    private Integer totalFlats;

}
