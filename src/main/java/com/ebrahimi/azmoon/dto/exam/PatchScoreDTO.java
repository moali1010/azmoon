package com.ebrahimi.azmoon.dto.exam;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchScoreDTO {
    @NotNull(message = "Student ID must NOT be Null!")
    private String studentRegisterId;
    @NotNull(message = "Question ID must NOT be Null!")
    private String questionId;
    @NotNull(message = "Score must NOT be Null!")
    private Integer score;
}
