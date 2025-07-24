package com.ebrahimi.azmoon.dto.exam;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchExamDTO {

    @NotNull(message = "ID must NOT be Null!")
    private Integer id;

    private Integer courseId;
    private String title;
    private String description;
    private Integer duration;
}
