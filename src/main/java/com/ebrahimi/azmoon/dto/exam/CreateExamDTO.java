package com.ebrahimi.azmoon.dto.exam;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateExamDTO {

    @NotNull(message = "Course must NOT be Null!")
    private Integer courseId;

    @NotBlank(message = "Title must NOT be Empty!")
    @NotNull(message = "Title must NOT be Null!")
    private String title;

    @NotBlank(message = "Description must NOT be Empty!")
    @NotNull(message = "Description must NOT be Null!")
    private String description;

    @NotNull(message = "Duration must NOT be Null!")
    private Integer duration;
}
