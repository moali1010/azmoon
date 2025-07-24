package com.ebrahimi.azmoon.dto.question;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestionDTO {
    @Valid
    private List<CreateQuestionDTO> questions;

    @NotNull(message = "EXAM-ID must NOT be Null!")
    private Integer examId;
}
