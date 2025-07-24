package com.ebrahimi.azmoon.dto.question;

import com.ebrahimi.azmoon.domain.StudentAnswer;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentExamQuestionRecordedDTO {
    private List<CreateQuestionDTO> questions;
    private Integer examId;
    private List<StudentAnswer> studentAnswers;
}
